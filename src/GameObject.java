/* SWEN20003 Object Oriented Software Development
 * Racing Kart Game
 * Author: William Pan <wpan1>
 */

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public abstract class GameObject {
    /** The image of the object. */
    private Image img;
    /** The X coordinate of the GameObject (pixels). */
    private double x;
    /** The Y coordinate of the GameObject (pixels). */
    private double y;
    
    public GameObject(double x, double y, Image img){
    	this.img = img;
    	this.x = x;
    	this.y = y;
    }
    
    /** Draw the kart to the screen at the correct place.
     * @param g The current Graphics context.
     * @param camera Camera Object
     */
    public abstract void render(Graphics g, Camera camera);
    
	/** The image of the Item 
	 * @return The Image of item
	 */
    public Image getImg() {
		return img;
	}
    
    /** Tests for collisions
     * @param arrayList Item or GoKart objects
     * @param world World Object
     * @throws SlickException 
     */
    public GameObject collisionItem(ArrayList<GameObject> arrayList, World world) 
    throws SlickException{
    	for (GameObject obj : arrayList){
    		// Checks for item collisons
    		if (obj instanceof Item){
    			Item item = (Item)obj;
    			// Ignore collisions between items that are in wrong state
    			if (item.getState() == Item.FIRING || item.getState() == Item.BEING_USED){
		    		//Find distance between this gokart and others
		    		float dist = (float) Math.sqrt(
		    	            Math.pow(this.getX() - item.getX(), 2) +
		    	            Math.pow(this.getY() - item.getY(), 2) );
		    		if (dist < 40){
		    			return item;
	    			}
    			}
    		}
    	}
    	// Returns this if no collisions
    	return this;
    }
    
    /** Tests for collisions
     * @param arrayList Item or GoKart objects
     * @param world World Object
     * @throws SlickException 
     */
    public boolean collisionKart(ArrayList<GameObject> arrayList, World world, double next_x, double next_y) 
    throws SlickException{
    	for (GameObject obj : arrayList){
    		if (obj.equals((GameObject)(this))) continue;
        	// Checks for gokart collisions
    		if (obj instanceof GoKart){
    			GoKart gk = (GoKart)obj;
	    		//Find distance between this gokart and others
	    		float dist = (float) Math.sqrt(
	    	            Math.pow(next_x - gk.getX(), 2) +
	    	            Math.pow(next_y - gk.getY(), 2) );
	    		if (dist < 40){
	    			return true;
	    		}
    		}
    	}
		return false;
    }
    
	/** The image of the Item
	 * @param img Image to set item to
	 */
    public void setImg(Image img) {
		this.img = img;
	}
    
    /** Gets the X coordinate of the item */
	public double getX() {
		return x;
	}
	
    /** Sets the X coordinate of the item
     * @param x X coordinate
     */
	public void setX(double x) {
		this.x = x;
	}

	/** Gets the Y coordinate of the item */
	public double getY() {
		return y;
	}
	
    /** Sets the Y coordinate of the item
     * @param y Y coordinate
     */
	public void setY(double y) {
		this.y = y;
	}

}
