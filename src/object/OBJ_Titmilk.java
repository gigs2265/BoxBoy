package object;

import entity.Entity;
import entity.Projectile;
import main.Gamepanel;

public class OBJ_Titmilk extends Projectile {
	
	Gamepanel gp;
	
	public OBJ_Titmilk(Gamepanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "Titmilk";
		speed = 8;
		maxLife = 80;
		life = maxLife;
		attack = 4;
		usedCost = 1;
		alive = false;
		getImage();
	}
	
	public void getImage() {
		up1 = setup("/projectile/milkup1", gp.tileSize, gp.tileSize);
		up2 = setup("/projectile/milkup2", gp.tileSize, gp.tileSize);
		
		down1 = setup("/projectile/milkdown1", gp.tileSize, gp.tileSize);
		down2 = setup("/projectile/milkdown2", gp.tileSize, gp.tileSize);
		
		left1 = setup("/projectile/milkleft1", gp.tileSize, gp.tileSize);
		left2 = setup("/projectile/milkleft2", gp.tileSize, gp.tileSize);
		
		right1 = setup("/projectile/milkright1", gp.tileSize, gp.tileSize);
		right2 = setup("/projectile/milkright2", gp.tileSize, gp.tileSize);
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