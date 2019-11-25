package screens;

import java.awt.event.KeyEvent;
import asciiPanel.AsciiPanel;

public class StartScreen implements Screen {

	public void displayOutput(AsciiPanel terminal) {
		terminal.write("            /\\", 53, 20);
		terminal.writeCenter("/vvvvvvvvvvvv \\--------------------------------------,", 21);
		terminal.writeCenter("`^^^^^^^^^^^^ /=====================================\"", 22); 
		terminal.write("            \\/", 53, 23);	 
		terminal.write(" ___  ___   ___ _   _ ___ ___ ___ _  _ ", 68, 17);
		terminal.write("| _ \\/ _ \\ / __| | | | __|_ _/ __| || |", 68, 18); 
		terminal.write("|   / (_) | (_ | |_| | _| | |\\__ \\ __ |", 68, 19);
		terminal.write("|_|_\\\\___/ \\___|\\___/|___|___|___/_||_|", 68, 20);
		terminal.writeCenter("-- press [enter] to start --", 30);
	}
	
	public Screen respondToUserInput(KeyEvent key) {
		return key.getKeyCode() == KeyEvent.VK_ENTER ? new PlayScreen() : this;
	}
}
