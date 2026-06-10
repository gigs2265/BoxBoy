package entity;

import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import main.Gamepanel;

public class NPC_Ian extends Entity {
	
	
	String[] idleDialogues = new String[12];
	
	public NPC_Ian(Gamepanel gp) {
		super(gp);
		
		type = type_npc;  
		name = "Ian";  
		direction = "down";
		speed = 1;
		
		getImage();
		setDialouge();
		setIdleDialogues();
	}
	
	public void getImage() {
		up1 = setup ("/npc/biggayianup1", gp.tileSize, gp.tileSize);
		up2 = setup("/npc/biggayianup2", gp.tileSize, gp.tileSize);
		down1 = setup("/npc/biggayiandown1", gp.tileSize, gp.tileSize);
		down2 = setup("/npc/biggayiandown2", gp.tileSize, gp.tileSize);
		left1 = setup("/npc/biggayianleft1", gp.tileSize, gp.tileSize);
		left2 =setup("/npc/biggayianleft2", gp.tileSize, gp.tileSize);
		right1 = setup("/npc/biggayianright1", gp.tileSize, gp.tileSize);
		right2 = setup("/npc/biggayianright2", gp.tileSize, gp.tileSize);
	}
	
	public void setDialouge() {
		dialouges[0] = "Oh my goodness you found me!";
		dialouges[1] = "I'm so happy I could passionately \nkiss you with tounge!";
		dialouges[2] = "It has nothing to do with the fact\nthat I'm a massive homosexual...";
		dialouges[3] = "Anyway, the reason I ended up here was after i had \nwoke up is the Rat King, Rattatuchii told the God of \nthe island,nthat I started smoking again.";
		dialouges[4] = "I told Rattatuchii that in confidence after backsho...nevermind.";
		dialouges[5] = "I got a little info from this sweet skinny man that happened to \nviolently pound my ass after taking us hostage with Rattatuchii";
		dialouges[6] = "He told me his name but I honstely forgot because as he was \nusing my ass as a flesh light I knocked my head into the wall \na few times. I think it started with a J though.";
		dialouges[7] = "Him and Rattatuchii were running a train on on me it was \namazi....I mean horrible!";
		dialouges[8] = "He did mention that he was only following orders because he \nsaid his partner would lay on him and crush him if he didn't \nlisten.";
		dialouges[9] = "After that i was locked away by thoes rude \n(but sexy) rat people.";
		dialouges[10] = "If i wasn't so scared I would fuck one!";
		dialouges[11] = "Go on without me, my ass still hurts and its hard to walk.";
	}
	
	public void setIdleDialogues() {
		idleDialogues[0] = "Oh my...";
		idleDialogues[1] = "Fabulous!";
		idleDialogues[2] = "My ass hurts...";
		idleDialogues[3] = "Ow ow ow...";
		idleDialogues[4] = "I need some Dick!";
		idleDialogues[5] = "I need a cigarette!";
		idleDialogues[6] = "*Where's the dance floor?*";
		idleDialogues[7] = "I wonder if I'll make it modern next week.";
		idleDialogues[8] = "Mike's so jacked";
		idleDialogues[9] = "They better have left me some COCK";
		idleDialogues[10] = "Rattatuchii...";
		idleDialogues[11] = "This is what i get for stealing mike's savannah.";
	}
	
	public void setAction() {
		actionLockCounter++;
		
		// Random movement
		if(actionLockCounter == 120) {
			Random random = new Random();
			int i = random.nextInt(100)+1; 
			
			if (i <= 30) {
			    direction = "up";
			} else if (i > 30 && i <= 50) {
			    direction = "down";
			} else if (i > 50 && i <= 80) {
			    direction = "left";
			} else if (i > 80 && i <= 100) {
			    direction = "right";
			}
			
			actionLockCounter = 0;
		}
		
		// Random idle dialogue bubbles
		if(showIdleDialogue) {
			// Dialogue is showing, count down to hide it
			idleDialogueCounter++;
			if(idleDialogueCounter > 180) { // Show for 3 seconds (60 FPS * 3)
				showIdleDialogue = false;
				idleDialogueCounter = 0;
			}
		} else {
			// Dialogue is hidden, count until next one
			idleDialogueTimer++;
			if(idleDialogueTimer > 300) { // Wait 5 seconds between dialogues
				Random random = new Random();
				
				// 35% chance to show dialogue
				if(random.nextInt(100) < 35) {
					// Pick a random dialogue
					int dialogueIndex = random.nextInt(idleDialogues.length);
					idleDialogue = idleDialogues[dialogueIndex];
					showIdleDialogue = true;
					idleDialogueTimer = 0;
				} else {
					idleDialogueTimer = 0; // Reset timer, try again later
				}
			}
		}
	}
	
	public void speak() {
		//does charater stuff
		gp.ui.currentDialouge = dialouges[0];
		super.speak();
	}
}

		

	
