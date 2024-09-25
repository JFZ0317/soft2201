package pacman.model.entity.factory;

import pacman.model.entity.Renderable;
import javafx.scene.image.Image;
import java.util.*;

import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.dynamic.player.PacmanVisual;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.entity.staticentity.WallVisual;
import pacman.model.entity.staticentity.collectable.Pellet;


public class PelletFactory implements EntityFactory{
    private Image loadImage() {
        return new Image(getClass().getResourceAsStream("/maze/pellet.png"));
    }
    public Renderable createEntity(int x, int y) {
        Image pellet_image = loadImage();
        Vector2D topLeft = new Vector2D(x, y);
        BoundingBoxImpl box = new BoundingBoxImpl(topLeft,height,width);
        KinematicStateImpl.KinematicStateBuilder builder = new KinematicStateImpl.KinematicStateBuilder();
        builder = builder.setPosition(topLeft);
        builder = builder.setSpeed(0);
        builder = builder.setDirection(Direction.RIGHT);

        Renderable.Layer layer = Renderable.Layer.BACKGROUND;
        int points = 1;

        return new Pellet(box,layer,pellet_image, points);
    }

    @Override
    public Renderable createEntity(int x, int y, WallVisual wallVisual) {
        return null;
    }
}
