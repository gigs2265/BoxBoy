package main;

public class EventHandler {
    Gamepanel gp;

    EventRect eventRect[][][];
    int previousEventX, previousEventY;
    boolean canTouchEvent = true;
    int tempMap, tempCol, tempRow;

    public EventHandler(Gamepanel gp) {
        this.gp = gp;
        eventRect = new EventRect[gp.maxMap][gp.maxWorldCol][gp.maxWorldRow];
        int map = 0;
        int col = 0;
        int row = 0;

        while(map < gp.maxMap && col < gp.maxWorldCol && row < gp.maxWorldRow) {

            eventRect[map][col][row] = new EventRect();
            eventRect[map][col][row].x = 23;
            eventRect[map][col][row].y = 23;
            eventRect[map][col][row].width = 3;
            eventRect[map][col][row].height = 3;
            eventRect[map][col][row].eventRectDefaultX = eventRect[map][col][row].x;
            eventRect[map][col][row].eventRectDefaultY = eventRect[map][col][row].y;

            col++;
            if(col == gp.maxWorldCol) {
                col = 0;
                row++;

                if(row == gp.maxWorldRow) {
                    row = 0;
                    map++;
                }
            }
        }
    }

    public void checkEvent() {

        // CHECK IF THE PLAYER IS MORE THAN 1 TILE AWAY FROM LAST EVENT
        int xDistance = Math.abs(gp.player.worldX - previousEventX);
        int yDistance = Math.abs(gp.player.worldY - previousEventY);
        int distance = Math.max(xDistance, yDistance);
        if(distance > gp.tileSize) {
            canTouchEvent = true;
        }
        if(canTouchEvent == true) {
            if(hit(0, 28, 31, "up") == true) {damagePit(28);}
            else if(hit(0, 13, 23, "any") == true) {damagePit(13);}
            else if(hit(0, 48, 5, "any") == true) {teleport(1, 5, 47);}  // Enter dungeon: spawn in the small entrance hallway
            else if(hit(1, 2, 47, "any") == true) {teleport(0, 47, 5);}  // Exit dungeon via stairs (spawn next to overworld stairs, not on them)
            // JOSH'S HUT - step onto the hut to go inside, step on the door to leave
            else if(hit(0, 11, 30, "any") == true) {teleport(2, 25, 26);}
            else if(hit(2, 25, 28, "any") == true) {teleport(0, 11, 31);}
            // NICK INTRO CUTSCENE - fires once when entering Nick's room (3-tile-wide opening)
            else if(hit(1, 14, 29, "any") == true) {nickIntro();}
            else if(hit(1, 14, 30, "any") == true) {nickIntro();}
            else if(hit(1, 14, 31, "any") == true) {nickIntro();}
        }
    }
    
    public boolean hit(int map, int col, int row, String reqDirection) {
        boolean hit = false;

        if(map == gp.currentMap) {
            gp.player.solidArea.x = gp.player.worldX + gp.player.solidArea.x;
            gp.player.solidArea.y = gp.player.worldY + gp.player.solidArea.y;
            eventRect[map][col][row].x = col * gp.tileSize + eventRect[map][col][row].x;
            eventRect[map][col][row].y = row * gp.tileSize + eventRect[map][col][row].y;

            if (gp.player.solidArea.intersects(eventRect[map][col][row]) && eventRect[map][col][row].eventDone == false) {

                if (reqDirection.contentEquals("any") || gp.player.direction.contentEquals(reqDirection)) {
                    hit = true;

                    previousEventX = gp.player.worldX;
                    previousEventY = gp.player.worldY;
                }
            }

            gp.player.solidArea.x = gp.player.solidAreaDefaultX;
            gp.player.solidArea.y = gp.player.solidAreaDefaultY;
            eventRect[map][col][row].x = eventRect[map][col][row].eventRectDefaultX;
            eventRect[map][col][row].y = eventRect[map][col][row].eventRectDefaultY;
        }

        return hit;
    }

    public void damagePit(int gameState) {
        gp.gameState = gameState;
        gp.ui.currentDialouge = PcText.pick(gp,
            "FUCK! THAT HURTS...I need a Beer.",
            "Ouch! That was uncomfortable. \nA beverage would be nice.");
        gp.player.life -= 1;
        canTouchEvent = false;
    }
    
    public void teleport(int map, int col, int row) {
        gp.gameState = gp.transitionState;
        tempMap = map;
        tempCol = col;
        tempRow = row;
        canTouchEvent = false;
        gp.playSE(14);
        gp.stopMusic();
        // Play the right music for wherever we're going
        if(map == 1) {
            gp.playMusic(15); // dungeon music
        } else {
            gp.playMusic(0);  // island music
        }
    }
    
    public void nickIntro() {
        if(gp.quest.metNick == false && gp.quest.defeatedNick == false) {
            gp.quest.metNick = true;
            String[] lines = gp.pcMode ? gp.ui.nickCutscenePc : gp.ui.nickCutscene;
            gp.ui.startCutscene(lines, gp.ui.CUTSCENE_NICK_INTRO, null);
        }
    }
}
