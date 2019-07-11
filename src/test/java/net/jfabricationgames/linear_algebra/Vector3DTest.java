package net.jfabricationgames.linear_algebra;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Vector3DTest {
	
	public static final double EPSILON = 1e-8;
	
	@Test
	public void testVector3DDoubleDouble() {
		//test the angle constructor
		Vector3D v = new Vector3D(0, 0);
		Vector3D v2 = new Vector3D(0, 90);
		Vector3D v3 = new Vector3D(90, -90);
		Vector3D v4 = new Vector3D(0, 45);
		
		assertEquals(new Vector3D(1, 0, 0), v);
		assertEquals(new Vector3D(0, 0, 1).setLength(1), v2);
		assertEquals(new Vector3D(0, 0, -1).setLength(1), v3);
		assertEquals(new Vector3D(1, 0, 1).setLength(1), v4);
	}
	
	@Test
	public void testAsArray() {
		Vector3D v = new Vector3D(1, 2, 3);
		
		assertArrayEquals(new double[] {1, 2, 3}, v.asArray(), EPSILON);
	}
	
	@Test
	public void testLength() {
		Vector3D v = new Vector3D(1, 2, 3);
		Vector3D v2 = new Vector3D(3, 4, 5);
		
		assertEquals(Math.sqrt(14), v.length(), EPSILON);
		assertEquals(Math.sqrt(50), v2.length(), EPSILON);
	}
	
	@Test
	public void testLengthNorm() {
		Vector3D v = new Vector3D(0, 0, 0);
		Vector3D v2 = new Vector3D(1, 1, 1);
		Vector3D v3 = new Vector3D(3, 4, 5);
		//test three norms (1 (=sum), 3, inf (=max))
		//sum norm
		assertEquals(0, v.length(1), EPSILON);
		assertEquals(3, v2.length(1), EPSILON);
		assertEquals(12, v3.length(1), EPSILON);
		//3 norm
		assertEquals(0, v.length(3), EPSILON);
		assertEquals(1.44224957030, v2.length(3), EPSILON);
		assertEquals(6, v3.length(3), EPSILON);
		//max norm
		assertEquals(0, v.length(Integer.MAX_VALUE), EPSILON);
		assertEquals(1, v2.length(Integer.MAX_VALUE), EPSILON);
		assertEquals(5, v3.length(Integer.MAX_VALUE), EPSILON);
	}
	
	@Test
	public void testRotate() {
		Vector3D v = new Vector3D(1, 1, 1);
		Vector3D v2 = new Vector3D(5, 5, 5);
		
		assertEquals(new Vector3D(1, -1, 1), v.rotate(90, Vector3D.Axis.X));
		assertEquals(new Vector3D(1, 1, -1), v.rotate(90, Vector3D.Axis.Y));
		assertEquals(new Vector3D(-1, 1, 1), v.rotate(90, Vector3D.Axis.Z));
		
		assertEquals(new Vector3D(5, -5, 5), v2.rotate(90, Vector3D.Axis.X));
		assertEquals(new Vector3D(5, 5, -5), v2.rotate(90, Vector3D.Axis.Y));
		assertEquals(new Vector3D(-5, 5, 5), v2.rotate(90, Vector3D.Axis.Z));
		
		assertEquals(new Vector3D(1, 1, -1), v.rotate(-90, Vector3D.Axis.X));
		assertEquals(new Vector3D(-1, 1, 1), v.rotate(-90, Vector3D.Axis.Y));
		assertEquals(new Vector3D(1, -1, 1), v.rotate(-90, Vector3D.Axis.Z));
	}
	
	@Test
	public void testProject() {
		Vector3D v = new Vector3D(2, 2, 2);
		Vector3D v2 = new Vector3D(5, 5, 5);
		Vector3D v3 = new Vector3D(1, 1, 0);
		
		assertEquals(v2, v.project(v2));
		assertEquals(new Vector3D(2, 2, 0), v3.project(v));
	}
	
	@Test
	public void testAdd() {
		Vector3D v = new Vector3D(1, 1, 1);
		Vector3D v2 = new Vector3D(5, 5, 5);
		
		assertEquals(new Vector3D(6, 6, 6), v.add(v2));
		assertEquals(v.add(v2), v2.add(v));
	}
	
	@Test
	public void testSub() {
		Vector3D v = new Vector3D(1, 1, 1);
		Vector3D v2 = new Vector3D(5, 5, 5);
		
		assertEquals(new Vector3D(-4, -4, -4), v.sub(v2));
		assertEquals(new Vector3D(4, 4, 4), v2.sub(v));
	}
	
	@Test
	public void testMult() {
		Vector3D v = new Vector3D(1, 1, 1);
		Vector3D v2 = new Vector3D(1, 3, 5);
		
		assertEquals(new Vector3D(3, 3, 3), v.mult(3));
		assertEquals(new Vector3D(0.5, 0.5, 0.5), v.mult(0.5));
		assertEquals(new Vector3D(2, 6, 10), v2.mult(2));
		assertEquals(new Vector3D(7, 21, 35), v2.mult(7));
	}
	
	@Test
	public void testIsLinearlyDependentVector3D() {
		Vector3D v = new Vector3D(1, 3, 5);
		Vector3D v2 = new Vector3D(2, 6, 10);
		Vector3D v3 = new Vector3D(1, 3, 4);
		
		assertTrue(v.isLinearlyDependent(v));
		assertTrue(v.isLinearlyDependent(v2));
		assertFalse(v.isLinearlyDependent(v3));
		assertFalse(v2.isLinearlyDependent(v3));
	}
	
	@Test
	public void testIsLinearlyDependentVectors() {
		Vector3D v = new Vector3D(1, 3, 5);
		Vector3D v2 = new Vector3D(2, 6, 10);
		Vector3D v3 = new Vector3D(1, 3, 4);
		Vector3D v4 = new Vector3D(2, 6, 7);
		Vector3D v5 = new Vector3D(1, 5, 8);
		
		assertTrue(Vector3D.isLinearlyDependentVectors(v, v2));
		assertTrue(Vector3D.isLinearlyDependentVectors(v, v, v3));
		
		assertFalse(Vector3D.isLinearlyDependentVectors(v, v3));
		assertFalse(Vector3D.isLinearlyDependentVectors(v2, v3));
		
		assertFalse(Vector3D.isLinearlyDependentVectors(v, v3, v5));
		assertTrue(Vector3D.isLinearlyDependentVectors(v, v3, v4));//3*v3-v1=v4
		
		assertTrue(Vector3D.isLinearlyDependentVectors(v, v2, v4, v5));//4 vectors in R^3...
	}
	
	@Test
	public void testScalar() {
		Vector3D v = new Vector3D(1, 3, 5);
		Vector3D v2 = new Vector3D(1, 3, 4);
		
		assertEquals(30, v.scalar(v2), EPSILON);
		assertEquals(v.length(), Math.sqrt(v.scalar(v)), EPSILON);
	}
	
	@Test
	public void testCross() {
		Vector3D v = new Vector3D(1, 3, 5);
		Vector3D v2 = new Vector3D(2, 6, 10);
		Vector3D v3 = new Vector3D(1, 3, 4);
		
		assertEquals(new Vector3D(0, 0, 0), v.cross(v2));
		assertEquals(new Vector3D(-3, 1, 0), v.cross(v3));
		assertEquals(new Vector3D(-6, 2, 0), v2.cross(v3));
		
		assertEquals(v.cross(v3), v3.cross(v).mult(-1));
	}
	
	@Test
	public void testSetLength() {
		Vector3D v = new Vector3D(0, 4, 3);
		
		assertEquals(new Vector3D(0, 8, 6), v.setLength(10));
	}
	
	@Test
	public void testDistance() {
		Vector3D v = new Vector3D(1, 3, 5);
		Vector3D v2 = new Vector3D(2, 6, 10);
		Vector3D v3 = new Vector3D(1, 3, 4);
		
		assertEquals(Math.sqrt(35), v.distance(v2), EPSILON);
		assertEquals(1, v.distance(v3), EPSILON);
	}
	
	@Test
	public void testDistanceWithNorm() {
		Vector3D v = new Vector3D(1, 3, 5);
		Vector3D v2 = new Vector3D(2, 6, 10);
		Vector3D v3 = new Vector3D(1, 3, 4);
		
		assertEquals(9, v.distance(v2, 1), EPSILON);
		assertEquals(1, v.distance(v3, 1), EPSILON);
		
		assertEquals(Math.sqrt(35), v.distance(v2, 2), EPSILON);
		assertEquals(1, v.distance(v3, 2), EPSILON);
		
		assertEquals(Math.pow(153, 1d / 3), v.distance(v2, 3), EPSILON);
		assertEquals(1, v.distance(v3, 3), EPSILON);
	}
	
	@Test
	public void testMoveTo() {
		Vector3D v = new Vector3D(1, 3, 5);
		Vector3D v2 = new Vector3D(2, 6, 10);
		Vector3D v3 = new Vector3D(1, 3, 4);
		
		assertEquals(v2, v.moveTo(v2, Math.sqrt(35)));
		assertEquals(new Vector3D(3, 9, 15), v.moveTo(v2, 2 * Math.sqrt(35)));
		assertEquals(v3, v.moveTo(v3, v.distance(v3)));
	}
	
	@Test
	public void testGetAngleTo() {
		Vector3D v = new Vector3D(1, 0, 0);
		Vector3D v2 = new Vector3D(2, 0, 0);
		Vector3D v3 = new Vector3D(0, 1, 1);
		
		assertEquals(0, v.getAngleTo(v2), EPSILON);
		assertEquals(90, v.getAngleTo(v3), EPSILON);
		assertEquals(90, v2.getAngleTo(v3), EPSILON);
	}
	
	@Test
	public void testVectorTo() {
		Vector3D v = new Vector3D(1, 3, 5);
		Vector3D v2 = new Vector3D(2, 6, 10);
		Vector3D v3 = new Vector3D(1, 3, 4);
		
		assertEquals(v, v.vectorTo(v2));
		assertEquals(new Vector3D(0, 0, -1), v.vectorTo(v3));
		assertEquals(v2.vectorTo(v3), v3.vectorTo(v2).mult(-1));
	}
	
	@Test
	public void testIsInRange() {
		Vector3D v = new Vector3D(0, 0, 0);
		Vector3D v2 = new Vector3D(2, 0, 0);
		Vector3D v3 = new Vector3D(1, 1, 1);
		
		assertTrue(v.isInRange(v2, 2));
		assertTrue(v.isInRange(v2, 42));
		
		assertFalse(v.isInRange(v2, 1));
		assertFalse(v.isInRange(v2, 2 - EPSILON));
		
		assertTrue(v.isInRange(v3, Math.sqrt(3)));
		assertTrue(v.isInRange(v3, 42));
		
		assertFalse(v.isInRange(v3, 1));
		assertFalse(v.isInRange(v3, Math.sqrt(3) - EPSILON));
	}
	
}