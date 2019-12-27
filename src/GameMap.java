/**
 * CIS 120 Game HW
 * (c) University of Pennsylvania
 * @version 2.1, Apr 2017
 */


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * GameCourt
 * 
 * This class initializes the TileMap and holds the primary game logic for how different objects 
 * interact with one another.
 */
@SuppressWarnings("serial")
public class GameMap extends JPanel {
	
	private Player player;

    public boolean playing = false; // whether the game is running 
    private JLabel status; // Current status text, i.e. "Running..."
    private JLabel health; // Current health percentage
    private JLabel highScore; // Current high score

    // Tile size and number constants. Intended window size is 300.
    public static final int TILE_WIDTH = 20;
    public static final int TILE_HEIGHT = 20;
    public static final int NUM_ROWS = 15;
    public static final int NUM_COLS = 15;
    public static final int MAP_WIDTH = TILE_WIDTH * NUM_COLS;
    public static final int MAP_HEIGHT = TILE_HEIGHT * NUM_ROWS;
    
    // Tile file paths
    public static final String tilePath = "files/tile.png";
    public static final String homePath = "files/home.png";
    
    // Player movement speed
    public static final int PLAYER_VELOCITY = 4;
    public static final int BULLET_VELOCITY = 10;

    // Update interval for timer, in milliseconds
    public static final int INTERVAL = 35;
    public static final int WANDERER_SPAWN = 125;
    public static final int SCOUT_SPAWN = 350;
    public static int num_intervals_passed = 0;

    private final Tile[][] tileMap; //2D array used to generate map
    
    // Keep track of all objects in map.
    private List<Wall> walls = new LinkedList<>();
    private List<Bullet> bullets = new CopyOnWriteArrayList<>(); 
    private List<Attacker> attackers = new LinkedList<>(); 
    
    public GameMap(JLabel status, JLabel health, JLabel highScore) {
    	tileMap = new Tile[NUM_ROWS][NUM_COLS];
    	
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        
        for(int i = 0; i < tileMap.length; i++) {
        	for(int j = 0; j < tileMap[0].length; j++) {
        		if(j == 0 || j == 1) {
        			tileMap[i][j] = new Tile(homePath, j * TILE_WIDTH, i * TILE_HEIGHT, TILE_WIDTH, 
            				TILE_HEIGHT);
        		}
        		else {
        			tileMap[i][j] = new Tile(tilePath, j * TILE_WIDTH, i * TILE_HEIGHT, TILE_WIDTH, 
            				TILE_HEIGHT);
        		}
        	}
        }
        
        // initialize walls in map
        for(int i = 4; i < 10; i++) {
        	tileMap[4][i] = new Wall(i * TILE_WIDTH, 4 * TILE_HEIGHT, TILE_WIDTH, 
    				TILE_HEIGHT);
        	walls.add((Wall) tileMap[4][i]);
        }
        tileMap[5][3] = new Wall(3 * TILE_WIDTH, 5 * TILE_HEIGHT, TILE_WIDTH, 
				TILE_HEIGHT);
        walls.add((Wall) tileMap[5][3]);
        tileMap[5][10] = new Wall(10 * TILE_WIDTH, 5 * TILE_HEIGHT, TILE_WIDTH, 
				TILE_HEIGHT);
        walls.add((Wall) tileMap[5][10]);
        tileMap[6][2] = new Wall(2 * TILE_WIDTH, 6 * TILE_HEIGHT, TILE_WIDTH, 
				TILE_HEIGHT);
        walls.add((Wall) tileMap[6][2]);
        tileMap[6][11] = new Wall(11 * TILE_WIDTH, 6 * TILE_HEIGHT, TILE_WIDTH, 
				TILE_HEIGHT);
        walls.add((Wall) tileMap[6][11]);
        tileMap[7][2] = new Wall(2 * TILE_WIDTH, 7 * TILE_HEIGHT, TILE_WIDTH, 
				TILE_HEIGHT);
        walls.add((Wall) tileMap[7][2]);
        tileMap[7][11] = new Wall(11 * TILE_WIDTH, 7 * TILE_HEIGHT, TILE_WIDTH, 
				TILE_HEIGHT);
        walls.add((Wall) tileMap[7][11]);
        tileMap[10][2] = new Wall(2 * TILE_WIDTH, 10 * TILE_HEIGHT, TILE_WIDTH, 
				TILE_HEIGHT);
        walls.add((Wall) tileMap[10][2]);
        tileMap[10][11] = new Wall(11 * TILE_WIDTH, 10 * TILE_HEIGHT, TILE_WIDTH, 
				TILE_HEIGHT);
        walls.add((Wall) tileMap[10][11]);
        for(int i = 3; i < 11; i++) {
        	tileMap[11][i] = new Wall(i * TILE_WIDTH, 11 * TILE_HEIGHT, TILE_WIDTH, 
    				TILE_HEIGHT);
        	walls.add((Wall) tileMap[11][i]);
        }

        // The timer is an object which triggers an action periodically with the given INTERVAL. We
        // register an ActionListener with this timer, whose actionPerformed() method is called each
        // time the timer triggers. We define a helper method called tick() that actually does
        // everything that should be done in a single timestep.
        Timer timer = new Timer(INTERVAL, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        timer.start(); // MAKE SURE TO START THE TIMER!

        // Enable keyboard focus on the court area.
        // When this component has the keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        // This key listener allows the player to move as long as an arrow key is pressed, by
        // changing the square's player accordingly. (The tick method below actually moves the
        // player.)
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    player.setVx(-PLAYER_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    player.setVx(PLAYER_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    player.setVy(PLAYER_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    player.setVy(-PLAYER_VELOCITY);
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                	bullets.add(new Bullet(BULLET_VELOCITY, MAP_WIDTH, MAP_HEIGHT, player.getPx(),
                			player.getPy(), player.getHeight(), player.getWidth()));
                	
                }
            }

            public void keyReleased(KeyEvent e) {
                player.setVx(0);
                player.setVy(0);
            }
        });

        this.status = status;
        this.health = health;
        this.highScore = highScore;
    }

