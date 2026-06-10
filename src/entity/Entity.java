package entity;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.Gamepanel;

public class Entity {
	Gamepanel gp;
	public int worldX, worldY;
	public int speed;
	
	public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
	public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
	public String direction = "down";
	public int spriteCounter = 0;
	public int spriteNum = 1;
	public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
	public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
	public int solidAreaDefaultX, solidAreaDefaultY;
	public boolean collisionOn = false; 
	public int actionLockCounter = 0;
	public boolean invincible = false;
	boolean attacking = false;
	public boolean alive = true;
	public boolean dying = false;
	boolean hpBarOn = false;
	public int invincibleCounter = 0;
	int dyingCounter = 0;
	int hpBarCounter = 0;
	public int shotAvailableCounter = 0;
	String dialouges[] = new String[20];
	int dialougeIndex = 0;
	public BufferedImage image, image2, image3;
	public String name;
	public boolean collision = false;
	
	// Idle dialogue variables
	public String idleDialogue = "";
	public int idleDialogueCounter = 0;
	public int idleDialogueTimer = 0;
	public boolean showIdleDialogue = false;
	
	
	//CHARATER STATS
	public int maxLife;
	public int life;
	public int maxMana;
	public int mana;
	public int level;
	public int  strength;
	public int dexterity;
	public int attack;
	public int defense;
	public int exp;
	public int nextLevelExp;
	public int $fartcoin;
	public Entity currentWepon;
	public Entity currentSheild;
	public Projectile projectile;
	
	
	//ITEM ATTRIBUTES
	public ArrayList<Entity> inventory = new ArrayList<>();
    public final int maxInventorySize = 20;
	public int value;
	public int attackValue;
	public int defenseValue;
	public String description = "";
	public int usedCost;
	public int price;
	
	//TYPES
	public int type;
	public final int type_player = 0;
	public final int type_npc = 1;
	public final int type_monster = 2;
	public final int type_spray = 3;
	public final int type_cutter = 4;
	public final int type_sheild = 5;
	public final int type_consumable = 6;
	public final int type_pickupOnly = 7;
	public final int type_dildo = 8;
	public final int type_ass = 9;
	
	
	
	
	// Constructor
	public Entity(Gamepanel gp) {
		this.gp = gp;
	}
	
	public void setAction() {}
	public void damageReaction() {}
	public void speak() {
		
		if(dialouges[dialougeIndex] == null) {
			dialougeIndex = 0;
		}
		gp.ui.currentDialouge = dialouges[dialougeIndex];
				dialougeIndex++;
				
				switch(gp.player.direction) {
				case"up":
					direction = "down";
					break;
				case "down":
					direction = "up";
					break;
				case "left":
					direction = "right";
					break;
				case "right":
					direction = "left";
					break;
				}
	}
	public void use(Entity entity) {}
	public void checkDrop() {}
	public void dropItem (Entity droppedItem) {
		
		for (int i = 0; i< gp.obj[1].length; i++) {
			if(gp.obj[gp.currentMap][i] == null) {
				gp.obj[gp.currentMap][i] = droppedItem;
				gp.obj[gp.currentMap][i].worldX = worldX; //dead monsters world x and y
				gp.obj[gp.currentMap][i].worldY = worldY;
				break;
			}
		}
	}
	public void update() {
		
		setAction();
		
		collisionOn = false;
		gp.cChecker.checkTile(this);
		gp.cChecker.checkObject(this, false);
		gp.cChecker.checkEntity(this, gp.npc);
		gp.cChecker.checkEntity(this, gp.monster);
		boolean contactPlayer = gp.cChecker.checkPlayer(this);
		
		
		if(this.type == type_monster && contactPlayer == true) {
			
			damagePlayer(attack);
			
		}
		
		// IF COLLISION IS FALSE PLAYER CANT MOVE
		if(collisionOn == false) {
			
			switch(direction) {
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
		
		spriteCounter++;
		if(spriteCounter > 12) {
		if(spriteNum == 1) {
			spriteNum = 2;
		}
		else if(spriteNum == 2) {
			spriteNum = 1;
		}
		spriteCounter = 0;
		}
		
	    if (invincible) {
            invincibleCounter++;
            if (invincibleCounter > 40) {
                invincible = false;
                invincibleCounter = 0;
            }
        }
	    if(shotAvailableCounter < 30) {
        	shotAvailableCounter++;
        }
	}
	public void damagePlayer(int attack) {
		
		if(gp.player.invincible == false) {
			//damage
			gp.playSE(8);
			int damage =  attack - gp.player.defense;
			if(damage < 0) {
				damage = 0;
			}
            gp.player.life -= damage;
			
			gp.player.invincible = true;
		}
		
	}
	public void draw(Graphics2D g2) {
		BufferedImage image = null;
		
		int screenX = worldX - gp.player.worldX + gp.player.screenX;
		int screenY = worldY - gp.player.worldY + gp.player.screenY;
		
		if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
				worldX - gp.tileSize < gp.player.worldX + gp.player.screenX&&
				worldY + gp.tileSize> gp.player.worldY - gp.player.screenY&&
				worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
			switch (direction) {
		    case "up":
		        if (spriteNum == 1) {
		            image = up1;
		        } else if (spriteNum == 2) {
		            image = up2;
		        }
		        break;
		    case "down":
		        if (spriteNum == 1) {
		            image = down1;
		        } else if (spriteNum == 2) {
		            image = down2;
		        }
		        break;
		    case "left":
		        if (spriteNum == 1) {
		            image = left1;
		        } else if (spriteNum == 2) {
		            image = left2;
		        }
		        break;
		    case "right":
		        if (spriteNum == 1) {
		            image = right1;
		        } else if (spriteNum == 2) {
		            image = right2;
		        }
		        break;
		}
			
			//rat hp
			if(type == 2 && hpBarOn == true) {
				double oneScale = (double)gp.tileSize/maxLife;
				double hpBarValue = oneScale*life;
				g2.setColor(new Color(35,35,35));
				g2.fillRect(screenX - 1, screenY - 16, gp.tileSize+2, 12);
				
			g2.setColor(new Color(255,0,35));
			g2.fillRect(screenX, screenY - 15, (int)hpBarValue, 10);
			
			hpBarCounter++;
			
			if(hpBarCounter > 600) {				
				hpBarCounter = 0;
			hpBarOn = false;
			}
			
			}
			
			
		     if (invincible) {
		    	 hpBarOn = true;
		    	 hpBarCounter = 0;
		    	 changeAlpha(g2,0.4f);
		            
		        }
		     if(dying == true) {
		    	 dyingAnimation(g2);
		     }
		     
		     g2.drawImage(image, screenX, screenY, null);
		     
		     changeAlpha(g2,1f);
		     
		     // Draw idle dialogue bubble (for NPCs) - AFTER restoring alpha
		     if(type == type_npc && showIdleDialogue) {
		         System.out.println("Drawing bubble for NPC: " + name + " - " + idleDialogue);
		         drawDialogueBubble(g2, idleDialogue, screenX, screenY);
		     }
		}
	}
	
	public void drawDialogueBubble(Graphics2D g2, String text, int screenX, int screenY) {
	    // Calculate bubble position (above the NPC's head)
	    int bubbleX = screenX - gp.tileSize/2;
	    int bubbleY = screenY - gp.tileSize - 10;
	    
	    // Measure text width
	    g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 16f));
	    int textWidth = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
	    int textHeight = 20;
	    
