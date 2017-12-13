package net.jfabricationgames.linear_algebra;

import java.util.Arrays;
import java.util.List;

public class Triangle2D extends Area2D {
	
	public Triangle2D(Vector2D... points) {
		this(Arrays.asList(points));
	}
	public Triangle2D(List<Vector2D> points) throws LinearAlgebraException {
		super(points);
		if (points.size() != 3) {
			throw new LinearAlgebraException("Triangles have three points. You used " + points.size());
		}
	}
	
	/**
	 * Calculate the size of this triangle.
	 * 
	 * @return
	 * 		The size of this triangle.
	 */
	public double getSize() {
		Vector2D p1 = points.get(0);
		Vector2D p2 = points.get(1);
		Vector2D p3 = points.get(2);
		double[] lens = new double[] {Math.hypot(p1.x - p2.x, p1.y - p2.y), Math.hypot(p2.x - p3.x, p2.y - p3.y), Math.hypot(p3.x - p1.x, p3.y - p1.y)};
		double s = (lens[0] + lens[1] + lens[2]) / 2;
		return Math.sqrt(s * (s - lens[0]) * (s - lens[1]) * (s - lens[2]));
	}
}