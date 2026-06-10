package object;

import entity.Projectile;
import main.Gamepanel;

public class OBJ_Cheese  extends Projectile {
	
	Gamepanel gp;

	public OBJ_Cheese(Gamepanel gp) {
		super(gp);
		this.gp = gp;
	
		
		name = "Cheese";
	    speed = 1;
		maxLife = 200;
		life = maxLife;
		attack = 8;
		usedCost = 1;
		alive = false;
		getImage();
	}
	
	public void getImage() {
		
		up1 = setup ("/projectile/cheesedown1", gp.tileSize, gp.tileSize);
		up2 = setup ("/projectile/cheesedown1", gp.tileSize, gp.tileSize);
		
		down1 = setup("/projectile/cheesedown1", gp.tileSize, gp.tileSize);
		down2 = setup ("/projectile/cheesedown1", gp.tileSize, gp.tileSize);
		
		left1 = setup("/projectile/cheesedown1", gp.tileSize, gp.tileSize);
		left2 = setup ("/projectile/cheesedown1", gp.tileSize, gp.tileSize);
		
		right1 = setup("/projectile/cheesedown1", gp.tileSize, gp.tileSize);
		right1 = setup ("/projectile/cheesedown1", gp.tileSize, gp.tileSize);
		
		
		
		
		}

}
