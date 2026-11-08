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
    
    //TODO: buildPrimal method, transpose method, buildDualMethod haha d q n kaya idle
}
