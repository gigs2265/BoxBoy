package object;

import entity.Entity;
import main.Gamepanel;

// Default boots slot - no speed bonus, just so currentBoots is never null.
public class OBJ_Flats extends Entity {

	public OBJ_Flats(Gamepanel gp) {
		super(gp);

		type = type_boots;
		name = "Flats";
		down1 = setup("/objects/flats", gp.tileSize, gp.tileSize);
		speedValue = 0;
		price = 2;
		description = "["+name+"]\n Stinky Flats.";
		pcDescription = "["+name+"]\n A minimalist, cost-effective footwear \n solution.";
	}

}
