package com.pixelrent.jumpingshelldemake;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player {
    private double x, y;
    // Variabile noi pentru a ține minte poziția de start
    private double spawnX, spawnY; 
    
    private double velocityX, velocityY;
    private boolean onGround;
    
    // --- SPRITES ---
    private Image spriteIdle;
    private Image spriteLeft;
    private Image spriteRight;
    private boolean facingRight = true; 

    private double gravity = 0.5; 
    private double moveSpeed = 5.0;
    private double jumpStrength;
    
    private final int TILE_SIZE = 16;
    private final int WIDTH = 16;
    private final int HEIGHT = 16;

    private int coyoteTimer = 0;
    private final int COYOTE_LIMIT = 6; 

    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        
        // Initializam spawn point-ul
        this.spawnX = startX;
        this.spawnY = startY;
        
        this.onGround = false;
        
        int targetJumpHeight = 52; 
        this.jumpStrength = Math.sqrt(2 * gravity * targetJumpHeight);

        try {
            spriteIdle = ImageIO.read(new File("src/main/resources/sprites/player/spr_player_idle.png"));
            spriteLeft = ImageIO.read(new File("src/main/resources/sprites/player/spr_player_left.png"));
            spriteRight = ImageIO.read(new File("src/main/resources/sprites/player/spr_player_right.png"));
        } catch (IOException e) {
            System.out.println("EROARE LA INCARCAREA SPRITE-URILOR PLAYERULUI!");
            e.printStackTrace();
        }
    }

    public void setPosition(double newX, double newY) {
        this.x = newX;
        this.y = newY;
        
        // IMPORTANT: Când jocul (GamePanel) ne așază pe hartă la început,
        // actualizăm și punctul de spawn. Astfel, butonul R va ști unde să ne întoarcă.
        this.spawnX = newX;
        this.spawnY = newY;
        
        this.velocityX = 0;
        this.velocityY = 0;
    }

    public void update(int[][] levelData) {
        if (coyoteTimer > 0) coyoteTimer--;

        x += velocityX;
        checkCollisionX(levelData);

        velocityY += gravity;
        y += velocityY;
        checkCollisionY(levelData);
    }

    private void checkCollisionX(int[][] levelData) {
        if (velocityX > 0) { 
            int tileRight = (int) (x + WIDTH - 1) / TILE_SIZE;
            int tileTop = (int) y / TILE_SIZE;
            int tileBottom = (int) (y + HEIGHT - 1) / TILE_SIZE;

            if (isSolid(tileRight, tileTop, levelData) || isSolid(tileRight, tileBottom, levelData)) {
                x = (tileRight * TILE_SIZE) - WIDTH;
                velocityX = 0; 
            }
        } else if (velocityX < 0) { 
            int tileLeft = (int) x / TILE_SIZE;
            int tileTop = (int) y / TILE_SIZE;
            int tileBottom = (int) (y + HEIGHT - 1) / TILE_SIZE;

            if (isSolid(tileLeft, tileTop, levelData) || isSolid(tileLeft, tileBottom, levelData)) {
                x = (tileLeft * TILE_SIZE) + TILE_SIZE;
                velocityX = 0; 
            }
        }
    }

    private void checkCollisionY(int[][] levelData) {
        if (velocityY > 0) { 
            int tileBottom = (int) (y + HEIGHT - 1) / TILE_SIZE;
            int tileLeft = (int) x / TILE_SIZE;
            int tileRight = (int) (x + WIDTH - 1) / TILE_SIZE;

            if (isSolid(tileLeft, tileBottom, levelData) || isSolid(tileRight, tileBottom, levelData)) {
                y = tileBottom * TILE_SIZE - HEIGHT;
                velocityY = 0;
                onGround = true;
                coyoteTimer = COYOTE_LIMIT;
            } else {
                onGround = false;
            }
        } else if (velocityY < 0) { 
            int tileTop = (int) y / TILE_SIZE;
            int tileLeft = (int) x / TILE_SIZE;
            int tileRight = (int) (x + WIDTH - 1) / TILE_SIZE;

            if (isSolid(tileLeft, tileTop, levelData) || isSolid(tileRight, tileTop, levelData)) {
                y = (tileTop * TILE_SIZE) + TILE_SIZE;
                velocityY = 0;
            }
        }
    }

    private boolean isSolid(int col, int row, int[][] levelData) {
        if (col < 0 || col >= 79 || row < 0 || row >= 47) return false;
        return levelData[row][col] == 1;
    }

    public void draw(Graphics g) {
        Image spriteToDraw;

        if (Math.abs(velocityX) > 0.1) {
            if (facingRight) {
                spriteToDraw = spriteRight;
            } else {
                spriteToDraw = spriteLeft;
            }
        } else {
            spriteToDraw = spriteIdle;
        }

        if (spriteToDraw != null) {
            g.drawImage(spriteToDraw, (int)x, (int)y, null);
        } else if (spriteIdle != null) {
            g.drawImage(spriteIdle, (int)x, (int)y, null);
        }
    }

    public void goLeft() { 
        velocityX = -moveSpeed;
        facingRight = false; 
    }
    public void goRight() { 
        velocityX = moveSpeed;
        facingRight = true; 
    }
    public void standStill() { velocityX = 0; }

    public void doJump() {
        if (coyoteTimer > 0) {
            velocityY = -jumpStrength - gravity;
            onGround = false;
            coyoteTimer = 0;
        }
    }

    public void respawn() {
        // AICI ESTE FIX-UL:
        // Resetăm poziția la coordonatele de spawn memorate
        this.x = spawnX;
        this.y = spawnY;
        this.velocityX = 0;
        this.velocityY = 0;
    }

    public double getX() { return x; }
    public double getY() { return y; }
}