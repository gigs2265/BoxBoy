package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import entity.Entity;
import object.OBJ_Beer;
import object.OBJ_Bloat;
import object.OBJ_Boxcutter;
import object.OBJ_Burger;
import object.OBJ_Cheese;
import object.OBJ_Chest;
import object.OBJ_Dildo_Sword;
import object.OBJ_Door;
import object.OBJ_Fart_Coin;
import object.OBJ_Jimmysass;
import object.OBJ_Key;
import object.OBJ_MilesDoor;
import object.OBJ_Rattail;
import object.OBJ_Spicyburger;
import object.OBJ_Spray_Normal;
import object.OBJ_Titmilk;
import object.OBJ_VicDoor;

public class SaveLoad {
	
	Gamepanel gp;
	
	public SaveLoad(Gamepanel gp) {
		this.gp = gp;
	}
	
	// Turns a saved item name back into a real object
	public Entity getObject(String itemName) {
		Entity obj = null;
		
		switch(itemName) {
			case "Rat Spray": obj = new OBJ_Spray_Normal(gp); break;
			case "Ian's Sword": obj = new OBJ_Dildo_Sword(gp); break;
			case "Box Cutter": obj = new OBJ_Boxcutter(gp); break;
			case "Bloat": obj = new OBJ_Bloat(gp); break;
			case "Key": obj = new OBJ_Key(gp); break;
			case "BEER": obj = new OBJ_Beer(gp); break;
			case "Jimmy's Big Fat Ass": obj = new OBJ_Jimmysass(gp); break;
			case "Hot Burg": obj = new OBJ_Spicyburger(gp); break;
			case "Burg": obj = new OBJ_Burger(gp); break;
			case "Cheese": obj = new OBJ_Cheese(gp); break;
			case "Rat Tail": obj = new OBJ_Rattail(gp); break;
			case "$Fartcoin": obj = new OBJ_Fart_Coin(gp); break;
			case "Titmilk": obj = new OBJ_Titmilk(gp); break;
			case "Chest": obj = new OBJ_Chest(gp); break;
			case "Door": obj = new OBJ_Door(gp); break;
			case "Miles' Door": obj = new OBJ_MilesDoor(gp); break;
			case "Vic's Door": obj = new OBJ_VicDoor(gp); break;
		}
		return obj;
	}
	
