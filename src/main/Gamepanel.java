package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JPanel;

import entity.Entity;
import entity.Player;
import tile.TileManager;

public class Gamepanel extends JPanel implements Runnable {
	
    // SCREEN SETTINGS
    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 20;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int maxMap = 10;
    public int currentMap = 0;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    
    // FULLSCREEN
    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;
    public boolean fullScreenOn = false;
    public boolean pcMode = false; // "PC Mode" - swaps dialogue/text for sanitized versions

    // FPS
    final int FPS = 60;

    public TileManager tileM = new TileManager(this);
    public KeyHandler keyH = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    public EventHandler eHandler = new EventHandler(this);
    Config config = new Config(this);
    public Quest quest = new Quest(this);
    public SaveLoad saveLoad = new SaveLoad(this);
    Thread gameThread;
    
    // Entity and obj
    public Player player = new Player(this, keyH);
    public Entity obj[][] = new Entity[maxMap][50]; // grown from 30 - the island ran out of slots
    public Entity npc[][] = new Entity[maxMap][18];
    public Entity monster[][] = new Entity[maxMap][30];
    public ArrayList<Entity> projectileList = new ArrayList<>();
    ArrayList<Entity> entityList = new ArrayList<>();

    // Current NPC being talked to
    public int currentNPC = 999;

    // GAME STATES
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialougeState = 3;
    public final int characterState = 4;
    public final int optionState = 5;
    public final int gameOverState = 6;
    public final int transitionState = 7;
    public final int tradeState = 8;
    public final int victoryState = 9;

    public Gamepanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
        gameState = titleState;
        
        // TYPE_INT_RGB (no alpha channel): the screen buffer is fully opaque anyway,
        // and opaque buffers draw noticeably faster on weaker hardware
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        g2 = (Graphics2D)tempScreen.getGraphics(); 
        
