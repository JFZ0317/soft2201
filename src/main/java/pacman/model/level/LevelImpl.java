package pacman.model.level;

import javafx.scene.control.Label;
import org.json.simple.JSONObject;
import pacman.ConfigurationParseException;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.PhysicsEngine;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.observe.LivesObserver;
import pacman.model.entity.observe.LivesObserverImpl;
import pacman.model.entity.observe.ScoreObserver;
import pacman.model.entity.observe.Subject;
import pacman.model.entity.staticentity.StaticEntity;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.maze.Maze;
import pacman.model.maze.RenderableType;
import pacman.view.GameWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Concrete implement of Pac-Man level
 */
public class LevelImpl implements Level,Subject {

    private static final int START_LEVEL_TIME = 200;
    private final Maze maze;
    private List<Renderable> renderables;
    private Controllable player;
    private List<Ghost> ghosts;
    private int tickCount;
    private Map<GhostMode, Integer> modeLengths;
    private int numLives;
    private List<Renderable> collectables;
    private GhostMode currentGhostMode;
    private List<ScoreObserver> scoreobservers;
    private List<LivesObserver> livesobservers;

    public int score;
    private GameWindow gameWindow;

    public LevelImpl(JSONObject levelConfiguration,
                     Maze maze) {
        this.renderables = new ArrayList<>();
        this.maze = maze;
        this.tickCount = 0;
        this.modeLengths = new HashMap<>();
        this.currentGhostMode = GhostMode.SCATTER;
        scoreobservers = new ArrayList<>();
        livesobservers = new ArrayList<>();

        this.score = 0;

        initLevel(new LevelConfigurationReader(levelConfiguration));
    }

    private void initLevel(LevelConfigurationReader levelConfigurationReader) {
        // Fetch all renderables for the level
        this.renderables = maze.getRenderables();
//        System.out.println(this.renderables.size());


//         Set up player
        if (!(maze.getControllable() instanceof Controllable)) {
            throw new ConfigurationParseException("Player entity is not controllable");
        }
        this.player = (Controllable) maze.getControllable();
        this.player.setSpeed(levelConfigurationReader.getPlayerSpeed());
        setNumLives(maze.getNumLives());


        // Set up ghosts
        this.ghosts = maze.getGhosts().stream()//
                .map(element -> (Ghost) element)
                .collect(Collectors.toList());
        Map<GhostMode, Double> ghostSpeeds = levelConfigurationReader.getGhostSpeeds();

        for (Ghost ghost : this.ghosts) {
            ghost.setSpeeds(ghostSpeeds);
            ghost.setGhostMode(this.currentGhostMode);
        }
        this.modeLengths = levelConfigurationReader.getGhostModeLengths();

        // Set up collectables
        this.collectables = new ArrayList<>(maze.getPellets());

    }

    @Override
    public List<Renderable> getRenderables() {
        return this.renderables;
    }

    private List<DynamicEntity> getDynamicEntities() {
        return renderables.stream().filter(e -> e instanceof DynamicEntity).map(e -> (DynamicEntity) e).collect(
                Collectors.toList());
    }