	public void save() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("save.dat")));
			
			DataStorage ds = new DataStorage();
			
			// PLAYER STATS
			ds.level = gp.player.level;
			ds.maxLife = gp.player.maxLife;
			ds.life = gp.player.life;
			ds.maxMana = gp.player.maxMana;
			ds.mana = gp.player.mana;
			ds.strength = gp.player.strength;
			ds.dexterity = gp.player.dexterity;
			ds.exp = gp.player.exp;
			ds.nextLevelExp = gp.player.nextLevelExp;
			ds.fartcoin = gp.player.$fartcoin;
			
			// PLAYER POSITION
			ds.worldX = gp.player.worldX;
			ds.worldY = gp.player.worldY;
			ds.currentMap = gp.currentMap;
			
			// PLAYER INVENTORY
			ds.currentWeaponSlot = -1;
			ds.currentShieldSlot = -1;
			for(int i = 0; i < gp.player.inventory.size(); i++) {
				ds.itemNames.add(gp.player.inventory.get(i).name);
				if(gp.player.inventory.get(i) == gp.player.currentWepon) {
					ds.currentWeaponSlot = i;
				}
				if(gp.player.inventory.get(i) == gp.player.currentSheild) {
					ds.currentShieldSlot = i;
				}
			}
			
			// QUEST PROGRESS
			ds.foundAllFriends = gp.quest.foundAllFriends;
			ds.foundJohn = gp.quest.foundJohn;
			ds.defeatedNick = gp.quest.defeatedNick;
			ds.metNick = gp.quest.metNick;
			ds.defeatedBrian = gp.quest.defeatedBrian;
			ds.talkedToVic = gp.quest.talkedToVic;
			ds.foundMike = gp.quest.foundMike;
			ds.foundIan = gp.quest.foundIan;
			ds.foundLiam = gp.quest.foundLiam;
			ds.foundMiles = gp.quest.foundMiles;
			
			// OBJECTS ON EVERY MAP
			ds.mapObjectNames = new String[gp.maxMap][gp.obj[0].length];
			ds.mapObjectWorldX = new int[gp.maxMap][gp.obj[0].length];
			ds.mapObjectWorldY = new int[gp.maxMap][gp.obj[0].length];
			ds.mapObjectLoot = new String[gp.maxMap][gp.obj[0].length];
			ds.mapObjectOpened = new boolean[gp.maxMap][gp.obj[0].length];
			
			for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
				for(int i = 0; i < gp.obj[0].length; i++) {
					if(gp.obj[mapNum][i] == null) {
						ds.mapObjectNames[mapNum][i] = "NA";
					}
					else {
						ds.mapObjectNames[mapNum][i] = gp.obj[mapNum][i].name;
						ds.mapObjectWorldX[mapNum][i] = gp.obj[mapNum][i].worldX;
						ds.mapObjectWorldY[mapNum][i] = gp.obj[mapNum][i].worldY;
						
						if(gp.obj[mapNum][i] instanceof OBJ_Chest) {
							OBJ_Chest chest = (OBJ_Chest)gp.obj[mapNum][i];
							ds.mapObjectOpened[mapNum][i] = chest.opened;
							
							// Store chest contents as comma-separated names
							String loot = "";
							for(int c = 0; c < chest.inventory.size(); c++) {
								if(c > 0) { loot += ","; }
								loot += chest.inventory.get(c).name;
							}
							ds.mapObjectLoot[mapNum][i] = loot;
						}
					}
				}
			}
			
			oos.writeObject(ds);
			oos.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	// Returns true if a save file existed and loaded successfully
	public boolean load() {
		File saveFile = new File("save.dat");
		if(saveFile.exists() == false) {
			return false;
		}
		
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(saveFile));
			DataStorage ds = (DataStorage)ois.readObject();
			ois.close();
			
			// PLAYER STATS
			gp.player.level = ds.level;
			gp.player.maxLife = ds.maxLife;
			gp.player.life = ds.life;
			gp.player.maxMana = ds.maxMana;
			gp.player.mana = ds.mana;
			gp.player.strength = ds.strength;
			gp.player.dexterity = ds.dexterity;
			gp.player.exp = ds.exp;
			gp.player.nextLevelExp = ds.nextLevelExp;
			gp.player.$fartcoin = ds.fartcoin;
			
			// PLAYER POSITION
			gp.player.worldX = ds.worldX;
			gp.player.worldY = ds.worldY;
			gp.currentMap = ds.currentMap;
			
			// PLAYER INVENTORY
			gp.player.inventory.clear();
			for(int i = 0; i < ds.itemNames.size(); i++) {
				Entity item = getObject(ds.itemNames.get(i));
				if(item != null) {
					gp.player.inventory.add(item);
				}
			}
			if(ds.currentWeaponSlot >= 0 && ds.currentWeaponSlot < gp.player.inventory.size()) {
				gp.player.currentWepon = gp.player.inventory.get(ds.currentWeaponSlot);
			}
			if(ds.currentShieldSlot >= 0 && ds.currentShieldSlot < gp.player.inventory.size()) {
				gp.player.currentSheild = gp.player.inventory.get(ds.currentShieldSlot);
			}
			gp.player.attack = gp.player.getAttack();
			gp.player.defense = gp.player.getDefense();
			
			// QUEST PROGRESS
			gp.quest.foundAllFriends = ds.foundAllFriends;
			gp.quest.foundJohn = ds.foundJohn;
			gp.quest.defeatedNick = ds.defeatedNick;
			gp.quest.metNick = ds.metNick;
			gp.quest.defeatedBrian = ds.defeatedBrian;
			gp.quest.talkedToVic = ds.talkedToVic;
			gp.quest.foundMike = ds.foundMike;
			gp.quest.foundIan = ds.foundIan;
			gp.quest.foundLiam = ds.foundLiam;
			gp.quest.foundMiles = ds.foundMiles;
			
			// Reset NPCs and monsters to their normal spots, then rebuild
			// objects exactly how they were when the game was saved
			gp.aSetter.setNPC();
			gp.aSetter.setMonster();
			
			for(int mapNum = 0; mapNum < gp.maxMap; mapNum++) {
				for(int i = 0; i < gp.obj[0].length; i++) {
					if(ds.mapObjectNames[mapNum][i].equals("NA")) {
						gp.obj[mapNum][i] = null;
					}
					else {
						gp.obj[mapNum][i] = getObject(ds.mapObjectNames[mapNum][i]);
						if(gp.obj[mapNum][i] == null) {
							continue;
						}
						gp.obj[mapNum][i].worldX = ds.mapObjectWorldX[mapNum][i];
						gp.obj[mapNum][i].worldY = ds.mapObjectWorldY[mapNum][i];
						
						if(gp.obj[mapNum][i] instanceof OBJ_Chest) {
							OBJ_Chest chest = (OBJ_Chest)gp.obj[mapNum][i];
							chest.opened = ds.mapObjectOpened[mapNum][i];
							
							String loot = ds.mapObjectLoot[mapNum][i];
							if(loot != null && loot.length() > 0) {
								for(String itemName : loot.split(",")) {
									Entity item = getObject(itemName);
									if(item != null) {
										chest.setLoot(item);
									}
								}
							}
						}
					}
				}
			}
			
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
