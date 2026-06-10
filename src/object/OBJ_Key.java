package object;

import java.io.IOException;

import javax.imageio.ImageIO;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Key extends Entity {
	
	
	public OBJ_Key(Gamepanel gp) {
		super (gp);
				
		name = "Key";
		down1 = setup("/objects/key", gp.tileSize, gp.tileSize);
		description = "["+name+"]\nI can probably use this to unlock \n doors and chests.";
	}

}