    private void moveBullets() throws ConcurrentModificationException {
    	Iterator<Bullet> bulletIter = bullets.iterator();
        while(bulletIter.hasNext()) {
        	Bullet bullet = bulletIter.next();
        	if(bullet.getPx() <= 0 || bullet.getPx() >= MAP_WIDTH || bullet.getPy() <= 0 || 
        			bullet.getPy() >= MAP_HEIGHT) {
        		bullets.remove(bullet);
        	}
        	else {
        		Iterator<Wall> wallIter = walls.iterator();
        		boolean bulletMoved = false;
            	while(wallIter.hasNext()) {
            		Wall wall = wallIter.next();
            		if(bullet.intersects(wall)) {
            			bullets.remove(bullet);
            			bulletMoved = true;
            			break;
            		}
            		else if(bullet.willHitWall(wall)) {
            			bullet.moveToWall(wall);
            			bulletMoved = true;
            			break;
            		}
            	}
            	if(!bulletMoved) {
            		Iterator<Attacker> attackerIter = attackers.iterator();
                	while(attackerIter.hasNext()) {
                		Attacker attacker = attackerIter.next();
                		if(bullet.intersects(attacker)) {
                			bullets.remove(bullet);
                			attackers.remove(attacker);
                			bulletMoved = true;
                			break;
                		}
                	}
            	}
            	if(!bulletMoved) {
            		bullet.move();
            		if(bullet.intersects(player)) {
            			player.lowerHealth(50);
            			bullets.remove(bullet);
            			health.setText("Health: " + player.getHealth());
            		}
            	}
        	}
        }
    }
    
    /**
     * (Re-)set the game to its initial state.
     */
    public void reset() {
    	bullets.clear();
    	attackers.clear();
    	player = new Player(MAP_WIDTH, MAP_HEIGHT);
    	
    	num_intervals_passed = 0;
        playing = true;
        status.setText("Running...");

        // Make sure that this component has the keyboard focus
        requestFocusInWindow();
    }

    /**
     * This method is called every time the timer defined in the constructor triggers.
     */
    void tick() {
        if (playing) {
        	if(player.getHealth() <= 0) {
        		playing = false;
        		status.setText("Game over!");
        	}
        	
        	player.increaseScore();
        	highScore.setText("High Score: " + player.getScore());
        	
        	num_intervals_passed++;
        	if(num_intervals_passed % WANDERER_SPAWN == 0) {
        		attackers.add(new Wanderer(MAP_WIDTH, MAP_HEIGHT, TILE_WIDTH, TILE_HEIGHT));
        	}
        	if(num_intervals_passed % SCOUT_SPAWN == 0) {
        		attackers.add(new Scout(MAP_WIDTH, MAP_HEIGHT, TILE_WIDTH, TILE_HEIGHT));
        	}
        	
        	
        	boolean hasPlayerMoved = false;
        	Iterator<Wall> iter = walls.iterator();
        	while(iter.hasNext()) {
        		Wall wall = iter.next();
        		if(player.willHitWall(wall)) {
        			player.moveToWall(wall);
        			hasPlayerMoved = true;
        			break;
        		}
        		Iterator<Attacker> attackerIter = attackers.iterator();
        		while(attackerIter.hasNext()) {
        			Attacker attacker = attackerIter.next();
        			if(attacker.willHitWall(wall)) {
        				attacker.moveToWall(wall);
        			}
        		}
        	}
            
        	// advance the player and bullets in their current direction.
            if(!hasPlayerMoved) {
            	player.move();
            }
            moveBullets();
            
            Iterator<Attacker> attackerIter = attackers.iterator();
            while(attackerIter.hasNext()) {
            	Attacker attacker = attackerIter.next();
            	if(attacker instanceof Wanderer) {
            		((Wanderer) attacker).move(tileMap, player);
            		if(((Wanderer) attacker).attack(player) && num_intervals_passed % 20 == 0) {
            			bullets.add(new Bullet(-BULLET_VELOCITY, MAP_WIDTH, MAP_HEIGHT, 
            					attacker.getPx() - 20, attacker.getPy(), attacker.getHeight(), 
            					0));
            		}
            	}
            	else if(attacker instanceof Scout) {
            		((Scout) attacker).move(tileMap, player);
            		if(attacker.intersects(player)) {
            			playing = false;
            			status.setText("Game over! Do not get anywhere near the scout.");
            		}
            	}
            	if(attacker.getPx() <= this.TILE_WIDTH * 2) {
            		playing = false;
            		status.setText("Game over! Turf infiltrated.");
            	}
            	
            }

            // update the display
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for(int i = 0; i < tileMap.length; i++) {
        	for(int j = 0; j < tileMap[0].length; j++) {
        		tileMap[i][j].draw(g);
        	}
        }
        player.draw(g);
        
        Iterator<Bullet> bulletIter = bullets.iterator();
        while(bulletIter.hasNext()) {
        	Bullet bullet = bulletIter.next();
        	bullet.draw(g);
        }
        
        Iterator<Attacker> attackerIter = attackers.iterator();
        while(attackerIter.hasNext()) {
        	Attacker attacker = attackerIter.next();
        	attacker.draw(g);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(MAP_WIDTH, MAP_HEIGHT);
    }
}