package net.jfabricationgames.linear_algebra;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class Triangle3DTest {
	
	private static final double EPSILON = 1e-8;
	
	@Test
	public void testIntersectsUnchecked() {
		Triangle3D triangleXY = new Triangle3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 1, 0));
		
		LineSegment3D lineDown = new LineSegment3D(new Vector3D(0.1, 0.1, 1), new Vector3D(0, 0, -1));//intersects the triangleXY
		LineSegment3D lineUp = new LineSegment3D(new Vector3D(0.1, 0.1, 1), new Vector3D(0, 0, 1));//doesn't intersect because of the direction
		LineSegment3D lineFarAway = new LineSegment3D(new Vector3D(25, 25, 1), new Vector3D(0, 0, -1));//doesn't intersect because it's far away (but intersects the plane)
		
		assertTrue(triangleXY.intersectsUnchecked(lineDown));
		assertFalse(triangleXY.intersectsUnchecked(lineUp));
		assertFalse(triangleXY.intersectsUnchecked(lineFarAway));
	}
	
	@Test
	public void testGetIntersectionPointUnchecked() {
		Triangle3D triangleXY = new Triangle3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 1, 0));
		
		LineSegment3D lineDown = new LineSegment3D(new Vector3D(0.1, 0.1, 1), new Vector3D(0, 0, -1));//intersects the triangleXY
		LineSegment3D lineUp = new LineSegment3D(new Vector3D(0.1, 0.1, 1), new Vector3D(0, 0, 1));//doesn't intersect because of the direction
		
		assertArrayEquals(new double[] {0.1, 0.1, 0}, triangleXY.getIntersectionPointUnchecked(lineDown).asArray(), EPSILON);
		assertNull(triangleXY.getIntersectionPointUnchecked(lineUp));
	}
}