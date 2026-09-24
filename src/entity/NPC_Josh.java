package entity;

import java.util.Random;
import main.Gamepanel;
import object.OBJ_Beer;
import object.OBJ_Dildo_Sword;
import object.OBJ_Jimmysass;
import object.OBJ_Spicyburger;
import object.OBJ_FreshTimbs;

public class NPC_Josh extends Entity {
	
	String[] idleDialogues = new String[12];
	String[] idlePcDialogues = new String[12]; // "PC Mode" sanitized idle bubbles, same indices - see main.PcText
	
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

		pcDialouges[0] = "I HAVE GREAT ADMIRATION FOR BOTH JESUS AND BRIA...\nOh, hello there! I imagine you're interested in \nsome fellowship? I have some items available for \npurchase. All proceeds support the 'Community \nConversations with Brian Foundation'";
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

		idlePcDialogues[0] = "Brian appreciates you!";
		idlePcDialogues[1] = "Wonderful day!";
		idlePcDialogues[2] = "Hmm...";
		idlePcDialogues[3] = "Where's Community Leader Brian?";
		idlePcDialogues[4] = "Another day, another walk with Brian";
		idlePcDialogues[5] = "Need supplies?";
		idlePcDialogues[6] = "Brian is a wonderful leader!";
		idlePcDialogues[7] = "Connor should really finish tidying up soon";
		idlePcDialogues[8] = "*sigh*";
		idlePcDialogues[9] = "Business is a bit slow...";
		idlePcDialogues[10] = "Jesus or Brian? Both, really!";
		idlePcDialogues[11] = "Foundation could use some donations...";
	}
	
	public void setItems() {
		inventory.add(new OBJ_Beer(gp));
		inventory.add(new OBJ_Jimmysass(gp));
		inventory.add(new OBJ_Spicyburger(gp));
		inventory.add(new OBJ_Dildo_Sword(gp));
		inventory.add(new OBJ_FreshTimbs(gp));
		
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
					int idleIndex = random.nextInt(idleDialogues.length);
					idleDialogue = main.PcText.pick(gp, idleDialogues[idleIndex], idlePcDialogues[idleIndex]);
					showIdleDialogue = true;
					idleDialogueTimer = 0;
				} else { idleDialogueTimer = 0; }
			}
		}
	}
	
	public void speak() {
		gp.ui.currentDialouge = dialouges[0];
		super.speak();

		// Only open the shop right after his intro line is shown (dialougeIndex
		// just advanced to 1). super.speak() resets dialougeIndex back to 0 and
		// exits to playState once dialogue wraps - which is also how this method
		// gets called again to dismiss the trade menu's "See you later!" message
		// (KeyHandler.dialogueState() re-invokes speak() on ENTER). Forcing
		// tradeState back open in that case would trap the player in a loop.
		if(dialougeIndex != 0) {
			gp.gameState = gp.tradeState;
			gp.ui.npc = this;
		}
	}
}