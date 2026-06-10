package object;




import entity.Entity;
import main.Gamepanel;

public class OBJ_Beer extends Entity {
	
	Gamepanel gp;
	int value = 2;

	public OBJ_Beer(Gamepanel gp) {
		super(gp);
		
		this.gp = gp;
		
		type = type_consumable;
		name = "BEER";
		down1 = setup("/objects/beer1",gp.tileSize,gp.tileSize);
		description = "[Beer]\n Hopefully this gets me drunk";
		price = 2;
	}
	
public void use (Entity entity) {
		
		gp.gameState = gp.dialougeState;
		gp.ui.currentDialouge = "THEY GOT BREWSKIS ON THIS ISLAND?!?! \nFUCK YEA DAWG I FEEL SO MUCH BETTER";
		entity.life+= value;
		if(gp.player.life > gp.player.maxLife) {
			gp.player.life = gp.player.maxLife;
		}
		gp.playSE(4);
		
	}

}





