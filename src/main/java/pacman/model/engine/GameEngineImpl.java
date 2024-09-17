package pacman.model.engine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pacman.model.entity.Renderable;
import pacman.model.level.Level;
import pacman.model.level.LevelImpl;
import pacman.model.maze.Maze;
import pacman.model.maze.MazeCreator;

import java.util.List;

/**
 * Implementation of GameEngine - responsible for coordinating the Pac-Man model
 */
public class GameEngineImpl implements GameEngine {

    private Level currentLevel;
    private int numLevels;
    private final int currentLevelNo;
    private Maze maze;
    private JSONArray levelConfigs;

    public GameEngineImpl(String configPath) {
        this.currentLevelNo = 0;//本来是0

        init(new GameConfigurationReader(configPath));
        startLevel();//后加的
    }

    private void init(GameConfigurationReader gameConfigurationReader) {
        // Set up map
        String mapFile = gameConfigurationReader.getMapFile();
        MazeCreator mazeCreator = new MazeCreator(mapFile);
        this.maze = mazeCreator.createMaze();
        this.maze.setNumLives(gameConfigurationReader.getNumLives());

        // Get level configurations
        this.levelConfigs = gameConfigurationReader.getLevelConfigs();
        this.numLevels = levelConfigs.size();
        if (levelConfigs.isEmpty()) {
            System.exit(0);
        }
//        从 gameConfigurationReader 中读取游戏的配置文件，设置地图文件路径，创建迷宫对象，并为迷宫设置初始生命值。
//获取并存储关卡配置信息到 levelConfigs，并设置关卡总数 numLevels。
//如果配置文件中的关卡配置为空，程序会通过 System.exit(0) 退出游戏。
    }

    @Override
    public List<Renderable> getRenderables() {
        return this.currentLevel.getRenderables();
    }

    @Override
    public void moveUp() {
        currentLevel.moveUp();
    }

    @Override
    public void moveDown() {
        currentLevel.moveDown();
    }

    @Override
    public void moveLeft() {
        currentLevel.moveLeft();
    }

    @Override
    public void moveRight() {
        currentLevel.moveRight();
    }

    @Override
    public void startGame() {
        startLevel();
    }

    private void startLevel() {
        JSONObject levelConfig = (JSONObject) levelConfigs.get(currentLevelNo);
        // reset renderables to starting state
        maze.reset();
//        该方法根据当前关卡编号（currentLevelNo）从 levelConfigs 中获取对应的关卡配置信息，并重置迷宫的状态（例如，将所有元素重置到起始位置）。
        this.currentLevel = new LevelImpl(levelConfig, maze);
//        然后创建一个新的 LevelImpl 实例（代表新的关卡），并将其赋值给 currentLevel，从而更新当前关卡。
    }

    @Override
    public void tick() {
        if (currentLevel != null) {
            currentLevel.tick();
//            每个 tick() 调用表示游戏的一个时间步（通常对应一帧），该方法会调用 currentLevel 的 tick()，更新当前关卡的状态。
        } else {
            System.out.println("Current level is not initialized.");
        }
    }

}

