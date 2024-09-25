package pacman.model.entity.factory;

import pacman.model.entity.Renderable;
import javafx.scene.image.Image;
import java.util.*;

import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.dynamic.player.PacmanVisual;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.entity.staticentity.StaticEntityImpl;
import pacman.model.entity.staticentity.WallVisual;

public class WallFactory implements EntityFactory{
    private static final Map<WallVisual, String> imagePaths = new HashMap<>();
    static {
        // 初始化不同墙类型对应的图片路径
        imagePaths.put(WallVisual.Horizontal_Wall, "/maze/walls/horizontal.png");
        imagePaths.put(WallVisual.down_left, "/maze/walls/downLeft.png");
        imagePaths.put(WallVisual.down_right, "/maze/walls/downRight.png");
        imagePaths.put(WallVisual.up_left, "/maze/walls/upLeft.png");
        imagePaths.put(WallVisual.up_right, "/maze/walls/upRight.png");
        imagePaths.put(WallVisual.Vertical_Wall, "/maze/walls/vertical.png");


    }
    private Image loadImage(String fileName) {
        return new Image(getClass().getResourceAsStream(fileName));
    }
    public Renderable createEntity(int x, int y, WallVisual wallVisual) {
        String imagePath = imagePaths.get(wallVisual);
        Image image = loadImage(imagePath);

        Vector2D topLeft = new Vector2D(x, y);
        BoundingBoxImpl box = new BoundingBoxImpl(topLeft,height,width);

        Renderable.Layer layer = Renderable.Layer.BACKGROUND;

        return new StaticEntityImpl(box,layer,image);
    }

    @Override
    public Renderable createEntity(int x, int y) {
        return null;
    }
}
