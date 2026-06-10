package monster;

import java.util.Random;
import entity.Entity;
import main.Gamepanel;

public class MON_Armored_Tuchi extends Entity {
	
	Gamepanel gp;
	
	public MON_Armored_Tuchi(Gamepanel gp) {
		super(gp);
		this.gp = gp;
		type = type_monster;
		name = " an Armored Tuchi";
		speed = 2;
		maxLife = 12;
		life = maxLife;
		attack = 4;
		defense = 2;
		exp = 10;
		
		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
	}
	
	public void getImage() {
		up1 = setup("/monster/armorrat1", gp.tileSize, gp.tileSize);
		up2 = setup("/monster/armorrat2", gp.tileSize, gp.tileSize);
		down1 = setup("/monster/armorrat1", gp.tileSize, gp.tileSize);
		down2 = setup("/monster/armorrat2", gp.tileSize, gp.tileSize);
		left1 = setup("/monster/armorrat1", gp.tileSize, gp.tileSize);
		left2 = setup("/monster/armorrat2", gp.tileSize, gp.tileSize);
		right1 = setup("/monster/armorrat1", gp.tileSize, gp.tileSize);
		right2 = setup("/monster/armorrat2", gp.tileSize, gp.tileSize);
	}
	
	public void setAction() {
		actionLockCounter++;
		
		// Calculate distance to player
		int xDistance = Math.abs(worldX - gp.player.worldX);
		int yDistance = Math.abs(worldY - gp.player.worldY);
		int tileDistance = (xDistance + yDistance) / gp.tileSize;
		
		// AGGRESSIVE CHASE MODE - Armored rats have larger detection (8 tiles)
		if(tileDistance < 8) {
			// Check every 10 frames for very responsive chasing (faster than normal rats)
			if(actionLockCounter == 10) {
				// Calculate direction to player
				int xDiff = gp.player.worldX - worldX;
				int yDiff = gp.player.worldY - worldY;
				
				// Try to take shortest path
				if(Math.abs(xDiff) > Math.abs(yDiff)) {
					// Prioritize horizontal movement
					if(xDiff > 0) {
						direction = "right";
					} else {
						direction = "left";
					}
				} else {
					// Prioritize vertical movement
					if(yDiff > 0) {
						direction = "down";
					} else {
						direction = "up";
					}
				}
				
				actionLockCounter = 0;
			}
		}
		// PATROL MODE - slower random movement when player is far
		else {
			if(actionLockCounter == 100) {
				Random random = new Random();
				int i = random.nextInt(100) + 1;
				
				if(i <= 25) {
					direction = "up";
				} else if(i > 25 && i <= 50) {
					direction = "down";
				} else if(i > 50 && i <= 75) {
					direction = "left";
				} else {
					direction = "right";
				}
				
				actionLockCounter = 0;
			}
		}
	}
	
	public void damageReaction() {
		actionLockCounter = 0;
		// Armored rats are aggressive - always chase toward player when hit
		int xDiff = gp.player.worldX - worldX;
		int yDiff = gp.player.worldY - worldY;
		
		if(Math.abs(xDiff) > Math.abs(yDiff)) {
			if(xDiff > 0) {
				direction = "right";
			} else {
				direction = "left";
			}
		} else {
			if(yDiff > 0) {
				direction = "down";
			} else {
				direction = "up";
			}
		}
	}
}