package com.pixelrent.jumpingshelldemake;

public class ActionJump implements PlayerAction {
    private Player player;

    public ActionJump(Player player) {
        this.player = player;
    }

    @Override
    public void act() {
        player.doJump();
    }
}