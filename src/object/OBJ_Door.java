package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Door extends Entity {
	
	Gamepanel gamePanel;
	
	public OBJ_Door(Gamepanel gp) {
		super(gp);
		this.gamePanel = gp;
		
		name = "Door";
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
		gamePanel.ui.currentDialouge = "It's locked!";
	}
	
	public void use(Entity entity) {
		gamePanel.gameState = gamePanel.dialougeState;
		
		int keyIndex = 999;
		for(int i = 0; i < entity.inventory.size(); i++) {
			if(entity.inventory.get(i).name.equals("Key")) {
				keyIndex = i;
				break;
			}
		}
		
		if(keyIndex != 999) {
			entity.inventory.remove(keyIndex);
			gamePanel.ui.currentDialouge = "Door Unlocked!";
			gamePanel.playSE(5);
		} else {
			gamePanel.ui.currentDialouge = "You need a key to open this door!";
		}
	}
}