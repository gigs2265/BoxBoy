package object;

import entity.Entity;
import entity.Projectile;
import main.Gamepanel;

public class OBJ_Milkshot extends Projectile {
	
	Gamepanel gp;
	
	public OBJ_Milkshot(Gamepanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "Milkshot";
		speed = 8;  // INCREASED from 1 to 8 (much faster)
		maxLife = 80;  // DECREASED from 400 to 80 (shorter distance)
		life = maxLife;
		attack = 4;
		usedCost = 1;
		alive = false;
		getImage();
	}
	
	public void getImage() {
		up1 = setup("/projectile/milkshotup1", gp.tileSize, gp.tileSize);
		up2 = setup("/projectile/milkshotup2", gp.tileSize, gp.tileSize);
		
		down1 = setup("/projectile/milkshotdown1", gp.tileSize, gp.tileSize);
		down2 = setup("/projectile/milkshotdown2", gp.tileSize, gp.tileSize);
		
		left1 = setup("/projectile/milkshotleft1", gp.tileSize, gp.tileSize);
		left2 = setup("/projectile/milkshotleft2", gp.tileSize, gp.tileSize);
		
		right1 = setup("/projectile/milkshotright1", gp.tileSize, gp.tileSize);
		right2 = setup("/projectile/milkshotright2", gp.tileSize, gp.tileSize);
	}
	
	public boolean haveResource(Entity user) {
		boolean haveResource = false;
		if(user.mana >= usedCost) {
			haveResource = true;
		}
		return haveResource;
	}
	
	public void subtractResource(Entity user) {
		user.mana -= usedCost;
	}
}
