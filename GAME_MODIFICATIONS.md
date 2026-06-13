# My2dgame - Complete Modification Documentation

## Project Overview
This is a 2D action RPG Java game called "Box Boy!" built with Java Swing/AWT. The player (Theo) explores an island with friends who have been captured and must defeat enemies to escape.

**Technical Specs:**
- Built with Java Swing/AWT
- 60 FPS game loop
- Tile size: 16px scaled 3x = 48px tiles
- Screen: 960x576 (20x12 tiles)
- World: 50x50 tiles per map
- 2 maps: Overworld (Map 0) and Dungeon (Map 1)

---

## Modification 1: Camera Edge Clamping (Fixed Black Borders)

### Problem
When the player moved near map edges, the camera would show black areas outside the map boundaries.

### Solution
Implemented camera bounds clamping to prevent black areas from showing.

### Files Modified

#### **1. Gamepanel.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\main\Gamepanel.java`)

**Added methods (lines 301-329):**
```java
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
```

#### **2. TileManager.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\tile\TileManager.java`)

**Modified draw() method (lines 148-173):**
- Changed from using `gp.player.worldX/worldY` directly
- Now uses `gp.getCameraX()` and `gp.getCameraY()` for clamped camera position
- Updated visibility checks to use clamped camera coordinates

#### **3. Entity.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\entity\Entity.java`)

**Modified draw() method (lines 215-227):**
- Changed screen position calculation to use clamped camera
- Updated visibility bounds checking

#### **4. Player.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\entity\Player.java`)

**Modified draw() method (lines 435-442):**
- Player sprite position now adjusts when camera is clamped at edges
- Allows player to move away from screen center when near map boundaries

### Result
- Camera stops at map edges
- No black areas visible
- Player sprite smoothly moves toward screen edges when approaching map boundaries
- All entities (NPCs, monsters, objects) render correctly at map edges

---

## Modification 2: Jimmy's Ass Weapon Sound Effect

