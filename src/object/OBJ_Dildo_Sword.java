package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Dildo_Sword extends Entity{

	public OBJ_Dildo_Sword(Gamepanel gp) {
		
			super(gp);
			type = type_dildo;
			name = "Ian's Sword";
			down1 = setup("/objects/iansword", gp.tileSize, gp.tileSize);
			attackValue = 10;
			attackArea.width = 30;
			attackArea.height = 30;
			description = "["+name+"]\n Ian has alot of explaining to do.";
			price = 20;
		}
		
	}


