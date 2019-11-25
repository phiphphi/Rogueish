package creatures;

import asciiPanel.AsciiPanel;
import world.*;

public class CreatureFactory {

	private World world;
	
	public CreatureFactory(World world) {
		this.world = world;
	}
	
	public Creature newPlayer() {
		Creature player = new Creature(world, '@', AsciiPanel.brightWhite);
		world.addAtEmptyLocation(player);
		new PlayerAi(player);
		return player;
	}
	
	public Creature newFungus() {
		Creature fungus = new Creature(world, 'f', AsciiPanel.magenta);
		world.addAtEmptyLocation(fungus);
		new FungusAi(fungus, this);
		return fungus;
	}
}
