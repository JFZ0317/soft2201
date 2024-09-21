package pacman.model.state;

import pacman.model.entity.dynamic.ghost.GhostImpl;

public class ChaseState implements GhostState {
    @Override
    public void execute(GhostImpl ghost) {
        System.out.println("Ghost is chasing Pacman.");
        // 追逐模式的具体逻辑，例如计算路径追踪玩家
    }


}