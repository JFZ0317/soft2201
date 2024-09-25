package pacman.model.entity.observe;

import javafx.scene.control.Label;

public interface ScoreObserver {
    void updateScore(int score);
    Label draw();
}