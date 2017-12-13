package net.jfabricationgames.linear_algebra;

import java.util.ArrayList;
import java.util.List;

public class Gauss {
	
	private Matrix2D m;
	private double[] b;
	private double[] x;
	private List<int[]> swaps;
	private double[][] solutions;
	
	private Gauss beforeSolutionCalculation = null;
	
	private boolean xVecCalculated = false;
	private String xVecError = null;
	private boolean solutionsCalculated = false;
	private String solutionsError = null;
	
	private static final double EPSILON = 1e-5;
	
	public Gauss(Matrix2D m, double[] b, List<int[]> swaps) {
		this.m = m;
		this.b = b;
		this.swaps = swaps;
	}
	
	public static class GaussCalculationException extends LinearAlgebraException {
		
		private static final long serialVersionUID = -8723252408515001646L;

		public GaussCalculationException(String message, Throwable cause) {
			super(message, cause);
		}

		public GaussCalculationException(String message) {
			super(message);
		}
	}
	
	/**
	 * Calculate the gauss algorithm for the given input.
	 * 
	 * @param matrix
	 * 		The matrix m.
	 * 		The original matrix is not changed but cloned.
	 * 
	 * @param bVec
	 * 		The vector b.
	 * 		The original vector is not changed but cloned.
	 * 
	 * @return
	 * 		The calculated Gauss object which holds the matrix (cloned and) brought to triangular form.
	 */
	public static Gauss calculateGauss(Matrix2D matrix, double[] bVec) throws GaussCalculationException {
		if (matrix.getDimensions()[1] != bVec.length) {
			throw new GaussCalculationException("The matrix dimension (in y direction) has to be equal to the b-Vector.");
		}
		Matrix2D m = matrix.clone();
		double[] b = bVec.clone();
		List<int[]> swaps = new ArrayList<int[]>();
		int[] dimensions = m.getDimensions();
		int iter = Math.min(dimensions[0], dimensions[1]);
		for (int i = 0; i < iter; i++) {//i is in x dimension
			//iterate over the matrix to create a triangular matrix
			if (Math.abs(m.at(i, i)) < EPSILON) {
				//the value through which shall be divided is 0 -> change with the first non-zero-line
				boolean swaped = false;
				for (int j = i+1; j < dimensions[0]; j++) {
					if (!swaped && Math.abs(m.at(i, j)) > EPSILON) {
						//line j has a non-zero-value -> swap lines i and j
						swaps.add(new int[] {i, j});
						swap(m, b, i, j);
						swaped = true;
					}
				}
				if (!swaped) {
					//can't swap because all entries are 0 -> nothing to do here (flies away...) -> continue with the next iteration
					continue;
				}
			}
			//the value at the position (i, i) is != 0 -> bring the values at (i, i+1), (i, i+2), ... to 0
			for (int j = i+1; j < dimensions[1]; j++) {//j in in y dimension
				//change every entry in this line (from x=i+1 on)
				for (int k = i+1; k < dimensions[0]; k++) {//k is in x dimension
					m.set(k, j, m.at(k, j) - (m.at(i, j) * m.at(k, i) / m.at(i, i)));
				}
				//also change the entry in the b vector
				b[j] = b[j] - (m.at(i, j) * b[i] / m.at(i, i));
				//set this value at last because it's needed for the other calculations
				m.set(i, j, 0);//always bring to zero -> no calculation needed
			}
		}
		Gauss gauss = new Gauss(m, b, swaps);
		gauss.beforeSolutionCalculation = new Gauss(m.clone(), b.clone(), swaps);
		return gauss;
	}
	
	/**
	 * Swap two lines of a matrix.
	 */
	private static void swap(Matrix2D m, double[] b, int i, int j) {
		double tmp;
		for (int k = 0; k < m.entries.length; k++) {
			tmp = m.entries[k][i];
			m.entries[k][i] = m.entries[k][j];
			m.entries[k][j] = tmp;
			tmp = b[i];
			b[i] = b[j];
			b[j] = tmp;
		}
	}
	
