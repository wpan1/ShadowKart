/* SWEN20003 Object Oriented Software Development
 * Racing Kart Game
 * Author: William Pan <wpan1>
 */

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Oil extends Item {
    /** Location of the "Oilcan" directory. */
    public static final String OILCAN_PATH = "/items/oilcan.png";
    /** Location of the "OilSlick" directory. */
    public static final String OILSLICK_PATH = "/items/oilslick.png";
	// Item affects all character
	private static final int TYPE = Item.ALL;
	// Spin speed when gokart is hit by item
	private static final double SPIN_SPEED = 0.008;
	// How long the spin effect occurrs for
	private static final int DURATION = 700;
	// Where item starts when used
	private static final int DISTANCE = 40;
			
	/** Creates a new Item
     * @param x The player's initial X location (pixels).
     * @param y The monster's initial Y location (pixels).
	 * @throws SlickException 
     */
	public Oil(int x, int y) 
	throws SlickException 
	{
		// Initialise oil image, position and duration
        super(new Image(Game.ASSETS_PATH + Oil.OILCAN_PATH),x,y,DURATION);
	}
	
	/** Returns the type of oil item */
	int getType() {
		return Oil.TYPE;
	}
	
	/** Updates the item
	 * @param player Player class
	 * @param world World class
	 */
	@Override
	public void updateItem(Player player, World world) 
	throws SlickException {
		if (this.getState() == TO_FIRE){
			// Set item to firing state
			this.setState(FIRING);
			// Places item 40 pixels behind player
			Angle angle = player.getAngle();
			this.setX(player.getX()-angle.getXComponent(DISTANCE));
			this.setY(player.getY()-angle.getYComponent(DISTANCE));
			// Changes image to oil slick
			this.setImg(new Image(Game.ASSETS_PATH + Oil.OILSLICK_PATH));
			// Remove item from player
			player.setItem(null);
		}
	}

	/** Updates gokarts
	 * @param gokart Gokart to update
	 * @param world World class
	 */
	@Override
	public void updateGoKart(GoKart gokart,World world)
	throws SlickException {
		// Destroy item after duration of affect finishes
		if (this.getDuration() == 0){
			this.setState(DESTROYED);
			// Remove item from gokart
			gokart.setItem(null);
			return;
		}
		// Decreases duration time
		this.setDuration(this.getDuration() - 1);
		this.setState(BEING_USED);
		// Changes item to gokart position for collision updates
		this.setX(gokart.getX());
		this.setY(gokart.getY());
		
		// Rotates the gokart
        Angle rotateamount = new Angle(SPIN_SPEED);
        gokart.setAngle(gokart.getAngle().add(rotateamount));
        // Keeps default acceleration
		double friction = world.frictionAt((int) this.getX(), (int) this.getY());
		gokart.setVelocity(gokart.getVelocity() + GoKart.ACCELERATION);
		gokart.setVelocity(gokart.getVelocity() * (1 - friction));
        // Modify the position, based on velocity
        // Calculate the amount to move in each direction
        double amount = gokart.getVelocity();
        // Compute the next position, but don't move there yet
        double next_x = gokart.getX() + gokart.getAngle().getXComponent(amount);
        double next_y = gokart.getY() + gokart.getAngle().getYComponent(amount);
        // If the intended destination is a blocking tile, do not move there
        // (crash) -- instead, set velocity to 0
        if (world.blockingAt((int) next_x, (int) next_y) ||
        		gokart.collisionKart(world.getGameObjects(), world, next_x, next_y))
        {
            gokart.setVelocity(0);
        }
        else
        {
            // Actually move to the intended destination
            gokart.setX(next_x);
            gokart.setY(next_y);
        }
	}
}
