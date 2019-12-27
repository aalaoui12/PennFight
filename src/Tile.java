import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Tile {
	public final String IMG_FILE;
	
	public final int POS_X;
    public final int POS_Y;
    public final int TILE_WIDTH;
    public final int TILE_HEIGHT;
	
	private BufferedImage img;
	
	public Tile(String img_file, int pos_x, int pos_y, int tile_width, int tile_height) {
		this.IMG_FILE = img_file;
		
		this.POS_X = pos_x;
		this.POS_Y = pos_y;
		this.TILE_WIDTH = tile_width;
		this.TILE_HEIGHT = tile_height;
		
		try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
	}
	
	public void draw(Graphics g) {
        g.drawImage(img, this.POS_X, this.POS_Y, this.TILE_WIDTH, this.TILE_HEIGHT, null);
    }
}
