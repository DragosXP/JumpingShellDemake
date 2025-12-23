package com.pixelrent.jumpingshelldemake;

public class ActionMoveRight implements PlayerAction {
    private Player player;

    public ActionMoveRight(Player player) {
        this.player = player;
    }

    @Override
    public void act() {
        player.goRight();
    }
}