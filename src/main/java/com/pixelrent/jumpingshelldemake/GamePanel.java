package com.pixelrent.jumpingshelldemake;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel implements Runnable {
    private BufferedImage platformCache;
    private Player player;
    private Thread gameThread;
    private final int FPS = 60;
    
    // Referinta catre InputController
    private InputController inputController;
    
    private int[][] levelData;
    private final int MAX_COLS = 79;
    private final int MAX_ROWS = 47;
    private final int TILE_SIZE = 16;
    
    private int levelWidth = MAX_COLS * TILE_SIZE;
    private int levelHeight = MAX_ROWS * TILE_SIZE;

    public GamePanel() {
        this.setBackground(Color.decode("#1f102a"));
        this.setFocusable(true);
        this.setDoubleBuffered(true);

        levelData = new int[MAX_ROWS][MAX_COLS];
        player = new Player(100, 100);

        try {
            BufferedImage blockSprite = ImageIO.read(new File("src/main/resources/sprites/static/block/spr_block_white.png"));
            loadLevelFromFile("src/main/resources/level.txt", blockSprite);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PlayerAction actLeft = new ActionMoveLeft(player);
        PlayerAction actRight = new ActionMoveRight(player);
        PlayerAction actJump = new ActionJump(player);
        PlayerAction actStop = new ActionStop(player);
        PlayerAction actReload = new ActionReload(player);

        inputController = new InputController(actLeft, actRight, actJump, actStop, actReload);
        this.addKeyListener(inputController);

        startGameThread();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if (remainingTime < 0) remainingTime = 0;
                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) { e.printStackTrace(); }
        }
    }

    public void update() {
        if (inputController != null) {
            inputController.update();
        }
        player.update(levelData);
    }

    private void loadLevelFromFile(String path, BufferedImage blockSprite) {
        platformCache = new BufferedImage(levelWidth, levelHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = platformCache.getGraphics();

        try {
            Scanner scanner = new Scanner(new File(path));
            int row = 0;
            while (scanner.hasNextLine() && row < MAX_ROWS) {
                String line = scanner.nextLine().trim();
                
                for (int col = 0; col < line.length() && col < MAX_COLS; col++) {
                    char c = line.charAt(col);
                    int val = Character.getNumericValue(c);
                    
                    levelData[row][col] = val;

                    if (val == 1 && blockSprite != null) {
                        g.drawImage(blockSprite, col * TILE_SIZE, row * TILE_SIZE, null);
                    }
                    if (val == 2) {
                        player.setPosition(col * TILE_SIZE, row * TILE_SIZE);
                        levelData[row][col] = 0;
                    }
                }
                row++;
            }
            scanner.close();
        } catch (FileNotFoundException e) { 
            System.out.println("Nu s-a gasit fisierul level.txt!");
            e.printStackTrace(); 
        }
        g.dispose();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // --- HARTA STATICA (FARA CAMERA) ---
        // Desenam pur si simplu imaginea nivelului la 0,0
        if (platformCache != null) {
            g.drawImage(platformCache, 0, 0, null);
        }
        
        // Desenam jucatorul la coordonatele lui reale
        player.draw(g);
        
        Toolkit.getDefaultToolkit().sync();
    }
}