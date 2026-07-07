package monster;

import java.util.Random;
import entity.Entity;
import main.Gamepanel;
import object.OBJ_Titmilk;

public class MON_Brian extends Entity {
	
	Gamepanel gp;
	
	// POOP ATTACK - Brian poops out Rattatuchiis during the fight
	int poopCounter = 0;
	final int POOP_INTERVAL = 600;    // one poop every ~10 seconds of combat
	final int MAX_POOPED_TUCHIS = 3;  // max of his tuchis alive at once
	
	public MON_Brian(Gamepanel gp) {
		super(gp);
		this.gp = gp;
		type = type_monster;
		name = "Brian";
		speed = 3;
		maxLife = 40;
		life = maxLife;
		attack = 6;
		heavyHitter = true; // punches through heavy armor (see Entity.damagePlayer)
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
		
		// POOP ATTACK - while the fight is engaged, the poop timer ticks.
		// Every POOP_INTERVAL updates Brian squeezes out a Rattatuchii.
		if(tileDistance < 12) {
			poopCounter++;
			if(poopCounter >= POOP_INTERVAL) {
				poopCounter = 0;
				poopTuchi();
			}
		}
		
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
	
	public void poopTuchi() {
		// Count Brian's tuchis already alive near him (within 15 tiles) and
		// find a free slot in the monster array. The cap only counts NEARBY
		// tuchis so the one wandering the hallway doesn't eat into his quota.
		int nearbyTuchis = 0;
		int freeSlot = -1;
		for(int i = 0; i < gp.monster[gp.currentMap].length; i++) {
			if(gp.monster[gp.currentMap][i] == null) {
				if(freeSlot == -1) {
					freeSlot = i;
				}
			}
			else if(gp.monster[gp.currentMap][i] instanceof MON_Tuchi && gp.monster[gp.currentMap][i].alive == true) {
				int dist = (Math.abs(gp.monster[gp.currentMap][i].worldX - worldX)
				          + Math.abs(gp.monster[gp.currentMap][i].worldY - worldY)) / gp.tileSize;
				if(dist <= 15) {
					nearbyTuchis++;
				}
			}
		}
		if(nearbyTuchis >= MAX_POOPED_TUCHIS || freeSlot == -1) {
			return; // arena already crawling, or no room in the monster array
		}
		
		// Spawn position: right behind Brian based on where he's facing.
		// (Brian's sprite is 4 tiles big, a tuchi is 1 tile.)
		int spawnX = worldX + gp.tileSize + gp.tileSize/2; // his horizontal center
		int spawnY = worldY + gp.tileSize + gp.tileSize/2; // his vertical center
		switch(direction) {
			case "up":    spawnY = worldY + gp.tileSize*4; break; // out the bottom
			case "down":  spawnY = worldY - gp.tileSize;   break; // out the top
			case "left":  spawnX = worldX + gp.tileSize*4; break; // out the right side
			case "right": spawnX = worldX - gp.tileSize;   break; // out the left side
		}
		
		// Never poop a tuchi into a wall - if blocked, it plops out underneath him
		int col = (spawnX + gp.tileSize/2) / gp.tileSize;
		int row = (spawnY + gp.tileSize/2) / gp.tileSize;
		if(gp.tileM.tile[gp.tileM.mapTileNum[gp.currentMap][col][row]].collision == true) {
			spawnX = worldX + gp.tileSize + gp.tileSize/2;
			spawnY = worldY + gp.tileSize*2;
		}
		
		MON_Tuchi tuchi = new MON_Tuchi(gp);
		tuchi.worldX = spawnX;
		tuchi.worldY = spawnY;
		gp.monster[gp.currentMap][freeSlot] = tuchi;
		
		gp.playSE(16); // fartblast!
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