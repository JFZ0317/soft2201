package pacman.model.entity.factory;

import pacman.model.entity.Renderable;
import javafx.scene.image.Image;
import java.util.*;

import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.dynamic.player.PacmanVisual;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.entity.staticentity.WallVisual;

public class PlayerFactory implements EntityFactory{
    private Image loadImage(String fileName) {
        return new Image(getClass().getResourceAsStream("/maze/pacman/" + fileName));
    }
    public Renderable createEntity(int x, int y) {
        Map<PacmanVisual, Image> images = new EnumMap<>(PacmanVisual.class);
        images.put(PacmanVisual.UP, loadImage("playerUp.png"));
        images.put(PacmanVisual.DOWN, loadImage("playerDown.png"));
        images.put(PacmanVisual.LEFT, loadImage("playerLeft.png"));
        images.put(PacmanVisual.RIGHT, loadImage("playerRight.png"));
        images.put(PacmanVisual.CLOSED, loadImage("playerClosed.png"));
        Vector2D topLeft = new Vector2D(x, y);
        BoundingBoxImpl box = new BoundingBoxImpl(topLeft,height,width);
        KinematicStateImpl.KinematicStateBuilder builder = new KinematicStateImpl.KinematicStateBuilder();
        builder = builder.setPosition(topLeft);
        builder = builder.setSpeed(1);
        builder = builder.setDirection(Direction.RIGHT);
        return new Pacman(images.get(PacmanVisual.RIGHT),images,box,builder.build());
    }

    @Override
    public Renderable createEntity(int x, int y, WallVisual wallVisual) {
        return null;
    }
}
