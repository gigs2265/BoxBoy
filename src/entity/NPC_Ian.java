package entity;

import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import main.Gamepanel;

public class NPC_Ian extends Entity {
	
	
	String[] idleDialogues = new String[12];
	String[] idlePcDialogues = new String[12]; // "PC Mode" sanitized idle bubbles, same indices - see main.PcText
	
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

		pcDialouges[0] = "Oh my goodness, you found me!";
		pcDialouges[1] = "I'm so happy I could give you a \nwarm, enthusiastic hug!";
		pcDialouges[2] = "That has nothing to do with the fact \nthat I am a proud member of the community...";
		pcDialouges[3] = "Anyway, the reason I ended up here is that after I \nwoke up, the Rat King, Rattatuchii, informed the \nisland's leadership that I'd resumed smoking.";
		pcDialouges[4] = "I shared that with Rattatuchii in confidence \nduring a private conversation...nevermind.";
		pcDialouges[5] = "I gathered some information from a very kind, slender \ngentleman who was involved in detaining us alongside \nRattatuchii.";
		pcDialouges[6] = "He told me his name but I honestly forgot, since our \nconversation was quite memorable in other ways. \nI believe it started with a J though.";
		pcDialouges[7] = "He and Rattatuchii spent considerable time with me, \nit was quite an experience... a difficult one!";
		pcDialouges[8] = "He did mention he was only following orders, and that \nhis colleague would apply significant pressure if he \ndidn't comply.";
		pcDialouges[9] = "After that I was kept here by those \nunfriendly (but well-dressed) rat people.";
		pcDialouges[10] = "If I weren't so nervous, I'd try to befriend one!";
		pcDialouges[11] = "Go on without me, I'm still a bit sore and \nwalking is difficult right now.";
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

		idlePcDialogues[0] = "Oh my...";
		idlePcDialogues[1] = "Fabulous!";
		idlePcDialogues[2] = "I'm a bit sore...";
		idlePcDialogues[3] = "Ow ow ow...";
		idlePcDialogues[4] = "I could use some companionship!";
		idlePcDialogues[5] = "I need a cigarette!";
		idlePcDialogues[6] = "*Where's the dance floor?*";
		idlePcDialogues[7] = "I wonder if I'll update my style next week.";
		idlePcDialogues[8] = "Mike's in great shape";
		idlePcDialogues[9] = "They better have left me a snack";
		idlePcDialogues[10] = "Rattatuchii...";
		idlePcDialogues[11] = "This is what I get for borrowing Mike's \ncologne without asking.";
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
					idleDialogue = main.PcText.pick(gp, idleDialogues[dialogueIndex], idlePcDialogues[dialogueIndex]);
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

		// Check if all dialogues are complete and Mike quest is done
		if(dialogueComplete && gp.quest.foundMike && !gp.quest.foundIan) {
			gp.quest.completeFoundIan();
		}
	}
}

		

	
