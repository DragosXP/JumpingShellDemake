package com.pixelrent.jumpingshelldemake;

public class ActionStop implements PlayerAction {
    private Player player;

    public ActionStop(Player player) {
        this.player = player;
    }

    @Override
    public void act() {
        player.standStill();
    }
}