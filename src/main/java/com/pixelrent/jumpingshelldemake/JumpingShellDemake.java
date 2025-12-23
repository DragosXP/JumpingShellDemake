package com.pixelrent.jumpingshelldemake;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class JumpingShellDemake {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Jumping Shell Demake");
        frame.setSize(1280, 960);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);

        frame.setVisible(true);
    }

    static class GamePanel extends JPanel {
        private Image playerSprite;
        private Image blockSprite;

        public GamePanel() {
            this.setBackground(Color.decode("#1f102a"));
            
            try {
                // Am modificat calea aici pentru a include src/main/
                playerSprite = ImageIO.read(new File("src/main/resources/sprites/player/spr_player_idle.png"));
                blockSprite = ImageIO.read(new File("src/main/resources/sprites/static/block/spr_block_white.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (playerSprite != null) {
                int playerX = (getWidth() / 2) - 8;
                int playerY = (getHeight() / 2) - 8;
                g.drawImage(playerSprite, playerX, playerY, null);
            }

            if (blockSprite != null) {
                int rows = 3;
                int cols = 80;
                int startY = getHeight() - (rows * 16);

                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < cols; c++) {
                        g.drawImage(blockSprite, c * 16, startY + (r * 16), null);
                    }
                }
            }
        }
    }
}