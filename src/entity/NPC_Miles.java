package entity;

import java.util.Random;
import main.Gamepanel;

public class NPC_Miles extends Entity{
	
	String[] idleDialogues = new String[12];
	String[] idlePcDialogues = new String[12]; // "PC Mode" sanitized idle bubbles, same indices - see main.PcText
	
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

		pcDialouges[0] = "About time you found me...";
		pcDialouges[1] = "Wow, you're really not the sharpest tool.";
		pcDialouges[2] = "Anyway, more about me heh heh heh. I dozed off before we even left the bar \nbut when I woke up \nI was resting on the floor here!";
		pcDialouges[3] = "They took my phone, my wallet (quite a substantial amount of \ncash in there) AND my drinks! I bet when those less-fortunate \nindividuals open my wallet it will \nprobably be the most money they've ever seen!";
		pcDialouges[4] = "heh heh heh I'm pretty great.";
		pcDialouges[5] = "Once we're off this island, I'm DEFINITELY getting that 5th jet ski. \nLife's too short to settle for only 4.";
		pcDialouges[6] = "AND I'm going to treat myself to something nice!!!!!";
		pcDialouges[7] = "There might be other friends downstairs, go check.";
		pcDialouges[8] = "You want me to go with you? No thanks...I should stay here and find \na TV or something, the fights are on soon.";
		pcDialouges[9] = "Plus I couldn't really help anyway, unless these rats \nexclusively do grappling.";
		pcDialouges[10] = "...";
		pcDialouges[11] = "By the way, if you see Mike D, let him know I said \nhe's a bit misguided.";
		pcDialouges[12] = "Okay, you're free to go now.";
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
		idleDialogues[9] = "BLOW JOBS! BLOWJOBS! BLOW JOBS!!";
		idleDialogues[10] = "Vin and Karen are gonna be so pissed if I cant come in later..";
		idleDialogues[11] = "Fuck off.";

		idlePcDialogues[0] = "Heh heh heh...";
		idlePcDialogues[1] = "I'm the best.";
		idlePcDialogues[2] = "GOODNESS GRACIOUS!";
		idlePcDialogues[3] = "F.N.C!!!!";
		idlePcDialogues[4] = "Theo is quite bothersome";
		idlePcDialogues[5] = "I better not miss the fights tonight";
		idlePcDialogues[6] = "I need a TV";
		idlePcDialogues[7] = "Mike's a bit misguided.";
		idlePcDialogues[8] = "I truly enjoy financial success";
		idlePcDialogues[9] = "TREATS! TREATS! TREATS!!";
		idlePcDialogues[10] = "Vin and Karen will be disappointed if I \ncan't make it in later..";
		idlePcDialogues[11] = "Please excuse me.";
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

		// Check if all dialogues are complete and Liam quest is done
		if(dialogueComplete && gp.quest.foundLiam && !gp.quest.foundMiles) {
			gp.quest.completeFoundMiles();
		}
	}
}

