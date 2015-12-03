/* SWEN20003 Object Oriented Software Development
 * Racing Kart Game
 * Author: William Pan <wpan1>
 */

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Tomato extends Item {
    /** Location of the "Toamto" directory. */
    public static final String TOMATO_PATH = "/items/tomato.png";
    /** Location of the "Toamto Projectile" directory. */
    public static final String TOMATOPROJ_PATH = "/items/tomato-projectile.png";
	/** Item affects all character */
	private static final int TYPE = ALL;
	/** Spin speed when gokart is hit by item */
	private static final double SPIN_SPEED = 0.008;
	/** How long the spin effect occurrs for */
	private static final int DURATION = 700;
	/** Speed of tomato projectile */
	private static final double SPEED = 1.7;
	/** Where item starts when fired */
	private static final int DISTANCE = 40;
	
	Angle angle = new Angle(0);
	/**Creates a new Item
	 * @param x The item's initial X location (pixels).
	 * @param y The item's initial Y location (pixels).
	 * @throws SlickException
	 */
	public Tomato(int x, int y) 
	throws SlickException 
	{
		// Initialise tomato image, position and duration
		super(new Image(Game.ASSETS_PATH + Tomato.TOMATO_PATH),x,y,DURATION);
	}
	
	/** Returns type of Tomato item */
	int getType() {
		return Tomato.TYPE;
	}
	
	/** Updates the item
	 * @param player Player class
	 * @param world World class
	 */
	@Override
	public void updateItem(Player player, World world) 
	throws SlickException {
        // Determine the friction of the current location
        double friction = world.frictionAt((int) this.getX(), (int) this.getY());
        // Destroy item if it hits a wall
        if (friction == 1){
        	this.setState(DESTROYED);
        	return;
        }
        // If item is used
		if (this.getState() == TO_FIRE){
			// Change to tomato projectile image
			this.setImg(new Image(Game.ASSETS_PATH + Tomato.TOMATOPROJ_PATH));
			this.setState(FIRING);
			// Fires tomato projectile to be ahead of player
			this.angle = player.getAngle();
			this.setX(player.getX()+angle.getXComponent(DISTANCE));
			this.setY(player.getY()+angle.getYComponent(DISTANCE));
			player.setItem(null);
		}
		// If item is in the air
		else if (this.getState() == FIRING){
			// Move item at constant speed
			this.setX(this.getX()+angle.getXComponent(SPEED));
			this.setY(this.getY()+angle.getYComponent(SPEED));
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
