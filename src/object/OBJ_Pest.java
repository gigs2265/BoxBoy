package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Pest extends Entity {
	public OBJ_Pest(Gamepanel gp) {
		super (gp);
				
		name = "Key";
		down1 = setup("/objects/pest", gp.tileSize, gp.tileSize);		
	}

}




