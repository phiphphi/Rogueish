package world;

import java.util.*;

public class WorldBuilder {
	
	private int width;
	private int height;
	private Tile[][] tiles;
	
	public WorldBuilder(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new Tile[width][height];
	}
	
	// Creates a new World for use.
	public World build() {
		return new World(tiles);
	}
	
	// Randomizes if the tiles are floor or wall pieces.
	private WorldBuilder randomizeTiles() {
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				tiles[x][y] = Math.random() < 0.5 ? Tile.FLOOR : Tile.WALL;
			}
		}
		
		return this;
	}
	
	// Repeatedly smoothes tiles using the cellular automata formula,
	// made by smarter people than me.
	private WorldBuilder smooth(int times) {
		Tile[][] tiles2 = new Tile[width][height];
		for (int time = 0; time < times; time++) {
			
			for (int x = 0; x < width; x++) {
				for (int y = 0; y < height; y++) {
					int floors = 0;
					int rocks = 0;
					
					for (int ox = -1; ox < 2; ox++) {
						for (int oy = -1; oy < 2; oy++) {
							if (x + ox < 0 || x + ox >= width ||
									y + oy < 0 || y + oy >= height) {
								continue;
							}
							
							if (tiles[x + ox][y + oy] == Tile.FLOOR) {
								floors++;
							} else {
								rocks++;
							}
						}
					}
					tiles2[x][y] = floors >= rocks ? Tile.FLOOR : Tile.WALL;
				}
			}
			tiles = tiles2;
		}
		return this;
	}
	
	private void contourBomber() {
		// implent contour bombing algorithm
	}
	
	public WorldBuilder makeCaves() {
		return randomizeTiles().smooth(8);
	}
}
