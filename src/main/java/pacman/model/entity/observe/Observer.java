package pacman.model.entity.observe;

import javafx.scene.control.Label;

public interface Observer {
    void update(int score);
    Label draw();
}