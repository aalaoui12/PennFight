
public class Scout extends Attacker {
	private final static String scout_img = "files/scout.png";
	
	private final static int velocity = 4;
	private Player player;
	
	private Tile[][] tileMap;
	private int tileWidth;
	private int tileHeight;
	
	public Scout(int mapWidth, int mapHeight, int tileWidth, int tileHeight) {
		super(mapWidth, mapHeight, tileWidth, tileHeight, scout_img);
		this.tileWidth = tileWidth;
		this.tileHeight = tileHeight;
	}

	public void move(Tile[][] tileMap, Player player) {
		this.tileMap = tileMap;
		this.player = player;
		move();
	}
	
	@Override
	public void move() {
		BreadthFirstSearch search = new BreadthFirstSearch(this.tileMap, 
				((this.getPx()) / (this.tileWidth)), 
				((this.getPy()) / this.tileHeight), 
				0, (int)(Math.random()*((14)+1)));
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
		if(this.intersects(player))
			return true;
		else
			return false;
	}
	
}
