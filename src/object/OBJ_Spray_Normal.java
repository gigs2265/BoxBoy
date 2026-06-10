package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Spray_Normal extends Entity{

	public OBJ_Spray_Normal(Gamepanel gp) {
		super(gp);
		type = type_spray;
		name = "Rat Spray";
		down1 = setup("/objects/ratspray",gp.tileSize, gp.tileSize);
		attackValue = 1;
		attackArea.width = 36;
		attackArea.height = 36;
		description = "["+name+"]\n Standard Rat Killer.";
		
		
	}

}
