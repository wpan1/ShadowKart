/* SWEN20003 Object Oriented Software Development
 * Racing Kart Game
 * Author: William Pan <wpan1>
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.newdawn.slick.SlickException;

public class DataReader {
	public static final String WAYPOINTS_TXT = "data/waypoints.txt";
	public static final String ITEM_TXT = "data/items.txt";
	
    /** Generate waypoint ArrayList 
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    public ArrayList<int[]> genWaypoints()
    throws FileNotFoundException, IOException
    {
    	// Create arraylist of int arrays
    	ArrayList<int[]> waypoints = new ArrayList<int[]>();
    	// Read data from txt
    	try {
    		BufferedReader br = new BufferedReader(new FileReader(WAYPOINTS_TXT));
    		String line;
    		// Read until EOF (null) no more lines
    		while((line = br.readLine()) != null){
    			// Ignore blank lines
    			if (line.isEmpty()) continue;
    			// Read lines starting with digits
    			if(Character.isDigit(line.charAt(0))){
    				// Seperate spaces ( ) and commas (,)
    				StringTokenizer sT = new StringTokenizer(line, " ,");
    				// Add to waypoints ArrayList
    				int waypoint[] = {Integer.parseInt(sT.nextToken()),Integer.parseInt(sT.nextToken())};
    				waypoints.add(waypoint);
    			}
    		}
		return waypoints;
    	}
    	catch (Exception e){
    		System.out.println("Exception thrown  :" + e);
    	}
		return waypoints;
    }
    
    /** Generate items ArrayList 
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws SlickException */
    public ArrayList<Item> genItems()
    throws FileNotFoundException, IOException
    {
    	ArrayList<Item> item = new ArrayList<Item>();
    	String[] itemNames = new String[] {"Oil","Tomato","Boost"};
    	// Read lines from items.txt
    	try {
    		BufferedReader br = new BufferedReader(new FileReader(ITEM_TXT));
    	    String line;
    	    while ((line = br.readLine()) != null) {
    	       for(String itemName : itemNames){
    	    	   // Reading only lines containing item information
    	    	   if (line.startsWith(itemName)){
    	    	       StringTokenizer sT = new StringTokenizer(line," ,");
    	    	       // Removing "Oil Can" string and creating Oil object with x,y
    	    		   if (itemName.equals("Oil")) {
    	    			   sT.nextToken();
    	    			   sT.nextToken();
        	    	       int x = Integer.parseInt(sT.nextToken());
        	    	       int y = Integer.parseInt(sT.nextToken());
    	    			   item.add(new Oil(x,y));
    	    		   }
    	    	       // Removing "Tomato"/"Boost" string and creating object with x,y
    	    		   else{
    	    			   sT.nextToken();
        	    	       int x = Integer.parseInt(sT.nextToken());
        	    	       int y = Integer.parseInt(sT.nextToken());
        	    		   if (itemName.equals("Tomato")) item.add(new Tomato(x,y));
        	    		   else if (itemName.equals("Boost")) item.add(new Boost(x,y));
    	    		   }
    	    	   }
    	       }
    	    }
    	}
    	catch (Exception e){
    		System.out.println("Exception thrown  :" + e);
    	}
    	
		return item;
    }
}
