package net.jfabricationgames.linear_algebra;

import java.util.Arrays;
import java.util.List;

public class Kite2D extends Area2D {
	
	public Kite2D(Vector2D... points) {
		this(Arrays.asList(points));
	}
	public Kite2D(List<Vector2D> points) throws LinearAlgebraException {
		super(points);
		if (points.size() != 4 || points.get(0) == null || points.get(1) == null || points.get(2) == null || points.get(3) == null) {
			throw new LinearAlgebraException("Diamonds have 4 points and no NULL point!");
		}
		else if (!correctAngles()) {
			throw new LinearAlgebraException("The given points don't form a kite.");
		}
	}
	
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
		//two opposite angles are equal
		return Math.abs(angles[0] - angles[2]) < 1e-5 || Math.abs(angles[1] - angles[3]) < 1e-5;
	}
	
	/**
	 * Calculate the center of gravity point of the kite.
	 * If the kite is convex it's the point in which the two axes cross.
	 * If the kite is non-convex it's one of the points of the kite (the one that makes it non-convex).
	 * 
	 * @return
	 * 		The main emphasis of the kite as a Vector2D.
	 */
	public Vector2D getCenterOfGravity() {
		//calculate the angles in all points (not all are inner angles if it's not convex)
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
		Vector2D centerOfGravity;
		if (convex) {
			//find the points with equal angles; the main emphasis of the kite is the middle between them
			if (Math.abs(angles[0]-angles[2]) < 1e-8) {
				centerOfGravity = points.get(0).add(points.get(0).vectorTo(points.get(2)).mult(0.5));
			}
			else {
				centerOfGravity = points.get(1).add(points.get(1).vectorTo(points.get(3)).mult(0.5));
			}
		}
		else {
			//the main emphasis is the point that makes the kite non-convex (the one with the smaller angle)
			if (Math.abs(angles[0]-angles[2]) < 1e-8) {
				if (angles[1] < angles[3]) {
					centerOfGravity = points.get(3);
				}
				else {
					centerOfGravity = points.get(1);
				}
			}
			else {
				if (angles[0] < angles[2]) {
					centerOfGravity = points.get(2);
				}
				else {
					centerOfGravity = points.get(0);
				}
			}
		}
		return centerOfGravity;
	}
}