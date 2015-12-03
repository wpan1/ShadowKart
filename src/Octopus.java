/* SWEN20003 Object Oriented Software Development
 * Racing Kart Game
 * Author: William Pan <wpan1>
 */

import java.util.ArrayList;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Octopus extends Enemy {
    /** Location of the "octopus" directory. */
    public static final String OCTOPUS_PATH = "/karts/octopus.png";
    /** Min Chanse Distance */
    public static final int MIN_CHASEDIST = 100;
    /** Max Chanse Distance */
    public static final int MAX_CHASEDIST = 250;
	
	/**
	 * Creates a new Octopus enemy.
	 * @param x The Octpus's initial X location (pixels).
	 * @param y The Octpus's initial Y location (pixels).
	 * @param angle The Octpus's initial angle.
	 * @throws SlickException
	 */
	public Octopus(double x, double y, Angle angle)
	throws SlickException {
		super(new Image(Game.ASSETS_PATH + Octopus.OCTOPUS_PATH), x, y, angle);
	}

    /** Updates the Octopus's Go Kart
     * @param waypoints ArrayList of waypoints
     * @param player GoKart object for player
     * @param world World object
     *  */
	public void update(ArrayList<int[]> waypoints, GoKart player ,World world) 
	throws SlickException
		{
		// Stops gokart after finishing
		if (this.getY() <= GoKart.MAPEND_Y){
			endUpdate(world);
			return;
		}
		//Find distance between this gokart and others
		float dist = (float) Math.sqrt(
	            Math.pow(this.getX() - player.getX(), 2) +
	            Math.pow(this.getY() - player.getY(), 2) );
		// If between MIN/MAX distance
		if (dist >= Octopus.MIN_CHASEDIST && dist <= Octopus.MAX_CHASEDIST){
			// Angle is set to go towards player
			this.setAngle(this.getAngle().subtract(new Angle(getRotateSpeedPlayer(player))));
			// Increment waypoints despite following player - changes waypoint to closest
			// This is so that if octopus follows player for a while it doesn't backtrack
			this.setWaypointPos(this.getClosetWaypointIndex(waypoints));
		}
		// Else Angle is set to go to waypoint
		else 
			this.setAngle(this.getAngle().subtract(new Angle(getRotateSpeed(waypoints))));
		// Default acceleration applies
		defaultUpdate(world,1);
    }
	
	/** Gets the rotation speed when player is within 100 - 250 pixels
	 * @param player Player class to get (x,y) coordiantes
	 * @return Rotational Speed of Octopus with respect to player
	 */
	public double getRotateSpeedPlayer(GoKart player){
		// Calculates angle to player
		Angle rotateAngle = Angle.fromCartesian(
				player.getX() - this.getX(),
				player.getY() - this.getY());
		Angle rotateAmount = new Angle(this.getAngle().getRadians() - rotateAngle.getRadians());
		// Checks whether to rotate left or right
		int rotate_dir = 1;
		if (rotateAmount.getDegrees() < 0)
			rotate_dir = -1;
		else if (rotateAmount.getDegrees() == 0)
			rotate_dir = 0;
		return (rotate_dir * ROTATE_SPEED);
	}
}
