/* SWEN20003 Object Oriented Software Development
 * Racing Kart Game
 * Author: William Pan <wpan1>
 */

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

abstract public class Item extends GameObject{
	/** Type of item */
	// Affects everyone
	static int ALL = 1;
	// Affects only player
	static int PLAYER = 0;
    /** State of Item */
    private int state;
    /** Item States */
    public static final int ON_FLOOR = 0;
    public static final int PICKED_UP = 1;
    public static final int TO_FIRE = 2;
    public static final int FIRING = 3;
    public static final int BEING_USED = 4;
    public static final int DESTROYED = 5;
    /** Duration of the item */
    private int duration;
    
    /** Creates a new Item.
     * @param image Image of the item
     * @param x The player's initial X location (pixels).
     * @param y The monster's initial Y location (pixels).
     */
    public Item(Image image, double x, double y, int duration){
    	super(x,y,image);
    	this.state = 0;
    	this.duration = duration;
    }
    
    /** Gets the type of item */
    abstract int getType();
	
    /** Gets the duration of item
     * @return Duration left of item
     */
    public int getDuration() {
		return duration;
    }

    /** Sets the duration left of item 
     * @param duration Duration of item to change to
     */
	public void setDuration(int duration) {
		this.duration = duration;
	}

	/** Gets the current state of the item 
	 * @return Returns the state of item (refer to Item JAVADOC)
	 */
    public int getState() {
		return state;
	}

    /** Changes the current state of the item
     * @param state State to change item to (refer to Item JAVADOC)
     */
	public void setState(int state) {
		this.state = state;
	}
	
	/** Draw the item to the screen at the correct place.
     * @param g The current Graphics context.
     * @param camera Camera Object
     */
	@Override
    public void render(Graphics g, Camera camera)
    {
    	// Checks if item is not on the ground or being used
    	if (this.getState() != 0 && this.getState() != 3) return;
        // Calculate the item's on-screen location from the camera
        int screen_x = (int) (this.getX() - camera.getLeft());
        int screen_y = (int) (this.getY() - camera.getTop());
        this.getImg().drawCentered(screen_x, screen_y);
    }
    
    /** Updates the item position */
    abstract public void updateItem(Player player, World world) 
    throws SlickException;
    
    /** Manipulates the gokart */
    abstract public void updateGoKart(GoKart gokart, World world) 
    throws SlickException;
}
