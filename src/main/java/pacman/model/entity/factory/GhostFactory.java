package pacman.model.entity.factory;

import pacman.model.entity.Renderable;
import javafx.scene.image.Image;
import java.util.*;

import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.dynamic.player.PacmanVisual;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.state.ChaseState;
import pacman.model.state.GhostState;

public class GhostFactory implements EntityFactory{
    private Image loadImage() {
        return new Image(getClass().getResourceAsStream("/maze/ghosts/ghost.png"));
    }
    public Renderable createEntity(int x, int y) {
        Image ghost_image = loadImage();
        Vector2D topLeft = new Vector2D(x, y);
        BoundingBoxImpl box = new BoundingBoxImpl(topLeft,height,width);
        KinematicStateImpl.KinematicStateBuilder builder = new KinematicStateImpl.KinematicStateBuilder();
        builder = builder.setPosition(topLeft);
        builder = builder.setSpeed(0);
        builder = builder.setDirection(Direction.RIGHT);

        GhostMode ghostMode = GhostMode.CHASE;

        Direction direction = Direction.UP;



        return new GhostImpl(ghost_image,box,builder.build(),ghostMode,topLeft, direction);
    }
}
