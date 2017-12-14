package net.jfabricationgames.linear_algebra;

public class Matrix2D {
	
	protected double[][] entries;
	
	public enum Orientation {
		ROW,
		COL;
	}
	
	public Matrix2D(Orientation orientation, double[]... entries) {
		this(entries);
		if (orientation == Orientation.ROW) {
			transpose(this);
		}
	}
	public Matrix2D(Orientation orientation, Vector2D... entries) throws LinearAlgebraException {
		if (entries.length == 0) {
			throw new LinearAlgebraException("The dimensions of a matrix must be greater than 0.");
		}
		this.entries = new double[entries.length][];
		for (int i = 0; i < entries.length; i++) {
			this.entries[i] = entries[i].asArray();
		}
		if (orientation == Orientation.ROW) {
			transpose(this);
		}
	}
	public Matrix2D(Orientation orientation, Vector3D... entries) throws LinearAlgebraException {
		if (entries.length == 0) {
			throw new LinearAlgebraException("The dimensions of a matrix must be greater than 0.");
		}
		this.entries = new double[entries.length][];
		for (int i = 0; i < entries.length; i++) {
			this.entries[i] = entries[i].asArray();
		}
		if (orientation == Orientation.ROW) {
			transpose(this);
		}
	}
	public Matrix2D(int dimensionX, int dimensionY) throws LinearAlgebraException {
		entries = new double[dimensionY][dimensionX];
		if (dimensionX <= 0 || dimensionY <= 0) {
			throw new LinearAlgebraException("The dimensions of a matrix must be greater than 0.");
		}
	}
	public Matrix2D(double[][] entries) throws LinearAlgebraException {
		this(entries, false);
	}
	private Matrix2D(double[][] entries, boolean clone) throws LinearAlgebraException {
		if (clone) {
			this.entries = new double[entries.length][entries[0].length];
			for (int i = 0; i < entries.length; i++) {
				this.entries[i] = entries[i].clone();
			}
		}
		else {
			this.entries = entries;
			if (entries.length == 0 || entries[0].length == 0) {
				throw new LinearAlgebraException("The dimensions of a matrix must be greater than 0.");
			}
			int dim0 = entries[0].length;
			for (int i = 0; i < entries.length; i++) {
				if (entries[i].length != dim0) {
					throw new LinearAlgebraException("All vectors in the matrix must have the same length.");
				}
			}
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Matrix2D) {
			Matrix2D m = (Matrix2D) obj;
			if (entries.length == m.entries.length && entries[0].length == m.entries[0].length) {
				boolean equal = true;
				for (int i = 0; i < entries.length; i++) {
					for (int j = 0; j < entries[0].length; j++) {
						equal &= Math.abs(entries[i][j] - m.entries[i][j]) < 1e-8; 
					}
				}
				return equal;				
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Matrix2D: ");
		for (int i = 0; i < entries[0].length; i++) {
			sb.append('[');
			for (int j = 0; j < entries.length; j++) {
				sb.append(String.format(" %.5f ", entries[j][i]));
			}
			sb.append("]\n          ");
		}
		return sb.toString();
	}
	
	/**
	 * Clone this matrix.
	 */
	@Override
	public Matrix2D clone() {
		return new Matrix2D(entries, true);
	}
	
	/**
	 * Get the matrix entry at a position.
	 * 
	 * @param x
	 * 		The x dimension of the position.
	 * 
	 * @param y
	 * 		The y dimension of the position.
	 * 
	 * @return
	 * 		The value at the position.
	 */
	public double at(int x, int y) {
		return entries[x][y];
	}
	/**
	 * Change the matrix value at a position to a new value.
	 * 
	 * @param x
	 * 		The x dimension of the value.
	 * 
	 * @param y
	 * 		The y dimension of the value.
	 * 
	 * @param val
	 * 		The new value.
	 */
	public void set(int x, int y, double val) {
		entries[x][y] = val;
	}
	
	/**
	 * Transpose a matrix.
	 * 
	 * @param matrix
	 * 		The matrix that is transposed.
	 */
	public static void transpose(Matrix2D matrix) {
		int[] dimensions = matrix.getDimensions();
		if (dimensions[0] == dimensions[1]) {
			//the matrix is quadratic
			double tmp;
			for (int i = 0; i < dimensions[0]-1; i++) {
				for (int j = i+1; j < dimensions[0]; j++) {
					tmp = matrix.at(j, i);
					matrix.set(j, i, matrix.at(i, j));
					matrix.set(i, j, tmp);
				}
			}
		}
		else {
			//the matrix is not quadratic
			double[][] entries = new double[dimensions[0]][dimensions[1]];//change x and y dimension
			for (int i = 0; i < dimensions[0]; i++) {
				for (int j = 0; j < dimensions[1]; j++) {
					entries[i][j] = matrix.at(j, i);
				}
			}
			matrix.entries = entries;
		}
	}
	
	/**
	 * Transpose the matrix returning the transposed version. This matrix is not changed.
	 * 
	 * @return
	 * 		The transposed matrix.
	 */
	public Matrix2D transpose() {
		Matrix2D t = clone();
		transpose(t);
		return t;
	}
	
	/**
	 * Generate a unit matrix of size dimension.
	 * 
	 * @param dimension
	 * 		The size of the unit matrix.
	 * 
	 * @return
	 * 		The unit matrix.
	 */
	public static Matrix2D getUnitMatrix(int dimension) {
		Matrix2D unitMatrix = new Matrix2D(dimension, dimension);
		for (int i = 0; i < dimension; i++) {
			unitMatrix.set(i, i, 1);
		}
		return unitMatrix;
	}
	
	/**
	 * Get the dimensions of this matrix (y and x).
	 * 
	 * @return
	 * 		The matrix dimensions as integer-Array.
	 */
	public int[] getDimensions() {
		return new int[] {entries[0].length, entries.length};
	}
}