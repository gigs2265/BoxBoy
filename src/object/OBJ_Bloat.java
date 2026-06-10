package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Bloat extends Entity {

	public OBJ_Bloat(Gamepanel gp) {
		super(gp);
		
		name = "Bloat";
		down1 = setup("/objects/burger", gp.tileSize, gp.tileSize);
		defenseValue = 1;
		description = "["+name+"]\nburgers+bloat=Hard Belly";
	}

}
