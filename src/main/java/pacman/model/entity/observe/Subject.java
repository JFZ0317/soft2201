package pacman.model.entity.observe;

import javafx.scene.control.Label;

import java.util.List;

public interface Subject {
    void addScoreObserver(ScoreObserver scoreObserver);
    void removeScoreObserver(ScoreObserver scoreObserver);
    void notifyScoreObservers();
    void addLivesObserver(LivesObserver livesObserver);
    void removeLivesObserver(LivesObserver livesObserver);
    void notifyLivesObservers();
    List<Label> draw_labels();
}