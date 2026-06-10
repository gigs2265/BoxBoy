package object;

import entity.Entity;
import main.Gamepanel;

public class OBJ_Chest extends Entity {
	
	boolean opened = false;
	Gamepanel gamePanel;
	
	public OBJ_Chest(Gamepanel gp) {
		super(gp);
		this.gamePanel = gp;
		
		name = "Chest";
		down1 = setup("/objects/chest", gp.tileSize, gp.tileSize);
		collision = true;
		
		solidArea.x = 4;
		solidArea.y = 16;
		solidArea.width = 40;
		solidArea.height = 32;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
	}
	
	// Method to add items to the chest's inventory
	public void setLoot(Entity item) {
		inventory.add(item);
	}
	
	public void interact() {
		gamePanel.gameState = gamePanel.dialougeState;
		
		if(opened == false) {
			gamePanel.ui.currentDialouge = "It's locked. You need a key!";
		} else {
			// If chest is already opened, show the inventory selection
			if(inventory.size() > 0) {
				gamePanel.gameState = gamePanel.tradeState;
				gamePanel.ui.npc = this;
			} else {
				gamePanel.ui.currentDialouge = "The chest is empty!";
			}
		}
	}
	
	public void use(Entity entity) {
		gamePanel.gameState = gamePanel.dialougeState;
		
		if(opened == false) {
			int keyIndex = 999;
			for(int i = 0; i < entity.inventory.size(); i++) {
				if(entity.inventory.get(i).name.equals("Key")) {
					keyIndex = i;
					break;
				}
			}
			
			if(keyIndex != 999) {
				// Remove the key
				entity.inventory.remove(keyIndex);
				opened = true;
				
				// Try to change chest appearance (if image exists)
				try {
					down1 = setup("/objects/chestopen", gamePanel.tileSize, gamePanel.tileSize);
				} catch (Exception e) {
					// If chestopen image doesn't exist, just keep the chest image
					System.out.println("Warning: chestopen.png not found, using default chest image");
				}
				
				// Remove collision so player can walk through
				collision = false;
				
				// Play sounds
				gamePanel.playSE(3);
				gamePanel.playSE(5);
				
				// Show message and open inventory selection
				gamePanel.ui.currentDialouge = "You opened the chest!\nPress ENTER to see what's inside.";
				
			} else {
				gamePanel.ui.currentDialouge = "You need a key to open this chest!";
			}
		} else {
			// Chest is already opened, show the inventory selection
			if(inventory.size() > 0) {
				gamePanel.gameState = gamePanel.tradeState;
				gamePanel.ui.npc = this;
			} else {
				gamePanel.ui.currentDialouge = "The chest is empty!";
			}
		}
	}
}