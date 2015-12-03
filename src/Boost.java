/* SWEN20003 Object Oriented Software Development
 * Kart Racing Game
 * Author: William Pan <wpan1>
 */

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Boost extends Item {
    /** Location of the "Boost" directory. */
    public static final String BOOST_PATH = "/items/boost.png";
	/** Type of item (If item affects player or all gokarts) */
	private final static int TYPE = Item.PLAYER;
	/** Duration of the item after being used */
	private final static int DURATION = 3000;
	/** Default acceleration speed */
	private final static double DEFAULT_ACCEL = GoKart.ACCELERATION;
	/** Acceleration after using boost */
	private final static double BOOST_ACCEL = 0.0008;
	
	/**Creates a new Boost Item
	 * @param x The item's initial X location (pixels).
	 * @param y The item's initial Y location (pixels).
	 * @throws SlickException
	 */
	public Boost(int x, int y) 
	throws SlickException 
	{
		// Initialise boost image, position and duration
		super(new Image(Game.ASSETS_PATH + Boost.BOOST_PATH),x,y,DURATION);
	}
	
	/** Updates item
	 * @param player The player class used to manipulate
	 * @param world World object
	 */
	@Override
	public void updateItem(Player player, World world)
	throws SlickException {
		// If item is not on ground
		if (this.getState() != 0){
			// If player activates item
			if (this.getState() == TO_FIRE) this.setState(BEING_USED);
			// Set item to be on player, used to activate boost
			// using collisions
			this.setX(player.getX());
			this.setY(player.getY());
		}
	}

	/** Manipulates gokart according to item
	 * @param gokart GoKart to manipualte
	 * @param world World object
	 */
	@Override
	public void updateGoKart(GoKart gokart, World world)
	throws SlickException {
		// Decreases duration
		if (this.getDuration() > 0){
			// If item duration hasn't ended, change acceleration to boost
    		this.setDuration(this.getDuration() - 1);
    		Player.ACCELERATION = BOOST_ACCEL;
		}
		else{
			// Change to default accel after item is destroyed
			this.setState(DESTROYED);
			Player.ACCELERATION = DEFAULT_ACCEL;
		}
	}
	
    /** Gets the type of item */
	int getType() {
		return Boost.TYPE;
	}
}
