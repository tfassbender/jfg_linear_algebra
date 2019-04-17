package net.jfabricationgames.linear_algebra;

import net.jfabricationgames.linear_algebra.Gauss.GaussCalculationException;

public class Plane3D {
	
	protected Vector3D start;
	protected Vector3D direction1;
	protected Vector3D direction2;
	
	public Plane3D(Vector3D start, Vector3D direction1, Vector3D direction2) {
		this.start = start;
		this.direction1 = direction1;
		this.direction2 = direction2;
		if (direction1.equals(Vector3D.NULL_VEC) || direction2.equals(Vector3D.NULL_VEC)) {
			throw new LinearAlgebraException("A Plane's direction vector can't be a null-vector.");
		}
	}
	
	/**
	 * Calculate the intersection point of a Line3D with this plane.
	 * 
	 * @param line
	 * 		The line that may intersects this plane.
	 * 
	 * @return
	 * 		The intersection point as Vector3D if there is one.
	 * 		If there is no intersection point because the line is parallel to the plane null is returned.
	 * 		If there are infinite intersection points a {@link LinearAlgebraException} is thrown.
	 * 
	 * @throws LinearAlgebraException
	 * 		A {@link LinearAlgebraException} is thrown when the line lies in the plane (so there are infinite crossing points) or the calculation fails for some reason.
	 */
	public Vector3D getIntersectionPoint(Line3D line) throws LinearAlgebraException {
		//check whether the line is parallel to the plane
		if (Vector3D.isLinearlyDependentVectors(line.direction, direction1, direction2)) {
			if (isOnPlane(line.start)) {
				//line is in the plane
				throw new LinearAlgebraException("The line is in the plane.");
			}
			else {
				//the line is parallel to the plane
				return null;				
			}
		}
		//solve a gauss system to find the cross-point
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.COL, direction1, direction2, line.direction.mult(-1));
		double[] b = line.start.sub(start).asArray();
		try {
			Gauss gauss = Gauss.calculateGauss(m, b);
			Vector3D solution = new Vector3D(gauss.getXVec());
			return line.start.add(line.direction.mult(solution.z));
		}
		catch (GaussCalculationException gce) {
			throw new LinearAlgebraException("The cross-point could not be calculated.", gce);
		}
	}
	
	/**
	 * Calculate the distance from this plane to a point.
	 * 
	 * @param point
	 * 		The point to which the distance is calculated.
	 * 
	 * @return
	 * 		The distance to the point.
	 */
	public double getDistance(Vector3D point) {
		//create a line from the point with the normal vector as direction
		Line3D line = new Line3D(point, getNormalVector());
		//calculate the cross-point of the line and this plane (there must be one)
		Vector3D intersection = getIntersectionPoint(line);
		//the distance is the distance between the intersection point and the point
		return point.distance(intersection);
	}
	
	/**
	 * Calculate the intersection line of a Plane3D with this plane.
	 * 
	 * @param plane
	 * 		The other plane.
	 * 
	 * @return
	 * 		The intersection line if there is one.
	 * 		If the planes are parallel null is returned.
	 * 		If the planes are the same a {@link LinearAlgebraException} is thrown.
	 * 
	 * @throws LinearAlgebraException
	 * 		A {@link LinearAlgebraException} is thrown when the planes are the same or if the calculation isn't successful for some reason.
	 */
	/*public Line3D getIntersectionLine(Plane3D plane) throws LinearAlgebraException {
		//check whether the planes are parallel or the same (using the normal-vectors...)
		if (getNormalVector().isLinearlyDependent(plane.getNormalVector())) {
			//parallel or same
			if (isOnPlane(plane.start)) {
				//same planes
				throw new LinearAlgebraException("The planes are the same.");
			}
			else {
				return null;
			}
		}
		//solve a underdetermined equation system with the gauss algorithm
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.COL, direction1, direction2, plane.direction1.mult(-1), plane.direction2.mult(-1));
		double[] b = plane.start.sub(start).asArray();
		try {
			Gauss gauss = Gauss.calculateGauss(m, b);
			double[][] solutions = gauss.getSolutions();
			Line3D line = new Line3D(new Vector3D(solutions[0]), new Vector3D(solutions[1]));
			return line;
		}
		catch (GaussCalculationException gce) {
			throw new LinearAlgebraException("The intersection line could not be calculated", gce);
		}
	}*/
	
	/**
	 * Check whether a Line3D is in the plane.
	 * 
	 * @param line
	 * 		The line that is checked.
	 * 
	 * @return
	 * 		True if the line lies in this plane. False otherwise.
	 */
	public boolean isOnPlane(Line3D line) {
		return isOnPlane(line.start) && Vector3D.isLinearlyDependentVectors(line.direction, direction1, direction2);
	}
	
	/**
	 * Check whether a point is on this plane.
	 * 
	 * @param point
	 * 		The point that is checked.
	 * 
	 * @return
	 * 		True if the point is on this plane. False otherwise.
	 */
	public boolean isOnPlane(Vector3D point) {
		//create an equation system and solve it to check if the point is on the line
		//create a 2x2 system to calculate t1 and t2
		/*Matrix2D m = new Matrix2D(Matrix2D.Orientation.ROW, new double[] {direction1.x, direction1.y}, new double[] {direction2.x, direction2.y});
		double[] b = new double[] {point.x - start.x, point.y - start.y};
		Gauss gauss = Gauss.calculateGauss(m, b);
		try {
			double[] x = gauss.getXVec();
			if (x == null) {
				//there is no solution
				return false;
			}
			//if x can be calculated it holds t1 and t2 -> insert in the third equation and check if its true
			if ((x[0] * direction1.z - x[1] * direction2.z) - (point.z - start.z) < 1e-5) {
				//the third equation results in a true statement -> the point is on the plane
				return true;
			}
			else {
				//the third equation results in a false statement -> the point is not on the plane
				return false;
			}
		}
		catch (GaussCalculationException gce) {
			//the system can't be calculated -> the point is not on the plane
			return false;
		}*/
		
		//just solve a equation system
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.COL, direction1, direction2);
		double[] b = point.sub(start).asArray();
		Gauss gauss = Gauss.calculateGauss(m, b);
		try {
			double[][] solutions = gauss.getSolutions();
			return solutions != null;
		}
		catch (LinearAlgebraException lae) {
			return false;
		}
	}
	
	/**
	 * Get the normal vector of this plane.
	 * 
	 * @return
	 * 		The normal vector.
	 */
	public Vector3D getNormalVector() {
		return direction1.cross(direction2);
	}
}