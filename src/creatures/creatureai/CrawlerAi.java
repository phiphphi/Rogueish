package creatures.creatureai;

import java.util.List;

import creatures.Creature;
import world.Path;
import world.Point;

public class CrawlerAi extends CreatureAi {

	private Creature player;
	
	public CrawlerAi(Creature creature, Creature player) {
		super(creature);
		this.player = player;
	}
	
	public void onUpdate() {
	      if (creature.canSee(player.x, player.y, player.z))
	          escape(player);
	      else
	          wander();
	}
	
	public void escape(Creature target) {
	      List<Point> points = new Path(creature, target.x, target.y).points();
	  
	      int mx = points.get(0).x - creature.x;
	      int my = points.get(0).y - creature.y;
	  
	      creature.moveBy(-mx, -my, 0);
	}
}
