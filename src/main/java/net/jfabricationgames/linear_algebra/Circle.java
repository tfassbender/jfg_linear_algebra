package net.jfabricationgames.linear_algebra;

public class Circle extends Area2D {
	
	protected double radius;
	
	public Circle(Vector2D middle, double radius) {
		super(middle);
		this.radius = radius;
		if (middle == null || radius <= 0) {
			throw new LinearAlgebraException("A circle needs a middle point and a radius > 0.");
		}
	}
	
	@Override
	public boolean isPointInArea(Vector2D p) {
		return getMiddle().distance(p) <= radius;
	}
	
	/**
	 * Calculate the intersection points of two circles. 
	 * Doesn't calculate precise. Epsilon = 1e-3 (for double inaccuracy).
	 * 
	 * @param circle
	 * 		The other circle which's intersection with this circle is calculated.
	 * 
	 * @return
	 * 		null -> there is no solution
	 * 		empty Vector2D array (Vector2D[0]) -> circles are equal (infinite points)
	 * 		single Point (as Vector2D array) -> one intersection point
	 * 		two Points (as Vector2D array) -> two intersection points
	 */
	public Vector2D[] getIntercectionPoints(Circle circle) {
		double epsilon = 1e-3;//epsilon for double inaccuracy
		Vector2D m1 = getMiddle();
		Vector2D m2 = circle.getMiddle();
		double r1 = radius;
		double r2 = circle.getRadius();
		if (m1.distance(m2) > r1+r2 + epsilon) {
			//no solution
			return null;
		}
		else if (m1.distance(m2) < epsilon) {
			//circles have the same mid point
			if (Math.abs(r1-r2) < epsilon) {
				//circles are equal
				return new Vector2D[0];				
			}
			else {
				//no solution
				return null;
			}
		}
		else if (Math.abs(m1.distance(m2) - (r1+r2)) < epsilon) {
			//one point solution
			return new Vector2D[] {m1.vectorTo(m2).setLength(r1)};
		}
		else {
			//two point solution
			double c = Math.sqrt(Math.pow(m2.x-m1.x, 2) + Math.pow(m2.y - m1.y, 2));
			double x = (Math.pow(r1, 2) + Math.pow(c, 2) - Math.pow(r2, 2)) / (2*c);
			double y = Math.sqrt(Math.pow(r1, 2) - Math.pow(x, 2));
			double p1x = m1.x + (x * (m2.x - m1.x) / c) - (y * (m2.y - m1.y)/c); 
			double p1y = m1.y + (x * (m2.y - m1.y) / c) + (y * (m2.x - m1.x)/c);
			double p2x = m1.x + (x * (m2.x - m1.x) / c) + (y * (m2.y - m1.y)/c); 
			double p2y = m1.y + (x * (m2.y - m1.y) / c) - (y * (m2.x - m1.x)/c);
			if (Double.isNaN(x) || Double.isNaN(y)) {
				return null;
			}
			return new Vector2D[] {new Vector2D(p1x, p1y), new Vector2D(p2x, p2y)};
		}
	}
	
	/**
	 * Calculate an approximative intersecting area of two circles as a diamond.
	 * 
	 * @param circle
	 * 		The circle which's intersection with this circle is calculated.
	 * 
	 * @return
	 * 		The approximative intersecting area of the circles or null if they don't intersect.
	 */
	public Kite2D calculateIntersectingKite(Circle circle) {
		Vector2D m1 = getMiddle();
		Vector2D m2 = circle.getMiddle();
		double r1 = radius;
		double r2 = circle.getRadius();
		Vector2D[] intersectionPoints = getIntercectionPoints(circle);
		if (intersectionPoints != null && intersectionPoints.length == 2) {
			//intersecting circles
			Vector2D[] diamondPoints = new Vector2D[2];
			diamondPoints[0] = m1.add(m1.vectorTo(m2).setLength(r1));
			diamondPoints[1] = m2.add(m2.vectorTo(m1).setLength(r2));
			Kite2D kite = new Kite2D(intersectionPoints[0], diamondPoints[0], intersectionPoints[1], diamondPoints[1]);
			return kite;
		}
		else if (intersectionPoints != null && intersectionPoints.length == 0) {
			//circles are the same
			Kite2D kite = new Kite2D(m1.add(new Vector2D(1, 0).setLength(r1)), m1.add(new Vector2D(0, 1).setLength(r1)), 
					m1.add(new Vector2D(-1, 0).setLength(r1)), m1.add(new Vector2D(0, -1).setLength(r1)));
			return kite;
		}
		return null;
	}
	
	public Vector2D getMiddle() {
		return points.get(0);
	}
	public double getRadius() {
		return radius;
	}
}