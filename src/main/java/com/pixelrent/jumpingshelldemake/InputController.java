package com.pixelrent.jumpingshelldemake;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputController implements KeyListener {

    private PlayerAction actionLeft;
    private PlayerAction actionRight;
    private PlayerAction actionJump;
    private PlayerAction actionStop;
    private PlayerAction actionReload;
    
    // Stări pentru taste
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean isJumpPressed = false;
    
    // --- VARIABILA NOUA ---
    // Ne ajuta sa nu dam reload continuu cat timp tinem tasta apasata
    private boolean isRPressed = false;
    
    // Tine minte ultima directie apasata pentru a rezolva conflictul stanga+dreapta
    private int lastKeyPressed = 0; // -1 pt Stanga, 1 pt Dreapta

    public InputController(PlayerAction left, PlayerAction right, PlayerAction jump, PlayerAction stop, PlayerAction reload) {
        this.actionLeft = left;
        this.actionRight = right;
        this.actionJump = jump;
        this.actionStop = stop;
        this.actionReload = reload;
    }

    // Aceasta metoda decide in ce directie mergem bazat pe ce taste sunt apasate
    // Este apelata in fiecare frame din GamePanel.update()
    public void update() {
        if (leftPressed && rightPressed) {
            // Daca ambele sunt apasate, mergem in directia ultimei taste apasate
            if (lastKeyPressed == -1) {
                actionLeft.act();
            } else {
                actionRight.act();
            }
        } else if (leftPressed) {
            actionLeft.act();
        } else if (rightPressed) {
            actionRight.act();
        } else {
            actionStop.act();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = true;
            lastKeyPressed = -1; // -1 inseamna stanga
        }
        
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = true;
            lastKeyPressed = 1; // 1 inseamna dreapta
        }
        
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W || key == KeyEvent.VK_SPACE) {
            if (!isJumpPressed) {
                actionJump.act();
                isJumpPressed = true;
            }
        }
        
        // --- FIX RELOAD ---
        if (key == KeyEvent.VK_R) {
            // Executam reload DOAR daca tasta nu era deja apasata
            if (!isRPressed) {
                actionReload.act();
                isRPressed = true; // Marcam ca fiind apasata
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = false;
        }
        
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = false;
        }

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W || key == KeyEvent.VK_SPACE) {
            isJumpPressed = false;
        }
        
        // --- RESET RELOAD ---
        // Cand dam drumul la tasta, permitem sa se faca reload din nou data viitoare
        if (key == KeyEvent.VK_R) {
            isRPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}