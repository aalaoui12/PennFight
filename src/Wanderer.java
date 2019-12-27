import java.util.LinkedList;

public class Wanderer extends Attacker {
	private final static String wanderer_img = "files/wanderer.png";
	
	private final static int velocity = 2;
	
	private Tile[][] tileMap;
	private int tileWidth;
	private int tileHeight;
	
	private int playerPositionX;
	private int playerPositionY;
	private int playerWidth;
	private int playerHeight;
	
	public Wanderer(int mapWidth, int mapHeight, int tileWidth, int tileHeight) {
		super(mapWidth, mapHeight, tileWidth, tileHeight, wanderer_img);
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}
	
	public void move(Tile[][] tileMap, Player player) {
		this.tileMap = tileMap;
		this.playerPositionX = player.getPx();
		this.playerPositionY = player.getPy();
		this.playerWidth = player.getWidth();
		this.playerHeight = player.getHeight();
		move();
    }
	
	@Override
	public void move() {
		BreadthFirstSearch search = new BreadthFirstSearch(this.tileMap, 
				((this.getPx()) / (this.tileWidth)), 
				((this.getPy()) / this.tileHeight), 
				(this.playerPositionX) / this.tileWidth, 
				(this.playerPositionY) / this.tileHeight);
		Direction direction = search.bfs();
		if(direction == Direction.UP) {
			this.setVy(-velocity);
			this.setVx(0);
		}
		else if(direction == Direction.DOWN) {
			this.setVy(velocity);
			this.setVx(0);
		}
		else if(direction == Direction.LEFT) {
			this.setVx(-velocity);
			this.setVy(0);
		}
		else if(direction == Direction.RIGHT) {
			this.setVx(velocity);
			this.setVy(0);
		}
		
		super.move();
	}
	
	@Override
	public boolean attack(Player player) {
		if(player.getHeight() > Math.abs(this.getPy() - player.getPy())) {
			return true;
		}
		else {
			return false;
		}
	}
}
