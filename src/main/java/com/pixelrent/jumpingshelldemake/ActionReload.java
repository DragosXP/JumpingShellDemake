package com.pixelrent.jumpingshelldemake;

public class ActionReload implements PlayerAction {
    private Player player;

    public ActionReload(Player player) {
        this.player = player;
    }

    @Override
    public void act() {
        player.respawn();
    }
}