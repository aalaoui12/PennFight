import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public abstract class Attacker extends GameObj {
	
	public static final int SIZE = 20;
    
    private BufferedImage img;
    
    public Attacker(int mapWidth, int mapHeight, int tileWidth, int tileHeight, String player_img) {
    	super(0, 0, mapWidth - tileWidth, randomizeSpawn(tileHeight, mapHeight), SIZE, SIZE, 
    			mapWidth, mapHeight);
        
        try {
            if (img == null) {
                img = ImageIO.read(new File(player_img));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    private static int randomizeSpawn(int tileHeight, int mapHeight) {
    	int[] spawnPositions = new int[mapHeight / tileHeight];
    	for(int i = 0; i < spawnPositions.length; i++) {
    		spawnPositions[i] = tileHeight * i;
    	}
    	int randomIndex = (int)(Math.random()*((14)+1));
    	
    	return spawnPositions[randomIndex];
    }
    
    @Override
    public void draw(Graphics g) {
    	g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
    
	public abstract boolean attack(Player player); // If a certain condition for attack is met, then
												   // the attack is performed in GameMap.
}
