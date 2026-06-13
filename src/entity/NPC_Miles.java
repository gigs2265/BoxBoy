package entity;

import java.util.Random;
import main.Gamepanel;

public class NPC_Miles extends Entity{
	
	String[] idleDialogues = new String[12];
	
	public NPC_Miles(Gamepanel gp) {
		super(gp);
		type = type_npc;
		name = "Miles";
		direction = "down";
		speed = 1;
		setDialouge();
		setIdleDialogues();
		getImage();
	}
	
	public void getImage() {
		up1 = setup ("/npc/milesup1", gp.tileSize, gp.tileSize);
		up2 = setup("/npc/milesup2", gp.tileSize, gp.tileSize);
		down1 = setup("/npc/milesdown1", gp.tileSize, gp.tileSize);
		down2 = setup("/npc/milesdown2", gp.tileSize, gp.tileSize);
		left1 = setup("/npc/milesleft1", gp.tileSize, gp.tileSize);
		left2 =setup("/npc/milesleft2", gp.tileSize, gp.tileSize);
		right1 = setup("/npc/milesright1", gp.tileSize, gp.tileSize);
		right2 = setup("/npc/milesright2", gp.tileSize, gp.tileSize);
	}
	
	public void setDialouge() {
		dialouges[0] = "Bout time you found me...";
		dialouges[1] = "God, you're so fucking dumb";
		dialouges[2] = "Anyway, more about me heh heh heh. I blacked out before we even left the bar \nbut when i woke up \nI was laying on the floor here!";
		dialouges[3] = "They took my phone, my wallet ( I have 2 jet skis worth of cash in there) \nAND my brews dude!  I bet when thoes broke fucks \nopen my  wallet it will \nprob be the most  money they have seen  in their lives!";
		dialouges[4] = "heh heh heh I'm the best.";
		dialouges[5] = "Once we're off this island, I'm DEF getting that 5th jetski. Life is way \ntoo short to only have 4";
		dialouges[6] = " AND I'm getting a BLOWJOB!!!!!";
		dialouges[7] = "There might be other friends downstairs, go check.";
		dialouges[8] = "you want me to go with you? No thanks...I need to stay here and find a Tv or \nsomething  the fights are on soon";
		dialouges[9] = "Plus I couldnt help anyway, unless these rats exsclusively grapple";
		dialouges[10] = "...";
		dialouges[11] = "By the way, if you see Mike D tell him I said  he's an idiot.";
		dialouges[12] = "Ok, you can fuck off now.";
	}
	
	public void setIdleDialogues() {
		idleDialogues[0] = "Heh heh heh...";
		idleDialogues[1] = "I'm the best.";
		idleDialogues[2] = "FAT CUNT!";
		idleDialogues[3] = "F.N.C!!!!";
		idleDialogues[4] = "Theo is so fucking annoying";
		idleDialogues[5] = "I better not miss the fights tonight";
		idleDialogues[6] = "I need a TV";
		idleDialogues[7] = "Mike's an idiot.";
		idleDialogues[8] = "GOD I love being rich";
		idleDialogues[9] = "boobs";
		idleDialogues[10] = "2 jet skis worth...";
		idleDialogues[11] = "Fuck off.";
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
			if(idleDialogueTimer > 250) {
				Random random = new Random();
				if(random.nextInt(100) < 42) {
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

		// Check if all dialogues are complete and Liam quest is done
		if(dialogueComplete && gp.quest.foundLiam && !gp.quest.foundMiles) {
			gp.quest.completeFoundMiles();
		}
	}
}

