package screens;

import java.awt.Color;
import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import world.*;
import creatures.*;
import java.util.*;
import items.*;
import org.apache.commons.lang3.StringUtils;

public class PlayScreen implements Screen {
	
	private List<String> messages;
	private List<String> history;
	private World world;
	private Creature player;
	private List<Integer> playerAttributes;
	private int screenWidth;
	private int screenHeight;
	private FieldOfView fov;
	private Screen subscreen;
	private ItemFactory itemFactory;
	
	public PlayScreen(List<Integer> stats) {
		screenWidth = 104;
		screenHeight = 38;
		messages = new ArrayList<String>();
		history  = new ArrayList<String>();
		createWorld();
		fov = new FieldOfView(world);
		playerAttributes = stats;
			
		CreatureFactory creatureFactory = new CreatureFactory(world);
		createCreatures(creatureFactory, playerAttributes);
		
		world.addExitStairs(player);
		
		itemFactory = new ItemFactory(world);
		createItems(itemFactory);
	}
	
	private void createCreatures(CreatureFactory creatureFactory,
			List<Integer> playerAttributes) {
		player = creatureFactory.newPlayer(messages, fov, playerAttributes);
		
		for (int z = 0; z < world.depth(); z++) {
			for (int i = 0; i < 16; i++) {
				creatureFactory.newFungus(z);
			}
			
			for (int i = 0; i < 40; i++) {
				creatureFactory.newBat(z);
			}
			
			for (int i = 0; i < z + 3; i++) {
				creatureFactory.newShambler(z, player);
			}
			
			for (int i = 0; i < 8; i++) {
				creatureFactory.newCrawler(z, player);
			}
		}
	}
	
	private void createItems(ItemFactory itemFactory) {
		for (int z = 0; z < world.depth(); z++) {
			for (int i = 0; i < world.width() * world.height() / 20; i++) {
				itemFactory.newRock(z);
			}
			
			for (int i = 0; i < 5; i++) {
				itemFactory.newBaton(z);
				itemFactory.newLightArmor(z);
			}
			
			if (z >= 2) {
				for (int i = 0; i < 3; i++) {
					itemFactory.newStaff(z);
					itemFactory.newMediumArmor(z);
				}
			}
			
			if (z >= 4) {
				itemFactory.newLance(z);
				itemFactory.newHeavyArmor(z);
			}
		}
		
		itemFactory.newVictoryItem(world.depth() - 1);
	}
		
	private void createWorld() {
		world = new WorldBuilder(132, 60, 5)
				.makeCaves()
				.build();
	}

	public void displayOutput(AsciiPanel terminal) {
		int left = getScrollX();
		int top = getScrollY();
		
		displayTiles(terminal, left, top);
		displayMessages(terminal, messages);
		
		terminal.write(player.glyph(), player.x - left, player.y - top, player.color());

		
		String health = String.format(" %3d/%3d hp", player.hp(), player.maxHp());
		String healthBar = "|" + displayBar(player.hp(), player.maxHp(), 10, 
				(char)178, (char)176, ' ') + "|";
		String healthStatus = String.format("%-8s", health());
		terminal.write(health, 5, 40);
		terminal.write(healthBar, 5, 41);
		terminal.write(StringUtils.center(healthStatus, 10), 7, 43);
		
		String hunger = String.format(" %3d/%3d food", player.food(), player.maxFood());
		String hungerBar = "|" + displayBar(player.food(), player.maxFood(), 10, 
				(char)178, (char)176, ' ') + "|";
		String[] hungerStatus = hunger().split(" - ");
		terminal.write(hunger, 17, 40);
		terminal.write(hungerBar, 18, 41);
		terminal.write(String.format("%-8s", hungerStatus[0]), 21, 43);
		for (int i = 1; i < hungerStatus.length; i++) {
			terminal.write(hungerStatus[i], 19, 43 + i);
		}
		
		terminal.write("Strength:  " + StringUtils.leftPad(player.attackValue() + "", 2), 35, 40);
		terminal.write("Toughness: " + StringUtils.leftPad(player.defenseValue() + "", 2),35, 41);
		terminal.write("Vision:    " + StringUtils.leftPad(player.visionRadius() + "", 2), 35, 42);
		
		String experience = String.format("%d / %d xp", player.xp(), player.maxXp());
		String experienceBar = "|" + displayBar(player.xp(), player.maxXp(), 50,
				(char)178, (char)176, ' ') + "|";
		terminal.writeCenter(experience, 39);
		terminal.writeCenter(experienceBar, 40);
		terminal.writeCenter("Level: " + player.level(), 41);
		
		
		// tutorial-type information
		terminal.write("num keys to move", 112, 39);
		terminal.write("g to pickup items", 112, 40);
		terminal.write("d to open inventory screen", 112, 41);
		terminal.write("e to open eat screen", 112, 42);
		terminal.write("w to change equipment", 112, 43);
		
		displayEquipment(terminal);
		
		if (subscreen != null) {
			subscreen.displayOutput(terminal);
		}
	}
	
