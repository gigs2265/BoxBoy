package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Boxcutter extends Entity {

	public OBJ_Boxcutter(Gamepanel gp) {
		super(gp);
		type = type_cutter;
		name = "Box Cutter";
		down1 = setup("/objects/boxcutter", gp.tileSize, gp.tileSize);
		attackValue = 2;
		attackArea.width = 30;
		attackArea.height = 30;
		description = "["+name+"]\n AKA a Theo's head cutter.";
	}

}
