package net.jfabricationgames.linear_algebra;

import java.util.Arrays;
import java.util.List;

/**
 * An area identified by a list of (ordered) points.
 */
public class Area2D {
	
	protected List<Vector2D> points;

	public Area2D() {
		
	}
	public Area2D(List<Vector2D> points) {
		this.points = points;
	}
	public Area2D(Vector2D... points) {
		this(Arrays.asList(points));
	}
	
	public List<Vector2D> getPoints() {
		return points;
	}
	public void setPoints(List<Vector2D> points) {
		this.points = points;
	}
	
	/**
	 * Check whether the point p is in the polygon build of the points in polygon.
	 * All points are given as java.awt.geom.Point2D.Double.
	 * 
	 * @param p
	 * 		The point to be checked
	 * 
	 * @param polygon
	 * 		The edge points of the polygon as a list
	 * 
	 * @return
	 * 		Returns true if p is inside of the polygon. False otherwise.
	 * 
	 * @throws GeometricalException
	 * 		A GeometricalException is thrown if the answer couldn't be calculated.
	 */
	public boolean isPointInArea(Vector2D p) throws LinearAlgebraException {
		Vector2D start = p.clone();
		Vector2D vec;//a vector in any direction
		int crossPoints = 0;//count the number of lines the vector from the point crosses
		int triesLeft = 100;//if you can't find an answer after 100 tests interrupt
		boolean foundAnswer;
		for (int i = 0; i < triesLeft; i++) {
			vec = new Vector2D(Math.random()*90 + 10, Math.random()*90 + 10);//a vector in any direction
			foundAnswer = true;
			for (int j = 0; j < points.size(); j++) {
				Vector2D polygonPoint2 = points.get((j+1)%points.size());
				Vector2D polygonLineStart = points.get(j).clone();
				Vector2D polygonLineVec = new Vector2D(polygonPoint2.x - polygonLineStart.x, polygonPoint2.y - polygonLineStart.y);
				Vector2D crossPoint = new Line2D(start, vec).calculateCrosspoint(new Line2D(polygonLineStart, polygonLineVec));
				if (crossPoint.x == Double.NaN || crossPoint.y == Double.NaN) {
					foundAnswer = false;
				}
				else {
					if (crossPoint.x >= Math.min(polygonLineStart.x, polygonPoint2.x) && crossPoint.x <= Math.max(polygonLineStart.x, polygonPoint2.x) &&
							crossPoint.y >= Math.min(polygonLineStart.y, polygonPoint2.y) && crossPoint.y <= Math.max(polygonLineStart.y, polygonPoint2.y)) {
						if (Math.signum(vec.x) < 0) {
							if (Math.signum(vec.y) < 0) {
								if (crossPoint.x < p.x && crossPoint.y < p.y) {
									crossPoints++;
								}
							}
							else {
								if (crossPoint.x < p.x && crossPoint.y > p.y) {
									crossPoints++;
								}
							}
						}
						else {
							if (Math.signum(vec.y) < 0) {
								if (crossPoint.x > p.x && crossPoint.y < p.y) {
									crossPoints++;
								}
							}
							else {
								if (crossPoint.x > p.x && crossPoint.y > p.y) {
									crossPoints++;
								}
							}
						}
					}
				}
			}
			if (foundAnswer) {
				return crossPoints % 2 == 1;
			}
		}
		throw new LinearAlgebraException("Calculation failed. No result found");
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