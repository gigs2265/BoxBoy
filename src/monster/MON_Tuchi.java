package monster;

import java.util.Random;
import entity.Entity;
import main.Gamepanel;

public class MON_Tuchi extends Entity {
	
	Gamepanel gp;
	
	public MON_Tuchi(Gamepanel gp) {
		super(gp);
		this.gp = gp;
		type = type_monster;
		name = " a Tuchi";
		speed = 2;
		maxLife = 6;
		life = maxLife;
		attack = 2;
		defense = 0;
		exp = 5;
		
		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
	}
	
	public void getImage() {
		up1 = setup("/monster/ratdown1", gp.tileSize, gp.tileSize);
		up2 = setup("/monster/ratdown2", gp.tileSize, gp.tileSize);
		down1 = setup("/monster/ratdown1", gp.tileSize, gp.tileSize);
		down2 = setup("/monster/ratdown2", gp.tileSize, gp.tileSize);
		left1 = setup("/monster/ratdown1", gp.tileSize, gp.tileSize);
		left2 = setup("/monster/ratdown2", gp.tileSize, gp.tileSize);
		right1 = setup("/monster/ratdown1", gp.tileSize, gp.tileSize);
		right2 = setup("/monster/ratdown2", gp.tileSize, gp.tileSize);
	}
	
	public void setAction() {
		actionLockCounter++;
		
		// Calculate distance to player
		int xDistance = Math.abs(worldX - gp.player.worldX);
		int yDistance = Math.abs(worldY - gp.player.worldY);
		int tileDistance = (xDistance + yDistance) / gp.tileSize;
		
		// CHASE MODE - if player is within 6 tiles
		if(tileDistance < 6) {
			// Check every 15 frames for more responsive chasing
			if(actionLockCounter == 15) {
				// Determine which direction brings us closer to player
				int xDiff = gp.player.worldX - worldX;
				int yDiff = gp.player.worldY - worldY;
				
				// Move in the direction with greater distance first
				if(Math.abs(xDiff) > Math.abs(yDiff)) {
					// Move horizontally
					if(xDiff > 0) {
						direction = "right";
					} else {
						direction = "left";
					}
				} else {
					// Move vertically
					if(yDiff > 0) {
						direction = "down";
					} else {
						direction = "up";
					}
				}
				
				actionLockCounter = 0;
			}
		}
		// WANDER MODE - if player is far away
		else {
			// Random movement every 120 frames (slower when not chasing)
			if(actionLockCounter == 120) {
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
		// Instead of moving away, move toward player aggressively
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


