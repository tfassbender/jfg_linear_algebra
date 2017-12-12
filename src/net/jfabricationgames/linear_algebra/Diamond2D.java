package net.jfabricationgames.linear_algebra;

import java.util.Arrays;
import java.util.List;

public class Diamond2D extends Area2D {
	
	protected double size;
	
	public Diamond2D(Vector2D... points) {
		this(Arrays.asList(points));
	}
	public Diamond2D(List<Vector2D> points) throws LinearAlgebraException {
		super(points);
		if (points.size() != 4 || points.get(0) == null || points.get(1) == null || points.get(2) == null || points.get(3) == null) {
			throw new LinearAlgebraException("Diamonds have 4 points and no NULL point!");
		}
		if (!isConvex() || !(correctAngles())) {
			throw new LinearAlgebraException("The given points don't form a Diamond.");
		}
		calculateSize();
	}
	
	/**
	 * Check whether the given points form a diamond.
	 */
	private boolean correctAngles() {
		double[] angles = new double[4];
		int n = angles.length;
		for (int i = 1; i < angles.length+1; i++) {
			Vector2D p = points.get(i%n);
			Vector2D p2 = points.get((i+1)%n);
			Vector2D p3 = points.get((i-1)%n);
			Vector2D v1 = p.vectorTo(p2);
			Vector2D v2 = p.vectorTo(p3);
			angles[i % n] = v1.getAngleDeltaTo(v2);
		}
		//the opposite angles are equal
		return Math.abs(angles[0] - angles[2]) < 1e-5 && Math.abs(angles[1] - angles[3]) < 1e-5;
	}
	
	/**
	 * Get the middle point of this diamond.
	 * 
	 * @return
	 * 		The middle point as a Vector2D.
	 */
	public Vector2D getMiddle() {
		//find points across of each other
		Vector2D p1 = points.get(0);
		Vector2D p2 = points.get(1);
		Vector2D p3 = points.get(2);
		Vector2D p4 = points.get(3);
		Vector2D crossLine;
		double angle_p1p2 = p1.vectorTo(p2).getAngle() % 180;
		double angle_p3p4 = p3.vectorTo(p4).getAngle() % 180;
		if (Math.abs(angle_p1p2 - angle_p3p4) < 1e-3) {
			//p1 and p3 or p2 and p4
			crossLine = p1.vectorTo(p3);
			return p1.add(crossLine.mult(0.5));
		}
		else {
			//p1 and p2 or p3 and p4
			crossLine = p1.vectorTo(p2);
			return p1.add(crossLine.mult(0.5));
		}
	}
	
	/**
	 * Get two span vectors from the first point. 
	 * 
	 * @return
	 * 		The span vectors as an array.
	 */
	public Vector2D[] getSpanVectors() {
		//find vectors with the same direction
		Vector2D p1 = points.get(0);
		Vector2D p2 = points.get(1);
		Vector2D p3 = points.get(2);
		Vector2D p4 = points.get(3);
		double angle_p1p2 = p1.vectorTo(p2).getAngle();
		double angle_p3p4 = p3.vectorTo(p4).getAngle();
		Vector2D[] span = new Vector2D[2];
		if (Math.abs(angle_p1p2 - angle_p3p4) < 1e-5) {
			span[0] = p1.vectorTo(p2);
			span[1] = p1.vectorTo(p3);
		}
		else {
			angle_p1p2 %= 180;
			angle_p3p4 %= 180;
			//check for opposite directions
			if (angle_p1p2 - angle_p3p4 < 1e-5) {
				span[0] = p1.vectorTo(p2);
				span[1] = p1.vectorTo(p4);
			}
			else {
				span[0] = p1.vectorTo(p3);
				span[1] = p1.vectorTo(p4);
			}
		}
		return span;
	}
	
	private void calculateSize() {
		Vector2D[] span = getSpanVectors();
		size = Math.abs(span[0].x*span[1].y - span[1].x*span[0].y);
	}
	
	public double getSize() {
		return size;
	}
}