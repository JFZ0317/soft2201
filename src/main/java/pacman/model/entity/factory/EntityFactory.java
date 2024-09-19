package pacman.model.entity.factory;

import pacman.model.entity.Renderable;

public interface EntityFactory {
    double width = 16;
    double height = 16;
    Renderable createEntity(int x, int y);
}
