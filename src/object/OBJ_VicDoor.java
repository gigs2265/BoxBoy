package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_VicDoor extends Entity {

	Gamepanel gamePanel;

	public OBJ_VicDoor(Gamepanel gp) {
		super(gp);
		this.gamePanel = gp;

		name = "Vic's Door";
		down1 = setup("/objects/door", gp.tileSize, gp.tileSize);
		collision = true;

		solidArea.x = 0;
		solidArea.y = 16;
		solidArea.width = 48;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
	}

	public void interact() {
		gamePanel.gameState = gamePanel.dialougeState;

		if(!gamePanel.quest.talkedToVic) {
			gamePanel.ui.currentDialouge = "Vic: HEY ASSHOLE GET THE FUCK OVER HERE!!!";
		} else {
			gamePanel.ui.currentDialouge = "It's locked! I need a key.";
		}
	}

	public void use(Entity entity) {
		gamePanel.gameState = gamePanel.dialougeState;

		// Check if Vic dialogue is complete first
		if(!gamePanel.quest.talkedToVic) {
			gamePanel.ui.currentDialouge = "Vic: HEY ASSHOLE GET THE FUCK OVER HERE!!!";
			// Don't consume the key or remove the door
			return;
		}

		// Now check for key
		int keyIndex = 999;
		for(int i = 0; i < entity.inventory.size(); i++) {
			if(entity.inventory.get(i).name.equals("Key")) {
				keyIndex = i;
				break;
			}
		}

		if(keyIndex != 999) {
			// Only remove key and allow door to be removed if dialogue is complete
			entity.inventory.remove(keyIndex);
			gamePanel.ui.currentDialouge = "Door Unlocked! Now go find the others.";
			gamePanel.playSE(5);
			// Door will be removed by Player.pickUpObject after this returns
		} else {
			gamePanel.ui.currentDialouge = "You need a key to unlock this door!";
		}
	}
}
