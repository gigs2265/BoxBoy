package monster;

import java.util.Random;
import entity.Entity;
import main.Gamepanel;
import object.OBJ_Milkshot;



public class MON_Nick extends Entity {
	
	Gamepanel gp;
	
	public MON_Nick(Gamepanel gp) {
		super(gp);
		this.gp = gp;
		type = type_monster;
		name = "Nick";
		speed = 3;
		maxLife = 50;
		life = maxLife;
		attack = 7;
		defense = 3;
		exp = 50;
		
		
		solidArea.x = 3;
		solidArea.y = 18;
		solidArea.width = 42;
		solidArea.height = 30;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
		getProjectile();  // ADDED: Initialize projectile
	}
	
	public void getImage() {
		up1 = setup("/monster/nickdown1", gp.tileSize, gp.tileSize);
		up2 = setup("/monster/nickdown2", gp.tileSize, gp.tileSize);
		down1 = setup("/monster/nickdown1", gp.tileSize, gp.tileSize);
		down2 = setup("/monster/nickdown2", gp.tileSize, gp.tileSize);
		left1 = setup("/monster/nickdown1", gp.tileSize, gp.tileSize);
		left2 = setup("/monster/nickdown2", gp.tileSize, gp.tileSize);
		right1 = setup("/monster/nickdown1", gp.tileSize, gp.tileSize);
		right2 = setup("/monster/nickdown2", gp.tileSize, gp.tileSize);
	}
	
	// ADDED: Method to initialize projectile
	public void getProjectile() {
		projectile = new OBJ_Milkshot(gp);  // You can change this to whatever projectile you want
	}
	
	public void setAction() {
		actionLockCounter++;
		
		// Calculate distance to player
		int xDistance = Math.abs(worldX - gp.player.worldX);
		int yDistance = Math.abs(worldY - gp.player.worldY);
		int tileDistance = (xDistance + yDistance) / gp.tileSize;
		
		// BOSS AI - Always aware of player
		
		// RANGED ATTACK MODE - Keep distance and shoot
		if(tileDistance < 10 && tileDistance > 3) {
			// Try to maintain distance while shooting
			if(actionLockCounter == 30) {
				int xDiff = gp.player.worldX - worldX;
				int yDiff = gp.player.worldY - worldY;
				
				// Face the player
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
				
				// Shoot projectile at player
				if(shotAvailableCounter == 30 && projectile.alive == false) {
					projectile.set(worldX, worldY, direction, true, this);
					gp.projectileList.add(projectile);
					shotAvailableCounter = 0;
				}
				
				actionLockCounter = 0;
			}
		}
		// RETREAT MODE - back away if player gets too close
		else if(tileDistance <= 3) {
			if(actionLockCounter == 15) {
				int xDiff = gp.player.worldX - worldX;
				int yDiff = gp.player.worldY - worldY;
				
				// Move away from player
				if(Math.abs(xDiff) > Math.abs(yDiff)) {
					if(xDiff > 0) {
						direction = "left";  // Move away
					} else {
						direction = "right";
					}
				} else {
					if(yDiff > 0) {
						direction = "up";  // Move away
					} else {
						direction = "down";
					}
				}
				
				actionLockCounter = 0;
			}
		}
		// APPROACH MODE - move closer if player is far
		else {
			if(actionLockCounter == 20) {
				int xDiff = gp.player.worldX - worldX;
				int yDiff = gp.player.worldY - worldY;
				
				// Move toward player
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
				
				actionLockCounter = 0;
			}
		}
	}
	
	public void damageReaction() {
		actionLockCounter = 0;
		
		// Boss behavior: sometimes retreat when hit if low health
		if(life < maxLife / 3) {
			// Retreat when low health
			int xDiff = gp.player.worldX - worldX;
			int yDiff = gp.player.worldY - worldY;
			
			if(Math.abs(xDiff) > Math.abs(yDiff)) {
				if(xDiff > 0) {
					direction = "left";
				} else {
					direction = "right";
				}
			} else {
				if(yDiff > 0) {
					direction = "up";
				} else {
					direction = "down";
				}
			}
		} else {
			// Attack when healthy
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
}