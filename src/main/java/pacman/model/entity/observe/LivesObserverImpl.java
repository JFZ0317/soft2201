package pacman.model.entity.observe;

import pacman.model.entity.observe.LivesObserver;

public class LivesObserverImpl implements LivesObserver {
    @Override
    public void updateLives(int lives) {
        if (lives > 0) {
            System.out.println("Player has " + lives + " lives remaining.");
        } else {
            System.out.println("Player has no lives left!");
        }
    }
}