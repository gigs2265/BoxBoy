package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Puke extends Entity {

	Gamepanel gp;
	
	public OBJ_Puke(Gamepanel gp) {
		super(gp);
		this.gp = gp;
		
		name = "puke";
		image = setup ("/objects/pukefull", gp.tileSize, gp.tileSize);
		image2 = setup ("/objects/pukeempty", gp.tileSize, gp.tileSize);
	}

}
