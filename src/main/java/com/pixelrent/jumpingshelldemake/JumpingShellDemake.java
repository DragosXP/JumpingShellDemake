package com.pixelrent.jumpingshelldemake;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class JumpingShellDemake {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Jumping Shell Demake");
        
        // Revenim la cererea ta exacta: fereastra de 1280x960
        frame.setSize(1280, 960);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        GamePanel gamePanel = new GamePanel();
        frame.add(gamePanel);
        
        frame.setVisible(true);
        gamePanel.requestFocusInWindow();
    }
}