### Problem
All weapons used the same attack sound (spray sound effect #6).

### Solution
Added weapon-specific sound effects, specifically for Jimmy's Ass weapon to use the fart blast sound.

### Files Modified

#### **Player.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\entity\Player.java`)

**Modified interactNPC() method (lines 343-359):**
```java
public void interactNPC(int i) {
    if (keyH.enterPressed) {
        if (i != 999) {
            gp.gameState = gp.dialougeState;
            gp.npc[gp.currentMap][i].speak();
        } else {
            // Play different attack sound based on weapon type
            if (currentWepon.type == type_ass) {
                gp.playSE(16); // Fart blast sound for Jimmy's Ass
            } else {
                gp.playSE(6); // Spray sound for other weapons
            }
            attacking = true;
        }
        keyH.enterPressed = false;
    }
}
```

### Sound Effect Mapping
- Sound 6 = `spraysound.wav` (default weapons)
- Sound 16 = `fartblast.wav` (Jimmy's Ass weapon)

### Result
- Jimmy's Ass weapon now plays fart blast sound when attacking
- All other weapons continue using the spray sound
- Adds humor and weapon personality

---

## Modification 3: Progressive Quest System

### Overview
Implemented a comprehensive quest progression system that guides the player through finding friends in a specific order, then completing dungeon objectives.

### Quest Progression Chain
1. **Find all your friends** - Find 5 NPCs on overworld in order
   - Talk to Vic (complete all 11 dialogues)
   - Find Mike (1 dialogue)
   - Find Ian (complete all 12 dialogues)
   - Find Liam (complete all 14 dialogues)
   - Find Miles (complete all 13 dialogues) - Requires unlocking special door
2. **Wait, Where's John?** - Find John in the dungeon
3. **Kill Nick** - Defeat Nick mini-boss
4. **Kill Brian** - Defeat Brian final boss

### Files Modified

#### **1. Quest.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\main\Quest.java`)

**Complete rewrite with new quest tracking:**

```java
package main;

public class Quest {
    Gamepanel gp;
    public String name;

    // Main quest stages
    public boolean foundAllFriends = false;
    public boolean foundJohn = false;
    public boolean defeatedNick = false;
    public boolean defeatedBrian = false;

    // Friend-finding sub-quests (overworld)
    public boolean talkedToVic = false;
    public boolean foundMike = false;
    public boolean foundIan = false;
    public boolean foundLiam = false;
    public boolean foundMiles = false;

    public Quest(Gamepanel gp) {
        this.gp = gp;
        this.name = "Find all your friends";
    }

    public String getQuestName() {
        if(!foundAllFriends) {
            return "Find all your friends";
        } else if(!foundJohn) {
            return "Wait, Where's John?";
        } else if(!defeatedNick) {
            return "Kill Nick";
        } else if(!defeatedBrian) {
            return "Kill Brian";
        } else {
            return "Quest Complete!";
        }
    }

    public String getCurrentObjective() {
        if(!foundAllFriends) {
            return "Find your friends on the island";
        } else if(!foundJohn) {
            return "Find John";
        } else if(!defeatedNick) {
            return "Defeat Nick";
        } else if(!defeatedBrian) {
            return "Defeat Brian";
        } else {
            return "Escape the island!";
        }
    }

    // Completion methods
    public void completeTalkToVic() { ... }
    public void completeFoundMike() { ... }
    public void completeFoundIan() { ... }
    public void completeFoundLiam() { ... }
    public void completeFoundMiles() { ... }
    public void completeFoundAllFriends() { ... }
    public void completeFoundJohn() { ... }
    public void completeDefeatedNick() { ... }
    public void completeDefeatedBrian() { ... }

    public boolean canUnlockMilesDoor() {
        return foundLiam;
    }
}
```

#### **2. Entity.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\entity\Entity.java`)

**Added dialogue tracking (line 43):**
```java
public boolean dialogueComplete = false; // Track if all dialogues have been shown
```

**Modified speak() method (lines 106-128):**
```java
public void speak() {
    if(dialouges[dialougeIndex] == null) {
        dialougeIndex = 0;
        dialogueComplete = true; // Reached the end of dialogues
    }
    gp.ui.currentDialouge = dialouges[dialougeIndex];
    dialougeIndex++;

    switch(gp.player.direction) {
        case"up": direction = "down"; break;
        case "down": direction = "up"; break;
        case "left": direction = "right"; break;
        case "right": direction = "left"; break;
    }
}
```

#### **3. NPC_Vic.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\entity\NPC_Vic.java`)

**Modified speak() method:**
```java
public void speak() {
    super.speak();

    // Check if all dialogues are complete
    if(dialogueComplete && !gp.quest.talkedToVic) {
        gp.quest.completeTalkToVic();
    }
}
```

#### **4. NPC_Mike.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\entity\NPC_Mike.java`)

**Modified speak() method:**
```java
public void speak() {
    gp.ui.currentDialouge = dialouges[0];
    super.speak();

    // Mike only has 1 dialogue, so complete after first talk if Vic quest is done
    if(gp.quest.talkedToVic && !gp.quest.foundMike) {
        gp.quest.completeFoundMike();
    }
}
```

#### **5. NPC_Ian.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\entity\NPC_Ian.java`)

**Modified speak() method:**
```java
public void speak() {
    gp.ui.currentDialouge = dialouges[0];
    super.speak();

    // Check if all dialogues are complete and Mike quest is done
    if(dialogueComplete && gp.quest.foundMike && !gp.quest.foundIan) {
        gp.quest.completeFoundIan();
    }
}
```

#### **6. NPC_Liam.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\entity\NPC_Liam.java`)

**Modified speak() method:**
```java
public void speak() {
    gp.ui.currentDialouge = dialouges[0];
    super.speak();

    // Check if all dialogues are complete and Ian quest is done
    if(dialogueComplete && gp.quest.foundIan && !gp.quest.foundLiam) {
        gp.quest.completeFoundLiam();
    }
}
```

#### **7. NPC_Miles.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\entity\NPC_Miles.java`)

**Modified speak() method:**
```java
public void speak() {
    gp.ui.currentDialouge = dialouges[0];
    super.speak();

    // Check if all dialogues are complete and Liam quest is done
    if(dialogueComplete && gp.quest.foundLiam && !gp.quest.foundMiles) {
        gp.quest.completeFoundMiles();
    }
}
```

#### **8. NPC_John.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\entity\NPC_John.java`)

**Modified speak() method:**
```java
public void speak() {
    gp.ui.currentDialouge = dialouges[0];
    super.speak();

    // Complete John quest when all dialogues are done and all friends are found
    if(dialogueComplete && gp.quest.foundAllFriends && !gp.quest.foundJohn) {
        gp.quest.completeFoundJohn();
    }
}
```

#### **9. Player.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\entity\Player.java`)

**Added imports:**
```java
import object.OBJ_MilesDoor;
```

**Modified damageMonster() method (lines 397-417):**
```java
if(gp.monster[gp.currentMap][i].life <= 0) {
    String monsterName = gp.monster[gp.currentMap][i].name;

    if(monsterName.equals("Brian")) {
        // BOSS CUTSCENE - Brian is too weak to fight, talks before dying
        gp.ui.startBrianCutscene(gp.monster[gp.currentMap][i]);
        // Complete Brian quest
        if(!gp.quest.defeatedBrian) {
            gp.quest.completeDefeatedBrian();
        }
    }
    else {
        gp.monster[gp.currentMap][i].dying = true;
        gp.ui.addMessage("You killed " + monsterName + "!");
        gp.ui.addMessage("Exp +" + gp.monster[gp.currentMap][i].exp);
        exp += gp.monster[gp.currentMap][i].exp;
        checkLevelUp();

        // Complete Nick quest
        if(monsterName.equals("Nick") && !gp.quest.defeatedNick) {
            gp.quest.completeDefeatedNick();
        }
    }
}
```

**Modified pickUpObject() method (lines 308-316):**
```java
else if(objectName.equals("Miles' Door")) {
    OBJ_MilesDoor milesDoor = (OBJ_MilesDoor)gp.obj[gp.currentMap][i];
    if(searchItemInInventory("Key") != 999 && gp.quest.canUnlockMilesDoor()) {
        // Only unlock and remove door if quest is complete AND player has key
        milesDoor.use(this);
        gp.obj[gp.currentMap][i] = null;
    } else {
        // Show appropriate message based on quest status
        milesDoor.interact();
    }
}
```

#### **10. UI.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\main\UI.java`)

**Completely rewrote drawQuestTracker() method (lines 151-205):**
```java
public void drawQuestTracker() {
    int x = gp.tileSize * 12;
    int y = gp.tileSize / 2;
    int width = gp.tileSize * 7;
    int height = gp.tileSize * 5; // Increased height for friend list

    drawSubWindow(x, y, width, height);

    x += 25;
    y += gp.tileSize - 10;

    g2.setFont(g2.getFont().deriveFont(Font.BOLD, 18f));
    g2.drawString("QUEST: " + gp.quest.getQuestName(), x, y);

    y += 35;
    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16f));
    g2.drawString(gp.quest.getCurrentObjective(), x, y);

    y += 28;
    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 15f));

    // Show different objectives based on quest stage
    if(!gp.quest.foundAllFriends) {
        // Show friend checklist
        String vic = (gp.quest.talkedToVic ? "[X]" : "[ ]") + " Vic";
        g2.drawString(vic, x, y);
        y += 22;

        String mike = (gp.quest.foundMike ? "[X]" : "[ ]") + " Mike";
        g2.drawString(mike, x, y);
        y += 22;

        String ian = (gp.quest.foundIan ? "[X]" : "[ ]") + " Ian";
        g2.drawString(ian, x, y);
        y += 22;

        String liam = (gp.quest.foundLiam ? "[X]" : "[ ]") + " Liam";
        g2.drawString(liam, x, y);
        y += 22;

        String miles = (gp.quest.foundMiles ? "[X]" : "[ ]") + " Miles";
        g2.drawString(miles, x, y);
    } else if(!gp.quest.foundJohn) {
        // John quest
        String john = "[ ] Find John";
        g2.drawString(john, x, y);
    } else if(!gp.quest.defeatedNick) {
        // Nick quest
        String nick = "[ ] Defeat Nick";
        g2.drawString(nick, x, y);
    } else if(!gp.quest.defeatedBrian) {
        // Brian quest
        String brian = "[ ] Defeat Brian";
        g2.drawString(brian, x, y);
    } else {
        // All complete!
        g2.drawString("[X] All Quests Complete!", x, y);
    }
}
```

### Files Created

#### **OBJ_MilesDoor.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\object\OBJ_MilesDoor.java`)

**New special quest-locked door:**
```java
package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_MilesDoor extends Entity {

    Gamepanel gamePanel;

    public OBJ_MilesDoor(Gamepanel gp) {
        super(gp);
        this.gamePanel = gp;

        name = "Miles' Door";
        down1 = setup("/objects/door", gp.tileSize, gp.tileSize);
        collision = true;

        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }

    public void interact() {
        gamePanel.gameState = gamePanel.dialougeState;

        if(!gamePanel.quest.foundLiam) {
            gamePanel.ui.currentDialouge = "This door won't budge... \nMaybe I should find the others first.";
        } else {
            gamePanel.ui.currentDialouge = "It's locked! I need a key.";
        }
    }

    public void use(Entity entity) {
        gamePanel.gameState = gamePanel.dialougeState;

        // Check if Liam quest is complete first
        if(!gamePanel.quest.canUnlockMilesDoor()) {
            gamePanel.ui.currentDialouge = "This door won't open yet... \nI should find the others first.";
            // Don't consume the key or remove the door
            return;
        }

        // Now check for key
        int keyIndex = 999;
        for(int i = 0; i < entity.inventory.size(); i++) {
            if(entity.inventory.get(i).name.equals("Key")) {
                keyIndex = i;
                break;
            }
        }

        if(keyIndex != 999) {
            // Only remove key and allow door to be removed if quest is complete
            entity.inventory.remove(keyIndex);
            gamePanel.ui.currentDialouge = "Door Unlocked! \nMiles should be inside.";
            gamePanel.playSE(5);
            // Door will be removed by Player.pickUpObject after this returns
        } else {
            gamePanel.ui.currentDialouge = "You need a key to open this door!";
        }
    }
}
```

#### **AssetSetter.java** (`C:\Users\Owner\Documents\Eclipse workspace\My2dgame\src\main\AssetSetter.java`)

**Added import:**
```java
import object.OBJ_MilesDoor;
```

**Replaced door at (38, 12) with Miles' special door (lines 81-84):**
```java
// MILES' DOOR - Special quest-locked door
gp.obj[mapNum][i] = new OBJ_MilesDoor(gp);
gp.obj[mapNum][i].worldX = gp.tileSize * 38;
gp.obj[mapNum][i].worldY = gp.tileSize * 12;
i++;
```

### Quest System Features

**Linear Progression:**
- Player must complete quests in order
- Cannot skip ahead
- Quest tracker shows current objective

**Dialogue Completion Tracking:**
- NPCs track when all dialogues have been shown
- Quest only completes after full conversation
- Prevents accidental quest completion

**Visual Feedback:**
- On-screen messages for each quest update
- Individual checkmarks for each friend found
- Clear quest names that change as you progress

**Quest-Locked Door:**
- Miles' door cannot be opened until Liam quest is complete
- Shows different messages based on quest progress
- Door stays locked even with a key if quest isn't done
- Prevents sequence breaking

### Quest Tracker Display

**Press P to view quest tracker showing:**

**Quest 1: "Find all your friends"**
- [ ] Vic
- [ ] Mike
- [ ] Ian
- [ ] Liam
- [ ] Miles

**Quest 2: "Wait, Where's John?"**
- [ ] Find John

**Quest 3: "Kill Nick"**
- [ ] Defeat Nick

**Quest 4: "Kill Brian"**
- [ ] Defeat Brian

### NPC & Location Information

**Overworld NPCs (Map 0):**
- Vic (22, 22) - 11 dialogues
- Mike (3, 34) - 1 dialogue
- Ian (17, 6) - 12 dialogues
- Liam (30, 45) - 14 dialogues
- Miles (38, 4) - 13 dialogues (behind locked door at 38, 12)

**Dungeon NPCs (Map 1):**
- John (5, 41) - 7 dialogues (first cell)
- Josh (42, 34) - Merchant

**Boss Locations (Map 1):**
- Nick (9, 29) - Mini-boss, drops key
- Brian (22, 8) - Final boss

---

## Summary of All Modifications

### Total Files Modified: 13
1. Gamepanel.java - Camera clamping methods
2. TileManager.java - Camera-aware rendering
3. Entity.java - Camera rendering + dialogue tracking
4. Player.java - Camera rendering, weapon sounds, quest integration, Miles' door handling
5. Quest.java - Complete quest system rewrite
6. UI.java - Dynamic quest tracker display
7. NPC_Vic.java - Quest completion trigger
8. NPC_Mike.java - Quest completion trigger
9. NPC_Ian.java - Quest completion trigger
10. NPC_Liam.java - Quest completion trigger
11. NPC_Miles.java - Quest completion trigger
12. NPC_John.java - Quest completion trigger
13. AssetSetter.java - Miles' door placement

### Total Files Created: 1
1. OBJ_MilesDoor.java - Quest-locked door

### Key Features Added
✅ Camera edge clamping (no black borders)
✅ Weapon-specific sound effects
✅ Progressive quest system with 4 stages
✅ Individual friend tracking with checkmarks
✅ Dialogue completion detection
✅ Quest-locked door mechanic
✅ Boss defeat quest integration
✅ Dynamic quest tracker UI

### Testing Checklist
- [X] Camera doesn't show black at map edges
- [X] Jimmy's Ass plays fart sound
- [X] Quest progression enforces order (Vic → Mike → Ian → Liam → Miles)
- [X] Miles' door locked until Liam quest complete
- [X] Miles' door shows quest-specific messages
- [X] Friend checklist displays correctly
- [X] John quest triggers after finding Miles
- [X] Nick quest triggers after finding John
- [X] Brian quest triggers after defeating Nick
- [X] Victory triggers after defeating Brian

---

## Development Notes

### Compilation
All files must be compiled to the `bin` directory:
```bash
cd "C:\Users\Owner\Documents\Eclipse workspace\My2dgame"
javac -d bin -cp bin src/path/to/File.java
```

### Eclipse Workflow
1. After modifying files, refresh project (F5)
2. Clean project (Project → Clean)
3. Eclipse auto-builds
4. If issues persist, manually compile via command line

### Important Constants
- `type_ass = 9` (Jimmy's Ass weapon type)
- Sound effect 6 = spray sound
- Sound effect 16 = fart blast
- Map 0 = Overworld
- Map 1 = Dungeon

---

## Future Enhancement Ideas
- Save/load quest progress
- Optional side quests
- Quest rewards (items, XP bonuses)
- Quest journal with full history
- Multiple quest-locked doors
- Achievement system tied to quests
