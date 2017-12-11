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
		calculateSize();
	}
	
	public Vector2D getMiddle() {
		//find points across of each other
		Vector2D p1 = points.get(0);
		Vector2D p2 = points.get(1);
		Vector2D p3 = points.get(2);
		Vector2D p4 = points.get(3);
		Vector2D crossLine;
		double len;
		double angle_p1p2 = p1.vectorTo(p2).getAngle() % 180;
		double angle_p3p4 = p3.vectorTo(p4).getAngle() % 180;
		if (angle_p1p2 - angle_p3p4 < 1e-3) {
			//p1 and p4 or p2 and p3
			crossLine = p1.vectorTo(p4);
			len = crossLine.length();
			return p1.add(new Vector2D(crossLine.x/len, crossLine.y/len));
		}
		else {
			//p1 and p2 or p3 and p4
			crossLine = p1.vectorTo(p2);
			len = crossLine.length();
			return p1.add(new Vector2D(crossLine.x/len, crossLine.y/len));
		}
	}
	
	public Vector2D[] getSpanVectors() {
		//find vectors with the same direction
		Vector2D p1 = points.get(0);
		Vector2D p2 = points.get(1);
		Vector2D p3 = points.get(2);
		Vector2D p4 = points.get(3);
		double angle_p1p2 = p1.vectorTo(p2).getAngle();
		double angle_p3p4 = p3.vectorTo(p4).getAngle();
		Vector2D[] span = new Vector2D[2];
		if (angle_p1p2 - angle_p3p4 < 1e-3) {
			span[0] = p1.vectorTo(p2);
			span[1] = p1.vectorTo(p3);
		}
		else {
			angle_p1p2 %= 180;
			angle_p3p4 %= 180;
			//check for opposite directions
			if (angle_p1p2 - angle_p3p4 < 1e-3) {
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
		size =  Math.abs(span[0].x*span[1].y - span[1].x*span[0].y);
	}
	
	public double getSize() {
		return size;
	}
}