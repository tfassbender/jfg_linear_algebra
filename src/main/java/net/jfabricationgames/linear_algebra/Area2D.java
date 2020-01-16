package net.jfabricationgames.linear_algebra;

import java.util.Arrays;
import java.util.List;

/**
 * An area identified by a list of (ordered) points.
 */
public class Area2D {
	
	protected List<Vector2D> points;
	
	protected boolean convex;
	
	public Area2D(List<Vector2D> points) {
		this.points = points;
		//don't check because circle is a subclass with one point.
		/*if (points == null || points.size() < 3) {
			throw new LinearAlgebraException("Areas have at least 3 points.");
		}*/
		convex = isConvex();
	}
	public Area2D(Vector2D... points) {
		this(Arrays.asList(points));
	}
	
	public List<Vector2D> getPoints() {
		return points;
	}
	
	/**
	 * Get the size of the area.
	 * This will only give the right result if the area is a convex polygon.
	 * 
	 * @return
	 * 		The size of this area.
	 * 
	 * @throws LinearAlgebraException
	 * 		A {@link LinearAlgebraException} is thrown when the area is no convex polygon.
	 */
	public double getSize() throws LinearAlgebraException {
		//https://en.wikipedia.org/wiki/Polygon#Area_and_centroid
		if (!convex) {
			throw new LinearAlgebraException("The polygon area is not convex. This algorithm doesn't work for this area.");
		}
		double size = 0;
		int n = points.size();
		for (int i = 0; i < points.size(); i++) {
			size += points.get(i).x * points.get((i+1)%n).y - points.get((i+1)%n).x * points.get(i).y;
		}
		size *= 0.5;
		return size;
	}
	
