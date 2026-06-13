package entity;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import main.Gamepanel;
import main.KeyHandler;
import object.OBJ_Bloat;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_MilesDoor;
import object.OBJ_VicDoor;
import object.OBJ_Key;
import object.OBJ_Milkshot;
import object.OBJ_Spray_Normal;

public class Player extends Entity {

    KeyHandler keyH;
    public final int screenX;
    public final int screenY;
    
    public Player(Gamepanel gp, KeyHandler keyH) {
        super(gp);
        this.keyH = keyH;

        screenX = gp.screenWidth / 2 - (gp.tileSize / 2);
        screenY = gp.screenHeight / 2 - (gp.tileSize / 2);
        solidArea = new Rectangle(8, 16, 22, 22);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        
        setDefaultValues();
        getPlayerImage();
        getPlayerAttackImage();
        setItems();
    }

    public void setDefaultValues() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        speed = 4;
        direction = "down";

        level = 1;
        maxLife = 6;
        maxMana = 4;
        mana = maxMana;
        life = maxLife;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;
        $fartcoin = 5;
        currentWepon = new OBJ_Spray_Normal(gp);
        currentSheild = new OBJ_Bloat(gp);
        projectile = new OBJ_Milkshot(gp);
        attack = getAttack();
        defense = getDefense();
    }
    
    public void setDefaultPositions() {
        worldX = gp.tileSize * 23;
        worldY = gp.tileSize * 21;
        direction = "down";
    }
    
    public void restoreLifeAndMana() {
        life = maxLife;
        mana = maxMana;
        invincible = false;
    }
    
    public void setItems() {
        inventory.clear();
        inventory.add(currentWepon);
        inventory.add(currentSheild);
        inventory.add(new OBJ_Key(gp));
    }
    
    public int getAttack() {
        attackArea = currentWepon.attackArea;
        return attack = strength * currentWepon.attackValue;
    }
    
    public int getDefense() {
        return defense = dexterity * currentSheild.defenseValue;
    }

    public void getPlayerImage() {
        try {
            up1 = ImageIO.read(getClass().getResource("/player/theoup1.png"));
            up2 = ImageIO.read(getClass().getResource("/player/theoup2.png"));
            down1 = ImageIO.read(getClass().getResource("/player/downtheo1.png"));
            down2 = ImageIO.read(getClass().getResource("/player/downtheo2.png"));
            left1 = ImageIO.read(getClass().getResource("/player/theoleft1.png"));
            left2 = ImageIO.read(getClass().getResource("/player/theoleft2.png"));
            right1 = ImageIO.read(getClass().getResource("/player/theoright1.png"));
            right2 = ImageIO.read(getClass().getResource("/player/theoright2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getPlayerAttackImage() {
        if(currentWepon.type == type_spray) {
            attackUp1 = setup("/player/theosprayup1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/theosprayup2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/theospraydown1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/theospraydown2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/theosprayleft1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/theosprayleft2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/theosprayright1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/theosprayright2", gp.tileSize * 2, gp.tileSize);
        }
       
        if(currentWepon.type == type_cutter) {
            attackUp1 = setup("/player/boxcutterup1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/boxcutterup2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/boxcutterdown1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/boxcutterdown2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/boxcutterleft1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/boxcutterleft2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/boxcutterright1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/boxcutterright2", gp.tileSize * 2, gp.tileSize);
        }
        
        if(currentWepon.type == type_dildo) {
            attackUp1 = setup("/player/dildoup1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/dildoup2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/dildodown1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/dildodown2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/dildoleft1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/dildoleft2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/dildoright1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/dildoright2", gp.tileSize * 2, gp.tileSize);
        }

        if(currentWepon.type == type_ass) {
            attackUp1 = setup("/player/jimmysassup1", gp.tileSize, gp.tileSize * 2);
            attackUp2 = setup("/player/jimmysassup2", gp.tileSize, gp.tileSize * 2);
            attackDown1 = setup("/player/jimmysassdown1", gp.tileSize, gp.tileSize * 2);
            attackDown2 = setup("/player/jimmysassdown2", gp.tileSize, gp.tileSize * 2);
            attackLeft1 = setup("/player/jimmysassleft1", gp.tileSize * 2, gp.tileSize);
            attackLeft2 = setup("/player/jimmysassleft2", gp.tileSize * 2, gp.tileSize);
            attackRight1 = setup("/player/jimmysassright1", gp.tileSize * 2, gp.tileSize);
            attackRight2 = setup("/player/jimmysassright2", gp.tileSize * 2, gp.tileSize);
        }
    }

    public void update() {
        if (attacking) {
            attacking();
        }

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.enterPressed) {
            if (keyH.upPressed) {
                direction = "up";
            } else if (keyH.downPressed) {
                direction = "down";
            } else if (keyH.leftPressed) {
                direction = "left";
            } else if (keyH.rightPressed) {
                direction = "right";
            }

            collisionOn = false;
            gp.cChecker.checkTile(this);

            int objIndex = gp.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            int npcIndex = gp.cChecker.checkEntity(this, gp.npc);
            interactNPC(npcIndex);

            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            contactMonster(monsterIndex);

            gp.eHandler.checkEvent();

            if (!collisionOn && !keyH.enterPressed) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;
                    case "down":
                        worldY += speed;
                        break;
                    case "left":
                        worldX -= speed;
                        break;
                    case "right":
                        worldX += speed;
                        break;
                }
            }

            keyH.enterPressed = false;

            // Don't touch spriteCounter while an attack is playing - the attack
            // animation uses the same counter to know when to end. If the walk
            // animation keeps resetting it, the attack repeats forever.
            if (attacking == false) {
                spriteCounter++;
                if (spriteCounter > 12) {
                    spriteNum = (spriteNum == 1) ? 2 : 1;
                    spriteCounter = 0;
                }
            }
        }
        
        if(gp.keyH.shotKeyPressed == true && projectile.alive == false && 
           shotAvailableCounter == 30 && projectile.haveResource(this) == true) {
            projectile.set(worldX, worldY, direction, true, this);
            projectile.subtractResource(this);
            gp.projectileList.add(projectile);
            shotAvailableCounter = 0;
            gp.playSE(11);
        }

        if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 60) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
        
        if(shotAvailableCounter < 30) {
            shotAvailableCounter++;
        }
        
        if(life > maxLife) {
            life = maxLife;
        }
        
        if (mana > maxMana) {
            mana = maxMana;
        } 
        
        if(life <= 0) {
            gp.gameState = gp.gameOverState;
            gp.ui.commandNum = -1;
            gp.stopMusic();
            gp.playSE(13);
        }
    }

    public void attacking() {
        spriteCounter++;
        if (spriteCounter <= 5) {
            spriteNum = 1;
        } else if (spriteCounter <= 25) {
            spriteNum = 2;
            
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;
            
            switch(direction) {
                case "up":
                    worldY -= attackArea.height;
                    break;
                case "down":
                    worldY += attackArea.height;
                    break;
                case "left":
                    worldX -= attackArea.width;
                    break;
                case "right":
                    worldX += attackArea.width;
                    break;
            }
            
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            int monsterIndex = gp.cChecker.checkEntity(this, gp.monster);
            damageMonster(monsterIndex, attack);
            
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;
        } else {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void pickUpObject(int i) {
        if (i != 999) {
            String objectName = gp.obj[gp.currentMap][i].name;

            if(objectName.equals("Door")) {
                OBJ_Door door = (OBJ_Door)gp.obj[gp.currentMap][i];
                if(searchItemInInventory("Key") != 999) {
                    door.use(this);
                    gp.obj[gp.currentMap][i] = null;
                } else {
                    door.interact();
                }
            }
            else if(objectName.equals("Vic's Door")) {
                OBJ_VicDoor vicDoor = (OBJ_VicDoor)gp.obj[gp.currentMap][i];
                if(searchItemInInventory("Key") != 999 && gp.quest.talkedToVic) {
                    // Only unlock and remove door if talked to Vic AND player has key
                    vicDoor.use(this);
                    gp.obj[gp.currentMap][i] = null;
                } else {
                    // Show appropriate message based on quest status
                    vicDoor.interact();
                }
            }
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
            else if(objectName.equals("Chest")) {
                OBJ_Chest chest = (OBJ_Chest)gp.obj[gp.currentMap][i];
                if(searchItemInInventory("Key") != 999) {
                    chest.use(this);
                } else {
                    chest.interact();
                }
            }
            else if(gp.obj[gp.currentMap][i].type == type_pickupOnly) {
                gp.obj[gp.currentMap][i].use(this);
                gp.obj[gp.currentMap][i] = null;
            }
            else {
                String text;
                if(inventory.size() != maxInventorySize) {
                    inventory.add(gp.obj[gp.currentMap][i]);
                    gp.playSE(4);
                    text = "I got a " + gp.obj[gp.currentMap][i].name + "!";
                } else {
                    text = "Inventory Full";
                }
                gp.ui.addMessage(text);
                gp.obj[gp.currentMap][i] = null;
            }
        }
    }
    
    public int searchItemInInventory(String itemName) {
        for(int i = 0; i < inventory.size(); i++) {
            if(inventory.get(i).name.equals(itemName)) {
                return i;
            }
        }
        return 999;
    }

    public void interactNPC(int i) {
        if (keyH.enterPressed) {
            if (i != 999) {
                gp.gameState = gp.dialougeState;
                gp.currentNPC = i; // Store which NPC we're talking to
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

    public void contactMonster(int i) {
        if (i != 999 && !invincible) {
            gp.playSE(8);
            int damage = gp.monster[gp.currentMap][i].attack - defense;
            if(damage < 0) {
                damage = 0;
            }
            life -= damage;
            invincible = true;
        }
    }
    
    public void damageMonster(int i, int attack) {
        if(i != 999) {
            if(gp.monster[gp.currentMap][i].invincible == false) {
                gp.playSE(7);
                int damage = attack - gp.monster[gp.currentMap][i].defense;
                if(damage < 0) {
                    damage = 0;
                }
                gp.monster[gp.currentMap][i].life -= damage;
                gp.monster[gp.currentMap][i].invincible = true;
                gp.monster[gp.currentMap][i].damageReaction();
                
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
            }
        }
    }
    
    public void checkLevelUp() {
        if(exp >= nextLevelExp) {
            level++;
            nextLevelExp = nextLevelExp * 5;
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();
            
            gp.playSE(9);
            gp.gameState = gp.dialougeState;
            gp.ui.currentDialouge = "Your dumbass reached level " + level + "\nGood job idiot!";
        }
    }
    
    public void selectItem() {
        int itemIndex = gp.ui.getItemIndexOnSlot(gp.ui.playerSlotCol, gp.ui.playerSlotRow);
        if(itemIndex < inventory.size()) {
            Entity selectedItem = inventory.get(itemIndex);
            
            if(selectedItem.type == type_spray || selectedItem.type == type_cutter || 
               selectedItem.type == type_dildo || selectedItem.type == type_ass) {
                currentWepon = selectedItem;
                attack = getAttack();
                getPlayerAttackImage();
            }
            if(selectedItem.type == type_sheild) {
                currentSheild = selectedItem;
                defense = getDefense();
            }
            if(selectedItem.type == type_consumable) {
                selectedItem.use(this);
                inventory.remove(itemIndex);
            }
        }
    }
    
    public void draw(Graphics2D g2) {
        BufferedImage image = null;

        // Adjust player screen position when camera is clamped at map edges
        int cameraX = gp.getCameraX();
        int cameraY = gp.getCameraY();
        int tempScreenX = worldX - cameraX;
        int tempScreenY = worldY - cameraY;

        switch (direction) {
            case "up":
                if(attacking == false) {
                    if(spriteNum == 1) {image = up1;}
                    if(spriteNum == 2) {image = up2;}
                }
                if(attacking == true) {
                    if(spriteNum == 1) {image = attackUp1;}
                    if(spriteNum == 2) {image = attackUp2;}
                }
                break;
            case "down":
                if(attacking == false) {
                    if(spriteNum == 1) {image = down1;}
                    if(spriteNum == 2) {image = down2;}
                }
                if(attacking == true) {
                    if(spriteNum == 1) {image = attackDown1;}
                    if(spriteNum == 2) {image = attackDown2;}
                }
                break;
            case "left":
                if(attacking == false) {
                    if(spriteNum == 1) {image = left1;}
                    if(spriteNum == 2) {image = left2;}
                }
                if(attacking == true) {
                    if(spriteNum == 1) {image = attackLeft1;}
                    if(spriteNum == 2) {image = attackLeft2;}
                }
                break;
            case "right":
                if(attacking == false) {
                    if(spriteNum == 1) {image = right1;}
                    if(spriteNum == 2) {image = right2;}
                }
                if(attacking == true) {
                    if(spriteNum == 1) {image = attackRight1;}
                    if(spriteNum == 2) {image = attackRight2;}
                }
                break;
        }

        if (invincible) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f));
        }

        g2.drawImage(image, tempScreenX, tempScreenY, gp.tileSize, gp.tileSize, null);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}