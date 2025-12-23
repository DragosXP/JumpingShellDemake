package com.pixelrent.jumpingshelldemake;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.Color;

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
        frame.getContentPane().setBackground(Color.decode("#1f102a"));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}