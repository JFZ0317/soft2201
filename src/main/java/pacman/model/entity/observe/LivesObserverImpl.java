package pacman.model.entity.observe;

import javafx.scene.control.Label;

public class LivesObserverImpl implements Observer {
    private Label liveLabel;
    public LivesObserverImpl(Label liveLabel){
        this.liveLabel = liveLabel;
    }
    @Override
    public void update(int lives) {
        String str = "Player lives: " + lives;
        liveLabel.setText(str);
    }
    @Override
    public Label draw(){
        return liveLabel;
    }
}