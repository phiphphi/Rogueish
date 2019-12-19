package items;

import asciiPanel.AsciiPanel;
import world.*;

public class ItemFactory {
	
	private World world;
	
	public ItemFactory(World world) {
		this.world = world;
	}
	
	public Item newRock(int depth) {
		Item rock = new Item(',', AsciiPanel.yellow, "rock");
		world.addAtEmptyLocation(rock, depth);
		return rock;
	}
	
	public Item newVictoryItem(int depth) {
		Item item = new Item('*', AsciiPanel.brightWhite, "relic");
		world.addAtEmptyLocation(item, depth);
		return item;
	}
	
	public Item newBaton(int depth){
		String[] image = {"                 ",
						  "                 ",
						  "   _________     ",
						  "  |___|||___>====",
						  "                 ",
						  "                 "};
	    Item item = new Item('i', AsciiPanel.white, "baton");
	    item.modifyAttackValue(5);
	    item.modifyImage(image);
	    world.addAtEmptyLocation(item, depth);
	    return item;
	  }

	  public Item newLance(int depth){
		String[] image = {"                 ",
						  "  |\\ z          ",
						  "   \\\\__        ",
						  " z <  _[]{(((((((",
						  "   //            ",
						  "  |/ z           "};
	    Item item = new Item('I', AsciiPanel.brightYellow, "electrolance");
	    item.modifyAttackValue(10);
	    item.modifyImage(image);
	    world.addAtEmptyLocation(item, depth);
	    return item;
	  }

	  public Item newStaff(int depth){
		String[] image = {"                 ",
				   		  "   ___           ",
				   		  "  / _ \\__________",
				   		  " | [ \\____///____",
				   		  "  \\\\           ",
				  	      "                 "};
	    Item item = new Item(')', AsciiPanel.yellow, "staff");
	    item.modifyAttackValue(5);
	    item.modifyDefenseValue(3);
	    item.modifyImage(image);
	    world.addAtEmptyLocation(item, depth);
	    return item;
	  }

	  public Item newLightArmor(int depth){
		  String[] image = {"    _________    ",
						    "   (   ___   )   ",
			   			    "   |= <   > =|   ",
						    "   |[  | |  ]|   ",
						    "   |[  | |  ]|   ",
						    "    \\_/   \\_/  "};
	    Item item = new Item('[', AsciiPanel.green, "security armor");
	    item.modifyDefenseValue(2);
	    item.modifyImage(image);
	    world.addAtEmptyLocation(item, depth);
	    return item;
	  }

	  public Item newMediumArmor(int depth){
	    Item item = new Item('[', AsciiPanel.yellow, "combat armor");
	    item.modifyDefenseValue(4);
	    world.addAtEmptyLocation(item, depth);
	    return item;
	  }

	  public Item newHeavyArmor(int depth){
	    Item item = new Item('[', AsciiPanel.brightWhite, "power armor");
	    item.modifyDefenseValue(6);
	    world.addAtEmptyLocation(item, depth);
	    return item;
	  }

	  public Item randomWeapon(int depth) {
	    switch ((int)(Math.random() * 3)) {
	    case 0: return newBaton(depth);
	    case 1: return newLance(depth);
	    default: return newStaff(depth);
	    }
	  }

	  public Item randomArmor(int depth) {
	    switch ((int)(Math.random() * 3)) {
	    case 0: return newLightArmor(depth);
	    case 1: return newMediumArmor(depth);
	    default: return newHeavyArmor(depth);
	    }
	  }
}