	/**
	 * Rollback the gauss calculation after a solution was calculated (and maybe failed) to try another solution calculation (with another method).
	 */
	public void rollback() {
		if (beforeSolutionCalculation != null) {
			x = null;
			xVecCalculated = false;
			xVecError = null;
			solutions = null;
			solutionsCalculated = false;
			solutionsError = null;
			m = beforeSolutionCalculation.m;
			b = beforeSolutionCalculation.b;
			//swaps are not changed after the initial calculation
		}
	}
	
	/**
	 * Get the X-Vector that was calculated by the gauss system (if it can be calculated).
	 * 
	 * @return
	 * 		The X-Vector or null if it can't be calculated.
	 * 
	 * @throws GaussCalculationExcption
	 * 		A {@link GaussCalculationException} is thrown when the system can't be calculated (e.g. when there is more than one solution; if there is no solution null is returned)
	 */
	public double[] getXVec() throws GaussCalculationException {
		if (!xVecCalculated) {
			calculateXVec();
		}
		if (xVecError != null) {
			throw new GaussCalculationException(xVecError);
		}
		return x;
	}
	private void calculateXVec() throws GaussCalculationException {
		xVecCalculated = true;
		x = new double[b.length];
		int[] dimensions = m.getDimensions();
		//solve the last line first
		for (int i = 0; i < dimensions[1]; i++) {
			if (Math.abs(m.at(dimensions[0]-i-1, dimensions[1]-i-1)) < EPSILON) {
				//the value at (i, i) is 0
				if (b[dimensions[1]-i-1] == 0) {
					xVecError = "The system can't be calculated. There is a zero-line in the system so it could have more than one solution (or also no solution).";
					throw new GaussCalculationException(xVecError);
				}
				else {
					//the system has no solution -> set x to null and abort
					x = null;
					return;
				}
			}
			else if (dimensions[0]-i-2 >= 0 && m.at(dimensions[0]-i-2, dimensions[1]-i-1) != 0) {
				//values left in this line of the matrix (on the left side) -> multiple solutions
				xVecError = "The system can't be calculated because it has more that one solution.";
				throw new GaussCalculationException(xVecError);
			}
			else {
				//no values left from this value in the matrix and this value is not zero
				x[dimensions[1]-i-1] = b[dimensions[1]-i-1]/m.at(dimensions[0]-i-1, dimensions[1]-i-1);
				//the x_i value is known -> backwards running
				for (int j = 0; j < dimensions[1]-i-1; j++) {
					//update the b-vector and the matrix (maybe the matrix is used somehow else...)
					b[j] = b[j] - (x[dimensions[1]-i-1] * m.at(dimensions[0]-i-1, j));
					m.set(dimensions[0]-i-1, j, 0);
				}
			}
		}
		//swap the lines and vector entries back (last entry first)
		for (int i = swaps.size()-1; i >= 0; i--) {
			int[] swap = swaps.get(i);
			/*double[] tmpLine = m.entries[swap[0]];
			m.entries[swap[0]] = m.entries[swap[1]];
			m.entries[swap[1]] = tmpLine;
			//also swap the b vector entries
			double tmp = b[swap[0]];
			b[swap[0]] = b[swap[1]];
			b[swap[1]] = tmp;*/
			swap(m, b, swap[0], swap[1]);
			//and the x vector entries
			double tmp;
			tmp = x[swap[0]];
			x[swap[0]] = x[swap[1]];
			x[swap[1]] = tmp;
		}
	}
	
