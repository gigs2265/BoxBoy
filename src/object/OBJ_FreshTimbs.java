package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_FreshTimbs extends Entity {

	public OBJ_FreshTimbs(Gamepanel gp) {
		super(gp);

		type = type_boots;
		name = "Fresh Timbs";
		down1 = setup("/objects/freshtimbs.png", gp.tileSize, gp.tileSize);
		speedValue = 2;
		description = "["+name+"]\n IM FASTER THAN IAN! BITCH!!";
		pcDescription = "["+name+"]\n Premium footwear that enhances speed. \n personal mobility.";
		price = 2;
	}

}
