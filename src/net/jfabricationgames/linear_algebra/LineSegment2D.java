package net.jfabricationgames.linear_algebra;

public class LineSegment2D extends Line2D {
	
	public LineSegment2D(Vector2D start, Vector2D direction) {
		super(start, direction);
	}
	
	/**
	 * Calculate the crossing point of two line segments (or this line segment and a line).
	 * 
	 * @param line
	 * 		The line which's crossing-point with this line is calculated.
	 * 
	 * @return
	 * 		The crossing point of the two lines (if there is one).
	 */
	@Override
	public Vector2D calculateCrosspoint(Line2D line) {
		if (line instanceof LineSegment2D) {
			return calculateCrosspoint(line);
		}
		Vector2D crosspoint = super.calculateCrosspoint(line);
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
	 * 		The line which's crossing-point with this line is calculated.
	 * 
	 * @return
	 * 		The crossing point of the two line segments (if there is one).
	 */
	public Vector2D calculateCrosspoint(LineSegment2D line) {
		Vector2D crosspoint = super.calculateCrosspoint(line);
		double t = calculateT(crosspoint);
		double t2 = line.calculateT(crosspoint);
		if (t >= 0 && t <= 1 && t2 >= 0 && t2 <= 1) {
			return crosspoint;
		}
		return null;
	}
	
	/**
	 * Check whether a point is on this line.
	 * 
	 * @param point
	 * 		The checked point.
	 * 
	 * @return
	 * 		True if the point is on the line. False otherwise.
	 */
	@Override
	public boolean isPointOnLine(Vector2D point) {
		//the calculation of the parameter t is equal in all cases (x and y).
		double t1 = direction.x == 0 ? 0 : -((start.x - point.x) / direction.x);
		double t2 = direction.y == 0 ? 0 : -((start.y - point.y) / direction.y);
		if (direction.x == 0) {
			return Math.abs(start.x - point.x) < 1e-8 && t2 >= 0 && t2 <= 1;
		}
		else if (direction.y == 0) {
			return Math.abs(start.y - point.y) < 1e-8 && t1 >= 0 && t1 <= 1;
		}
		return Math.abs(t1-t2) < 1e-5 && t1 >= 0 && t1 <= 1;
	}
	
	/**
	 * Calculate the minimum distance between a point and this line.
	 * 
	 * @param point
	 * 		The point.
	 * 
	 * @return
	 * 		The minimum distance between the point and this line.
	 */
	@Override
	public double calculateMinDistToPoint(Vector2D point) {
		//project the vector from a point of the line on the line; then calculate the distance between the two points
		Vector2D toPoint = start.vectorTo(point);
		Vector2D projected = direction.project(toPoint);
		Vector2D p2 = start.add(projected);
		if (isPointOnLine(p2)) {
			//the projected point is one the line
			return point.distance(p2);
		}
		else {
			//the shortest distance is from one of the edges
			return Math.min(start.distance(point), start.add(direction).distance(point));
		}
    }
}