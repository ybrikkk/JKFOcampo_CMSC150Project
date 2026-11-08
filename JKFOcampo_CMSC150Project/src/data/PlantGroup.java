/* Plant Group class
 * 
 * This class shall be used for the creation of the plant group used in creating the initial tableau
 * */

package data;

public class PlantGroup {
	public String name;
	public double cap;
	public double emission;
	public double[] cost;
	public double[] avail;
	//variables, public again for easy access.
	
	public PlantGroup(String n, double cap, double emi, double baseCost, double[] heat, double[] avail) {
		this.name = n;
		this.cap = cap;
		this.emission = emi;
		this.avail = avail;
		this.cost = new double[heat.length];
		for(int i = 0; i < heat.length; i++) {
			this.cost[i] = baseCost*heat[i]; //used for computing the cost based on heat and time block
		}
	}
	
	public double capacityVal(int x) {
		return cap*avail[x]; //method used for getting the capacity right hand side
	}
}
