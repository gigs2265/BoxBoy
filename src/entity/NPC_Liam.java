package entity;

import java.util.Random;
import main.Gamepanel;

public class NPC_Liam extends Entity {
	
	String[] idleDialogues = new String[12];
	
	public NPC_Liam(Gamepanel gp) {
	    super(gp);
	    type = type_npc;
	    name = "Liam";
	    direction = "down";
	    speed = 1;
	    setDialouge();
	    setIdleDialogues();
	    getImage();
	}
	
	public void getImage() {
		up1 = setup ("/npc/liamup1", gp.tileSize, gp.tileSize);
		up2 = setup("/npc/liamup2", gp.tileSize, gp.tileSize);
		down1 = setup("/npc/liamdown1", gp.tileSize, gp.tileSize);
		down2 = setup("/npc/liamdown2", gp.tileSize, gp.tileSize);
		left1 = setup("/npc/liamleft1", gp.tileSize, gp.tileSize);
		left2 =setup("/npc/liamleft2", gp.tileSize, gp.tileSize);
		right1 = setup("/npc/liamright1", gp.tileSize, gp.tileSize);
		right2 = setup("/npc/liamright2", gp.tileSize, gp.tileSize);
	}
	
	public void setDialouge() {
		dialouges[0] = "I do not permit a woman to teach or to assume \nauthority over a man; she must be quiet!!";
		dialouges[1] = "Th...Theo?";
		dialouges[2] = "The Lord is good to all, and His tender mercies are \nover all His works!";
		dialouges[3] = "I thought you guys were all dead! \n Have you found the others?";
		dialouges[4] = "I'm not too sure what happend after we got back in \nyour car last night";
		dialouges[5] = "All i know is as soon as i shut the door I saw a \nbright white light then ended up here";
		dialouges[6] = "Thankfully, I kept this cross up my ass all night so \nwhoever put us here couldn't take it! \n Good thing it wasn't Ian or I would be without my \ncross.";
		dialouges[7] = "It's what has been keeping me sane since i woke up.";
		dialouges[8] = "I do wish I had my 9mm with my granade launcher \nattachment, but I think it was stolen..";
		dialouges[9] = "I WOULD HAVE FINALLY BEEN ABLE \nTO USE IT!!!!!!";
		dialouges[10] = "...";
		dialouges[11] = "Anyway...";
		dialouges[12] = "If you find the others come back and get me so we \ncan get out of this hell hole";
		dialouges[13] = "I would tag along but I have  about 80 more hail \nMarys left to do plus if someone sees my \nSOS signal in the sand I dont want to be gone."; 
	}
	
	public void setIdleDialogues() {
		idleDialogues[0] = "Hail Mary...";
		idleDialogues[1] = "The Lord is good...";
		idleDialogues[2] = "Praise Jesus!";
		idleDialogues[3] = "Fuck off mom, im stranded on an island";
		idleDialogues[4] = "HELP!!";
		idleDialogues[5] = "Watch out for kunt krabs!";
		idleDialogues[6] = "God help us";
		idleDialogues[7] = "Tim 212";
		idleDialogues[8] = "I miss my launcher...";
		idleDialogues[9] = "The SOS signal...";
		idleDialogues[10] = "Faith will save us...";
		idleDialogues[11] = "Amen.";
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
			if(idleDialogueTimer > 320) {
				Random random = new Random();
				if(random.nextInt(100) < 40) {
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

		// Check if all dialogues are complete and Ian quest is done
		if(dialogueComplete && gp.quest.foundIan && !gp.quest.foundLiam) {
			gp.quest.completeFoundLiam();
		}
	}
}