        if(fullScreenOn == true) {
            setFullScreen();
        }
    }
    
    public void retry() {
        // RESPAWN PLAYER TO OVERWORLD START POSITION
        player.setDefaultPositions();
        player.restoreLifeAndMana();
        player.revive();
        
        // SWITCH BACK TO OVERWORLD MAP
        currentMap = 0;
        
        // RESPAWN NPCs AND MONSTERS
        aSetter.setNPC();
        aSetter.setMonster();
        playMusic(0);
    }
    
    public void restart() {
        player.setDefaultPositions();
        player.setDefaultValues();
        player.restoreLifeAndMana();
        player.revive();
        player.setItems();
        
        // RESET TO OVERWORLD
        currentMap = 0;
        
        aSetter.setObject();
        aSetter.setNPC();
        aSetter.setMonster();
    }
    
    public void setFullScreen() {
        // Real exclusive fullscreen (GraphicsDevice.setFullScreenWindow) fights
        // with this game's plain JPanel.getGraphics()-based rendering in
        // drawToScreen() - the display-mode switch can leave that call drawing
        // into a stale/invalid graphics context, which shows up as a black
        // screen. A maximized borderless window avoids the exclusive-mode
        // handoff entirely while still filling the screen.
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        java.awt.Rectangle bounds = ge.getMaximumWindowBounds();

        Main.window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        Main.window.setBounds(bounds);

        screenWidth2 = bounds.width;
        screenHeight2 = bounds.height;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        // FIXED-TIMESTEP GAME LOOP
        // Game logic always runs at 60 updates per real second, no matter how
        // slow the computer is. If rendering can't keep up, we skip drawing
        // some frames instead of slowing the whole game down. Slow machines
        // get a lower visual FPS but the game always PLAYS at correct speed.
        double drawInterval = 1000000000.0 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            int updatesThisLoop = 0;
            while (delta >= 1 && updatesThisLoop < 5) {
                update();
                delta--;
                updatesThisLoop++;
            }
            if (delta > 1) {
                delta = 1; // discard unrecoverable backlog
            }

            if (updatesThisLoop > 0) {
                drawToTempScreen();
                drawToScreen();
            }

            try {
                Thread.sleep(1); // don't burn 100% of a CPU core
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if(gameState == playState) {
            player.update();
            
            for(int i = 0; i < npc[1].length; i++) {
                if(npc[currentMap][i] != null) {
                    npc[currentMap][i].update();
                }
            }
            
            for(int i = 0; i < monster[1].length; i++) {
                if(monster[currentMap][i] != null) {  
                    if(monster[currentMap][i].alive == true && monster[currentMap][i].dying == false) {
                        monster[currentMap][i].update();
                    }
                    if(monster[currentMap][i].alive == false) {
                        monster[currentMap][i].checkDrop();
                        monster[currentMap][i] = null;
                    }
                }
            }
            
            for(int j = 0; j < projectileList.size(); j++) {
                if(projectileList.get(j) != null) {  
                    if(projectileList.get(j).alive == true) {
                        projectileList.get(j).update();
                    }
                    if(projectileList.get(j).alive == false) {
                        projectileList.remove(j);
                    }
                }
            }
        }
        
        if(gameState == pauseState) {
        }
    }
        
    public void drawToTempScreen() {
        g2.clearRect(0, 0, screenWidth2, screenHeight2);
        
        long drawStart = 0;
        if(keyH.showDebugText == true) {
            drawStart = System.nanoTime();
        }
        
        if (gameState == titleState) {
            ui.draw(g2);
        }
        else {
            tileM.draw(g2);
              
            entityList.add(player);
            
            for(int i = 0; i < npc[1].length; i++) {
                if(npc[currentMap][i] != null) {
                    entityList.add(npc[currentMap][i]);
                }
            }
            
            for(int i = 0; i < obj[1].length; i++) {
                if(obj[currentMap][i] != null) {
                    entityList.add(obj[currentMap][i]);
                }
            }
            
            for(int i = 0; i < monster[1].length; i++) {
                if(monster[currentMap][i] != null) {
                    entityList.add(monster[currentMap][i]);
                }
            }
            
            for(int i = 0; i < projectileList.size(); i++) {
                if(projectileList.get(i) != null) {
                    entityList.add(projectileList.get(i));
                }
            }
            
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    int result = Integer.compare(e1.worldY, e2.worldY);
                    return result;
                }
            });
            
            for(int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }
            
            entityList.clear();
            
            ui.draw(g2);
        }
    
        if(keyH.showDebugText == true) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            int x = 10;
            int y = 400;
            int lineHeight = 20;
            g2.drawString("WorldX: " + player.worldX, x, y); y += lineHeight;
            g2.drawString("WorldY: " + player.worldY, x, y); y += lineHeight; 
            g2.drawString("Col: " + (player.worldX + player.solidArea.x) / tileSize, x, y); y += lineHeight;
            g2.drawString("Row: " + (player.worldY + player.solidArea.y) / tileSize, x, y); y += lineHeight;
            g2.setColor(Color.white);
            g2.drawString("Draw Time: " + passed, x, y);
        }
    }
    
    public void drawToScreen() {
        Graphics g = getGraphics();
        g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);
        g.dispose();
    }
    
    public void playMusic(int i) {
        music.setFile(pcSoundIndex(i));
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(pcSoundIndex(i));
        se.play();
    }

    // "PC Mode" sanitized sound swap - see main.Sound for the PC clip indices (20-24)
    // and res/sound/pc*.wav. Only sounds with an actual PC variant get remapped;
    // everything else plays unchanged.
    private int pcSoundIndex(int i) {
        if(pcMode == true) {
            switch(i) {
                case 8: return 20;  // hittaken -> pchittaken
                case 16: return 21; // fartblast -> pcfartblast
                case 17: return 22; // brianhit -> pcbrianhit
                case 18: return 23; // briandies -> pcbriandies
                case 19: return 24; // victorytheme -> pcvictorytheme
            }
        }
        return i;
    }

    // Helper methods for camera bounds clamping
    // Prevents black areas from showing at map edges
    public int getCameraX() {
        int cameraX = player.worldX - player.screenX;

        // Clamp to map boundaries
        if (cameraX < 0) {
            cameraX = 0;
        }
        if (cameraX > worldWidth - screenWidth) {
            cameraX = worldWidth - screenWidth;
        }

        return cameraX;
    }

    public int getCameraY() {
        int cameraY = player.worldY - player.screenY;

        // Clamp to map boundaries
        if (cameraY < 0) {
            cameraY = 0;
        }
        if (cameraY > worldHeight - screenHeight) {
            cameraY = worldHeight - screenHeight;
        }

        return cameraY;
    }
}