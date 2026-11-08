/* Matrix Generator class
 * 
 * This class is responsible for the generation of the initial matrix, transposing it, and creating the initial tableau
 * */

package data;

import java.util.ArrayList;
import java.util.List;

public class MatrixGenerator {
	
	private static final double[] BASE_COST = {3800, 6500, 1500, 300, 14000, 40, 60};
    private static final double[] CAP = {7000, 3300, 800, 2500, 1500, 2500, 400};
    private static final double[] EMISSIONS = {0.95, 0.40, 0.10, 0, 0.75, 0, 0};
    private static final double[] HEAT = {1.00, 1.01, 1.03, 1.05, 1.04, 1.02};
    private static final double[] DEMAND = {6300, 8600, 11800, 12300, 14000, 10600};
    private static final double[] SOLAR_AVAIL = {0, 0.10, 0.60, 0.55, 0.08, 0};
    private static final double[] WIND_AVAIL = {0.35, 0.30, 0.25, 0.25, 0.30, 0.35};
    private static final double[] OTHER_PLANT_AVAIL = {1, 1, 1, 1, 1, 1};
    private static final double[] NO_HEAT = OTHER_PLANT_AVAIL;
    private static final double RAMP = 1500, HYDRO_ENERGY = 8000, COAL_MIN_FRAC = 0.40;
    private static final int H = 4;
    // constants for generating the groups array to be used in creating the primal problem matrix
    
    private static final PlantGroup[] GROUPS = {
    		new PlantGroup("Coal", CAP[0],EMISSIONS[0], BASE_COST[0], HEAT, OTHER_PLANT_AVAIL),
    		new PlantGroup("Natural Gas", CAP[1],EMISSIONS[1], BASE_COST[1], HEAT, OTHER_PLANT_AVAIL),
    		new PlantGroup("Geothermal", CAP[2],EMISSIONS[2], BASE_COST[2], HEAT, OTHER_PLANT_AVAIL),
    		new PlantGroup("Hydro", CAP[3],EMISSIONS[3], BASE_COST[3], NO_HEAT, OTHER_PLANT_AVAIL),
    		new PlantGroup("Oil-based", CAP[4],EMISSIONS[4], BASE_COST[4], HEAT, OTHER_PLANT_AVAIL),
    		new PlantGroup("Solar", CAP[5],EMISSIONS[5], BASE_COST[5], NO_HEAT, SOLAR_AVAIL),
    		new PlantGroup("Wind", CAP[6],EMISSIONS[6], BASE_COST[6], NO_HEAT, WIND_AVAIL),
    };
    // constant groups array to be used in overall solving/project
    
    public PlantGroup getPlantGroup(int x) {
    	return GROUPS[x];
    }
    
    public double[][] buildPrimal(boolean[] selected, boolean co2, double co2Cap){
    	List<Integer> sel = new ArrayList<>(); //create an array list for collecting which groups were selected
    	for(int i = 0; i < GROUPS.length; i++) {
    		if(selected[i]) sel.add(i); //if selected, add the index of the selected group to the list
    	}
    	
    	int n = sel.size()*6;
    	List<double[]> rows = new ArrayList<>();
    	List<Double> rhs = new ArrayList<>();
    	
    	for(int i = 0; i < 6; i++) {
    		double[] row = new double[n];
    		for(int j = 0; j < sel.size(); j++) row[j*6+i] = 1; //at the variable column, set it to 1
    		rows.add(row);
    		rhs.add(DEMAND[i]);
    	//Demands constraint
    	}
    	
    	for(int i = 0; i < sel.size();i++) {
    		PlantGroup p = GROUPS[sel.get(i)];
    		for(int j = 0; j < 6; j++) {
    			double[] row = new double[n];
    			row[i*6+j] = -1;
    			rows.add(row);
    			rhs.add(0 - p.capacityVal(j));
    		}
    	}
    	//Capacity constraint, -1 because of inequality & maximization property/law
    	
    	int coal = sel.indexOf(0); //check if coal is selected, return -1 if not
    	if(coal >= 0) {
    		for(int i = 0; i < 6; i++) {
    			double[] row = new double[n];
    			row[coal*6+i] = 1;
    			rows.add(row);
    			rhs.add(COAL_MIN_FRAC*CAP[0]);
    		}//Coal minimum constraint;
    		
    		for(int i = 1; i < 6; i++) {
    			double[] up = new double[n];
    			up[coal*6+i] = -1;
    			up[coal*6+i-1] = 1;
    			rows.add(up);
    			rhs.add(-RAMP);
    		}//coal ramping up
    		
    		for(int i = 1; i < 6; i++) {
    			double[] down = new double[n];
    			down[coal*6+i] = 1;
    			down[coal*6+i-1] = -1;
    			rows.add(down);
    			rhs.add(-RAMP);
    		}//coal ramping down constraints
    	}
    	
    	int hydro = sel.indexOf(3); //check if hydro is selected
    	if (hydro >= 0) {
    		double[] row = new double[n];
    		for(int i = 0; i < 6; i++) row[hydro*6+i] = -H;
    		rows.add(row);
    		rhs.add(-HYDRO_ENERGY);
    	} //Hydro water constraint
    	
    	if(co2) { //CO2 cap constraint
    		double[] row = new double[n];
    		for(int i = 0; i < sel.size(); i++) {
    			for(int j = 0; j < 6; j++) {
    				row[i*6+j] = -H * GROUPS[sel.get(i)].emission;
    			}
    		}
    		rows.add(row);
    		rhs.add(-co2Cap);
    	}
    	
    	int m = rows.size();
    	double[][] primalMatrix = new double[m+1][n+1];
    	for(int i = 0; i < m; i++) {
    		System.arraycopy(rows.get(i), 0, primalMatrix[i], 0, n);
    		primalMatrix[i][n] = rhs.get(i);
    	} //assemble the primal matrix
    	for(int i = 0; i < sel.size(); i++) {
    		PlantGroup p = GROUPS[sel.get(i)];
    		for(int j = 0; j < 6; j++) {
    			primalMatrix[m][i*6+j] = H*p.cost[j];
    		}
    	}
    	return primalMatrix;
    }
    
    public double[][] transpose(double[][] matrix){
    	int nrow = matrix.length;
    	int ncol = matrix[0].length;
    	double[][] result = new double[ncol][nrow];
    	for(int i = 0; i < nrow; i++) {
    		for(int j = 0; j < ncol; j++) {
    			result[j][i] = matrix[i][j];
    		}
    	}
    	return result;
    }
    
    public double[][] buildInitialTableau(double[][] transposed){
    	int n = transposed.length-1; //dual constraints / primal variables
    	int m = transposed[0].length-1; //dual variables / primal slack
    	int width = n+m+2; //add the z and rhs columns
    	
    	double[][] tableau = new double[n+1][width];
    	for(int i = 0; i < n; i++) {
    		for(int j = 0; j < m; j++) tableau[i][j] = transposed[i][j]; //insert everything that is in the transposed matrix in the tableau
    		tableau[i][m+i] = 1; //insert the slack variable
    		tableau[i][width-1] = transposed[i][m]; // insert the rhs 
    	}
    	for(int i = 0; i < m; i++) tableau[n][i] = 0 - transposed[n][i]; //bottom row,  negative value
    	tableau[n][m+n] = 1; //z column/element
    	return tableau;
    }
}