    private List<StaticEntity> getStaticEntities() {
        return renderables.stream().filter(e -> e instanceof StaticEntity).map(e -> (StaticEntity) e).collect(
                Collectors.toList());
    }

//这两段代码分别是两个私有方法：getDynamicEntities() 和 getStaticEntities()。
// 它们的作用是从 renderables 列表中过滤出特定类型的实体对象，并将它们转换为各自的类型（DynamicEntity 和 StaticEntity），然后返回对应的列表。
    @Override
    public void tick() {
        //定期切换鬼怪的模式（如追逐或散开）。
        //切换 Pac-Man 的图像以模拟动画效果。
        //更新所有动态实体的状态（如 Pac-Man 和鬼怪的移动）。
        //检查并处理动态实体之间以及动态实体与静态实体之间的碰撞。
        //每次调用 tick() 方法，游戏的时间步（帧）都会推进。
        if (tickCount == modeLengths.get(currentGhostMode)) {

            // update ghost mode
            //this.currentGhostMode = GhostMode.getNextGhostMode(currentGhostMode);
            this.currentGhostMode = GhostMode.CHASE;
//            System.out.println(this.currentGhostMode);
            for (Ghost ghost : this.ghosts) {
                ghost.setGhostMode(this.currentGhostMode);
            }

            tickCount = 0;
        }

        if (tickCount % Pacman.PACMAN_IMAGE_SWAP_TICK_COUNT == 0) {
            this.player.switchImage();
        }

        // Update the dynamic entities
        List<DynamicEntity> dynamicEntities = getDynamicEntities();

        for (DynamicEntity dynamicEntity : dynamicEntities) {
            maze.updatePossibleDirections(dynamicEntity);
            dynamicEntity.update();
        }

        for (int i = 0; i < dynamicEntities.size(); ++i) {
            DynamicEntity dynamicEntityA = dynamicEntities.get(i);

            // handle collisions between dynamic entities
            for (int j = i + 1; j < dynamicEntities.size(); ++j) {
                DynamicEntity dynamicEntityB = dynamicEntities.get(j);

                if (dynamicEntityA.collidesWith(dynamicEntityB) ||
                        dynamicEntityB.collidesWith(dynamicEntityA)) {
                    dynamicEntityA.collideWith(this, dynamicEntityB);
                    dynamicEntityB.collideWith(this, dynamicEntityA);


                }
            }

            // handle collisions between dynamic entities and static entities
            for (StaticEntity staticEntity : getStaticEntities()) {
                if (dynamicEntityA.collidesWith(staticEntity)) {
                    dynamicEntityA.collideWith(this, staticEntity);
                    if ((dynamicEntityA instanceof Pacman) && this.isCollectable(staticEntity)){
//                        System.out.println(staticEntity);
                        this.collect((Collectable) staticEntity);

                    }
//                    System.out.println("get points");
                    PhysicsEngine.resolveCollision(dynamicEntityA, staticEntity);
                }
            }
        }

        tickCount++;
    }

    @Override
    public boolean isPlayer(Renderable renderable) {
        return renderable == this.player;
    }

    @Override
    public boolean isCollectable(Renderable renderable) {
        return maze.getPellets().contains(renderable) && ((Collectable) renderable).isCollectable();
    }

    @Override
    public void moveLeft() {
//        System.out.println("left");
        player.left();
    }

    @Override
    public void moveRight() {
        player.right();
    }

    @Override
    public void moveUp() {
        player.up();
    }

    @Override
    public void moveDown() {
        player.down();
    }

    @Override
    public boolean isLevelFinished() {
        return collectables.isEmpty();
    }

    @Override
    public int getNumLives() {
        return this.numLives;
    }

    private void setNumLives(int numLives) {
        this.numLives = numLives;
    }

    @Override
    public void handleLoseLife() {
        numLives--;

        notifyLivesObservers();
        if(numLives == 0){
            handleGameEnd();
        }
    }

    @Override
    public void handleGameEnd() {
        System.out.println("Game over");
//        showGameOverMessage();


    }


    @Override
    public void collect(Collectable collectable) {
        score += collectable.getPoints();
        notifyScoreObservers();
        collectable.collect();
//        System.out.println(score);

    }

    @Override
    public void addScoreObserver(ScoreObserver scoreObserver) {
        scoreobservers.add(scoreObserver);

    }

    @Override
    public void removeScoreObserver(ScoreObserver scoreObserver) {
        scoreobservers.remove(scoreObserver);

    }

    @Override
    public void notifyScoreObservers() {
        for (ScoreObserver observer : scoreobservers){
            observer.updateScore(score);

        }
    }

    @Override
    public void addLivesObserver(LivesObserver livesObserver) {
        livesobservers.add(livesObserver);

    }

    @Override
    public void removeLivesObserver(LivesObserver livesObserver) {
        livesobservers.remove(livesObserver);

    }

    @Override
    public void notifyLivesObservers() {
        for (LivesObserver observer : livesobservers){
            observer.updateLives(numLives);

        }
    }

    @Override
    public List<Label> draw_labels(){
        List<Label> labels = new ArrayList<>();
        for (ScoreObserver o : scoreobservers){
            labels.add(o.draw());
        }
        return labels;
    }


}
