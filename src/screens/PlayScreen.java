package screens;

import java.awt.Color;
import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import world.*;
import creatures.*;
import java.util.*;

public class PlayScreen implements Screen {
	
	private List<String> messages;
	private List<String> history;
	private World world;
	private Creature player;
	private int screenWidth;
	private int screenHeight;
	private FieldOfView fov;
	
	public PlayScreen() {
		screenWidth = 132;
		screenHeight = 42;
		messages = new ArrayList<String>();
		history  = new ArrayList<String>();
		createWorld();
		fov = new FieldOfView(world);
			
		CreatureFactory creatureFactory = new CreatureFactory(world);
		createCreatures(creatureFactory);
	}
	
	private void createCreatures(CreatureFactory creatureFactory) {
		player = creatureFactory.newPlayer(messages, fov);
		
		for (int z = 0; z < world.depth(); z++) {
			for (int i = 0; i < 8; i++) {
				creatureFactory.newFungus(z);
			}
			
			for (int i = 0; i < 40; i++) {
				creatureFactory.newBat(z);
			}
		}
	}
		
	private void createWorld() {
		world = new WorldBuilder(132, 42, 5)
				.makeCaves()
				.build();
	}

	public void displayOutput(AsciiPanel terminal) {
		int left = getScrollX();
		int top = getScrollY();
		
		displayTiles(terminal, left, top);
		displayMessages(terminal, messages);
		
		terminal.write(player.glyph(), player.x - left, player.y - top, player.color());
		terminal.writeCenter("-- press [escape] to lose or [enter] to win --", 44);
		
		String stats = String.format(" %3d/%3d hp", player.hp(), player.maxHp());
		String health = "|" + displayHealth(player.hp(), player.maxHp()) + "|";
		terminal.write(stats, 139, 4);
		terminal.write(health, 139, 5);
	}
	
	public Screen respondToUserInput(KeyEvent key) {
		switch (key.getKeyCode()) {
		case KeyEvent.VK_ESCAPE: return new LoseScreen();
		case KeyEvent.VK_ENTER: return new WinScreen();
		case KeyEvent.VK_LEFT: player.moveBy(-1, 0, 0); break;
		case KeyEvent.VK_RIGHT: player.moveBy(1, 0, 0); break;
		case KeyEvent.VK_UP: player.moveBy(0, -1, 0); break;
		case KeyEvent.VK_DOWN: player.moveBy(0, 1, 0); break;
		case KeyEvent.VK_HOME: player.moveBy(-1, -1, 0); break;
		case KeyEvent.VK_PAGE_UP: player.moveBy(1, -1, 0); break;
		case KeyEvent.VK_END: player.moveBy(-1, 1, 0); break;
		case KeyEvent.VK_PAGE_DOWN: player.moveBy(1, 1, 0); break;
		case KeyEvent.VK_W: player.moveBy( 0, 0, -1); break;
		case KeyEvent.VK_S: player.moveBy( 0, 0, 1); break;
		}
		
		switch (key.getKeyChar()) {
		case ',': player.moveBy(0, 0, -1); break;
		case '.': player.moveBy(0, 0, 1); break;
		}
		
		world.update();
		if (player.hp() < 1) {
			return new LoseScreen();
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
	
	// Fix to display a history of messages
	public void displayMessages(AsciiPanel terminal, List<String> messages) {
		int top = screenHeight - messages.size();
		for (int i = 0; i < messages.size(); i++) {
			history.add(messages.get(i));
			terminal.writeCenter(history.get(i), top + i);
		}
		
		messages.clear();
		if (history.size() > 3) {
			history.remove(0);
		}
	}
	
	public String displayHealth(int hp, int maxHp) {
		String health = "";
		for (int i = 1; i <= 10; i++) {
			if (hp >= i * (maxHp / 10)) {
				health += (char)178;
			} else if (hp < i * (maxHp / 10) && hp > (i - 1) * (maxHp / 10)) {
				health += (char)176;
			} else {
				health += " ";
			}
		}
		
		return health;
	}
		
}
