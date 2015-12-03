/* SWEN20003 Object Oriented Software Development
 * Racing Kart Game
 * Author: William Pan <wpan1>
 */

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Dog extends Enemy{
	// Acceleration if Dog is ahead
	private final double AHEAD_ACCEL = 0.9;
	// Acceleration if Dog is behind
	private final double BEHIND_ACCEL = 1.1;

    /** Creates a new Dog enemy.
     * @param x The Dog's initial X location (pixels).
     * @param y The Dog's initial Y location (pixels).
     * @param angle The Octpus's initial angle.
     */
    public Dog(double x, double y, Angle angle)
    throws SlickException
    {
		// Initialise dog image, position and angle
        super(new Image(Game.ASSETS_PATH + "/karts/dog.png"),x,y,angle);
    }
    
    /** Updates the dog
     * @param waypoints Waypoints of where the dog follows
     * @param player Player gokart object
     * @param world World object
     * @throws SlickException 
     */
	public void update(ArrayList<int[]> waypoints, GoKart player ,World world)
	throws SlickException 
		{
		// Stops gokart after finishing
		if (this.getY() <= GoKart.MAPEND_Y){
			endUpdate(world);
			return;
		}
		// Set angle to face closest waypoint
		this.setAngle(this.getAngle().subtract(new Angle(getRotateSpeed(waypoints))));
		// Sets speed according to player position
		// If behind
		if (this.getY() >= player.getY()) defaultUpdate(world, BEHIND_ACCEL);
		// If ahead
		else defaultUpdate(world, AHEAD_ACCEL);
	}
}
