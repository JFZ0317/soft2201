package pacman.model.entity.factory;

import pacman.model.entity.Renderable;
import pacman.model.entity.staticentity.WallVisual;

public interface EntityFactory {
    double width = 16;
    double height = 16;
    Renderable createEntity(int x, int y);
    Renderable createEntity(int x, int y, WallVisual wallVisual);

}
