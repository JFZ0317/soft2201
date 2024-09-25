package pacman.model.entity.observe;

import javafx.scene.control.Label;

import java.util.List;

public interface Subject {
    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers();
    List<Label> draw_labels();
}