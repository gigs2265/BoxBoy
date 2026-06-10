package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Rattail extends Entity {
	
	Gamepanel gp;
	

	public OBJ_Rattail(Gamepanel gp) {
		super(gp);
		
		this.gp = gp;
		
		type = type_pickupOnly;
		name = "Rat Tail";
		value = 1;
		down1 = setup("/objects/rattail",gp.tileSize,gp.tileSize);
		description = "[Rat Tail]\n Gain " + value + " Puke";
	}
	public void use (Entity entity) {
		
		gp.gameState = gp.dialougeState;
		gp.ui.currentDialouge = "This " + name +  " tastes like shit! UGHGUGHG \n[+" + value + " Puke]";
		entity.life+= value;
		entity.mana+= value;
		if(gp.player.life > gp.player.maxLife) {
			gp.player.life = gp.player.maxLife;
		}
		gp.playSE(4);
		
	}

}
