package net.jfabricationgames.linear_algebra;

import static org.junit.Assert.*;

import org.junit.Test;

public class Plane3DTest {
	
	private static final double EPSILON = 1e-8;
	
	@Test
	public void testGetIntersectionPoint() {
		Line3D line = new Line3D(new Vector3D(0, 0, 0), new Vector3D(1, 1, 1));
		Line3D line2 = new Line3D(new Vector3D(1, 1, 0), new Vector3D(1, 0, 0));
		Line3D line3 = new Line3D(new Vector3D(1, 2, 3), new Vector3D(2, 2, 0));
		
		Plane3D planeXY = new Plane3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 1, 0));
		Plane3D planeXZ = new Plane3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1));
		Plane3D plane = new Plane3D(new Vector3D(1, 1, 0), new Vector3D(1, 1, -1), new Vector3D(2, 0, 1));
		
		assertArrayEquals(new double[] {0, 0, 0}, planeXY.getIntersectionPoint(line).asArray(), EPSILON);
		try {
			planeXY.getIntersectionPoint(line2);
			fail("A LinearAlgebraException was expected here...");
		}
		catch (LinearAlgebraException lae) {
			//do nothing here because the exception was expected
		}
		assertNull(planeXY.getIntersectionPoint(line3));
		
		assertArrayEquals(new double[] {0, 0, 0}, planeXZ.getIntersectionPoint(line).asArray(), EPSILON);
		assertNull(planeXZ.getIntersectionPoint(line2));
		assertArrayEquals(new double[] {-1, 0, 3}, planeXZ.getIntersectionPoint(line3).asArray(), EPSILON);
		
		assertArrayEquals(new double[] {0.5, 0.5, 0.5}, plane.getIntersectionPoint(line).asArray(), EPSILON);
		assertArrayEquals(new double[] {1, 1, 0}, plane.getIntersectionPoint(line2).asArray(), EPSILON);
		assertArrayEquals(new double[] {-3.5, -2.5, 3}, plane.getIntersectionPoint(line3).asArray(), EPSILON);
	}
	
	@Test
	public void testIsOnPlaneLine3D() {
		Line3D line = new Line3D(new Vector3D(0, 0, 0), new Vector3D(1, 1, 1));
		Line3D line2 = new Line3D(new Vector3D(1, 1, 0), new Vector3D(1, 0, 0));
		Line3D line3 = new Line3D(new Vector3D(5, 1, 2), new Vector3D(2, 2, -2));
		
		Plane3D planeXY = new Plane3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 1, 0));
		Plane3D planeXZ = new Plane3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1));
		Plane3D plane = new Plane3D(new Vector3D(1, 1, 0), new Vector3D(1, 1, -1), new Vector3D(2, 0, 1));
		
		assertFalse(planeXY.isOnPlane(line));
		assertTrue(planeXY.isOnPlane(line2));
		assertFalse(planeXY.isOnPlane(line3));
		
		assertFalse(planeXZ.isOnPlane(line));
		assertFalse(planeXZ.isOnPlane(line2));
		assertFalse(planeXZ.isOnPlane(line3));
		
		assertFalse(plane.isOnPlane(line));
		assertFalse(plane.isOnPlane(line2));
		assertTrue(plane.isOnPlane(line3));
	}
	
	@Test
	public void testGetDistance() {
		Plane3D planeXY = new Plane3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 1, 0));
		Plane3D planeXZ = new Plane3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1));
		Plane3D plane = new Plane3D(new Vector3D(1, 1, 0), new Vector3D(1, 1, -1), new Vector3D(2, 0, 1));
		
		assertEquals(0, planeXY.getDistance(new Vector3D(0, 0, 0)), EPSILON);
		assertEquals(1, planeXY.getDistance(new Vector3D(1, 1, 1)), EPSILON);
		assertEquals(17, planeXY.getDistance(new Vector3D(5, 42, 17)), EPSILON);

		assertEquals(3, planeXZ.getDistance(new Vector3D(2, 3, 4)), EPSILON);
		assertEquals(42, planeXZ.getDistance(new Vector3D(5, 42, 17)), EPSILON);

		assertEquals(Math.sqrt(14), plane.getDistance(new Vector3D(2, -2, -2)), EPSILON);
		assertEquals(3d/2 * Math.sqrt(14), plane.getDistance(new Vector3D(4, 5, 6)), EPSILON);
	}
	
	@Test
	public void testIsOnPlaneVector3D() {
		Plane3D planeXY = new Plane3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 1, 0));
		Plane3D planeXZ = new Plane3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1));
		Plane3D plane = new Plane3D(new Vector3D(1, 1, 0), new Vector3D(1, 1, -1), new Vector3D(2, 0, 1));
		
		assertTrue(planeXY.isOnPlane(new Vector3D(0, 0, 0)));
		assertTrue(planeXY.isOnPlane(new Vector3D(2, 2, 0)));
		assertFalse(planeXY.isOnPlane(new Vector3D(1, 1, 1)));
		
		assertTrue(planeXZ.isOnPlane(new Vector3D(0, 0, 0)));
		assertTrue(planeXZ.isOnPlane(new Vector3D(4, 0, 7)));
		assertFalse(planeXZ.isOnPlane(new Vector3D(3, 1, 2)));
		
		assertTrue(plane.isOnPlane(new Vector3D(1, 1, 0)));
		assertTrue(plane.isOnPlane(new Vector3D(4, 6, -6)));
		assertFalse(plane.isOnPlane(new Vector3D(5, 1, 6)));
	}
	
	@Test
	public void testGetNormalVector() {
		Plane3D planeXY = new Plane3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 1, 0));
		Plane3D planeXZ = new Plane3D(new Vector3D(0, 0, 0), new Vector3D(1, 0, 0), new Vector3D(0, 0, 1));
		Plane3D plane = new Plane3D(new Vector3D(1, 1, 0), new Vector3D(1, 1, -1), new Vector3D(2, 0, 1));

		assertEquals(new Vector3D(0, 0, 1), planeXY.getNormalVector());
		assertEquals(new Vector3D(0, -1, 0), planeXZ.getNormalVector());
		assertEquals(new Vector3D(1, -3, -2), plane.getNormalVector());
	}
}