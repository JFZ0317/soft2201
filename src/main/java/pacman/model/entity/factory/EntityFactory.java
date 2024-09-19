package pacman.model.entity.factory;

import pacman.model.entity.Renderable;

public interface EntityFactory {
    Renderable createEntity(int x, int y);
}
