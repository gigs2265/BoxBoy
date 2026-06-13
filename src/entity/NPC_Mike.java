package entity;

import java.util.Random;
import main.Gamepanel;

public class NPC_Mike extends Entity{
	
	String[] idleDialogues = new String[12];
	
	public NPC_Mike(Gamepanel gp) {
		super(gp);
		type = type_npc;
		name = "Mike";
		direction = "down";
		speed = 1;
		getImage();
		setDialouge();
		setIdleDialogues();
	}
	
	public void getImage() {
		up1 = setup ("/npc/mikedown1", gp.tileSize, gp.tileSize);
		up2 = setup("/npc/mikedown2", gp.tileSize, gp.tileSize);
		down1 = setup("/npc/mikedown1", gp.tileSize, gp.tileSize);
		down2 = setup("/npc/mikedown2", gp.tileSize, gp.tileSize);
		left1 = setup("/npc/mikedown1", gp.tileSize, gp.tileSize);
		left2 =setup("/npc/mikedown2", gp.tileSize, gp.tileSize);
		right1 = setup("/npc/mikedown1", gp.tileSize, gp.tileSize);
		right2 = setup("/npc/mikedown2", gp.tileSize, gp.tileSize);
	}
	
	public void setDialouge() {
		dialouges[0] = "Oh daddy *smooch*";
	}
	
	public void setIdleDialogues() {
		idleDialogues[0] = "*smooch*";
		idleDialogues[1] = "Daddy...";
		idleDialogues[2] = "Mwah!";
		idleDialogues[3] = "*kissy noises*";
		idleDialogues[4] = "Oh my...";
		idleDialogues[5] = "*giggle*";
		idleDialogues[6] = "Hehe...";
		idleDialogues[7] = "*wink*";
		idleDialogues[8] = "Tee hee!";
		idleDialogues[9] = "*blush*";
		idleDialogues[10] = "Mwah mwah!";
		idleDialogues[11] = "*heart eyes*";
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
			if(idleDialogueTimer > 260) {
				Random random = new Random();
				if(random.nextInt(100) < 45) {
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

		// Mike only has 1 dialogue, so complete after first talk if Vic quest is done
		if(gp.quest.talkedToVic && !gp.quest.foundMike) {
			gp.quest.completeFoundMike();
		}
	}
}

