package screens;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;

public class LoseScreen implements Screen {

	public void displayOutput(AsciiPanel terminal) {
		terminal.writeCenter("You suck.", 21);
		terminal.writeCenter("-- press [enter] to restart --", 22);
	}
	
	public Screen respondToUserInput(KeyEvent key) {
		return key.getKeyCode() == KeyEvent.VK_ENTER ? new StartScreen() : this;
	}
}
