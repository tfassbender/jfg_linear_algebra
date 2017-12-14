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
	}
	
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
			return solution;
		}
		catch (GaussCalculationException gce) {
			throw new LinearAlgebraException("The cross-point could not be calculated.", gce);
		}
	}
	
	public Line3D getIntersectionLine(Plane3D plane) throws LinearAlgebraException {
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
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.ROW, direction1, direction2, plane.direction1.mult(-1), plane.direction2.mult(-1));
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
	}
	
	public boolean isOnPlane(Line3D line) {
		return isOnPlane(line.start) && Vector3D.isLinearlyDependentVectors(line.direction, direction1, direction2);
	}
	
	public boolean isOnPlane(Vector3D point) {
		//create an equation system and solve it to check if the point is on the line
		//create a 2x2 system to calculate t1 and t2
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.ROW, new double[] {direction1.x, direction1.y}, new double[] {direction2.x, direction2.y});
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
		}
	}
	
	public Vector3D getNormalVector() {
		return direction1.cross(direction2);
	}
}