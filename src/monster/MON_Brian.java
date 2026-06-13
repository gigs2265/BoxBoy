package monster;

import java.util.Random;
import entity.Entity;
import main.Gamepanel;
import object.OBJ_Titmilk;

public class MON_Brian extends Entity {
	
	Gamepanel gp;
	
	public MON_Brian(Gamepanel gp) {
		super(gp);
		this.gp = gp;
		type = type_monster;
		name = "Brian";
		speed = 3;
		maxLife = 40;
		life = maxLife;
		attack = 6;
		defense = 2;
		exp = 40;
		projectile = new OBJ_Titmilk(gp);
		
		// Brian's sprite is 4 tiles (192px) - his collision box needs to cover
		// almost the whole sprite so he can't visually overlap walls and so
		// the player's attacks can actually reach him.
		solidArea.x = 16;
		solidArea.y = 24;
		solidArea.width = 160;
		solidArea.height = 160;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		
		getImage();
	}
	
	public void getImage() {
		// Brian is 2x bigger than normal sprites (boss size!)
		int brianSize = gp.tileSize * 4;
		
		up1 = setup("/monster/briandown1", brianSize, brianSize);
		up2 = setup("/monster/briandown2", brianSize, brianSize);
		down1 = setup("/monster/briandown1", brianSize, brianSize);
		down2 = setup("/monster/briandown2", brianSize, brianSize);
		left1 = setup("/monster/brianleft1", brianSize, brianSize);
		left2 = setup("/monster/brianleft2", brianSize, brianSize);
		right1 = setup("/monster/brianright1", brianSize, brianSize);
		right2 = setup("/monster/brianright2", brianSize, brianSize);
	}
	
	public void setAction() {
		actionLockCounter++;
		
		// Calculate distance to player
		int xDistance = Math.abs(worldX - gp.player.worldX);
		int yDistance = Math.abs(worldY - gp.player.worldY);
		int tileDistance = (xDistance + yDistance) / gp.tileSize;
		
		// BOSS BRIAN AI - Aggressive milkshot spammer
		
		// RANGED ATTACK MODE - Shoot from medium range
		if(tileDistance < 12 && tileDistance > 2) {
			// Face player and shoot rapidly
			if(actionLockCounter == 20) {
				int xDiff = gp.player.worldX - worldX;
				int yDiff = gp.player.worldY - worldY;
				
				// Face the player for shooting
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
				
				// Shoot milkshot projectile
				if(shotAvailableCounter == 30 && projectile.alive == false) {
					projectile.set(worldX, worldY, direction, true, this);
					gp.projectileList.add(projectile);
					shotAvailableCounter = 0;
					gp.playSE(6);
				}
				
				actionLockCounter = 0;
			}
		}
		// STRAFE MODE - Move side to side while shooting when close
		else if(tileDistance <= 2) {
			if(actionLockCounter == 15) {
				int xDiff = gp.player.worldX - worldX;
				int yDiff = gp.player.worldY - worldY;
				
				// Strafe perpendicular to player
				Random random = new Random();
				if(Math.abs(xDiff) > Math.abs(yDiff)) {
					// Player is horizontal, strafe vertically
					if(random.nextBoolean()) {
						direction = "up";
					} else {
						direction = "down";
					}
				} else {
					// Player is vertical, strafe horizontally
					if(random.nextBoolean()) {
						direction = "left";
					} else {
						direction = "right";
					}
				}
				
				actionLockCounter = 0;
			}
		}
		// APPROACH MODE - Move closer if player is too far
		else {
			if(actionLockCounter == 25) {
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
		
		// Brian gets aggressive when hit - faces player
		int xDiff = gp.player.worldX - worldX;
		int yDiff = gp.player.worldY - worldY;
		
		// Face player for next shot
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
	
	public void checkDrop() {
		// Brian drops good loot!
		int i = new Random().nextInt(100) + 1;
		
		if(i < 50) {
			dropItem(new object.OBJ_Fart_Coin(gp));
		}
		if(i >= 50 && i < 75) {
			dropItem(new object.OBJ_Beer(gp));
		}
		if(i >= 75 && i < 100) {
			dropItem(new object.OBJ_Spicyburger(gp));
		}
	}
}