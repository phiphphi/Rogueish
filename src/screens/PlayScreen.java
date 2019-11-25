package screens;

import java.awt.Color;
import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;
import world.*;
import creatures.*;
import java.util.*;

public class PlayScreen implements Screen {

	public void displayOutput(AsciiPanel terminal) {
		int left = getScrollX();
		int top = getScrollY();
		
		displayTiles(terminal, left, top);
		
		terminal.write(player.glyph(), player.x - left, player.y - top, player.color());
		terminal.writeCenter("-- press [escape] to lose or [enter] to win --", 44);
	}
	
	public Screen respondToUserInput(KeyEvent key) {
		switch (key.getKeyCode()) {
		case KeyEvent.VK_LEFT: 
			player.moveBy(-1, 0);
		break;
		case KeyEvent.VK_RIGHT:
			player.moveBy(1, 0);
		break;
		case KeyEvent.VK_UP:
			player.moveBy(0, -1);
		break;
		case KeyEvent.VK_DOWN:
			player.moveBy(0, 1);
		break;
		case KeyEvent.VK_HOME: 
			player.moveBy(-1, -1);
		break;
		case KeyEvent.VK_PAGE_UP:
			player.moveBy(1, -1);
		break;
		case KeyEvent.VK_END:
			player.moveBy(-1, 1);
		break;
		case KeyEvent.VK_PAGE_DOWN:
			player.moveBy(1, 1);
		break;
		
		case KeyEvent.VK_ESCAPE: return new LoseScreen();
		case KeyEvent.VK_ENTER: return new WinScreen();
		}
		
		return this;
	}
	
	public void update() {
		List<Creature> toUpdate = new ArrayList<Creature>(world.creatures());
		for (Creature creature : toUpdate) {
			creature.update();
		}
	}
	
	private World world;
		private Creature player;
		private int screenWidth;
		private int screenHeight;
		
		public PlayScreen() {
			screenWidth = 160;
			screenHeight = 42;
			createWorld();
			CreatureFactory creatureFactory = new CreatureFactory(world);
			createCreatures(creatureFactory);
		}
		
		private void createCreatures(CreatureFactory creatureFactory) {
			player = creatureFactory.newPlayer();
			
			for (int i = 0; i < 12; i++) {
				creatureFactory.newFungus();
			}
		}
		
		private void createWorld() {
			world = new WorldBuilder(160, 42)
					.makeCaves()
					.build();
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
			for (int x = 0; x < screenWidth; x++) {
				for (int y = 0; y < screenHeight; y++) {
					int wx = x + left;
					int wy = y + top;
					
					terminal.write(world.glyph(wx, wy), x, y, world.color(wx, wy));
				}
			}
			
			for (Creature c : world.creatures()) {
				if (c.x >= left && c.x < left + screenWidth) {
					if (c.y >= top && c.y < top + screenHeight) {
						terminal.write(c.glyph(), c.x, c.y, c.color());
					}
				}
			}
		}
}
