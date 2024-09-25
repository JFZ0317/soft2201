package pacman.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import pacman.model.engine.GameEngine;
import pacman.model.entity.Renderable;
import pacman.model.entity.observe.Subject;
import pacman.view.background.BackgroundDrawer;
import pacman.view.background.StandardBackgroundDrawer;
import pacman.view.entity.EntityView;
import pacman.view.entity.EntityViewImpl;
import pacman.view.keyboard.KeyboardInputHandler;
import javafx.scene.control.Label;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for managing the Pac-Man Game View
 */
public class GameWindow {

    public static final File FONT_FILE = new File("src/main/resources/maze/PressStart2P-Regular.ttf");

    private final Scene scene;
    private final Pane pane;
    private final GameEngine model;
    private final List<EntityView> entityViews;




//    参数: GameEngine model 是游戏的逻辑模型，int width 和 int height 是游戏窗口的宽度和高度。
//pane 和 scene: 创建 Pane 作为界面容器，并通过 Scene 包裹它，表示整个游戏窗口的视图。
//entityViews: 初始化一个空的 ArrayList，用来存储游戏中的实体视图。
//键盘输入: 使用 KeyboardInputHandler 处理键盘输入，将按键事件传递给游戏模型。
//背景绘制: 使用 StandardBackgroundDrawer 绘制背景，背景是静态的迷宫结构
    public GameWindow(GameEngine model, int width, int height) {
        this.model = model;

        pane = new Pane();
        scene = new Scene(pane, width, height);

//        scoreLabel = new Label("score: 0");
//        scoreLabel.setLayoutX(10);
//        scoreLabel.setLayoutY(10);
//        pane.getChildren().add(scoreLabel);

//        ScoreObserver scoreObserver = new ScoreObserverImpl(scoreLabel);
//        ((Subject) (model.getlevel())).addObserver(scoreObserver); // 假设你的 GameEngine 模型支持观察者模式



        entityViews = new ArrayList<>();

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(model);
        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);

        BackgroundDrawer backgroundDrawer = new StandardBackgroundDrawer();
        backgroundDrawer.draw(model, pane);
    }

//    作用: 返回当前的 Scene，以便其他部分可以访问该窗口的可视区域。
    public Scene getScene() {
        return scene;
    }


//Timeline 用于创建一个定时器，每 34 毫秒调用一次 draw() 方法（即每秒钟约调用 30 次）。这控制了游戏的刷新率，使得游戏每帧更新一次。
//setCycleCount(Timeline.INDEFINITE): 表示这个定时器会一直运行，直到手动停止。
//model.startGame(): 启动游戏逻辑，开始运行游戏。
    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(34),
                t -> this.draw()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        model.startGame();
        for (Label label: ((Subject) (model.getlevel())).draw_labels()){
            pane.getChildren().add(label);
        }
    }


//model.tick(): 更新游戏的逻辑状态。这会调用模型的 tick() 方法，推进游戏的一帧。
//List<Renderable> entities = model.getRenderables(): 获取当前所有需要渲染的游戏实体（如 Pac-Man、鬼怪等）。
//markForDelete(): 遍历 entityViews 列表，标记所有现有的实体视图为“待删除”。这是为了在后面判断哪些实体需要移除。
//查找和更新视图:
//对每个实体，查找现有的 EntityView，如果找到了匹配的视图，更新该视图。
//如果没有找到对应的视图，创建新的 EntityViewImpl 并添加到 entityViews 列表和 pane 中。
//删除过时的实体视图:
//如果某个 EntityView 标记为删除，则从 pane 中移除对应的节点（即从界面上移除该实体）。
//从 entityViews 列表中移除所有标记为删除的实体视图。
    private void draw() {
        model.tick();

        List<Renderable> entities = model.getRenderables();

        for (EntityView entityView : entityViews) {
            entityView.markForDelete();
        }

        for (Renderable entity : entities) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update();
                    break;
                }
            }
            if (notFound) {
//                System.out.println(entity);
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (EntityView entityView : entityViews) {
            if (entityView.isMarkedForDelete()) {
//                System.out.println("delete");
                pane.getChildren().remove(entityView.getNode());
            }
        }

        entityViews.removeIf(EntityView::isMarkedForDelete);
    }
    private void showGameOverMessage() {
        // 在屏幕上显示 "Game Over" 信息，假设有一个 Pane 作为根布局
        Label gameOverLabel = new Label("Game Over!");
        gameOverLabel.setStyle("-fx-font-size: 40px; -fx-text-fill: red;");
        gameOverLabel.setLayoutX(200); // 设置游戏结束文本的 X 位置
        gameOverLabel.setLayoutY(150); // 设置游戏结束文本的 Y 位置

        pane.getChildren().add(gameOverLabel); // 将文本添加到 Pane 中显示
    }
}
