package com.pixelrent.jumpingshelldemake;

public class ActionMoveLeft implements PlayerAction {
    private Player player;

    public ActionMoveLeft(Player player) {
        this.player = player;
    }

    @Override
    public void act() {
        player.goLeft();
    }
}