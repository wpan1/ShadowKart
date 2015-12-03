/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Matt Giuca <mgiuca>
 */

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class GoKart extends GameObject {

    /** The angle the enemy is currently facing.
     * Note: This is in neither degrees nor radians -- the Angle class allows
     * angles to be manipulated without worrying about units, and the angle
     * value can be extracted in either degrees or radians.
     */
    private Angle angle;
    
    /** The enemy's current velocity (px/ms). */
    private double velocity;
   
    /** Item that affects gokart */
    private Item item;
	
    /** Rotation speed, in radians per ms. */
    static final double ROTATE_SPEED = 0.004;
    /** Acceleration while the enemy is driving, in px/ms^2. */
    static final double ACCELERATION = 0.0005;
    /** Spin speed when hit by item */
    static final double SPIN_SPEED = 0.008;
    /** Boost speed when using boost */
	static final double BOOST = 0.0008;
	
	/** End of map y-coordinate */
	static final int MAPEND_Y = 1026;

    /** Creates a new GoKart.
     * @param img Image of the gokart.
     * @param x The gokart's initial X location (pixels).
     * @param y The gokart's initial Y location (pixels).
     * @param angle The gokart's initial angle.
     */
    public GoKart(Image img, double x, double y, Angle angle){
    	super(x,y,img);
    	this.angle = angle;
    	this.velocity = 0;
    }
    
    /** Gets the item being used on gokart */
    public Item getItem() {
		return item;
	}

    /** Sets the item being used on gokart */
	public void setItem(Item item) {
		this.item = item;
	}
	
    /** Set the angle */
    public void setAngle(Angle angle) {
		this.angle = angle;
	}

	/** The angle the enemy is facing. */
    public Angle getAngle()
    {
        return this.angle;
    }
    
    /** Set the velocity */
    public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	/** The enemy's current velocity, in the direction the enemy is facing
     * (px/ms).
     */
    public double getVelocity()
    {
        return this.velocity;
    }
    
    /** Updates karts once they finish
     * Uses default friction, velocity calculations
     * @param world World object
     * @throws SlickException 
     */
    public void endUpdate(World world)
    throws SlickException
    	{
        // Determine the friction of the current location
        double friction = world.frictionAt((int) this.getX(), (int) this.getY());
        // Reduce due to friction (this has the effect of creating a
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
    
    /** Draw the kart to the screen at the correct place.
     * @param g The current Graphics context.
     * @param camera Camera Object
     */
    @Override
    public void render(Graphics g, Camera camera)
    {
        // Calculate the GameObject's on-screen location from the camera
        int screen_x = (int) (this.getX() - camera.getLeft());
        int screen_y = (int) (this.getY() - camera.getTop());
        this.getImg().setRotation((float) this.angle.getDegrees());
        this.getImg().drawCentered(screen_x, screen_y);
    }
}
