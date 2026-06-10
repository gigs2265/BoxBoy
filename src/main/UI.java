package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import entity.Entity;
import object.OBJBeer;
import object.OBJ_Chest;
import object.OBJ_Fart_Coin;
import object.OBJ_Puke;

public class UI {
    Gamepanel gp;
    Graphics2D g2;
    Font maruMonica, PurisaB;
    BufferedImage beer1, beerhalf1, beerhalf2, pukefull, pukeempty, fartcoin;
    public boolean messageOn = false;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinshed = false;
    public String currentDialouge = "";
    public int commandNum = 0;
    public int titleScreenState = 0;
    public int playerSlotCol = 0;
    public int playerSlotRow = 0;
    public int npcSlotCol = 0;
    public int npcSlotRow = 0;
    int subState = 0;
    int counter = 0;
    public Entity npc;
    
    public UI(Gamepanel gp) {
        this.gp = gp;
        
        try {
            InputStream is = getClass().getResourceAsStream("/font/x12y16pxMaruMonica.ttf");
            maruMonica = Font.createFont(Font.TRUETYPE_FONT, is);
            is = getClass().getResourceAsStream("/font/Purisa Bold.ttf");
            PurisaB = Font.createFont(Font.TRUETYPE_FONT, is);
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // CREATE HUD OBJ
        Entity beer = new OBJBeer(gp);
        beer1 = beer.image;
        beerhalf1 = beer.image2;
        beerhalf2 = beer.image3;
        Entity puke = new OBJ_Puke(gp);
        pukefull = puke.image;
        pukeempty = puke.image2;
        Entity fartCoin = new OBJ_Fart_Coin(gp);
        fartcoin = fartCoin.down1;
    }
    
    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
    }
    
    public void draw(Graphics2D g2) {
        this.g2 = g2;
        
        g2.setFont(maruMonica);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.white);
        
        // TITLE STATE
        if(gp.gameState == gp.titleState) {
            drawTitleScreen();
        }
        
        // PLAY STATE
        if(gp.gameState == gp.playState) {
            drawPlayerLife();
            drawMessage();
        }
        
        // PAUSED STATE
        if(gp.gameState == gp.pauseState) {
            drawPlayerLife();
            drawPauseScreen();
            drawQuestTracker();
        }
        
        // OPTION STATE
        if(gp.gameState == gp.optionState) {
            drawOptionScreen();
        }
        
        // DIALOGUE STATE
        if(gp.gameState == gp.dialougeState) {
            drawPlayerLife();
            drawDialougeScreen();
        }
        
        // CHARACTER STATE
        if(gp.gameState == gp.characterState) {
            drawCharacterScreen();
            drawInventory(gp.player, true);
        }
        
        // TRANSITION STATE
        if(gp.gameState == gp.transitionState) {
            drawTransition();
        }
        
        // GAME OVER
        if(gp.gameState == gp.gameOverState) {
            drawGameOverScreen();
        }
        
        // TRADE STATE
        if(gp.gameState == gp.tradeState) {
            drawTradeScreen();
        }
        
