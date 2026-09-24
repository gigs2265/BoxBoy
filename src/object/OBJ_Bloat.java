package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Bloat extends Entity {

	public OBJ_Bloat(Gamepanel gp) {
		super(gp);

		type = type_sheild;
		name = "Bloat";
		down1 = setup("/objects/burger", gp.tileSize, gp.tileSize);
		defenseValue = 1;
		price = 2;
		description = "["+name+"]\nburgers+bloat=Hard Belly";
		pcDescription = "["+name+"]\nA well-balanced meal contributes to \noverall core wellness.";
	}

}
