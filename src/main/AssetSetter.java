package main;

import entity.NPC_Ian;
import monster.MON_Brian;
import entity.NPC_John;
import entity.NPC_Josh;
import entity.NPC_Liam;
import entity.NPC_Mike;
import entity.NPC_Miles;
import entity.NPC_Vic;
import monster.MON_Armored_Tuchi;
import monster.MON_Kunt_Krab;
import monster.MON_Nick;
import monster.MON_Tuchi;
import object.OBJ_Beer;
import object.OBJ_Boxcutter;
import object.OBJ_Chest;
import object.OBJ_Door;
import object.OBJ_MilesDoor;
import object.OBJ_VicDoor;
import object.OBJ_Fart_Coin;
import object.OBJ_Jimmysass;
import object.OBJ_Key;
import object.OBJ_Rattail;
import object.OBJ_Spicyburger;

public class AssetSetter {
	
	Gamepanel gp;
	
	public AssetSetter(Gamepanel gp) {
		this.gp = gp;
	}
	
	public void setObject() {
		int mapNum = 0;
		int i = 0;
		
		// KEYS FOR DOORS/CHESTS
		gp.obj[mapNum][i] = new OBJ_Key(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 25;
		gp.obj[mapNum][i].worldY = gp.tileSize * 22;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Key(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 832;
		gp.obj[mapNum][i].worldY = gp.tileSize * 188;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Key(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 10;
		gp.obj[mapNum][i].worldY = gp.tileSize * 1;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Key(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 2;
		gp.obj[mapNum][i].worldY = gp.tileSize * 37;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Key(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 36;
		gp.obj[mapNum][i].worldY = gp.tileSize * 30;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Key(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 33;
		gp.obj[mapNum][i].worldY = gp.tileSize * 13;
		i++;
		
	
		
		// DOORS ON OVERWORLD STRUCTURES (adjust positions based on your structures)
		gp.obj[mapNum][i] = new OBJ_Door(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 16;
		gp.obj[mapNum][i].worldY = gp.tileSize * 8;
		i++;
		
		// VIC'S DOOR - Special quest-locked door (unlocks after talking to Vic)
		gp.obj[mapNum][i] = new OBJ_VicDoor(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 23;
		gp.obj[mapNum][i].worldY = gp.tileSize * 32;
		i++;
		
		// MILES' DOOR - Special quest-locked door
		gp.obj[mapNum][i] = new OBJ_MilesDoor(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 38;
		gp.obj[mapNum][i].worldY = gp.tileSize * 12;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Door(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 38;
		gp.obj[mapNum][i].worldY = gp.tileSize * 42;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Door(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 8;
		gp.obj[mapNum][i].worldY = gp.tileSize * 21;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Boxcutter(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 11;
		gp.obj[mapNum][i].worldY = gp.tileSize * 20;
		i++;
		
		// CHESTS WITH LOOT
		OBJ_Chest chest1 = new OBJ_Chest(gp);
		chest1.worldX = gp.tileSize * 1;
		chest1.worldY = gp.tileSize * 34;
		chest1.setLoot(new OBJ_Spicyburger(gp));
		chest1.setLoot(new OBJ_Beer(gp));
		gp.obj[mapNum][i] = chest1;
		i++;
		
		OBJ_Chest chest2 = new OBJ_Chest(gp);
		chest2.worldX = gp.tileSize * 35;
		chest2.worldY = gp.tileSize * 3;
		chest2.setLoot(new OBJ_Fart_Coin(gp));
		gp.obj[mapNum][i] = chest2;
		i++;
		
		OBJ_Chest chest3 = new OBJ_Chest(gp);
		chest3.worldX = gp.tileSize * 1;
		chest3.worldY = gp.tileSize * 46;
		chest3.setLoot(new OBJ_Fart_Coin(gp));
		chest3.setLoot(new OBJ_Fart_Coin(gp));
		chest3.setLoot(new OBJ_Key(gp));
		chest3.setLoot(new OBJ_Beer(gp));
		gp.obj[mapNum][i] = chest3;
		i++;
		
		// COLLECTIBLES
		gp.obj[mapNum][i] = new OBJ_Fart_Coin(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 24;
		gp.obj[mapNum][i].worldY = gp.tileSize * 20;
		i++;
		
	
		
		gp.obj[mapNum][i] = new OBJ_Rattail(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 13;
		gp.obj[mapNum][i].worldY = gp.tileSize * 39;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Rattail(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 35;
		gp.obj[mapNum][i].worldY = gp.tileSize * 45;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Rattail(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 42;
		gp.obj[mapNum][i].worldY = gp.tileSize * 23;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Rattail(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 34;
		gp.obj[mapNum][i].worldY = gp.tileSize * 16;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Rattail(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 13;
		gp.obj[mapNum][i].worldY = gp.tileSize * 18;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Rattail(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 42;
		gp.obj[mapNum][i].worldY = gp.tileSize * 14;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Beer(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 7;
		gp.obj[mapNum][i].worldY = gp.tileSize * 1;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Beer(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 43;
		gp.obj[mapNum][i].worldY = gp.tileSize * 22;
		i++;
		

		gp.obj[mapNum][i] = new OBJ_Beer(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 1;
		gp.obj[mapNum][i].worldY = gp.tileSize * 43;
		i++;
		
		// DUNGEON OBJECTS (Map 1)
		mapNum = 1;
		i = 0;
		
		// LOCKED CELL DOORS along the entrance hallway (one per imprisoned character)
		int[] cellDoorCols = {5, 11, 17, 23, 29, 35};
		for(int c = 0; c < cellDoorCols.length; c++) {
			gp.obj[mapNum][i] = new OBJ_Door(gp);
			gp.obj[mapNum][i].worldX = gp.tileSize * cellDoorCols[c];
			gp.obj[mapNum][i].worldY = gp.tileSize * 44;
			i++;
		}
		
		// CHEST in the armored tuchi room - holds the key for the locked door
		OBJ_Chest chest5 = new OBJ_Chest(gp);
		chest5.worldX = gp.tileSize * 44;
		chest5.worldY = gp.tileSize * 28;
		chest5.setLoot(new OBJ_Key(gp));
		chest5.setLoot(new OBJ_Beer(gp));
		gp.obj[mapNum][i] = chest5;
		i++;
		
		// LOCKED DOOR on the far side of the armored tuchi room -> bigger hallway
		gp.obj[mapNum][i] = new OBJ_Door(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 32;
		gp.obj[mapNum][i].worldY = gp.tileSize * 30;
		i++;
		
		// LOCKED DOOR to the boss area (opened with the key Nick drops)
		gp.obj[mapNum][i] = new OBJ_Door(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 9;
		gp.obj[mapNum][i].worldY = gp.tileSize * 21;
		i++;
		
		// Pickups
		gp.obj[mapNum][i] = new OBJ_Fart_Coin(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 36;
		gp.obj[mapNum][i].worldY = gp.tileSize * 33;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Beer(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 25;
		gp.obj[mapNum][i].worldY = gp.tileSize * 30;
		i++;
		
		gp.obj[mapNum][i] = new OBJ_Beer(gp);
		gp.obj[mapNum][i].worldX = gp.tileSize * 12;
		gp.obj[mapNum][i].worldY = gp.tileSize * 32;
		i++;
	}
	
	public void setNPC() {
		int i = 0;
		int mapNum = 0;
		
		gp.npc[mapNum][i] = new NPC_Ian(gp);
		gp.npc[mapNum][i].worldX = gp.tileSize * 17;
		gp.npc[mapNum][i].worldY = gp.tileSize * 6;
		i++;
		
		gp.npc[mapNum][i] = new NPC_Vic(gp);
		gp.npc[mapNum][i].worldX = gp.tileSize * 22;
		gp.npc[mapNum][i].worldY = gp.tileSize * 22;
		i++;
		
		gp.npc[mapNum][i] = new NPC_Liam(gp);
		gp.npc[mapNum][i].worldX = gp.tileSize * 30;
		gp.npc[mapNum][i].worldY = gp.tileSize * 45;
		i++;
		
		gp.npc[mapNum][i] = new NPC_Miles(gp);
		gp.npc[mapNum][i].worldX = gp.tileSize * 38;
		gp.npc[mapNum][i].worldY = gp.tileSize * 4;
		i++;
		
		gp.npc[mapNum][i] = new NPC_Mike(gp);
		gp.npc[mapNum][i].worldX = gp.tileSize * 3;
		gp.npc[mapNum][i].worldY = gp.tileSize * 34;
		i++;
		
		mapNum = 1;
		i = 0;
		
		// JOHN - locked in the first cell along the entrance hallway
		gp.npc[mapNum][i] = new NPC_John(gp);
		gp.npc[mapNum][i].worldX = gp.tileSize * 5;
		gp.npc[mapNum][i].worldY = gp.tileSize * 41;
		i++;
		
		// JOSH the merchant - at the end of the hallway, in the armored tuchi room
		gp.npc[mapNum][i] = new NPC_Josh(gp);
		gp.npc[mapNum][i].worldX = gp.tileSize * 42;
		gp.npc[mapNum][i].worldY = gp.tileSize * 34;
		i++;
	}
	
	public void setMonster() {
		int mapNum = 0;
		int i = 0;
		
		gp.monster[mapNum][i] = new MON_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 5;
		gp.monster[mapNum][i].worldY = gp.tileSize * 35;	
		i++;
		
		gp.monster[mapNum][i] = new MON_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 13;
		gp.monster[mapNum][i].worldY = gp.tileSize * 22;	
		i++;
		
		gp.monster[mapNum][i] = new MON_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 13;
		gp.monster[mapNum][i].worldY = gp.tileSize * 21;
		i++;
		
		gp.monster[mapNum][i] = new MON_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 5;
		gp.monster[mapNum][i].worldY = gp.tileSize * 30;
		i++;
		
		gp.monster[mapNum][i] = new MON_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 28;
		gp.monster[mapNum][i].worldY = gp.tileSize * 45;
		i++;
		
		gp.monster[mapNum][i] = new MON_Armored_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 43;
		gp.monster[mapNum][i].worldY = gp.tileSize * 15;
		i++;
		
		gp.monster[mapNum][i] = new MON_Armored_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 48;
		gp.monster[mapNum][i].worldY = gp.tileSize * 2;
		i++;
		
		gp.monster[mapNum][i] = new MON_Armored_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 31;
		gp.monster[mapNum][i].worldY = gp.tileSize * 1;
		i++;
		
		gp.monster[mapNum][i] = new MON_Armored_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 44;
		gp.monster[mapNum][i].worldY = gp.tileSize * 15;
		i++;
		
		gp.monster[mapNum][i] = new MON_Kunt_Krab(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 6;
		gp.monster[mapNum][i].worldY = gp.tileSize * 43;
		i++;
		
		gp.monster[mapNum][i] = new MON_Kunt_Krab(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 6;
		gp.monster[mapNum][i].worldY = gp.tileSize * 45;
		i++;
		
		gp.monster[mapNum][i] = new MON_Kunt_Krab(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 48;
		gp.monster[mapNum][i].worldY = gp.tileSize * 46;
		i++;
		
		gp.monster[mapNum][i] = new MON_Kunt_Krab(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 48;
		gp.monster[mapNum][i].worldY = gp.tileSize * 44;
		i++;
		
		gp.monster[mapNum][i] = new MON_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 42;
		gp.monster[mapNum][i].worldY = gp.tileSize * 15;
		i++;
		
		gp.monster[mapNum][i] = new MON_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 1;
		gp.monster[mapNum][i].worldY = gp.tileSize * 3;
		i++;
		
		gp.monster[mapNum][i] = new MON_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 6;
		gp.monster[mapNum][i].worldY = gp.tileSize * 5;
		i++;
		
		gp.monster[mapNum][i] = new MON_Kunt_Krab(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 45;
		gp.monster[mapNum][i].worldY = gp.tileSize * 44;
		i++;
		
		gp.monster[mapNum][i] = new MON_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 20;
		gp.monster[mapNum][i].worldY = gp.tileSize * 15;
		i++;
		
		gp.monster[mapNum][i] = new MON_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 20;
		gp.monster[mapNum][i].worldY = gp.tileSize * 11;
		i++;
	
		
		mapNum = 1;
		i = 0;
		
		// ARMORED TUCHIS guarding the open room at the end of the entrance hallway
		// This first one drops a key when killed
		MON_Armored_Tuchi keyTuchi = new MON_Armored_Tuchi(gp);
		keyTuchi.dropsKey = true;
		keyTuchi.worldX = gp.tileSize * 36;
		keyTuchi.worldY = gp.tileSize * 28;
		gp.monster[mapNum][i] = keyTuchi;
		i++;
		
		gp.monster[mapNum][i] = new MON_Armored_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 40;
		gp.monster[mapNum][i].worldY = gp.tileSize * 31;
		i++;
		
		gp.monster[mapNum][i] = new MON_Armored_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 44;
		gp.monster[mapNum][i].worldY = gp.tileSize * 33;
		i++;
		
		// TUCHI in the bigger hallway behind the locked door
		gp.monster[mapNum][i] = new MON_Tuchi(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 24;
		gp.monster[mapNum][i].worldY = gp.tileSize * 30;
		i++;
		
		// NICK - mini-boss in the medium room, drops the key to the boss area
		gp.monster[mapNum][i] = new MON_Nick(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 9;
		gp.monster[mapNum][i].worldY = gp.tileSize * 29;
		i++;
		
		// BRIAN - final boss in the large arena
		gp.monster[mapNum][i] = new MON_Brian(gp);
		gp.monster[mapNum][i].worldX = gp.tileSize * 22;
		gp.monster[mapNum][i].worldY = gp.tileSize * 8;
		i++;
	}
}