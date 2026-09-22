package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Jimmysass extends Entity {
	public OBJ_Jimmysass(Gamepanel gp) {
		
		super(gp);
		type = type_ass;
		name = "Jimmy's Big Fat Ass";
		pcName = "Jimmy's Generous Donation";
		down1 = setup("/objects/jimmysass", gp.tileSize, gp.tileSize);
		attackValue = 5;
		attackArea.width = 30;
		attackArea.height = 30;
		description = "["+name+"]\n Heavy but powerful";
		pcDescription = "["+pcName+"]\n A dear friend, on loan, in full \n support of this endeavor.";
		price = 5;
	}
	
}


