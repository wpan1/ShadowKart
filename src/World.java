/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Sample Solution
 * Author: Matt Giuca <mgiuca>
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

/** Represents the entire game world.
 * (Designed to be instantiated just once for the whole game).
 */
public class World
{
    /** The world map (two dimensional grid of tiles).
     * The concept of tiles is a private implementation detail to World. All
     * public methods deal with pixels, not tiles.
     */
    private TiledMap map;
    
    /** The panel
     * Displays the player's current item and positon
     */
    private Panel panel;

    /** The world's camera
     * Camera class used to display the world
     */
    private Camera camera;
    
    /** DataReader
     * Reader class to import information for items/waypoints
     * from textfiles
     */
    private DataReader dataReader = new DataReader();
    
    /** List of waypoints
     * Enemy gokarts follow waypoints  to navigate map
     */
    private ArrayList<int[]> waypoints;
 
    /** The player's kart. */
    private Player player;
    /** Calculated time passed */
    private int timer = 0;
    /** Time after player crossed line */
    private int endTimer;
    /** Ranking of the player after crossing line */
    private int endRanking;
    /** Stops rank update after game ends */
    private boolean stopUpdate = false;
    /** The octopus's kart */
    private Octopus octopus;
    /** The dog's kart */
    private Dog dog;
    /** The elephant's kart */
    private Elephant elephant;
    
    /** Item arrayList */
    private ArrayList<Item> items;
    
    /** GameObjects arrayList */
    private ArrayList<GameObject> gameObjects;
    
    /** Create a new World object. 
     * @throws IOException 
     * @throws FileNotFoundException
     * @throws SlickException
     *  */
    public World()
    throws SlickException, FileNotFoundException, IOException
    {
    	// Create map using TiledMap
        map = new TiledMap(Game.ASSETS_PATH + "/map.tmx", Game.ASSETS_PATH);        
        // Generate waypoints from txt file
        waypoints = dataReader.genWaypoints();        
        // Items from txt file
        items = dataReader.genItems();
        // Add to gameObjects
        gameObjects = new ArrayList<GameObject>();
        gameObjects.addAll(items);       
        // Create a panel
        panel = new Panel();        
        // GoKarts
        player = new Player(1332, 13086, Angle.fromDegrees(0));
        gameObjects.add(player);
        
        octopus = new Octopus(1476,13086, Angle.fromDegrees(0));
        gameObjects.add(octopus);
        
        dog = new Dog(1404,13086, Angle.fromDegrees(0));
        gameObjects.add(dog);
        
        elephant = new Elephant(1260,13086,Angle.fromDegrees(0));
        gameObjects.add(elephant);      
        // Camera
        camera = new Camera(Game.SCREENWIDTH, Game.SCREENHEIGHT, this.player);
    }

    /** Get the width of the game world in pixels. */
    public int getWidth()
    {
        return map.getWidth() * map.getTileWidth();
    }

    /** Get the height of the game world in pixels. */
    public int getHeight()
    {
        return map.getHeight() * map.getTileHeight();
    }
    
    /** Return all gameObjects in the world */
    public ArrayList<GameObject> getGameObjects(){
    	return gameObjects;
    }

    /** Update the game state for a frame.
     * @param rotate_dir The player's direction of rotation
     *      (-1 for anti-clockwise, 1 for clockwise, or 0).
     * @param move_dir The player's movement in the car's axis (-1, 0 or 1).
     * @param use_item Use player's item
     */
    public void update(double rotate_dir, double move_dir, boolean use_item)
    throws SlickException
    {        
    	// Calculates time passed
    	timer += 1;
    	// Checks for itemPickups
        player.itemPickUp(items);
        // Update all gameObjects
        for (GameObject go : gameObjects){
        	// Update item
        	if (go instanceof Item) ((Item) go).updateItem(player, this);
        	else {
        		// Checks if gokart hits an item
        		GameObject goUpdate = go.collisionItem(gameObjects, this);
        		if (goUpdate instanceof Item){
        			// Uses item.update() if item collides with gokart
        			Item itemHit = (Item)goUpdate;
        			// If item only affects the player
        			if (itemHit.getType() == Item.PLAYER && go instanceof Player){
        				itemHit.updateGoKart((Player)go,this);
        				// Ignores move direction
        				((Player) go).update(rotate_dir, 1, use_item, this);
        			}
        			// If item affects every gokart
        			else if ((itemHit).getType() == Item.ALL)
        				itemHit.updateGoKart((GoKart)go, this);
        		}
        		// Otherwise call default update
        		else{
        			if (go instanceof Enemy) ((Enemy) go).update(waypoints, player, this);
        			if (go instanceof Player) ((Player) go).update(rotate_dir, move_dir, use_item, this);
        		}
        	}
        }
        // Calculates end ranking and time, stop further calculations after game finishes
        if (player.getY() <= GoKart.MAPEND_Y && !stopUpdate){
        	endTimer = timer;
        	endRanking = getRank(gameObjects, player);
        	stopUpdate = true;
        }
        // Follow player
        camera.follow(player);
    }

