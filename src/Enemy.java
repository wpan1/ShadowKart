/* SWEN20003 Object Oriented Software Development
 * Racing Kart Game
 * Author: William Pan <wpan1>
 */

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

abstract public class Enemy extends GoKart {
	/** The current (x,y) coordinates of the waypoint */
	private int waypointPos;
	/** The waypointPos of the last waypoint (x,y) coordinate */
	static final int WAYPOINT_END = 49;
	/** Rotate Left */
	static final int ROTATE_LEFT = -1;
	/** Rotate Right */
	static final int ROTATE_RIGHT = 1;
	
    /** Creates a new Enemy.
     * @param img The image of the enemy
     * @param x The Enemy's initial X location (pixels).
     * @param y The Enemy's initial Y location (pixels).
     * @param angle The Octpus's initial angle.
     */
	public Enemy(Image img, double x, double y, Angle angle) {
		super(img, x, y, angle);
		this.waypointPos = 0;
	}

	/** Get the waypointPos */
	public int getWaypointPos() {
		return waypointPos;
	}

	/** Set the waypointPos */
	public void setWaypointPos(int waypointPos) {
		this.waypointPos = waypointPos;
	}

	/** Gets the rotation speed of enemy karts
	 * @param waypoints Checks which waypoint for kart to follow
	 * @return double of rotation speed
	 */
	public double getRotateSpeed(ArrayList<int[]> waypoints){
    	int waypointPos = this.getWaypointPos();
    	// Calculate distance from next waypoint
    	float dist = (float) Math.sqrt(
	            Math.pow(this.getX() - waypoints.get(waypointPos)[0], 2) +
	            Math.pow(this.getY() - waypoints.get(waypointPos)[1], 2) );
    	// Incrementing waypoint when kart goes within 250 pixels
		if (dist <= 250) this.setWaypointPos(waypointPos+1);
		// Calculate angle to face waypoint
		Angle rotateAngle = Angle.fromCartesian(waypoints.get(waypointPos)[0] - this.getX(),
				waypoints.get(waypointPos)[1] - this.getY());
		Angle rotateAmount = new Angle(this.getAngle().getRadians() - rotateAngle.getRadians());
		// Checks direction to rotate
		int rotate_dir = ROTATE_RIGHT;
		if (rotateAmount.getDegrees() < 0)
			rotate_dir = ROTATE_LEFT;
		else if (rotateAmount.getDegrees() == 0)
			rotate_dir = 0;
		// Rotate to the left / right if needed
		return (rotate_dir * ROTATE_SPEED);
	}
	
	/** Gets the index of the closest waypoint
	 * @param waypoints ArrayList of waypoints
	 */
	public int getClosetWaypointIndex(ArrayList<int[]> waypoints){
		double min = Integer.MAX_VALUE;
		int index = -1;
		for (int[] pos : waypoints){
	    	// Calculate distance from next waypoint
	    	float dist = (float) Math.sqrt(
		            Math.pow(this.getX() - pos[0], 2) +
		            Math.pow(this.getY() - pos[1], 2) );
	    	// Change min distance if current distance is lower
	    	if (dist < min){
				index += 1;
	    		min = dist;
	    	}
		}
    	return index;
	}
	
	/** Changes (x,y) coordinates of Enemy GoKarts according to
	 * angle and current friction block. Constant acceleration
	 * @param world Uses World to get friction values of blocks
	 */
    public void defaultUpdate(World world, double speed) 
    throws SlickException
    	{
    	// Determine the friction of the current location
    	double friction = world.frictionAt((int) this.getX(), (int) this.getY());
        // Then, reduce due to friction (this has the effect of creating a
        // maximum velocity for a given coefficient of friction and
        // acceleration)
        this.setVelocity(this.getVelocity() + speed * ACCELERATION);
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
    
	/** Update Method for enemy 
	 * @param waypoints ArrayList of all (x,y) coordinates
	 * @param player Player class for calculations
	 * @param world The world class for calculations (friction)
	 * */
	abstract public void update(ArrayList<int[]> waypoints, GoKart player, World world)
	throws SlickException;
	
}