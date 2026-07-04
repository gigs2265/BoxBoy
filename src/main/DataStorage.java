package main;

import java.io.Serializable;
import java.util.ArrayList;

// Everything that gets written to the save file lives in here.
public class DataStorage implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	// PLAYER STATS
	int level;
	int maxLife;
	int life;
	int maxMana;
	int mana;
	int strength;
	int dexterity;
	int exp;
	int nextLevelExp;
	int fartcoin;
	
	// PLAYER POSITION
	int worldX;
	int worldY;
	int currentMap;
	
	// PLAYER INVENTORY (stored by item name, rebuilt on load)
	ArrayList<String> itemNames = new ArrayList<>();
	int currentWeaponSlot;
	int currentShieldSlot;
	
	// QUEST PROGRESS
	boolean foundAllFriends;
	boolean foundJohn;
	boolean defeatedNick;
	boolean metNick;
	boolean defeatedBrian;
	boolean talkedToVic;
	boolean foundMike;
	boolean foundIan;
	boolean foundLiam;
	boolean foundMiles;
	
	// OBJECTS ON EVERY MAP (so opened chests stay opened, used keys stay used, etc.)
	String[][] mapObjectNames;
	int[][] mapObjectWorldX;
	int[][] mapObjectWorldY;
	String[][] mapObjectLoot;    // chest contents as comma-separated item names
	boolean[][] mapObjectOpened; // chest opened state
}
