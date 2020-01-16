package net.jfabricationgames.linear_algebra;

public class LineSegment3D extends Line3D {
	
	public LineSegment3D(Vector3D start, Vector3D direction) {
		super(start, direction);
	}
	
	/**
	 * Calculate the crossing point of two line segments (or this line segment and a line).
	 * 
	 * @param line
	 *        The line which's crossing-point with this line is calculated.
	 * 
	 * @return The crossing point of the two lines (if there is one).
	 */
	@Override
	public Vector3D calculateCrosspoint(Line3D line) {
		if (line instanceof LineSegment3D) {
			return calculateCrosspoint((LineSegment3D) line);
		}
		Vector3D crosspoint = super.calculateCrosspointUnchecked(line);
		double t = calculateT(crosspoint);
		if (t >= 0 && t <= 1) {
			return crosspoint;
		}
		return null;
	}
	/**
	 * Calculate the crossing point of two line segments.
	 * 
	 * @param line
	 *        The line which's crossing-point with this line is calculated.
	 * 
	 * @return The crossing point of the two line segments (if there is one).
	 */
	public Vector3D calculateCrosspoint(LineSegment3D line) {
		Vector3D crosspoint = super.calculateCrosspointUnchecked(line);
		
		if (crosspoint != null) {
			double t = calculateT(crosspoint);
			double t2 = line.calculateT(crosspoint);
			if (t >= 0 && t <= 1 && t2 >= 0 && t2 <= 1) {
				return crosspoint;
			}
		}
		
		return null;
	}
	
	/**
	 * Check whether a point is on this line segment.
	 * 
	 * @param point
	 *        The checked point.
	 * 
	 * @return True if the point is on the line. False otherwise.
	 */
	@Override
	public boolean isPointOnLine(Vector3D point) {
		if (super.isPointOnLine(point)) {
			double t = calculateTUnchecked(point);
			return !Double.isNaN(t) && t >= 0;
		}
		return false;
	}
	
	/**
	 * Calculate the parameter t without checking whether the point is on the line.
	 */
	private double calculateTUnchecked(Vector3D point) {
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
}