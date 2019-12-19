package creatures;

import asciiPanel.AsciiPanel;
import creatures.creatureai.*;
import world.*;
import java.util.*;

public class CreatureFactory {

	private World world;
	
	public CreatureFactory(World world) {
		this.world = world;
	}
	
	public Creature newPlayer(List<String> messages, FieldOfView fov,
			List<Integer> playerAttributes) {
		Creature player = new Creature(world, '@', "wanderer", AsciiPanel.brightWhite, 
				100, 20, 5, 20, "How did you taste yourself!?");
		world.addAtEmptyLocation(player, 0);
		new PlayerAi(player, messages, fov);
		return player;
	}
	
	public Creature newFungus(int depth) {
		Creature fungus = new Creature(world, 'f', "fungus", AsciiPanel.magenta, 
				10, 0, 0, 1, "Tastes like dirt");
		world.addAtEmptyLocation(fungus, depth);
		new FungusAi(fungus, this);
		return fungus;
	}
	
	public Creature newBat(int depth) {
		Creature bat = new Creature(world, 'b', "bat", AsciiPanel.yellow, 
				15, 5, 0, 30, "Mmm, cave flavor");
		world.addAtEmptyLocation(bat, depth);
		new BatAi(bat);
		return bat;
	}
	
	public Creature newShambler(int depth, Creature player) {
		Creature shambler = new Creature(world, 's', "shambler", AsciiPanel.red,
				50, 10, 10, 6, "Fatty and sickeningly sweet. Ugh");
		world.addAtEmptyLocation(shambler, depth);
		new ShamblerAi(shambler, player);
		return shambler;
	}
	
	public Creature newCrawler(int depth, Creature player) {
		Creature crawler = new Creature(world, 'c', "crawler", AsciiPanel.brightBlack,
				5, 3, 2, 15, "A bitter, ashy taste lingers in your mouth");
		world.addAtEmptyLocation(crawler, depth);
		new CrawlerAi(crawler, player);
		return crawler;
	}
}
