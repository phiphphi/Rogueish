package items;

import java.awt.Color;

public class Item {
	
	private char glyph;
    public char glyph() { return glyph; }

    private Color color;
    public Color color() { return color; }

    private String name;
    public String name() { return name; }
    
    private int foodValue;
    public int foodValue() { return foodValue; }
    public void modifyFoodValue(int amount) { foodValue += amount; }
    
    private String flavor;
    public String flavor() { return flavor; }
    public void modifyFlavor(String taste) { flavor = taste; }
    
    private int attackValue;
    public int attackValue() { return attackValue; }
    public void modifyAttackValue(int amount) { attackValue += amount; }

    private int defenseValue;
    public int defenseValue() { return defenseValue; }
    public void modifyDefenseValue(int amount) { defenseValue += amount; }
    
    private String[] image; // weapon & armor images are 16 by 6
    public String[] image() { return image; }
    public void modifyImage(String[] symbols) { image = symbols; }

    public Item(char glyph, Color color, String name) {
        this.glyph = glyph;
        this.color = color;
        this.name = name;
    }
}
