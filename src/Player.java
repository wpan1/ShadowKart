/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Sample Solution
 * Author: Matt Giuca <mgiuca>
 */

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/** The player's kart (Donkey).
 */
public class Player extends GoKart
{
    /** Location of the "donkey" directory. */
    public static final String DONKEY_PATH = "/karts/donkey.png";
	/** Default acceleration of gokart */
    static double ACCELERATION = 0.0005;
	/** Item if player has item to use */
	private Item currentitem = null;
	
    /** Creates a new Player.
     * @param x The player's initial X location (pixels).
     * @param y The monster's initial Y location (pixels).
     * @param angle The player's initial angle.
     * @throws SlickException 
     */
    public Player(double x, double y, Angle angle) 
    throws SlickException
    {
    	// Starting positon and angle of player
        super(new Image(Game.ASSETS_PATH + Player.DONKEY_PATH),x,y,angle);
    }
    
	/** Picks up the item, changes state of item and adds to player class
	 * @param items ArrayList of all items
	 */
	public void itemPickUp(ArrayList<Item> items){
		// Checks if player has item picked up
		if (this.getItem() != null) return;
		for (Item item : items){
			float dist = (float) Math.sqrt(
    		Math.pow(this.getX() - item.getX(), 2) +
    		Math.pow(this.getY() - item.getY(), 2));
			if (dist <= 40){
				// Checks if item is on ground (State == 0)
				if (item.getState() == Item.ON_FLOOR){
					this.setCurrentItem(item);
					item.setState(Item.PICKED_UP);
					return;
				}
			}	
		}
	}

    /** Set the player's current item */
	public void setCurrentItem(Item item) {
		this.currentitem = item;
	}
	
    /** Get the player's current item */
	public Item getCurrentItem() {
		return currentitem;
	}
	
	/** Update the player for a frame.
     * Adjusts the player's angle and velocity based on input, and updates the
     * player's position. Prevents the player from entering a blocking tile.
     * @param rotate_dir The player's direction of rotation
     *      (-1 for anti-clockwise, 1 for clockwise, or 0).
     * @param move_dir The player's movement in the car's axis (-1, 0 or 1).
     * @param world The world the player is on (to get friction and blocking).
     */
    public void update(double rotate_dir, double move_dir, boolean use_item, World world)
    throws SlickException
    {
        // Changes the state for item to fire/place
        if(use_item && getCurrentItem() != null){
        	getCurrentItem().setState(Item.TO_FIRE);
        	// Remove item from player after being fired
        	setCurrentItem(null);
        }
        
    	// Stops gokart after finishing
		if (this.getY() <= GoKart.MAPEND_Y){
			this.endUpdate(world);
			return;
		}
        // Modify the player's angle
        Angle rotateamount = new Angle(ROTATE_SPEED * rotate_dir);
        this.setAngle(this.getAngle().add(rotateamount));
        
        // Determine the friction of the current location
        double friction = world.frictionAt((int) this.getX(), (int) this.getY());
        // Modify the player's velocity. First, increase due to acceleration.
        this.setVelocity(this.getVelocity() + ACCELERATION * move_dir);
        // Then, reduce due to friction (this has the effect of creating a
        // maximum velocity for a given coefficient of friction and
        // acceleration)
        this.setVelocity(this.getVelocity() * (1 - friction));
        // Modify the position, based on velocity
        // Calculate the amount to move in each direction
        double amount = this.getVelocity();
        // Compute the next position, but don't move there yet
        double next_x = this.getX() + this.getAngle().getXComponent(amount);
        double next_y = this.getY() + this.getAngle().getYComponent(amount);
        // If the intended destination is a blocking tile, do not move there
        // (crash) -- instead, set velocity to 0
        // Also computes whether kart collision occurrs
        if (world.blockingAt((int) next_x, (int) next_y)
        	|| collisionKart(world.getGameObjects(), world, next_x, next_y))
        {
            this.setVelocity(0);
        }
        else
        {
            // Actually move to the intended destination
            this.setX(next_x);
            this.setY(next_y);
        }
    }

	/** Render player gokart
     * Override due to rendering game over message
     * g Graphics object for drawing
     * camera Camera object
     */
    @Override
    public void render(Graphics g, Camera camera){
    	super.render(g,camera);
    }
}
