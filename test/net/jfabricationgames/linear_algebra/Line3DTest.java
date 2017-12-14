package net.jfabricationgames.linear_algebra;

import static org.junit.Assert.*;

import org.junit.Test;

public class Line3DTest {
	
	public static final double EPSILON = 1e-8;
	
	@Test
	public void testIsPointOnLine() {
		Line3D line = new Line3D(new Vector3D(0, 0, 0), new Vector3D(1, 1, 1));
		Line3D line2 = new Line3D(new Vector3D(1, 1, 0), new Vector3D(1, 0, 0));
		
		assertTrue(line.isPointOnLine(new Vector3D(0, 0, 0)));
		assertTrue(line.isPointOnLine(new Vector3D(1, 1, 1)));
		assertFalse(line.isPointOnLine(new Vector3D(1, 2, 3)));
		assertFalse(line.isPointOnLine(new Vector3D(0, 0, 1)));
		
		assertTrue(line2.isPointOnLine(new Vector3D(1, 1, 0)));
		assertTrue(line2.isPointOnLine(new Vector3D(5, 1, 0)));
		assertFalse(line2.isPointOnLine(new Vector3D(5, 2, 0)));
		assertFalse(line2.isPointOnLine(new Vector3D(0, 0, 0)));
	}
	
	@Test
	public void testCalculateT() {
		Line3D line = new Line3D(new Vector3D(0, 0, 0), new Vector3D(1, 1, 1));
		Line3D line2 = new Line3D(new Vector3D(1, 1, 0), new Vector3D(1, 0, 0));
		
		assertEquals(0, line.calculateT(new Vector3D(0, 0, 0)), EPSILON);
		assertEquals(1, line.calculateT(new Vector3D(1, 1, 1)), EPSILON);
		assertEquals(Double.NaN, line.calculateT(new Vector3D(1, 2, 3)), EPSILON);
		assertEquals(Double.NaN, line.calculateT(new Vector3D(0, 0, 1)), EPSILON);
		
		assertEquals(0, line2.calculateT(new Vector3D(1, 1, 0)), EPSILON);
		assertEquals(4, line2.calculateT(new Vector3D(5, 1, 0)), EPSILON);
		assertEquals(Double.NaN, line2.calculateT(new Vector3D(5, 2, 0)), EPSILON);
		assertEquals(Double.NaN, line2.calculateT(new Vector3D(0, 0, 0)), EPSILON);
	}
	
	@Test
	public void testCalculateCrosspoint() {
		Line3D line = new Line3D(new Vector3D(0, 0, 0), new Vector3D(1, 1, 1));
		Line3D line2 = new Line3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0));//crossing line1 in (0|0|0)
		Line3D line3 = new Line3D(new Vector3D(1, 0, 0), new Vector3D(1, 1, 1));//parallel to line1; crossing line2 in (1|0|0)
		Line3D line4 = new Line3D(new Vector3D(1, 2, 2), new Vector3D(1, 2, 3));//not crossing line1 nor parallel to line1
		Line3D line5 = new Line3D(new Vector3D(6, 6, 3), new Vector3D(-2, -1, 1));//crossing line 4 in point (2|4|5)
		
		assertNull(line.calculateCrosspoint(line));
		assertArrayEquals(new double[] {0, 0, 0}, line.calculateCrosspoint(line2).asArray(), EPSILON);
		assertNull(line.calculateCrosspoint(line3));
		assertNull(line.calculateCrosspoint(line4));
		assertArrayEquals(new double[] {1, 0, 0}, line2.calculateCrosspoint(line3).asArray(), EPSILON);
		assertNull(line3.calculateCrosspoint(line4));
		assertArrayEquals(new double[] {2, 4, 5}, line4.calculateCrosspoint(line5).asArray(), EPSILON);
	}
}