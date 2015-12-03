/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: Matt Giuca <mgiuca>
 */

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Elephant extends Enemy{
    /** Location of the "elephant" directory. */
    public static final String ELEPHANT_PATH = "/karts/Elephant.png";
    
    /** Creates a new Elephant enemy.
     * @param x The Elephant's initial X location (pixels).
     * @param y The Elephant's initial Y location (pixels).
     * @param angle The Octpus's initial angle.
     */
    public Elephant(double x, double y, Angle angle)
    throws SlickException
    {
    	// Take image of Elephant kart and create Enemy Class
        super(new Image(Game.ASSETS_PATH + Elephant.ELEPHANT_PATH),x,y,angle);
    }
    
    /** Updates the Elephant
     * @param waypoints Waypoints of where the dog follows
     * @param player Player gokart object
     * @param world World object
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
		// Uses default acceleration
		this.defaultUpdate(world,1);
    }
}
