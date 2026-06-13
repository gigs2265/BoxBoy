package entity;

import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import main.Gamepanel;

public class NPC_Vic extends Entity {
	
	
	String[] idleDialogues = new String[12];
	
	public NPC_Vic(Gamepanel gp) {
		super(gp);
		
		type = type_npc;  
		name = "Vic";  
		direction = "down";
		speed = 1;
		
		getImage();
		setDialouge();
		setIdleDialogues();
		
		
	}
	
	public void getImage() {
		up1 = setup ("/npc/vicup1", gp.tileSize, gp.tileSize);
		up2 = setup("/npc/vicup2", gp.tileSize, gp.tileSize);
		down1 = setup("/npc/vicdown1", gp.tileSize, gp.tileSize);
		down2 = setup("/npc/vicdown2", gp.tileSize, gp.tileSize);
		left1 = setup("/npc/vicleft1", gp.tileSize, gp.tileSize);
		left2 =setup("/npc/vicleft2", gp.tileSize, gp.tileSize);
		right1 = setup("/npc/vicright1", gp.tileSize, gp.tileSize);
		right2 = setup("/npc/vicright2", gp.tileSize, gp.tileSize);
	}
	
	public void setDialouge() {
		dialouges[0] = "Finally, your dumbass is awake.";
		dialouges[1] = "We must have blacked out on our way home from \nthe bar!.";
		dialouges[2] = "Black... EW!";
		dialouges[3] = "I TOLD YOU WE SHOULD HAVE GONE HOME AFTER MAGIC!! BUT \nNOOO FATTY HAD TO GET ALCHY!!!";
		dialouges[4] = "...When I woke up I saw this rat-like creature \nclosing the door down the hallway";
		dialouges[5] = " I think it was trying to lock us in here, but \nthe inbread fuck forgot to take the key with em";
		dialouges[6] = "Grab the key and unlock the door \nYOU BOX HEADED FUCK!!!";
		dialouges[7] = "I would have done it but I'm too tired. I havent \nhad a white monster in over an hour! ";
		dialouges[8] = "Oh wait! don't you still have that rat spray on you? \nTry using that on those rat-things if you run into them!";
		dialouges[9] = "Press ENTER when those mother fuckers get in your face. \nThat should fuck em up.";
		dialouges[10] = "For once you're square ass was right about bringing it to the \nshop.";
	}
	
	public void setIdleDialogues() {
		idleDialogues[0] = "I need a white monster...";
		idleDialogues[1] = "Ugh";
		idleDialogues[2] = "*sigh*";
		idleDialogues[3] = "COME HERE AND HIT ENTR ON ME AND KEEP DOIN IT DUMBASS!!";
		idleDialogues[4] = "Where are we?";
		idleDialogues[5] = "My sugar is low";
		idleDialogues[6] = "Hurry up asshole!!";
		idleDialogues[7] = "Someones getting an ass beating!";
		idleDialogues[8] = "Why me?";
		idleDialogues[9] = "THEOOO HELLO?!?!";
		idleDialogues[10] = "FUCK Rattatuchii!";
		idleDialogues[11] = "I could be golfing right now...";
	}
	
	public void setAction() {
		actionLockCounter++;
		
		// Random movement
		if(actionLockCounter == 60) {
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
			if(idleDialogueCounter > 140) { // Show for 3 seconds (60 FPS * 3)
				showIdleDialogue = false;
				idleDialogueCounter = 0;
				System.out.println("Vic: Hiding dialogue bubble");
			}
		} else {
			// Dialogue is hidden, count until next one
			idleDialogueTimer++;
			if(idleDialogueTimer > 240) { // Wait 4 seconds between dialogues
				Random random = new Random();
				
				// 40% chance to show dialogue (Vic complains more than Josh!)
				if(random.nextInt(100) < 40) {
					// Pick a random dialogue
					int dialogueIndex = random.nextInt(idleDialogues.length);
					idleDialogue = idleDialogues[dialogueIndex];
					showIdleDialogue = true;
					idleDialogueTimer = 0;
					System.out.println("Vic: Showing dialogue - " + idleDialogue);
				} else {
					idleDialogueTimer = 0; // Reset timer, try again later
				}
			}
		}
	}
	
	public void speak() {
		super.speak();

		// Check if all dialogues are complete
		if(dialogueComplete && !gp.quest.talkedToVic) {
			gp.quest.completeTalkToVic();
		}
	}
}