package entity;

import java.util.Random;
import main.Gamepanel;
import object.OBJ_Beer;
import object.OBJ_Dildo_Sword;
import object.OBJ_Jimmysass;
import object.OBJ_Spicyburger;

public class NPC_Josh extends Entity {
	
	String[] idleDialogues = new String[12];
	
	public NPC_Josh(Gamepanel gp) {
	    super(gp);
	    type = type_npc;
	    name = "Josh";
	    direction = "down";
	    speed = 1;
	    setDialouge();
	    setItems();
	    setIdleDialogues();
	    getImage();
	}
	
	public void getImage() {
		up1 = setup ("/npc/joshup1", gp.tileSize, gp.tileSize);
		up2 = setup("/npc/joshup2", gp.tileSize, gp.tileSize);
		down1 = setup("/npc/joshdown1", gp.tileSize, gp.tileSize);
		down2 = setup("/npc/joshdown2", gp.tileSize, gp.tileSize);
		left1 = setup("/npc/joshleft1", gp.tileSize, gp.tileSize);
		left2 =setup("/npc/joshleft2", gp.tileSize, gp.tileSize);
		right1 = setup("/npc/joshright1", gp.tileSize, gp.tileSize);
		right2 = setup("/npc/joshright2", gp.tileSize, gp.tileSize);
	}
	
	public void setDialouge() {
		dialouges[0] = "IM NO SURE WHO I LOVE MORE, JESUS OR BRIA...\nOh hey there! Im assuming your here for conversion?\nI have some stuff you might want to buy.\nAll proceeds go to the 'Talks with Brian Outside Foundation'";
	}
	
	public void setIdleDialogues() {
		idleDialogues[0] = "Brian loves you!";
		idleDialogues[1] = "Praise be!";
		idleDialogues[2] = "Hmm...";
		idleDialogues[3] = "Where's Lord Brian?";
		idleDialogues[4] = "Another day, another walk with Brian";
		idleDialogues[5] = "Need supplies?";
		idleDialogues[6] = "God (Brian) is good!";
		idleDialogues[7] = "Connor better be done cleaning the toliets";
		idleDialogues[8] = "*sigh*";
		idleDialogues[9] = "Business is slow...";
		idleDialogues[10] = "Jesus or Brian?";
		idleDialogues[11] = "Foundation needs funds...";
	}
	
	public void setItems() {
		inventory.add(new OBJ_Beer(gp));
		inventory.add(new OBJ_Jimmysass(gp));
		inventory.add(new OBJ_Spicyburger(gp));
		inventory.add(new OBJ_Dildo_Sword(gp));
		
		// Keys for sale - used to unlock the cell doors holding the captured characters
		object.OBJ_Key key = new object.OBJ_Key(gp);
		key.price = 10;
		inventory.add(key);
	}

	public void setAction() {
		actionLockCounter++;
		if(actionLockCounter == 120) {
			Random random = new Random();
			int i = random.nextInt(100)+1; 
			if (i <= 30) { direction = "up";
			} else if (i > 30 && i <= 50) { direction = "down";
			} else if (i > 50 && i <= 80) { direction = "left";
			} else if (i > 80 && i <= 100) { direction = "right"; }
			actionLockCounter = 0;
		}
		if(showIdleDialogue) {
			idleDialogueCounter++;
			if(idleDialogueCounter > 180) { showIdleDialogue = false; idleDialogueCounter = 0; }
		} else {
			idleDialogueTimer++;
			if(idleDialogueTimer > 300) {
				Random random = new Random();
				if(random.nextInt(100) < 30) {
					idleDialogue = idleDialogues[random.nextInt(idleDialogues.length)];
					showIdleDialogue = true;
					idleDialogueTimer = 0;
				} else { idleDialogueTimer = 0; }
			}
		}
	}
	
	public void speak() {
		gp.ui.currentDialouge = dialouges[0];
		super.speak();
		gp.gameState = gp.tradeState;
		gp.ui.npc = this;
	}
}