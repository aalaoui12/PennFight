/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * A player object starting in the upper left corner of the game court.
 */
public class Player extends GameObj {
	public static final String player_img = "files/player.png";
	
    public static final int SIZE = 20;
    
    private int health = 100;
    private int highScore = 0;
    
    private static BufferedImage img;

    public Player(int mapWidth, int mapHeight) {
        super(0, 0, 0, 0, SIZE, SIZE, mapWidth, mapHeight);
        
        try {
            if (img == null) {
                img = ImageIO.read(new File(player_img));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }
    
    public void lowerHealth(int damage) {
    	this.health = this.health - damage;
    }
    
    public int getHealth() {
    	return this.health;
    }

    public void increaseScore() {
    	highScore += 1;
    }
    
    public int getScore() {
    	return highScore;
    }
    
    @Override
    public void draw(Graphics g) {
    	g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }
}