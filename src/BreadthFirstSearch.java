import java.util.ArrayDeque;
import java.util.Queue;

public class BreadthFirstSearch {
	
	private final Tile[][] tileMap;
	private int sourceX;
	private int sourceY;
	private int destinationX;
	private int destinationY;
	
	public BreadthFirstSearch(Tile[][] tileMap, int sourceX, int sourceY, 
			int destinationX, int destinationY) {
		this.tileMap = tileMap;
		this.sourceX = sourceX;
		this.sourceY = sourceY;
		this.destinationX = destinationX;
		this.destinationY = destinationY;
	}
	
	public Direction bfs() {
		boolean[][] discovered = new boolean[tileMap.length][tileMap[0].length];
		discovered[sourceX][sourceY] = true;
		
		Queue<Integer> xQueue = new ArrayDeque<>();
		Queue<Integer> yQueue = new ArrayDeque<>();
		xQueue.add(sourceX);
		yQueue.add(sourceY);
		
		int[][] prevX = new int[tileMap.length][tileMap[0].length];
		int[][] prevY = new int[tileMap.length][tileMap[0].length];
		prevX[sourceX][sourceY] = -1;
		prevY[sourceX][sourceY] = -1;
		
		return bfs(xQueue, yQueue, prevX, prevY, discovered);
	}
	
	private Direction bfs(Queue<Integer> xQueue, Queue<Integer> yQueue, int[][] prevX, 
			int[][] prevY, boolean[][] discovered) {
		if(xQueue.isEmpty() || yQueue.isEmpty()) {
			return null;
		}
		
		int currentX = xQueue.poll();
		int currentY = yQueue.poll();
		
		//Exploring the four (or less) adjacent squares to find path 
		for(int i = Math.max(currentX - 1, 0); i < Math.min(currentX + 1, tileMap[0].length - 1); 
				i++) {
			if(i == currentX) {
				for(int j = Math.max(0, currentY - 1); j < 
						Math.min(currentY + 1, tileMap.length - 1); j++) {
					if(!(tileMap[currentX][j] instanceof Wall)) {
						if(!discovered[currentX][j]) {
							discovered[currentX][j] = true;
							prevX[currentX][j] = currentX;
							prevY[currentX][j] = currentY;
							xQueue.add(currentX);
							yQueue.add(j);
							
							if(currentX == destinationX && j == destinationY) {
								return findInitialDirection(prevX, prevY);
							}
						}
					}
				}
			}
			else {
				if(!(tileMap[i][currentY] instanceof Wall)) {
					if(!discovered[i][currentY]) {
						discovered[i][currentY] = true;
						prevX[i][currentY] = currentX;
						prevY[i][currentY] = currentY;
						xQueue.add(i);
						yQueue.add(currentY);
						
						if(i == destinationX && currentY == destinationY) {
							return findInitialDirection(prevX, prevY);
						}
					}
				}
			}
		}
		
		return bfs(xQueue, yQueue, prevX, prevY, discovered);
	}
	
	private Direction findInitialDirection(int[][] prevX, int[][] prevY) {
		int x = this.destinationX;
		int y = this.destinationY;
		while(!(prevX[x][y] == sourceX && prevY[x][y] == sourceY)) {
			int temp = x;
			x = prevX[x][y];
			y = prevY[temp][y];
		}
		
		if(x < sourceX)
			return Direction.LEFT;
		else if(x > sourceX)
			return Direction.RIGHT;
		else if(y < sourceY)
			return Direction.UP;
		else if(y > sourceY)
			return Direction.DOWN;
		else
			return null;
	}
}
