package pacman;

import javafx.application.Application;
import javafx.stage.Stage;
import pacman.model.engine.GameEngine;
import pacman.model.engine.GameEngineImpl;
import pacman.view.GameWindow;

public class App extends Application {
    public int width = 448;
    public int height = 576;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GameEngine model = new GameEngineImpl("src/main/resources/config.json");
        GameWindow window = new GameWindow(model, width, height);

        primaryStage.setTitle("Pac-Man");
        primaryStage.setScene(window.getScene());
        primaryStage.show();

        window.run();
    }
}