	/**
	 * Check whether this polygon is convex.
	 * 
	 * @return
	 * 		True if this polygon area is convex. False otherwise.
	 */
	public boolean isConvex() {
		//https://stackoverflow.com/questions/471962/how-do-determine-if-a-polygon-is-complex-convex-nonconvex
		if (points.size() < 4) {
			return true;
		}
		boolean sign = false;
		int n = points.size();
		for (int i = 0; i < n; i++) {
			double dx1 = points.get((i + 2) % n).x - points.get((i + 1) % n).x;
			double dy1 = points.get((i + 2) % n).y - points.get((i + 1) % n).y;
			double dx2 = points.get(i).x - points.get((i + 1) % n).x;
			double dy2 = points.get(i).y - points.get((i + 1) % n).y;
			double zcrossproduct = dx1 * dy2 - dy1 * dx2;
			if (i == 0) {
				sign = zcrossproduct > 0;
			}
			else if (sign != (zcrossproduct > 0)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Check whether the point p is in the polygon build of the points in polygon.
	 * This algorithm will only work for convex areas (which is tested first in the algorithm).
	 * 
	 * @param p
	 * 		The point to be checked
	 * 
	 * @return
	 * 		Returns true if p is inside of the polygon. False otherwise.
	 * 
	 * @throws LinearAlgebraException
	 * 		A {@link LinearAlgebraException} is thrown if the area is not convex.
	 */
	public boolean isPointInArea(Vector2D p) throws LinearAlgebraException {
		double size = getSize();
		double triangleAreaSum = 0;//sum up the areas from the point to the area points
		int n = points.size();
		for (int i = 0; i < points.size(); i++) {
			triangleAreaSum += calculateTriangleArea(p, points.get(i), points.get((i+1)%n));
		}
		return Math.abs(size - triangleAreaSum) <= 1e-8;
	}
	
	/**
	 * Check if one of the tested points is in the way from start to end (using a given width).
	 * 
	 * @param start
	 * 		The start point.
	 * 
	 * @param end
	 * 		The end point.
	 * 
	 * @param width
	 * 		The rectangle width.
	 * 
	 * @param checkedPoints
	 * 		The points that are tested.
	 * 
	 * @return
	 * 		True if one of the points is in the rectangle. False otherwise.
	 */
	public static boolean pointInWay(Vector2D start, Vector2D end, double width, Vector2D... checkedPoints) {
		Vector2D[] rectangleLimits = calculateWayRectangle(start, end, width);
		for (int i = 0; i < checkedPoints.length; i++) {
			double triangleAreaSum = 0;
			triangleAreaSum += calculateTriangleArea(rectangleLimits[0], checkedPoints[i], rectangleLimits[3]);
			triangleAreaSum += calculateTriangleArea(rectangleLimits[3], checkedPoints[i], rectangleLimits[2]);
			triangleAreaSum += calculateTriangleArea(rectangleLimits[2], checkedPoints[i], rectangleLimits[1]);
			triangleAreaSum += calculateTriangleArea(checkedPoints[i], rectangleLimits[1], rectangleLimits[0]);
			double rectangleArea = calculateTriangleArea(rectangleLimits[0], rectangleLimits[1], rectangleLimits[2]) + calculateTriangleArea(rectangleLimits[0], rectangleLimits[3], rectangleLimits[2]);
			if (triangleAreaSum - 1 < rectangleArea) {
				return true;
			}
		}
		return false;
	}
	/**
	 * Check whether a point is in a rectangle.
	 * 
	 * @param rectangleLimits
	 * 		The points of the rectangle.
	 * 
	 * @param point
	 * 		The checked point.
	 * 
	 * @return
	 * 		True if the point is in the rectangle. False otherwise.
	 */
	public static boolean isPointInRectangle(Vector2D[] rectangleLimits, Vector2D point) {
		double triangleAreaSum = 0;
		triangleAreaSum += calculateTriangleArea(rectangleLimits[0], point, rectangleLimits[3]);
		triangleAreaSum += calculateTriangleArea(rectangleLimits[3], point, rectangleLimits[2]);
		triangleAreaSum += calculateTriangleArea(rectangleLimits[2], point, rectangleLimits[1]);
		triangleAreaSum += calculateTriangleArea(point, rectangleLimits[1], rectangleLimits[0]);
		double rectangleArea = calculateTriangleArea(rectangleLimits[0], rectangleLimits[1], rectangleLimits[2]) + calculateTriangleArea(rectangleLimits[0], rectangleLimits[3], rectangleLimits[2]);
		if (triangleAreaSum - 1 < rectangleArea) {
			return true;
		}
		return false;
	}
	/**
	 * Calculate a rectangle for distance between two points with a given width.
	 * 
	 * @param pos
	 * 		The start point.
	 * 
	 * @param target
	 * 		The end point.
	 * 
	 * @param width
	 * 		The rectangle width (to both sides).
	 * 
	 * @return
	 * 		A Vector2D-Array with the rectangle points.
	 */
	public static Vector2D[] calculateWayRectangle(Vector2D start, Vector2D end, double width) {
		Vector2D[] rectangleLimits = new Vector2D[4];
		Vector2D posToTarget = start.vectorTo(end).setLength(width);
		rectangleLimits[0] = start.add(posToTarget.rotate(-90));
		rectangleLimits[1] = end.add(posToTarget.rotate(-90));
		rectangleLimits[2] = end.add(posToTarget.rotate(90));
		rectangleLimits[3] = start.add(posToTarget.rotate(90));
		return rectangleLimits;
	}
	
	/**
	 * Calculate the size of a triangle.
	 * 
	 * @param points
	 * 		The triangle points.
	 * 
	 * @return
	 * 		The size of the triangle.
	 */
	public static double calculateTriangleArea(Vector2D... points) {
		double[] lens = new double[] {Math.hypot(points[0].x - points[1].x, points[0].y - points[1].y), 
				Math.hypot(points[1].x - points[2].x, points[1].y - points[2].y), 
				Math.hypot(points[2].x - points[0].x, points[2].y - points[0].y)};
		double s = (lens[0] + lens[1] + lens[2]) / 2;
		return Math.sqrt(s * (s - lens[0]) * (s - lens[1]) * (s - lens[2]));
	}
}