package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Burger extends Entity {

	public OBJ_Burger(Gamepanel gp) {
		super (gp);
		
		type = type_sheild;
		name = "Burg";
		down1 = setup("/objects/burger", gp.tileSize, gp.tileSize);
		defenseValue = 1;
		
	}

}
