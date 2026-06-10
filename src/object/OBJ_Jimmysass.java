package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Jimmysass extends Entity {
	public OBJ_Jimmysass(Gamepanel gp) {
		
		super(gp);
		type = type_ass;
		name = "Jimmy's Big Fat Ass";
		down1 = setup("/objects/Jimmysass", gp.tileSize, gp.tileSize);
		attackValue = 20;
		attackArea.width = 30;
		attackArea.height = 30;
		description = "["+name+"]\n Heavy but powerful";
		price = 5;
	}
	
}