	/**
	 * Calculate the solutions of this gauss system if there is more than one solution (with only one free parameter in the solution).
	 * The Double-Array that is returned contains one solution vector (in the first entry) and a direction vector (in the second entry).
	 * The solutions of the gauss system are: solutions[0] + t * solutions[1] (with t from R)
	 * 
	 * @return
	 * 		All solution of this system as a Double-Array.
	 * 
	 * @throws GaussCalculationException
	 * 		A {@link GaussCalculationException} is thrown when the calculation fails.
	 */
	public double[][] getSolutions() throws GaussCalculationException {
		if (!solutionsCalculated) {
			calculateSolutions();
			if (solutions != null) {
				boolean singleSolution = true;
				for (int i = 0; i < solutions[1].length; i++) {
					singleSolution &= Math.abs(solutions[1][i]) < EPSILON;
				}
				if (singleSolution) {
					x = solutions[0];
					xVecCalculated = true;
				}
			}
		}
		if (solutionsError != null) {
			throw new GaussCalculationException(solutionsError);
		}
		return solutions;
	}
	private void calculateSolutions() throws GaussCalculationException {
		solutionsCalculated = true;
		int[] dimensions = m.getDimensions();
		solutions = new double[2][dimensions[1]];
		//check if the matrix has the right form
		int[] newDimension = new int[2];
		if (dimensions[0]-1 == dimensions[1]) {
			//the matrix is one dimension bigger in x direction (e.g. a 3x4 matrix)
			//solve an alternative problem with a smaller matrix instead
			newDimension[0] = dimensions[1];
			newDimension[1] = dimensions[1];
		}
		else if (dimensions[0] <= dimensions[1]) {
			//the matrix is quadratic or is bigger in y direction (e.g. a 3x3 matrix or a 4x3 matrix)
			//in this case the last lines must be zero-lines
			boolean zeroLines = true;
			for (int i = dimensions[0]-1; i < dimensions[1]; i++) {
				for (int j = 0; j < dimensions[0]; j++) {
					//zero line in m
					zeroLines &= Math.abs(m.at(j, i)) < EPSILON;
				}
				//b is also zero
				zeroLines &= Math.abs(b[i]) < EPSILON;
			}
			if (!zeroLines) {
				solutionsError = "The solutions can't be calculated because the matrix has a wrong form.";
				throw new GaussCalculationException(solutionsError);
			}
			//solve an alternative problem with a smaller matrix instead
			newDimension[0] = dimensions[0]-1;
			newDimension[1] = dimensions[0]-1;
		}
		else {
			solutionsError = "The solution has more than one free parameter. Can't be calculated.";
			throw new GaussCalculationException(solutionsError);
		}
		//create the alternative gauss system
		Matrix2D altM = new Matrix2D(newDimension[0], newDimension[1]);
		double[] altB = new double[newDimension[0]];
		//copy some of the entries of m to altM and from b to altB
		for (int i = 0; i < newDimension[0]; i++) {
			for (int j = 0; j < newDimension[1]; j++) {
				altM.set(i, j, m.at(i, j));
			}
		}
		for (int i = 0; i < newDimension[1]; i++) {
			altB[i] = b[i];
		}
		//create the alternative problem (without swaps)
		Gauss alt = new Gauss(altM, altB, new ArrayList<int[]>());
		//solve the alternative problem (if possible)
		try {
			double[] altX = alt.getXVec();
			//copy the single solution to the solutions vector
			for (int i = 0; i < altX.length; i++) {
				solutions[0][i] = altX[i];
			}
			//set the last x-value as parameter
			//add the vector in reverse order and change back for easier indexing
			solutions[1][0] = 1;
			//set the parameter values for the other fields in the solution vector
			for (int i = 1; i <= dimensions[1]-1; i++) {
				for (int j = i; j <= dimensions[1]-1; j++) {
					solutions[1][j] -= solutions[1][i-1] * m.at(dimensions[0]-1-j, dimensions[0]-1-i);					
				}
			}
			//reverse the vector again
			double tmp;
			int len = solutions[1].length;
			for (int i = 0; i < len/2; i++) {
				tmp = solutions[1][i];
				solutions[1][i] = solutions[1][len-i];
				solutions[1][len-i] = tmp;
			}
			//calculation is finally ready (returned by the calling method)
		}
		catch (GaussCalculationException gce) {
			solutionsError = "The solutions could not be calculated.";
			throw new GaussCalculationException(solutionsError, gce);
		}
	}

	public Matrix2D getM() {
		return m;
	}
	public double[] getB() {
		return b;
	}
	public List<int[]> getSwaps() {
		return swaps;
	}
}