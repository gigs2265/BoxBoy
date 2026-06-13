package entity;

import java.util.Random;
import main.Gamepanel;

public class NPC_John extends Entity {
	
	String[] idleDialogues = new String[12];
	
	public NPC_John(Gamepanel gp) {
		super(gp);
		type = type_npc;
		name = "John";
		direction = "down";
		speed = 1;
		getImage();
		setDialouge();
		setIdleDialogues();
	}
	
	public void getImage() {
		up1 = setup ("/npc/johnup1", gp.tileSize, gp.tileSize);
		up2 = setup("/npc/johnup2", gp.tileSize, gp.tileSize);
		down1 = setup("/npc/johndown1", gp.tileSize, gp.tileSize);
		down2 = setup("/npc/johndown2", gp.tileSize, gp.tileSize);
		left1 = setup("/npc/johnleft1", gp.tileSize, gp.tileSize);
		left2 =setup("/npc/johnleft2", gp.tileSize, gp.tileSize);
		right1 = setup("/npc/johnright1", gp.tileSize, gp.tileSize);
		right2 = setup("/npc/johnright2", gp.tileSize, gp.tileSize);
	}
	
	public void setDialouge() {
		dialouges[0] = "HOLY SHIT THEO?!?! IS THAT YOU?";
		dialouges[1] = "Dude I thought you were dead";
		dialouges[2] = "Have you seen my phone? I need to watch a few tiktoks";
		dialouges[3] = "no? FUCK!!!!!!";
		dialouges[4] = "Please dude just get me the fuck out of here \n I keep hearing screaming it sounds like a monster";
		dialouges[5] = "All night I heard 'JIMMY!!!! YOU ARE A FAT HOG!' Over and over again!.\n It sounda alot like Nick but I can't tell for sure I don't even have my dab rig \nto mellow me out";
		dialouges[6] = "All I've got is this joint i stole off Mike before the bar. I could'nt help it, \nit was the fellon in me, but it's not even calming me down though. \n Joints are below me but I have nothing else.";
	}
	
	public void setIdleDialogues() {
		idleDialogues[0] = "Where's my phone...";
		idleDialogues[1] = "I need tiktok...";
		idleDialogues[2] = "FUCK!!!";
		idleDialogues[3] = "They coulda left me a joint atleast";
		idleDialogues[4] = "Was that Nick?";
		idleDialogues[5] = "I need my dab rig...";
		idleDialogues[6] = "*nervous*";
		idleDialogues[7] = "My P.O is gonna be pissed";
		idleDialogues[8] = "Get me out!!";
		idleDialogues[9] = "Fellon life";
		idleDialogues[10] = "What was that?!";
		idleDialogues[11] = "YO BOX HEAD";
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
			if(idleDialogueTimer > 280) {
				Random random = new Random();
				if(random.nextInt(100) < 35) {
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

		// Complete John quest when all dialogues are done and all friends are found
		if(dialogueComplete && gp.quest.foundAllFriends && !gp.quest.foundJohn) {
			gp.quest.completeFoundJohn();
		}
	}
}



