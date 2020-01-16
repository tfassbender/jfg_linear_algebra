package net.jfabricationgames.linear_algebra;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LineSegment3DTest {
	
	public static final double EPSILON = 1e-8;
	
	@Test
	public void testIsPointOnLine() {
		LineSegment3D line = new LineSegment3D(new Vector3D(0, 0, 0), new Vector3D(1, 1, 1));
		LineSegment3D line2 = new LineSegment3D(new Vector3D(1, 1, 0), new Vector3D(1, 0, 0));
		
		assertTrue(line.isPointOnLine(new Vector3D(0, 0, 0)));
		assertTrue(line.isPointOnLine(new Vector3D(1, 1, 1)));
		assertFalse(line.isPointOnLine(new Vector3D(1, 2, 3)));
		assertFalse(line.isPointOnLine(new Vector3D(0, 0, 1)));
		assertFalse(line.isPointOnLine(new Vector3D(-1, -1, -1)));
		
		assertTrue(line2.isPointOnLine(new Vector3D(1, 1, 0)));
		assertTrue(line2.isPointOnLine(new Vector3D(5, 1, 0)));
		assertFalse(line2.isPointOnLine(new Vector3D(5, 2, 0)));
		assertFalse(line2.isPointOnLine(new Vector3D(0, 0, 0)));
		assertFalse(line.isPointOnLine(new Vector3D(0, 1, 0)));
	}
	
	@Test
	public void testCalculateCrosspoint() {
		LineSegment3D line = new LineSegment3D(new Vector3D(0, 0, 0), new Vector3D(1, 1, 1));
		LineSegment3D line2 = new LineSegment3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0));//crossing line1 in (0|0|0)
		LineSegment3D line3 = new LineSegment3D(new Vector3D(1, 0, 0), new Vector3D(1, 1, 1));//parallel to line1; crossing line2 in (1|0|0)
		LineSegment3D line4 = new LineSegment3D(new Vector3D(1, 2, 2), new Vector3D(1, 2, 3));//not crossing line1 nor parallel to line1
		LineSegment3D line5 = new LineSegment3D(new Vector3D(6, 6, 3), new Vector3D(-2, -1, 1));//crossing line 4 in point (2|4|5)
		
		assertNull(line.calculateCrosspoint(line));
		assertArrayEquals(new double[] {0, 0, 0}, line.calculateCrosspoint(line2).asArray(), EPSILON);
		assertNull(line.calculateCrosspoint(line3));
		assertNull(line.calculateCrosspoint(line4));
		assertArrayEquals(new double[] {1, 0, 0}, line2.calculateCrosspoint(line3).asArray(), EPSILON);
		assertNull(line3.calculateCrosspoint(line4));
		assertNull(line4.calculateCrosspoint(line5));
	}
}
