package creatures;

public class FungusAi extends CreatureAi {
	
	private CreatureFactory factory;
	private int spreadCount;
	
	public FungusAi(Creature creature, CreatureFactory factory) {
		super(creature);
		this.factory = factory;
	}
	
	public void onUpdate() {
		if (spreadCount < 5 && Math.random() < 0.01) {
			spread();
		}
	}
	
	private void spread() {
		int x = creature.x + (int)(Math.random() * 11) - 5;
		int y = creature.y + (int)(Math.random() * 11) - 5;
		
		if (!creature.canEnter(x, y, creature.z)) {
			return;
		}
		
		Creature child = factory.newFungus(creature.z);
		creature.doAction("Spawn a child");
		child.x = x;
		child.y = y;
		child.z = creature.z;
		spreadCount++;
	}
}