    /** Render the entire screen, so it reflects the current game state.
     * @param g The Slick graphics object, used for drawing.
     */
    public void render(Graphics g)
    throws SlickException
    {
    	// Calculate the camera location (in tiles) and offset (in pixels).
        // Render 24x18 tiles of the map to the screen, starting from the
        // camera location in tiles (rounded down). Begin drawing at a
        // negative offset relative to the screen, to ensure smooth scrolling.
        int cam_tile_x = camera.getLeft() / map.getTileWidth();
        int cam_tile_y = camera.getTop() / map.getTileHeight();
        int cam_offset_x = camera.getLeft() % map.getTileWidth();
        int cam_offset_y = camera.getTop() % map.getTileHeight();        
        int screen_tilew = camera.getWidth() / map.getTileWidth() + 2;
        int screen_tileh = camera.getHeight() / map.getTileHeight() + 2;
        
        // Render the map
        map.render(-cam_offset_x, -cam_offset_y, cam_tile_x, cam_tile_y,
            screen_tilew, screen_tileh);
        
        // Render the players
        for (GameObject go : gameObjects){
        	go.render(g,camera);
        }
        
        // Render end of game message if player gokart finishes race
    	if (player.getY() <= GoKart.MAPEND_Y){
    		g.drawString("GAME OVER", Game.SCREENWIDTH/2 - 42, Game.SCREENHEIGHT/2 - 60);
    		// Position
    		g.drawString("You Finihsed " + Panel.ordinal(endRanking),Game.SCREENWIDTH/2 - 70, Game.SCREENHEIGHT/2 - 80);
    		// Time
    		g.drawString("Time: " + 0.001 * endTimer, Game.SCREENWIDTH/2 - 52, Game.SCREENHEIGHT/2 - 40);
    		// Render panel
            panel.render(g, endRanking, this.player.getCurrentItem());
    	}
    	else
            // Render the panel
            panel.render(g, getRank(gameObjects, player), this.player.getCurrentItem());
    }

    /** Get the friction coefficient of a map location.
     * @param x Map tile x coordinate (in pixels).
     * @param y Map tile y coordinate (in pixels).
     * @return Friction coefficient at that location.
     */
    public double frictionAt(int x, int y)
    {
        int tile_x = x / map.getTileWidth();
        int tile_y = y / map.getTileHeight();
        int tileid = map.getTileId(tile_x, tile_y, 0);
        String friction = map.getTileProperty(tileid, "friction", null);
        return Double.parseDouble(friction);
    }

    /** Determines whether a particular map location blocks movement.
     * @param x Map tile x coordinate (in pixels).
     * @param y Map tile y coordinate (in pixels).
     * @return true if the tile at that location blocks movement.
     */
    public boolean blockingAt(int x, int y)
    {
        return frictionAt(x, y) >= 1;
    }
    
    /** Determines the ranking of a gokart
     * @param gameObjects arrayList
     * @param gk GoKart to calculate rank at
     */
    public int getRank(ArrayList<GameObject> gameObjects, GoKart gk){
    	int rank = 4;
    	for (GameObject obj : gameObjects){
    		if (obj instanceof GoKart && !obj.equals(gk)){
    			// Increase rank for every gokart behind
    			if (gk.getY() < obj.getY())
    				rank -= 1;
    		}
    	}
    	return rank;
    }
}