	    // Bubble dimensions (add padding)
	    int bubbleWidth = textWidth + 20;
	    int bubbleHeight = textHeight + 10;
	    
	    // Center the bubble over the NPC
	    bubbleX = screenX + gp.tileSize/2 - bubbleWidth/2;
	    
	    // Draw bubble background (black with transparency like dialogue box)
	    g2.setColor(new Color(0, 0, 0, 210));
	    g2.fillRoundRect(bubbleX, bubbleY, bubbleWidth, bubbleHeight, 10, 10);
	    
	    // Draw bubble border (white like dialogue box)
	    g2.setColor(new Color(255, 255, 255));
	    g2.setStroke(new BasicStroke(3));
	    g2.drawRoundRect(bubbleX + 2, bubbleY + 2, bubbleWidth - 4, bubbleHeight - 4, 8, 8);
	    
	    // Draw small triangle pointer pointing to NPC
	    int[] xPoints = {screenX + gp.tileSize/2 - 5, screenX + gp.tileSize/2 + 5, screenX + gp.tileSize/2};
	    int[] yPoints = {bubbleY + bubbleHeight, bubbleY + bubbleHeight, bubbleY + bubbleHeight + 8};
	    g2.setColor(new Color(0, 0, 0, 210));
	    g2.fillPolygon(xPoints, yPoints, 3);
	    g2.setColor(new Color(255, 255, 255));
	    g2.drawLine(xPoints[0], yPoints[0], xPoints[2], yPoints[2]);
	    g2.drawLine(xPoints[1], yPoints[1], xPoints[2], yPoints[2]);
	    
	    // Draw text (white like dialogue box)
	    g2.setColor(new Color(255, 255, 255));
	    g2.drawString(text, bubbleX + 10, bubbleY + textHeight);
	}
	
	public void dyingAnimation(Graphics2D g2) {
		
		dyingCounter++;
		
		int i = 5;
		
		if(dyingCounter <= i) {changeAlpha(g2,0f);}
		
		if(dyingCounter > i && dyingCounter <= i*2) {changeAlpha(g2,0f);}
		
		if(dyingCounter > i*2 && dyingCounter <= i*3) {changeAlpha(g2,1f);}
		
		if(dyingCounter > i*3 && dyingCounter <= i*4) {changeAlpha(g2,0f);}
		
		if(dyingCounter > i*4 && dyingCounter <= i*5) {changeAlpha(g2,1f);}
		
		if(dyingCounter > i*5 && dyingCounter <= i*6) {changeAlpha(g2,0f);}
		
		if(dyingCounter > i*6 && dyingCounter <= i*7) {changeAlpha(g2,1f);}
		
		if(dyingCounter > i*7) {
			dying = false;
			alive = false;
		}
	}
	public void changeAlpha(Graphics2D g2, float alphaValue) {
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}
	public BufferedImage setup(String imagePath, int width, int height) {
		UtilityTool uTool = new UtilityTool();
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = UtilityTool.scaleImage(image, width, height);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}
}