	public Screen respondToUserInput(KeyEvent key) {
		int level = player.level();
		
		if (subscreen != null) {
			subscreen = subscreen.respondToUserInput(key);
		} else {
			// movement and win/lose
			switch (key.getKeyCode()) {
			case KeyEvent.VK_LEFT: player.moveBy(-1, 0, 0); break;
			case KeyEvent.VK_RIGHT: player.moveBy(1, 0, 0); break;
			case KeyEvent.VK_UP: player.moveBy(0, -1, 0); break;
			case KeyEvent.VK_DOWN: player.moveBy(0, 1, 0); break;
			case KeyEvent.VK_HOME: player.moveBy(-1, -1, 0); break;
			case KeyEvent.VK_PAGE_UP: player.moveBy(1, -1, 0); break;
			case KeyEvent.VK_END: player.moveBy(-1, 1, 0); break;
			case KeyEvent.VK_PAGE_DOWN: player.moveBy(1, 1, 0); break;
			
			case KeyEvent.VK_D: subscreen = new DropScreen(player); break;
			case KeyEvent.VK_E: subscreen = new EatScreen(player); break;
			case KeyEvent.VK_W: subscreen = new EquipScreen(player); break;
			
			// DEV HACKS
			case KeyEvent.VK_1: player.drop(itemFactory.newVictoryItem(player.z));
			case KeyEvent.VK_2: player.drop(itemFactory.newLance(player.z));
			case KeyEvent.VK_3: player.drop(itemFactory.newHeavyArmor(player.z));
			case KeyEvent.VK_4: player.drop(itemFactory.randomWeapon(player.z));
			case KeyEvent.VK_5: player.drop(itemFactory.randomArmor(player.z));
			}
			
			switch (key.getKeyChar()) {
			// inventory
			case 'g': player.pickup(); break;
	        
	        // stairs
			case ',': 
				if (userIsTryingToExit())
					return userExits();
				else
					player.moveBy(0, 0, -1);
					break;
			case '.': player.moveBy(0, 0, 1); break;
			}
		}
		
		if (subscreen == null) {
			world.update();
		}
		
		if (player.hp() < 1) {
			return new LoseScreen();
		}
		
		if (player.level() > level) {
			subscreen = new LevelUpScreen(player, player.level() - level);
		}
		
		return this;
	}
		
	public int getScrollX() {
		return Math.max(0, Math.min(player.x - screenWidth / 2, 
				world.width() - screenWidth));
	}
		
	public int getScrollY() {
		return Math.max(0, Math.min(player.y - screenHeight / 2,
				world.height() - screenHeight));
	}
	
	private void displayTiles(AsciiPanel terminal, int left, int top) {
		fov.update(player.x, player.y, player.z, player.visionRadius());
		
		for (int x = 0; x < screenWidth; x++) {
			for (int y = 0; y < screenHeight; y++) {
				int wx = x + left;
				int wy = y + top;
				
				if (player.canSee(wx, wy, player.z)) {
					terminal.write(world.glyph(wx, wy, player.z), x, y, 
							world.color(wx, wy, player.z));
				} else {
					terminal.write(fov.tile(wx, wy, player.z).glyph(), x, y, 
							Color.darkGray);
				}
			}
		}
	}
	
