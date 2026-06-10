package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Spicyburger extends Entity{

	public OBJ_Spicyburger(Gamepanel gp) {
		super(gp);
		
		type = type_sheild;
		name = "Hot Burg";
		down1 = setup("/objects/spicyburger", gp.tileSize, gp.tileSize);	
		defenseValue = 2;
		description = "["+name+"]\n My ass is gonna hurt tomorrow, but \nmy gut is harder than ever! .";
		price = 4;
	}

}
