import java.awt.Color;
import java.awt.Graphics;

public class Bullet extends GameObj {
	private static final int size = 5;
	private int velocity = 10;
	public Bullet(int velocity, int mapWidth, int mapHeight, int player_x, 
			int player_y, int player_height, int player_width) {
		super(velocity, 0, (player_x + player_width), (player_y + player_height / 2), 
				size, size, mapWidth, mapHeight);
	}
	

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillOval(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
	}
}
