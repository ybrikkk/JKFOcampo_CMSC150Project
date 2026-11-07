/* Simplex Solver class
 * 
 * This class is for solving the problem through Simplex method.
 * This assumes that the initial tableau is already ready for solving and no need to transpose if it's a Minimization problem
 * 
 * */

package data;

public class SimplexSolver {
	
	public static SimplexResult doSimplex(double[][] tableau, boolean isMax) {
		
		int nrow = tableau.length;
		int ncol = tableau[0].length;
		SimplexResult result = new SimplexResult();
		
		result.history.add(copyMatrix(tableau));
		
		while(true) {
			int colIndex = -1; // hold the column index, if still -1 after checking the bottom row, the problem has reached its optimal solution
			double minVal = 0; // hold the minimum value (largest magnitude) at the bottom row, to be used in getting the Pivot column and element 
			
			for(int i = 0; i < ncol; i++) {
				if(tableau[nrow-1][i] < minVal) {
					minVal = tableau[nrow-1][i];
					colIndex = i;
				}
			}
			// check the bottom row (except the solution column), update minVal and colIndex
			
			if(colIndex == -1) {
				break;
			}
			//Optimal solution is reached since no more negative value in the bottom row
			
			int rowIndex = -1; // hold the row index, if still -1, problem is not feasible since no positive test ratio was obtained
			double minTR = Double.POSITIVE_INFINITY; // initial test ratio is an absurdly large number such as positive infinity to ensure it gets replaced
			
			for(int i = 0; i < nrow-1; i++) { // loop through the elements of the pivot column
				if(tableau[i][colIndex] > 0) { // check if positive
					double tr = tableau[i][ncol-1]/tableau[i][colIndex]; //get the test ratio
					if(tr < minTR) {
						minTR = tr;
						rowIndex = i; //if the test ratio in the current loop is smaller than previous minTR, update
					}
				}
			}
			
			if(rowIndex == -1 || minTR == Double.POSITIVE_INFINITY) { // if rowIndex or minTR did not get updated for the whole loop for Pivot Element
				result.isInfeasible = true; // the problem is not feasible
				return result; // return immediately
			}
			
			double pivotElement = tableau[rowIndex][colIndex];
			
			for(int i = 0; i < ncol; i++) {
				tableau[rowIndex][i] /= pivotElement;
			} // normalization
			
			for(int i = 0; i < nrow; i++) {
				if (i == rowIndex) continue; // skip if at the same row/element as the pivot elem
				
				double nPE = tableau[i][colIndex]; // hold the normalized pivot element for cleaner code
				for(int j = 0; j < ncol; j++) {
					tableau[i][j] -= nPE*tableau[rowIndex][j]; // tableau[i][j] (element in the tableau) minus the normalized pivot element
					// multiplied to that element to eliminate that element.
				}
			}// Elimination
			
			result.iter++;
			result.history.add(copyMatrix(tableau));
			
		}
		
		result.finalTableau = tableau;
		result.Z = tableau[nrow-1][ncol-1];
		//after the loop/reached the optimal solution, insert the tableau as the final tableau in the result object
		//get Z by getting the last element
		
		if(!result.isInfeasible) { //if the problem is feasible
			int varCols = ncol-2;
			result.solution = new double[varCols];
		 //get the number of variable columns by removing the solution and Z columns
		//then instantiate the solution vector/array with the size of the variable columns
		
			if(isMax) { //if the problem is a Maximization type
				for(int i = 0; i < varCols; i++) {
					int zeroCount = 0;
					int oneCount = 0;
					int oneRowIndex = -1;
					for(int j = 0; j < nrow; j++) {
						if(Math.abs(tableau[j][i] - 1) <  1e-9) {
							oneCount++;
							oneRowIndex = j;
						}else if(Math.abs(tableau[j][i]) < 1e-9) { //1e-9 is used to catch values that are not exactly zero but can be considered as zero
							zeroCount++;
						}
					}// check each element in the column if there are ones or zeroes
					
					if(oneCount == 1 && zeroCount == nrow-1) { //check if there is only one 1 in the column and the rest are zeroes
						result.solution[i] = tableau[oneRowIndex][ncol-1];
					}else {
						result.solution[i] = 0; // if the conditions above are not fulfilled, then the value of that variable is zero
					}
				}
			} else { //else if the problem is Minimization
				for(int i = 0; i < varCols; i++) {
					result.solution[i] = tableau[nrow-1][i];
				}
			}
		}
		return result;
	}
	
	public static double[][] copyMatrix(double[][] tableau){
		double[][] copy = new double[tableau.length][tableau[0].length];
		for(int i = 0; i < tableau.length; i++) {
			System.arraycopy(tableau[i], 0, copy[i], 0, tableau[i].length);
		}
		return copy;
	}
}
