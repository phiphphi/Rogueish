package screens;

import java.awt.event.KeyEvent;
import java.util.*;

import asciiPanel.AsciiPanel;

public class CharacterSelectScreen implements Screen {
	
	private List<Integer> stats;
    public List<Integer> Stats() { return stats; }
	
	public CharacterSelectScreen() {
		stats = new ArrayList<Integer>(5);
	}
	
	public void displayOutput(AsciiPanel terminal) {
		terminal.writeCenter("Who will you be?", 10);
		
		displayStats(terminal);
		
		terminal.writeCenter("Press [enter] once you are ready", 40);
		terminal.writeCenter("Press [t] if you would like to do the tutorial", 41);
	}
	
	public Screen respondToUserInput(KeyEvent key) {
		switch(key.getKeyCode()) {
		case KeyEvent.VK_ENTER: return new PlayScreen();
		case KeyEvent.VK_T: return new PlayScreen();
		}
		
		return this;
	}
	
	private void displayStats(AsciiPanel terminal) {
		
	}
}
