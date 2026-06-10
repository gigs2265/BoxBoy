package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Fart_Coin  extends Entity{
	Gamepanel gp;

	public OBJ_Fart_Coin(Gamepanel gp) {
		super(gp);
		this.gp = gp;
		
		type = type_pickupOnly;
		name = "$Fartcoin";
		value = 1;
		down1 = setup("/objects/fartcoin", gp.tileSize, gp.tileSize);
		
	}

public void use (Entity entity) {
		
		gp.playSE(12);
		gp.ui.addMessage("+" + value + " $Fartcoin");
		gp.player.$fartcoin += value;
	}
	
}
