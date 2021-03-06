package net.jfabricationgames.linear_algebra;

import net.jfabricationgames.linear_algebra.Gauss.GaussCalculationException;

public class Line3D {
	
	protected Vector3D start;
	protected Vector3D direction;
	
	public Line3D(Vector3D start, Vector3D direction) {
		this.start = start;
		this.direction = direction;
		if (direction.equals(Vector3D.NULL_VEC)) {
			throw new LinearAlgebraException("A Line's direction vector can't be a null-vector.");
		}
	}
	
	/**
	 * Check whether a line is equal to this line. Equal means the lines have linearly dependent directions and a point of the parameter line is on
	 * this line.
	 * 
	 * @param line
	 *        The other line.
	 * 
	 * @return True if the two lines describe the same line. False otherwise.
	 */
	public boolean lineEquals(Line3D line) {
		return line.getDirection().isLinearlyDependent(direction) && isPointOnLine(line.getStart());
	}
	
	/**
	 * Check whether a point is on this line.
	 * 
	 * @param point
	 *        The checked point.
	 * 
	 * @return True if the point is on the line. False otherwise.
	 */
	public boolean isPointOnLine(Vector3D point) {
		/*//the calculation of the parameter t is equal in all cases (x, y and z).
		double t1 = direction.x == 0 ? 0 : ((start.x - point.x) / direction.x);
		double t2 = direction.y == 0 ? 0 : ((start.y - point.y) / direction.y);
		double t3 = direction.z == 0 ? 0 : ((start.z - point.z) / direction.z);
		if (direction.x == 0) {
			return Math.abs(start.x - point.x) < 1e-8;
		}
		else if (direction.y == 0) {
			return Math.abs(start.y - point.y) < 1e-8;
		}
		else if (direction.z == 0) {
			return Math.abs(start.z - point.z) < 1e-8;
		}
		return Math.abs(t1-t2) < 1e-5 && Math.abs(t1-t3) < 1e-5 && t1 != 0;*/
		
		//solve a equation system to check whether the point is on the line
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.COL, direction);
		double[] b = point.sub(start).asArray();
		Gauss g = Gauss.calculateGauss(m, b);
		try {
			double[][] solutions = g.getSolutions();
			return solutions != null;
		}
		catch (LinearAlgebraException lae) {
			return false;
		}
	}
	
	/**
	 * Calculate the parameter t which describes how often the vector direction is to be added to the start vector to get to the point.
	 * 
	 * @param point
	 *        The checked point.
	 * 
	 * @return If the point is on the line the parameter t is returned. Double.NaN otherwise.
	 */
	public double calculateT(Vector3D point) {
		if (isPointOnLine(point)) {
			if (direction.x != 0) {
				return -((start.x - point.x) / direction.x);
			}
			else if (direction.y != 0) {
				return -((start.y - point.y) / direction.y);
			}
			else {
				return -((start.z - point.z) / direction.z);
			}
		}
		return Double.NaN;
	}
	
	/**
	 * Calculate the cross-point of a line with this line.
	 * 
	 * @param line
	 *        The other line.
	 * 
	 * @return The cross-point as Vector3D object or null if there is no cross-point.
	 */
	public Vector3D calculateCrosspoint(Line3D line) {
		if (line instanceof LineSegment3D) {
			return line.calculateCrosspoint(this);
		}
		return calculateCrosspointUnchecked(line);
	}
	protected Vector3D calculateCrosspointUnchecked(Line3D line) {
		if (direction.isLinearlyDependent(line.direction)) {
			//parallel or equal -> no single cross-point
			return null;
		}
		//create an equation system and solve it to check if the lines cross
		//create a 2x2 system to calculate t1 and t2
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.COL, new double[] {direction.x, direction.y},
				new double[] {-line.direction.x, -line.direction.y});
		double[] b = new double[] {line.start.x - start.x, line.start.y - start.y};
		Gauss gauss = Gauss.calculateGauss(m, b);
		try {
			double[] x = gauss.getXVec();
			if (x == null) {
				//there is no solution
				return null;
			}
			//if x can be calculated it holds t1 and t2 -> insert in the third equation and check if its true
			if (Math.abs((x[0] * direction.z - x[1] * line.direction.z) - (line.start.z - start.z)) < 1e-5) {
				//the third equation results in a true statement -> the lines have a cross-point
				return start.add(direction.mult(x[0]));
			}
			else {
				//the third equation results in a false statement -> the lines are not crossing
				return null;
			}
		}
		catch (GaussCalculationException gce) {
			//the system can't be calculated -> the lines don't cross
			return null;
		}
	}
	
	public Vector3D getStart() {
		return start;
	}
	public Vector3D getDirection() {
		return direction;
	}
}