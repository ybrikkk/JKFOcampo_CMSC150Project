/* Simplex Result class
 * 
 * This class is for holding the variables of the tableau used for Simplex.
 * Everything is in public for easier manipulation and reduction of methods haha
 * 
 * */

package data;

import java.util.ArrayList;
import java.util.List;

public class SimplexResult {

	public List<double[][]> history = new ArrayList<>(); 
	// create a list to store previous iterations, widening is done for compatibility (and to use general list methods)
 	public double[][] finalTableau;
 	//to store the last iteration which is the final tableau
 	public double Z; // to hold the minimized cost
 	public int iter = 0; //to hold the number of iterations
 	public boolean isInfeasible = false; //placeholder for feasibility
 	public double[] solution; // to hold the solution vector
 	
 	
}
