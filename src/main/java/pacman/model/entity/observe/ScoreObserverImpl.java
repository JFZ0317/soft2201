package pacman.model.entity.observe;

import javafx.scene.control.Label;

public class ScoreObserverImpl implements Observer {
    private Label scoreLabel;

    public ScoreObserverImpl(Label scoreLabel){
        this.scoreLabel = scoreLabel;
    }

    @Override
    public void update(int score) {
        String str = "Score: " + score;
        scoreLabel.setText(str);
    }
    @Override
    public Label draw(){
        return scoreLabel;
    }
}