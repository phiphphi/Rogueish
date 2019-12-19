package world;

import java.awt.Color;
import creatures.*;
import java.util.*;
import items.*;

public class World {

	public Tile[][][] tiles;
	
	private Item[][][] items;
	public Item item(int x, int y, int z) {
		return items[x][y][z];
	}
	
	private List<Creature> creatures;
	public List<Creature> creatures() {
		return creatures;
	}
	
	private int width;
	public int width() {
		return width;
	}
	
	private int height;
	public int height() {
		return height;
	}
	
	private int depth;
	public int depth() {
		return depth;
	}
	
	public World(Tile[][][] tiles) {
		this.tiles = tiles;
		this.width = tiles.length;
		this.height = tiles[0].length;
		this.depth = tiles[0][0].length;
		this.items = new Item[width][height][depth];
		this.creatures = new ArrayList<Creature>();
	}
	
	// Checks the bounds for a given tile.
	public Tile tile(int x, int y, int z) {
		if (x < 0 || x >= width || y < 0 || y >= height
				|| z < 0 || z >= depth)
            return Tile.BOUNDS;
        else
            return tiles[x][y][z];
    }
	

	public char glyph(int x, int y, int z){
	    Creature creature = creature(x, y, z);
	    if (creature != null)
	        return creature.glyph();
	    
	    if (item(x,y,z) != null)
	        return item(x,y,z).glyph();
	    
	    return tile(x, y, z).glyph();
	}
	
	public Color color(int x, int y, int z){
	    Creature creature = creature(x, y, z);
	    if (creature != null)
	        return creature.color();
	    
	    if (item(x,y,z) != null)
	        return item(x,y,z).color();
	    
	    return tile(x, y, z).color();
	}

	public void dig(int x, int y, int z) {
		if (tile(x, y, z).isDiggable())
			tiles[x][y][z] = Tile.FLOOR;
	}
	
	public void addAtEmptyLocation(Creature creature, int z) {
		int x;
		int y;
		
		
		do {
			x = (int)(Math.random() * width);
			y = (int)(Math.random() * height);
		}
		while (!tile(x,y,z).isGround() || creature(x,y,z) != null
				|| tile(x,y,z).isStairs());
		
		creature.x = x;
		creature.y = y;
		creature.z = z;
		creatures.add(creature);
	}
	
	public void addAtEmptyLocation(Item item, int depth) {
		int x;
		int y;
		
		
		do {
			x = (int)(Math.random() * width - 1);
			y = (int)(Math.random() * height - 1);
		}
		while (!tile(x,y,depth).isGround() || item(x,y,depth) != null
				|| tile(x,y, depth).isStairs());
		
		items[x][y][depth] = item;
	}
	
	public boolean addAtEmptySpace(Item item, int x, int y, int z){
	    if (item == null)
	        return false;
	    
	    List<Point> points = new ArrayList<Point>();
	    List<Point> checked = new ArrayList<Point>();
	    
	    points.add(new Point(x, y, z));
	    
	    while (!points.isEmpty()){
	        Point p = points.remove(0);
	        checked.add(p);
	        
	        if (!tile(p.x, p.y, p.z).isGround() || tile(p.x, p.y, p.z).isStairs())
	            continue;
	         
	        if (items[p.x][p.y][p.z] == null){
	            items[p.x][p.y][p.z] = item;
	            Creature c = this.creature(p.x, p.y, p.z);
	            if (c != null)
	                c.notify("A %s lands between your feet.", item.name());
	            return true;
	        } else {
	            List<Point> neighbors = p.neighbors8();
	            neighbors.removeAll(checked);
	            points.addAll(neighbors);
	        }
	    }
	    return false;
	}
	
	public void remove(int x, int y, int z) {
	    items[x][y][z] = null;
	}
	
	public Creature creature(int x, int y, int z) {
		for (Creature c : creatures) {
			if (c.x == x && c.y == y && c.z == z) {
				return c;
			}
		}
		return null;
	}
	
	public void remove(Creature other) {
		creatures.remove(other);
	}
	
	public void update() {
		List<Creature> toUpdate = new ArrayList<Creature>(creatures);
		for (Creature creature : toUpdate) {
			creature.update();
		}
	}
	
	public World addExitStairs(Creature player) {
		List<Point> Points = new ArrayList<Point>();
		  
	    for (int ox = -1; ox < 2; ox++){
	        for (int oy = -1; oy < 2; oy++){
	            if (ox == 0 && oy == 0)
	                continue;
	    
	            Points.add(new Point(player.x + ox, 
	            		player.y + oy, player.z));
	        }
	    }
	
	    Collections.shuffle(Points);
	    
	    int x = -1;
	    int y = -1;
	    
	    do { // make sure stairs aren't placed out of bounds
			x = Points.get((int)(Math.random() * 7)).x;
			y = Points.get((int)(Math.random() * 7)).y;
		} while (x < 0 || y < 0 || x >= 132 || y >= 60);
    
        tiles[x][y][0] = Tile.STAIRS_UP;
        return this;
    }
}
