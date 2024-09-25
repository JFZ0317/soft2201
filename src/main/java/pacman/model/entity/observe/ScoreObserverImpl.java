package pacman.model.entity.observe;

import javafx.scene.control.Label;
import pacman.model.entity.observe.ScoreObserver;

public class ScoreObserverImpl implements ScoreObserver {
    private Label scoreLabel;

    public ScoreObserverImpl(Label scoreLabel){
        this.scoreLabel = scoreLabel;
    }

    @Override
    public void updateScore(int score) {
        String str = String.valueOf(score);
        scoreLabel.setText(str);
//        System.out.println(scoreLabel);
    }
    @Override
    public Label draw(){
        return scoreLabel;
    }
}