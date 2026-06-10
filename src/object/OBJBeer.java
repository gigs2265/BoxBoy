package object;

import main.Gamepanel;

import java.awt.image.BufferedImage;

import entity.Entity;

public class OBJBeer extends Entity {

    public OBJBeer(Gamepanel gp) {
        super(gp);
        name = "beer";
        beer1 = setup("/objects/beer1", gp.tileSize, gp.tileSize);
        beerhalf1 = setup("/objects/beerhalf1", gp.tileSize, gp.tileSize);
        beerhalf2 = setup("/objects/beerhalf2", gp.tileSize, gp.tileSize);
    }

    // Declare separate fields for each image
    public BufferedImage beer1;
    public BufferedImage beerhalf1;
    public BufferedImage beerhalf2;
}

		
		
	


