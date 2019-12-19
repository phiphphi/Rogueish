package screens;

import java.awt.event.KeyEvent;
import java.io.*;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import asciiPanel.AsciiPanel;

public class BriefingScreen implements Screen {
	
	private int line;
	private int i;
	private List<Integer> increment;
	
	public BriefingScreen() {
		Integer[] array = {8, 14, 2, 2, 2, 10};
		increment = new ArrayList<Integer>(Arrays.asList(array));
	}
	
	public void displayOutput(AsciiPanel terminal) {
		terminal.writeCenter("press [enter] to go to character select", 43);
		
		terminal.write("Public Terminal 4D", 11, 5);
		terminal.write("Don't forget to log out when you're done!", 1, 6);
		terminal.write("Property of Systech", 11, 7);
		
		for (int i = 0; i < terminal.getHeightInCharacters(); i++) { // side bars
			terminal.write((char)178, 43, 0 + i);
			terminal.write((char)178, 115, 0 + i);
		}
		for (int i = 0; i < 43; i++) {
			terminal.write((char)178, 0 + i, 22);
		}
		
		displayLogo(terminal);
		displayEquipment(terminal);
		displayBriefing(terminal);
	}
	
	public Screen respondToUserInput(KeyEvent key) {
		switch(key.getKeyCode()) {
		case KeyEvent.VK_ENTER: return new CharacterSelectScreen();
		case KeyEvent.VK_Z:
			if (i < increment.size()) {
				line += increment.get(i);
			}
			i += 1;
		}
		
		return this;
	}

	private void displayLogo(AsciiPanel terminal) {
		File file = new File("C:\\Users\\phith\\eclipse-workspace"
				+ "\\Rogueish\\src\\screens\\briefingLogo.txt");
		
		Scanner sc = null;
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			System.out.println("Cant find briefing logo");
			e.printStackTrace();
		}
		
		int i = 0;
		while (sc.hasNextLine()) {		
			terminal.write(sc.nextLine().stripTrailing(), 118 + (i / 2), 2 + i, AsciiPanel.brightCyan);
			i++;
		}
	}
	private void displayEquipment(AsciiPanel terminal) {
		String[] equipCheck = {"Equipment Status:",
				"Service Revolver:",
				"Class-3 Exosuit:",
				"Organic Mat. Processor:",
				"Nanoinjectors",
				"Adaptive Combat Program:",
				"Distress Beacon:",
				"Escape Portal Vectorizer:"};
		String[] equipStatus = {"",
				"GOOD",
				"GOOD",
				"GOOD",
				"NEEDS REPAIRS",
				"GOOD",
				"NONOPERATIONAL",
				"NONOPERATIONAL"};
		
		if (line < 38) {
			terminal.write("Analyzing equipment condition. . .", 4, 21);
		}
		terminal.write(equipCheck[0], 12, 24);
		
		for (int j = 1; j <= i; j++) {
			if (j <= 5) {
				terminal.write(equipCheck[j], 2, 24 + (j * 2));
				if (j == 4) {
					terminal.write(equipStatus[j], 28, 24 + (j * 2), AsciiPanel.brightYellow);
				} else {
					terminal.write(equipStatus[j], 28, 24 + (j * 2), AsciiPanel.brightGreen);
				}
			} else {
				terminal.write(equipCheck[j], 2, 24 + (j * 2));
				terminal.write(equipStatus[j], 28, 24 + (j * 2), AsciiPanel.brightRed);
				terminal.write(equipCheck[j + 1], 2, 24 + ((j + 1) * 2));
				terminal.write(equipStatus[j + 1], 28, 24 + ((j + 1) * 2), AsciiPanel.brightRed);
			}
		}
		terminal.write("Contact an technican for service", 5, 41, AsciiPanel.red);
		terminal.write(" - without your portal, you'll", 5, 42, AsciiPanel.red);
		terminal.write("have to find your own way out!", 5, 43, AsciiPanel.red);

		
	}
	
	private void displayBriefing(AsciiPanel terminal) {
		File file = new File("C:\\Users\\phith\\eclipse-workspace"
				+ "\\Rogueish\\src\\screens\\introBriefing.txt");
		
		Scanner sc = null;
		
		try {
			sc = new Scanner(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		List<String> image = new ArrayList<String>();
		while (sc.hasNext()) {
			image.add(sc.nextLine());
		}
		
		// top header
		terminal.writeCenter(image.get(0), 2);
		terminal.writeCenter(image.get(1), 3);
		
		if (line < image.size()) { // advances through briefing block by block
			for (int i = 2; i <= line; i++) {
				terminal.write(StringUtils.rightPad(image.get(i), 13), 50, 3 + i);
				if (i == 26) {
					terminal.write(StringUtils.rightPad(image.get(26), 13), 50, 29, AsciiPanel.brightWhite);
					try { // simulates password typing and checking (default 30)
						Thread.sleep(0);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else if (i == 28) {
					terminal.write(StringUtils.rightPad(image.get(28), 13), 50, 31, AsciiPanel.green);
				}
			}
		} else { // display full text after fully advanced
			for (int i = 2; i < image.size(); i++) {
				terminal.write(StringUtils.rightPad(image.get(i), 13), 50, 3 + i);
				if (i == 26)
					terminal.write(StringUtils.rightPad(image.get(26), 13), 50, 29, AsciiPanel.brightWhite);
				else if (i == 28)
					terminal.write(StringUtils.rightPad(image.get(28), 13), 50, 31, AsciiPanel.green);
			}
		}
		
		if (line < 38) {
			terminal.writeCenter("press [z] to advance text", line + 5);
		}
	}

}