	public void displayMessages(AsciiPanel terminal, List<String> messages) {
		int boxLeft = 108;
		int boxRight = 146;
		
		
		// display box to hold messages
		for (int i = 0; i <= 20; i++) {
			terminal.write('|', boxLeft, screenHeight + i - 37);
			terminal.write('|', boxRight, screenHeight + i - 37);
		}
		for (int i = 0; i <= 38; i++) {
			terminal.write('_', boxLeft + i, 0);
			if (i == 0 || i == 38) 
				terminal.write('|', boxLeft + i, 21);
			else
				terminal.write('_', boxLeft + i, 21);
		}
		
		for(String message : messages) {
			String[] splitMessage = message.split(" ");
			
			String currSentence = "";
			int currLength = 0;
			int currLine = 0;
			
			for (int j = 0; j < splitMessage.length; j++) {
				if (currLength + splitMessage[j].length() <= 30) {
					currLength += splitMessage[j].length();
					currSentence += splitMessage[j] + " ";
				} else {
					history.add(currLine, currSentence);
					currSentence = splitMessage[j] + " ";
					currLine++;
					currLength = currSentence.length();
				}
			}
			
			if (!"".equals(currSentence)) {
				history.add(currLine, currSentence);
			}
		}
		messages.clear();
		
		while (history.size() >= 20) { // removes oldest messages
			history.remove(19);
		}
		
		for (int i = 0; i < history.size(); i++) { // write history
			terminal.write(history.get(i), boxLeft + 1, screenHeight - 37 + i); // i is how far down
		}
	}
	
	// takes in two stats to display: a maximum and a current value.
	// length represents how long the bar is
	// full, some, and none represent the char to display for those bar values
	// note: change to working with doubles to improve accuracy
	public String displayBar(double curr, double max, int length, char full, char some, char none) {
		String bar = "";
		for (int i = 1; i <= length; i++) {
			if (curr >= i * (max / length)) {
				bar += full;
			} else if (curr < i * (max / length) && curr > (i - 1) * (max / length)) {
				bar += some;
			} else {
				bar += none;
			}
		}
		
		return bar;
	}
	
	public void displayEquipment(AsciiPanel terminal) {
		for (int i = 0; i <= 6; i++) {
			terminal.write('|', 108, 30 - i);
			terminal.write('|', 126, 30 - i);
			terminal.write('|', 128, 30 - i);
			terminal.write('|', 146, 30 - i);
		}
		for (int i = 0; i <= 38; i++) {
			if (i == 19) {
				terminal.write(' ', 108 + i, 23);
				terminal.write(' ', 108 + i, 30);
			} else {
				terminal.write('_', 108 + i, 23);
				if (i == 0 || i == 18 || i == 20 || i == 38)  {
					terminal.write('|', 108 + i, 30);
				} else {
					terminal.write('_', 108 + i, 30);
				}
			}


		}
		
		if (player.weapon() != null) {
			terminal.write(StringUtils.center(player.weapon().name(), 18), 109, 31);
			String[] weaponImage = player.weapon().image();
			for (int i = 0; i < weaponImage.length; i++) {
				terminal.write(weaponImage[i], 109, 24 + i);
			}
		} else {
			terminal.write(StringUtils.center("No weapon equipped", 18), 109, 31);
		}
		if (player.armor() != null) {
			terminal.write(StringUtils.center(player.armor().name(), 18), 129, 31);
			String[] armorImage = player.armor().image();
			for (int i = 0; i < armorImage.length; i++) {
				terminal.write(armorImage[i], 129, 24 + i);
			}
		} else {
			terminal.write(StringUtils.center("No armor equipped", 18), 129, 31);
		}
	}

	private boolean userIsTryingToExit(){
	    return player.z == 0 && world.tile(player.x, player.y, player.z) == Tile.STAIRS_UP;
	}
	
	private Screen userExits(){
	    for (Item item : player.inventory().getItems()){
	        if (item != null && item.name().equals("relic"))
	            return new WinScreen();
	    }
	    return new LoseScreen();
	}
	
	private String health() {
		if (player.hp() < player.maxHp() * 0.1)
	        return "Barely Alive";
	    else if (player.hp() < player.maxHp() * 0.2)
	        return "Bloodied";
	    else if (player.hp() < player.maxHp() * 0.35)
	    	return "Bruised";
	    else if (player.hp() > player.maxHp() * 0.9)
	        return "Healthy";
	    else if (player.hp() > player.maxHp() * 0.75)
	        return "Scratched";
	    else
	    	return "Worn";
	}
	
	private String hunger() {
		if (player.food() == 0)
			return "Famished - Str Def Vis - -5  -2  -4";
		else if (player.food() < player.maxFood() * 0.1)
	        return "Starving - Str Def Vis - -3  -1  -2";
	    else if (player.food() < player.maxFood() * 0.2)
	        return "Hungry - Str Def Vis - -2      -1";
	    else if (player.food() < player.maxFood() * 0.35)
	    	return "Peckish - Str Def Vis - -1        ";
	    else if (player.food() > player.maxFood() * 0.9)
	        return "Stuffed";
	    else if (player.food() > player.maxFood() * 0.8)
	        return "Full";
	    else
	        return "Sated";
	}
}
