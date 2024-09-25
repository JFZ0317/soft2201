package pacman.model.engine;

import javafx.scene.control.Label;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pacman.model.entity.Renderable;
import pacman.model.entity.observe.ScoreObserver;
import pacman.model.entity.observe.ScoreObserverImpl;
import pacman.model.entity.observe.Subject;
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
//    private Label ScoreLabel;


    public GameEngineImpl(String configPath) {
        this.currentLevelNo = 0;
//        this.ScoreLabel = new Label();

        init(new GameConfigurationReader(configPath));
//        startLevel();
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
        this.currentLevel = new LevelImpl(levelConfig, maze);

        Label scoreLabel = new Label();
        if (currentLevel instanceof Subject) {
            Subject subjectLevel = (Subject) currentLevel;  // 类型转换

            // 创建得分观察者，并将其添加到当前 Level
            ScoreObserver scoreObserver = new ScoreObserverImpl(scoreLabel);
            subjectLevel.addScoreObserver(scoreObserver);
            System.out.println(scoreLabel.getText());
        } else {
            System.out.println("currentLevel does not support observers.");
        }

    }

    @Override
    public void tick() {
        if (currentLevel != null) {
            currentLevel.tick();
        } else {
            System.out.println("Current level is not initialized.");
        }
    }
    public Level getlevel(){
        return currentLevel;
    }
}