        // VICTORY STATE
        if(gp.gameState == gp.victoryState) {
            drawVictoryScreen();
        }
    }
    
    public void drawQuestTracker() {
        int x = gp.tileSize * 12;
        int y = gp.tileSize / 2;
        int width = gp.tileSize * 7;
        int height = gp.tileSize * 4;
        
        drawSubWindow(x, y, width, height);
        
        x += 25;
        y += gp.tileSize - 10;
        
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
        g2.drawString("QUEST: " + gp.quest.name, x, y);
        
        y += 35;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16f));
        g2.drawString(gp.quest.getCurrentObjective(), x, y);
        
        y += 28;
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15f));
        
        String obj1 = (gp.quest.objective1 ? "[X]" : "[ ]") + " Find friends";
        g2.drawString(obj1, x, y);
        
        y += 22;
        String obj2 = (gp.quest.objective2 ? "[X]" : "[ ]") + " Defeat Nick";
        g2.drawString(obj2, x, y);
        
        y += 22;
        String obj3 = (gp.quest.objective3 ? "[X]" : "[ ]") + " Escape island";
        g2.drawString(obj3, x, y);
    }
    
    public void drawPlayerLife() {
        int x = gp.tileSize / 2;
        int y = gp.tileSize / 2;
        int i = 0;

        OBJBeer beer = new OBJBeer(gp);

        while (i < gp.player.maxLife / 2) {
            g2.drawImage(beer.beerhalf2, x, y, null);
            i++;
            x += gp.tileSize;
        }

        x = gp.tileSize / 2;
        y = gp.tileSize / 2;
        i = 0;

        while (i < gp.player.life) {
            g2.drawImage(beer.beerhalf1, x, y, null);
            i++;
            if (i < gp.player.life) {
                g2.drawImage(beer.beer1, x, y, null);
                i++;
            }
            x += gp.tileSize;
        }
        
        x = (gp.tileSize/2) - 5;
        y = (int)(gp.tileSize*1.5);
        i = 0;
        while(i < gp.player.maxMana) {
            g2.drawImage(pukeempty, x, y, null);
            i++;
            x += gp.tileSize;
        }
        
        x = (gp.tileSize/2) - 5;
        y = (int)(gp.tileSize*1.5);
        i = 0;
        while(i < gp.player.mana) {
            g2.drawImage(pukefull, x, y, null);
            i++;
            x += gp.tileSize;
        }
    }
    
    public void drawInventory(Entity entity, boolean cursor) {
        int frameX = 0;
        int frameY = 0;
        int frameWidth = 0;
        int frameHeight = 0;
        int slotCol = 0;
        int slotRow = 0;
        
        if(entity == gp.player) {
            frameX = gp.tileSize*12;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize*6;
            frameHeight = gp.tileSize*5;
            slotCol = playerSlotCol;
            slotRow = playerSlotRow;
        } else {
            frameX = gp.tileSize*2;
            frameY = gp.tileSize;
            frameWidth = gp.tileSize*6;
            frameHeight = gp.tileSize*5;
            slotCol = npcSlotCol;
            slotRow = npcSlotRow;
        }
        
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        int slotSize = gp.tileSize+3;
        
        for(int i = 0; i < entity.inventory.size(); i++) {
            if(entity.inventory.get(i) == entity.currentWepon || 
               entity.inventory.get(i) == entity.currentSheild) {
                g2.setColor(new Color(240,200,0));    
                g2.fillRoundRect(slotX, slotY, gp.tileSize, gp.tileSize, 10, 10);
            }
            
            g2.drawImage(entity.inventory.get(i).down1, slotX, slotY, null);
            
            slotX += gp.tileSize + 3;
            
            if(i == 4 || i == 9 || i == 14) {
                slotX = slotXstart;
                slotY += slotSize;
            }
        }
        
        if(cursor == true) {
            int cursorX = slotXstart + (slotSize * slotCol);
            int cursorY = slotYstart + (slotSize * slotRow);
            int cursorWidth = gp.tileSize;
            int cursorHeight = gp.tileSize;
            
            g2.setColor(Color.white);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
            
            int dFrameX = frameX;
            int dFrameY = frameY + frameHeight;
            int dFrameWidth = frameWidth;
            int dFrameHeight = gp.tileSize*3;
            
            int textX = dFrameX + 20;
            int textY = dFrameY + gp.tileSize;
            g2.setFont(g2.getFont().deriveFont(20f));
            
            int itemIndex = getItemIndexOnSlot(slotCol, slotRow);
            
            if(itemIndex < entity.inventory.size()) {
                drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
                
                for(String line : entity.inventory.get(itemIndex).description.split("\n")) {
                    g2.drawString(line, textX, textY);
                    textY += 32;
                }
            }
        }
    }
    
    public void drawGameOverScreen() {
        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110));
        
        text = "You Suck!";
        g2.setColor(Color.black);
        x = getXforCenteredText(text);
        y = gp.tileSize*4;
        g2.drawString(text, x, y);
        g2.setColor(Color.white);
        g2.drawString(text, x-4, y-4);
        
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = getXforCenteredText(text);
        y += gp.tileSize*4;
        g2.drawString(text, x, y);
        if(commandNum == 0) {
            g2.drawString(">", x-40, y);
        }
        
        text = "Back to title screen";
        x = getXforCenteredText(text);
        y += 55;
        g2.drawString(text, x, y);
        if(commandNum == 1) {
            g2.drawString(">", x-40, y);
        }
    }
    
    public void drawVictoryScreen() {
        g2.setColor(new Color(0, 0, 0, 200));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        int x;
        int y;
        String text;
        
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 80f));
        text = "VICTORY!";
        g2.setColor(Color.yellow);
        x = getXforCenteredText(text);
        y = gp.tileSize * 3;
        g2.drawString(text, x, y);
        
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40f));
        g2.setColor(Color.white);
        
        text = "You and your friends escaped!";
        x = getXforCenteredText(text);
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30f));
        
        text = "Enemies Defeated: " + (gp.player.exp / 5);
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        
        text = "Final Level: " + gp.player.level;
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        
        text = "$FartCoins: " + gp.player.$fartcoin;
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
        
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 25f));
        text = "Thanks for playing Box Boy!";
        x = getXforCenteredText(text);
        y += gp.tileSize * 2;
        g2.drawString(text, x, y);
        
        text = "Press ENTER to return to title";
        x = getXforCenteredText(text);
        y += gp.tileSize;
        g2.drawString(text, x, y);
    }
    
    public void drawOptionScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32f));
        
        int frameX = gp.tileSize*6;
        int frameY = gp.tileSize;
        int frameWidth = gp.tileSize*8;
        int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        switch(subState) {
            case 0: option_top(frameX,frameY); break;
            case 1: option_fullScreenNotification(frameX, frameY); break;
            case 2: option_control(frameX, frameY); break;
            case 3: option_quitGameConfirm(frameX, frameY); break;
        }
        gp.keyH.enterPressed = false;
    }
    
    public void option_top(int frameX, int frameY) {
        int textX;
        int textY;
        
        String text = "Options";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);
        
        textX = frameX + gp.tileSize;
        textY += gp.tileSize*2;
        g2.drawString("Full Screen", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true) {
                if(gp.fullScreenOn == false) {
                    gp.fullScreenOn = true;
                } else if (gp.fullScreenOn == true) {
                    gp.fullScreenOn = false;
                }
                subState = 1;
            }
        }
        
        textY += gp.tileSize;
        g2.drawString("Music", textX, textY);
        if(commandNum == 1) {
            g2.drawString(">", textX-25, textY);
        }
        
        textY += gp.tileSize;
        g2.drawString("Sound EF", textX, textY);
        if(commandNum == 2) {
            g2.drawString(">", textX-25, textY);
        }
        
        textY += gp.tileSize;
        g2.drawString("Controls", textX, textY);
        if(commandNum == 3) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 2;
                commandNum = 0;
            }
        }
        
        textY += gp.tileSize;
        g2.drawString("Fuck Off", textX, textY);
        if(commandNum == 4) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 3;
                commandNum = 0;
            }
        }
        
        textY += gp.tileSize*2;
        g2.drawString("Back", textX, textY);
        if(commandNum == 5) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true) {
                gp.gameState = gp.playState;
                commandNum = 0;
            }
        }
        
        textX = frameX + (int)(gp.tileSize*4.5);
        textY = frameY + gp.tileSize*2 + 24;
        g2.setStroke(new BasicStroke(3));
        g2.drawRect(textX, textY, 24, 24);
        if(gp.fullScreenOn == true) {
            g2.fillRect(textX, textY, 24, 24);
        }
        
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);
        int volumeWidth = 24 * gp.music.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);
        
        textY += gp.tileSize;
        g2.drawRect(textX, textY, 120, 24);
        volumeWidth = 24 * gp.se.volumeScale;
        g2.fillRect(textX, textY, volumeWidth, 24);
        
        gp.config.saveConfig();
    }
    
    public void option_fullScreenNotification(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*3;
        
        currentDialouge = "Gotta restart the game for \nthat shit. \nHope you weren't busy IDIOT";
        for(String line: currentDialouge.split("\n")){
            g2.drawString(line, textX, textY);
            textY+= 40;
        }
        
        textY = frameY + gp.tileSize*9;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
            }
        }
    }
    
    public void option_control(int frameX, int frameY) {
        int textX;
        int textY;
        
        String text = "Controls";
        textX = getXforCenteredText(text);
        textY = frameY + gp.tileSize;
        g2.drawString(text, textX, textY);
        
        textX = frameX + gp.tileSize;
        textY += gp.tileSize;
        g2.drawString("Move", textX, textY); textY+=gp.tileSize;
        g2.drawString("Talk/Attack", textX, textY); textY+=gp.tileSize;
        g2.drawString("Throw up", textX, textY); textY+=gp.tileSize;
        g2.drawString("Inventory", textX, textY); textY+=gp.tileSize;
        g2.drawString("Pause", textX, textY); textY+=gp.tileSize;
        g2.drawString("Settings", textX, textY); textY+=gp.tileSize;
        
        textX = frameX + gp.tileSize*6;
        textY = frameY + gp.tileSize*2;
        g2.drawString("WASD", textX, textY);textY+=gp.tileSize;
        g2.drawString("ENTER", textX, textY);textY+=gp.tileSize;
        g2.drawString("E", textX, textY);textY+=gp.tileSize;
        g2.drawString("C", textX, textY);textY+=gp.tileSize;
        g2.drawString("P", textX, textY);textY+=gp.tileSize;
        g2.drawString("ESC", textX, textY);textY+=gp.tileSize;
        
        textX = frameX + gp.tileSize;
        textY = frameY + gp.tileSize*9;
        g2.drawString("Back", textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 3;
            }
        }
    }
    
    public void option_quitGameConfirm(int frameX, int frameY) {
        int textX = frameX + gp.tileSize;
        int textY = frameY + gp.tileSize*3;
        currentDialouge = "You sure?";
        
        for(String line: currentDialouge.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }
        
        String text = "Yes";
        textX = getXforCenteredText(text);
        textY += gp.tileSize*3;
        g2.drawString(text, textX, textY);
        if(commandNum == 0) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
                gp.gameState = gp.titleState;
            }
        }
        
        text = "No";
        textX = getXforCenteredText(text);
        textY += gp.tileSize;
        g2.drawString(text, textX, textY);
        if(commandNum == 1) {
            g2.drawString(">", textX-25, textY);
            if(gp.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 4;
            }
        }
    }
    
    public void drawTransition() {
        counter++;
        g2.setColor(new Color(0,0,0,counter*5));
        g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        
        if(counter == 50) {
            counter = 0;
            gp.gameState = gp.playState;
            gp.currentMap = gp.eHandler.tempMap;
            gp.player.worldX = gp.tileSize*gp.eHandler.tempCol;
            gp.player.worldY = gp.tileSize*gp.eHandler.tempRow;
            gp.eHandler.previousEventX = gp.player.worldX;
            gp.eHandler.previousEventY = gp.player.worldY;
        }
    }
    
    public void drawTradeScreen() {
        // CHECK IF IT'S A CHEST - if so, skip the buy/sell menu
        if(npc instanceof OBJ_Chest) {
            // For chests, go straight to showing the chest's inventory
            drawChestScreen();
        } else {
            // For NPCs like Josh, show the normal trade menu
            switch(subState) {
                case 0: trade_select(); break;
                case 1: trade_buy(); break;
                case 2: trade_sell(); break;
            }
        }
        gp.keyH.enterPressed = false;
    }
    
    public void drawChestScreen() {
        // Draw only the chest's inventory (no player inventory, no buy/sell buttons)
        drawInventory(npc, true);
        
        // Draw instruction at the bottom
        int x = gp.tileSize*2;
        int y = gp.tileSize*9;
        int width = gp.tileSize*6;
        int height = gp.tileSize*2;
        drawSubWindow(x,y,width,height);
        g2.drawString("ENTER=Take  Q=Close", x+24, y+60);
        
        // Handle taking items from the chest
        int itemIndex = getItemIndexOnSlot(npcSlotCol, npcSlotRow);
        if(itemIndex < npc.inventory.size()) {
            if(gp.keyH.enterPressed == true) {
                Entity item = npc.inventory.get(itemIndex);
                
                // Check if item is currency (pickup only type like coins)
                if(item.name.equals("$Fartcoin") || item.type == item.type_pickupOnly) {
                    // Add to player's currency instead of inventory
                    gp.player.$fartcoin += item.value;
                    gp.ui.addMessage("+" + item.value + " $Fartcoin");
                    gp.playSE(12); // Play coin sound
                    
                    // Remove from chest
                    npc.inventory.remove(itemIndex);
                    
                    // If chest is empty, close the chest screen
                    if(npc.inventory.size() == 0) {
                        gp.gameState = gp.dialougeState;
                        currentDialouge = "The chest is now empty!";
                    }
                } else {
                    // Regular items go to inventory
                    if(gp.player.inventory.size() < gp.player.maxInventorySize) {
                        // Add item to player inventory
                        gp.player.inventory.add(item);
                        gp.ui.addMessage("Obtained " + item.name + "!");
                        
                        // Remove item from chest
                        npc.inventory.remove(itemIndex);
                        
                        // If chest is empty, close the chest screen
                        if(npc.inventory.size() == 0) {
                            gp.gameState = gp.dialougeState;
                            currentDialouge = "The chest is now empty!";
                        }
                    } else {
                        gp.gameState = gp.dialougeState;
                        currentDialouge = "Inventory full!";
                    }
                }
            }
        }
    }
    
    public void trade_select() {
        drawDialougeScreen();
        
        int x = gp.tileSize * 13;
        int y = gp.tileSize * 4;
        int width = gp.tileSize * 5;
        int height = (int) (gp.tileSize * 3.5);
        drawSubWindow(x,y,width,height);
        
        x += gp.tileSize;
        y += gp.tileSize;
        g2.drawString("Buy", x, y);
        if(commandNum == 0) {
            g2.drawString(">", x-24, y);
            if(gp.keyH.enterPressed == true) {
                subState = 1;
            }
        }
        y += gp.tileSize;
        
        g2.drawString("Sell", x, y);
        if(commandNum == 1) {
            g2.drawString(">", x-24, y);
            if(gp.keyH.enterPressed == true) {
                subState = 2;
            }
        }
        y += gp.tileSize;
        
        g2.drawString("Leave", x, y);
        if(commandNum == 2) {
            g2.drawString(">", x-24, y);
            if(gp.keyH.enterPressed == true) {
                commandNum = 0;
                gp.gameState = gp.dialougeState;
                currentDialouge = "See you later!";
            }
        }
    }
    
    public void trade_buy() {
        drawInventory(gp.player, false);
        drawInventory(npc, true);
        
        int x = gp.tileSize*2;
        int y = gp.tileSize*9;
        int width = gp.tileSize*6;
        int height = gp.tileSize*2;
        drawSubWindow(x,y,width,height);
        g2.drawString("Q=Back", x+24, y+60);

        x = gp.tileSize*12;
        y = gp.tileSize*9;
        width = gp.tileSize*6;
        height = gp.tileSize*2;
        drawSubWindow (x, y, width, height);
        g2.drawString("$FartCoin:" + gp.player.$fartcoin, x+24, y+60);
        
        int itemIndex = getItemIndexOnSlot(npcSlotCol,npcSlotRow);
        if(itemIndex < npc.inventory.size()) {
            x = (int)(gp.tileSize*5.5);
            y = (int)(gp.tileSize*5.5);
            width = (int)(gp.tileSize*2.5);
            height = gp.tileSize;
            drawSubWindow (x, y, width, height);
            g2.drawImage(fartcoin, x+10, y+8, 32, 32, null);
            int price = npc.inventory.get(itemIndex).price;
            String text = "" + price;
            x = getXforAlignToRightText(text, gp.tileSize*8-20);
            g2.drawString(text, x, y+34);
            
            if(gp.keyH.enterPressed == true) {
                if(npc.inventory.get(itemIndex).price > gp.player.$fartcoin) {
                    subState = 0;
                    gp.gameState = gp.dialougeState;
                    currentDialouge = "Not enough Fartcoins.";
                    drawDialougeScreen();    
                } else if(gp.player.inventory.size() == gp.player.maxInventorySize) {
                    subState = 0;
                    gp.gameState = gp.dialougeState;
                    currentDialouge = "Inventory full!";
                } else {
                    gp.player.$fartcoin -= npc.inventory.get(itemIndex).price;
                    gp.player.inventory.add(npc.inventory.get(itemIndex));
                }
            }
        }
    }
    
    public void trade_sell() {
        drawInventory(gp.player, true);
        
        int x; 
        int y; 
        int width;
        int height; 
        
        x = gp.tileSize*2;
        y = gp.tileSize*9;
        width = gp.tileSize*6;
        height = gp.tileSize*2;
        drawSubWindow(x,y,width,height);
        g2.drawString("Q=Back", x+24, y+60);

        x = gp.tileSize*12;
        y = gp.tileSize*9;
        width = gp.tileSize*6;
        height = gp.tileSize*2;
        drawSubWindow (x, y, width, height);
        g2.drawString("$FartCoin:" + gp.player.$fartcoin, x+24, y+60);
        
        int itemIndex = getItemIndexOnSlot(playerSlotCol,playerSlotRow);
        if(itemIndex < gp.player.inventory.size()) {
            x = (int)(gp.tileSize*15.5);
            y = (int)(gp.tileSize*5.5);
            width = (int)(gp.tileSize*2.5);
            height = gp.tileSize;
            drawSubWindow (x, y, width, height);
            g2.drawImage(fartcoin, x+10, y+8, 32, 32, null);
            int price = gp.player.inventory.get(itemIndex).price/2;
            String text = "" + price;
            x = getXforAlignToRightText(text, gp.tileSize*18-20);
            g2.drawString(text, x, y+34);
            
            if(gp.keyH.enterPressed == true) {
                if(gp.player.inventory.get(itemIndex) == gp.player.currentWepon || 
                        gp.player.inventory.get(itemIndex) == gp.player.currentSheild) {
                    commandNum = 0;
                    subState = 0;
                    gp.gameState = gp.dialougeState;
                    currentDialouge = "Can't sell equipped!";
                    gp.ui.subState = 2; 
                    gp.gameState = gp.tradeState;
                } else {
                    gp.player.inventory.remove(itemIndex);
                    gp.player.$fartcoin += price;
                }
            }
        }
    }

    public int getItemIndexOnSlot(int slotCol, int slotRow) {
        int itemIndex = slotCol + (slotRow*5);
        return itemIndex;
    }    
    
    public void drawMessage() {
        int messageX = gp.tileSize;
        int messageY = gp.tileSize*4;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
        
        for(int i = 0; i< message.size(); i++) {
            if(message.get(i) != null) {
                g2.setColor(Color.black);
                g2.drawString(message.get(i), messageX+2, messageY+2);
                
                g2.setColor(Color.white);
                g2.drawString(message.get(i), messageX, messageY);
                
                int counter = messageCounter.get(i) +1;
                messageCounter.set(i, counter);
                messageY += 50;
                
                if(messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
        }
    }
    
    public void drawTitleScreen() {
        BufferedImage titleBackground = null;

        try {
            InputStream is = getClass().getResourceAsStream("/player/titlescreenboxboy.png");
            titleBackground = javax.imageio.ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (titleBackground != null) {
            g2.drawImage(titleBackground, 0, 0, gp.screenWidth, gp.screenHeight, null);
        } else {
            g2.setColor(new Color(41, 62, 146));
            g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
        }

        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));

        String[] options = {"New Game", "Load Game", "Fuck Off"};
        int y = gp.tileSize * 11;
        int x = (gp.screenWidth - (options.length * gp.tileSize * 6)) / 2 + gp.tileSize;

        for (int i = 0; i < options.length; i++) {
            String text = options[i];

            g2.setColor(Color.black);
            g2.drawString(text, x + 4, y + 4);

            g2.setColor(Color.red);
            g2.drawString(text, x, y);

            if (commandNum == i) {
                g2.drawString(">", x - gp.tileSize, y);
            }

            x += gp.tileSize * 6;
        }
    }

    public void drawPauseScreen() {
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 48f));
        String text = "PAUSED";
        int x = getXforCenteredText(text);
        int y = gp.screenHeight/2;
        
        g2.setColor(Color.black);
        g2.drawString(text, x+2, y+2);
        g2.setColor(Color.white);
        g2.drawString(text, x, y);
    }
    
    public void drawDialougeScreen() {
        int x = gp.tileSize*2;
        int y = gp.tileSize/2;
        int width = gp.screenWidth - (gp.tileSize*4);
        int height = gp.screenHeight / 3;
        
        drawSubWindow (x, y, width, height);
        g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 24f));
        x+= gp.tileSize;
        y += gp.tileSize;
        for(String line : currentDialouge.split("\n")) {
            g2.drawString(line, x, y);
            y += 40;
        }
    }
    
    public void drawCharacterScreen() {
        final int frameX = gp.tileSize*2;
        final int frameY = gp.tileSize;
        final int frameWidth = gp.tileSize*5;
        final int frameHeight = gp.tileSize*10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);
        
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(32F));
        
        int textX = frameX + 20;        
        int textY = frameY + gp.tileSize;
        final int lineHeight = 35;
        
        g2.drawString("Level", textX, textY);
        textY += lineHeight;
        g2.drawString("Life", textX, textY);
        textY += lineHeight;
        g2.drawString("Puke", textX, textY);
        textY += lineHeight;
        g2.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2.drawString("Next Lv", textX, textY);
        textY += lineHeight;
        g2.drawString("$fartcoin", textX, textY);
        textY += lineHeight + 10;
        g2.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        g2.drawString("Bloat", textX, textY);
        textY += lineHeight;
        
        int tailX = (frameX + frameWidth) - 30;
        textY = frameY + gp.tileSize;
        String value;
        
        value = String.valueOf(gp.player.level);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.life + "/" + gp.player.maxLife);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.mana + "/" + gp.player.maxMana);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.strength);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.dexterity);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.attack);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.defense);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.exp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.nextLevelExp);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        value = String.valueOf(gp.player.$fartcoin);
        textX = getXforAlignToRightText(value, tailX);
        g2.drawString(value, textX, textY);
        textY += lineHeight;
        
        g2.drawImage(gp.player.currentWepon.down1, tailX - gp.tileSize, textY - 24, null);
        textY += gp.tileSize;
        g2.drawImage(gp.player.currentSheild.down1, tailX - gp.tileSize, textY - 24, null);
    }
    
    public void drawSubWindow(int x, int y, int width, int height) {
        Color c = new Color(0,0,0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);
        
        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x+5, y+5, width - 10, height - 10, 25, 25);
    }
    
    public int getXforCenteredText(String text) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = gp.screenWidth/2 - length/2;
        return x;
    }
    
    public int getXforAlignToRightText(String text, int tailX) {
        int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = tailX - length;
        return x;
    }
}