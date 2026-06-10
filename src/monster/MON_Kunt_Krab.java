package monster;

import java.util.Random;

import entity.Entity;
import main.Gamepanel;

public class MON_Kunt_Krab extends Entity {
Gamepanel gp;
	public  MON_Kunt_Krab(Gamepanel gp) {
		super(gp);
		this.gp = gp;
		type = type_monster;
		name = "a Kunt Krab";
		speed = 4;
		maxLife = 7;
		life = maxLife;
		attack = 5;
		defense = 2;
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
	
	up1 = setup("/monster/cuntcrab1", gp.tileSize, gp.tileSize);
	up2 = setup("/monster/cuntcrab2", gp.tileSize, gp.tileSize);
	down1 = setup("/monster/cuntcrab1", gp.tileSize, gp.tileSize);
	down2 = setup("/monster/cuntcrab2", gp.tileSize, gp.tileSize);
	left1 = setup("/monster/cuntcrab1", gp.tileSize, gp.tileSize);
	left2 = setup("/monster/cuntcrab2", gp.tileSize, gp.tileSize);
	right1 = setup("/monster/cuntcrab1", gp.tileSize, gp.tileSize);
	right2 = setup("/monster/cuntcrab2", gp.tileSize, gp.tileSize);
	
}
public void setAction() {
	
		actionLockCounter ++;
		
		if(actionLockCounter == 120) {
			Random random = new Random();
			int i = random.nextInt(100)+1; 
			
			if (i <= 30) {
			    direction = "up";
			} else if (i > 30 && i <= 50) {
			    direction = "down";
			} else if (i > 50 && i <= 80) {
			    direction = "left";
			} else if (i > 80 && i <= 100) {
			    direction = "right";
			}

			
			actionLockCounter = 0;
			
		}
}
public void damageReaction() {
	actionLockCounter = 0;
	direction = gp.player.direction;
	
}


}