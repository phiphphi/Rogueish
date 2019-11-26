package creatures;

import asciiPanel.AsciiPanel;
import world.*;
import java.util.*;

public class CreatureFactory {

	private World world;
	
	public CreatureFactory(World world) {
		this.world = world;
	}
	
	public Creature newPlayer(List<String> messages, FieldOfView fov) {
		Creature player = new Creature(world, '@', "wanderer", AsciiPanel.brightWhite, 
				100, 20, 5, 20);
		world.addAtEmptyLocation(player, 0);
		new PlayerAi(player, messages, fov);
		return player;
	}
	
	public Creature newFungus(int depth) {
		Creature fungus = new Creature(world, 'f', "fungus", AsciiPanel.magenta, 
				10, 0, 0, 1);
		world.addAtEmptyLocation(fungus, depth);
		new FungusAi(fungus, this);
		return fungus;
	}
	
	public Creature newBat(int depth) {
		Creature bat = new Creature(world, 'b', "bat", AsciiPanel.yellow, 
				15, 15, 0, 30);
		world.addAtEmptyLocation(bat, depth);
		new BatAi(bat);
		return bat;
	}
}
