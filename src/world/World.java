package world;

import java.awt.Color;
import creatures.*;
import java.util.*;

public class World {

	public Tile[][] tiles;
	
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
	
	public World(Tile[][] tiles) {
		this.tiles = tiles;
		this.width = tiles.length;
		this.height = tiles[0].length;
		this.creatures = new ArrayList<Creature>();
	}
	
	// Checks the bounds for a given tile.
	public Tile tile(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height) {
			return Tile.BOUNDS;
		} else {
			return tiles[x][y];
		}
	}
	
	public char glyph(int x, int y) {
		return tile(x, y).glyph();
	}
	
	public Color color(int x, int y) {
		return tile(x, y).color();
	}
	
	public void dig(int x, int y) {
		if (tile(x,y).isDiggable())
			tiles[x][y] = Tile.FLOOR;
	}
	
	public void addAtEmptyLocation(Creature creature) {
		int x;
		int y;
		
		do {
			x = (int)(Math.random() * width);
			y = (int)(Math.random() * height);
		}
		while (!tile(x,y).isGround() || creature(x, y) != null);
		
		creature.x = x;
		creature.y = y;
		creatures.add(creature);
	}
	
	public Creature creature(int x, int y) {
		for (Creature c : creatures) {
			if (c.x == x && c.y == y) {
				return c;
			}
		}
		return null;
	}
	
	public void remove(Creature other) {
		creatures.remove(other);
	}
	
	public void update() {
		for (Creature creature : creatures) {
			creature.update();
		}
	}
}
