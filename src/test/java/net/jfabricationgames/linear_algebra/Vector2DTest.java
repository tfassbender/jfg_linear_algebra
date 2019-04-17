package net.jfabricationgames.linear_algebra;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Vector2DTest {
	
	public static final double EPSILON = 1e-8;
	
	@Test
	public void testVector2DDouble() {
		//test the angle constructor
		Vector2D v = new Vector2D(0);
		Vector2D v2 = new Vector2D(45);
		Vector2D v3 = new Vector2D(180);
		assertEquals(new Vector2D(1, 0), v);
		assertEquals(new Vector2D(1, 1).setLength(1), v2);
		assertEquals(new Vector2D(-1, 0), v3);
	}
	
	@Test
	public void testAsArray() {
		Vector2D v = new Vector2D(1, 2);
		Vector2D v2 = new Vector2D(-1.12345, 3.45678);
		assertArrayEquals(new double[] {1, 2}, v.asArray(), EPSILON);
		assertArrayEquals(new double[] {-1.12345, 3.45678}, v2.asArray(), EPSILON);
	}
	
	@Test
	public void testLength() {
		Vector2D v = new Vector2D(0, 0);
		Vector2D v2 = new Vector2D(1, 1);
		Vector2D v3 = new Vector2D(3, 4);
		assertEquals(v.length(), 0, EPSILON);
		assertEquals(v2.length(), Math.sqrt(2), EPSILON);
		assertEquals(v3.length(), 5, EPSILON);
	}
	
	@Test
	public void testLengthNorm() {
		Vector2D v = new Vector2D(0, 0);
		Vector2D v2 = new Vector2D(1, 1);
		Vector2D v3 = new Vector2D(3, 4);
		//test three norms (1 (=sum), 3, inf (=max))
		//sum norm
		assertEquals(v.length(1), 0, EPSILON);
		assertEquals(v2.length(1), 2, EPSILON);
		assertEquals(v3.length(1), 7, EPSILON);
		//3 norm
		assertEquals(v.length(3), 0, EPSILON);
		assertEquals(v2.length(3), 1.25992104989, EPSILON);
		assertEquals(v3.length(3), 4.49794144527, EPSILON);
		//max norm
		assertEquals(v.length(Integer.MAX_VALUE), 0, EPSILON);
		assertEquals(v2.length(Integer.MAX_VALUE), 1, EPSILON);
		assertEquals(v3.length(Integer.MAX_VALUE), 4, EPSILON);
	}
	
	@Test
	public void testRotate() {
		Vector2D v = new Vector2D(0, 0);
		Vector2D v2 = new Vector2D(1, 0);
		Vector2D v3 = new Vector2D(-3, 0);
		//rotate null-vector
		assertEquals(v, v.rotate(180));
		assertEquals(v, v.rotate(90));
		assertEquals(v, v.rotate(42));
		//rotate other vectors 90°, 180° and 360°
		assertEquals(new Vector2D(0, 1), v2.rotate(90));
		assertEquals(new Vector2D(-1, 0), v2.rotate(180));
		assertEquals(v2, v2.rotate(360));
		assertEquals(new Vector2D(0, -3), v3.rotate(90));
		//rotate 42° (expected is just estimated)
		assertArrayEquals(new Vector2D(1, 0.9).setLength(1).asArray(), v2.rotate(42).asArray(), 1e-1);
		assertArrayEquals(new Vector2D(-3, -2.75).setLength(3).asArray(), v3.rotate(42).asArray(), 1e-1);
	}
	
	@Test
	public void testProject() {
		Vector2D v = new Vector2D(0, 1);
		Vector2D v2 = new Vector2D(1, 1);
		Vector2D v3 = new Vector2D(5, 3);
		assertEquals(v, v.project(v2));
		assertEquals(new Vector2D(0, 3), v.project(v3));
	}
	
	@Test
	public void testAdd() {
		Vector2D v = new Vector2D(1, 1);
		Vector2D v2 = new Vector2D(1, 5);
		Vector2D v3 = new Vector2D(1.12345, 3.34567);
		assertEquals(new Vector2D(2, 6), v.add(v2));
		assertEquals(new Vector2D(2.12345, 8.34567), v2.add(v3));
	}
	
	@Test
	public void testSub() {
		Vector2D v = new Vector2D(1, 1);
		Vector2D v2 = new Vector2D(1, 5);
		Vector2D v3 = new Vector2D(1.12345, 3.34567);
		assertEquals(new Vector2D(0, -4), v.sub(v2));
		assertEquals(new Vector2D(0.12345, -1.65433), v3.sub(v2));
	}
	
	@Test
	public void testMult() {
		Vector2D v = new Vector2D(1, 1);
		Vector2D v2 = new Vector2D(1, 5);
		Vector2D v3 = new Vector2D(1.1, 3.3);
		assertEquals(new Vector2D(4, 4), v.mult(4));
		assertEquals(new Vector2D(2, 10), v2.mult(2));
		assertEquals(new Vector2D(3.3, 9.9), v3.mult(3));
		assertEquals(new Vector2D(0.3, 1.5), v2.mult(0.3));
	}
	
	@Test
	public void testIsLinearlyDependent() {
		Vector2D v = new Vector2D(1, 1);
		Vector2D v2 = new Vector2D(5, 5);
		Vector2D v3 = new Vector2D(1, 3);
		//two linear dependent vectors
		assertTrue(v.isLinearlyDependent(v2));
		//reversal must also be true
		assertTrue(v2.isLinearlyDependent(v));
		//not dependent
		assertFalse(v.isLinearlyDependent(v3));
		assertFalse(v3.isLinearlyDependent(v));
		assertFalse(v2.isLinearlyDependent(v3));
		assertFalse(v3.isLinearlyDependent(v2));
	}
	
	@Test
	public void testIsLinearlyDependentVectors() {
		Vector2D v = new Vector2D(1, 1);
		Vector2D v2 = new Vector2D(5, 5);
		Vector2D v3 = new Vector2D(1, 3);
		assertTrue(Vector2D.isLinearlyDependentVectors(v, v2));
		assertFalse(Vector2D.isLinearlyDependentVectors(v, v3));
		//three vectors in R^2 are always linearly dependent
		assertTrue(Vector2D.isLinearlyDependentVectors(v, v2, v3));
		//one vector is never linearly dependent
		assertFalse(Vector2D.isLinearlyDependentVectors(new Vector2D(1, 2)));
	}
	
	@Test
	public void testScalar() {
		Vector2D v = new Vector2D(1, 1);
		Vector2D v2 = new Vector2D(5, 5);
		Vector2D v3 = new Vector2D(1, 3);
		assertEquals(v.scalar(v2), 10, EPSILON);
		assertEquals(v2.scalar(v), 10, EPSILON);
		assertEquals(v.scalar(v3), 4, EPSILON);
		assertEquals(v2.scalar(v3), 20, EPSILON);
		//vector length
		assertEquals(Math.sqrt(v.scalar(v)), v.length(), EPSILON);
	}
	
	@Test
	public void testSetLength() {
		Vector2D v = new Vector2D(1, 0);
		Vector2D v2 = new Vector2D(3, 4);
		assertEquals(new Vector2D(4, 0), v.setLength(4));
		assertEquals(v2, v2.setLength(5));
		assertEquals(new Vector2D(6, 8), v2.setLength(10));
	}
	
	@Test
	public void testDistance() {
		Vector2D v = new Vector2D(1, 1);
		Vector2D v2 = new Vector2D(5, 5);
		Vector2D v3 = new Vector2D(1, 3);
		Vector2D v4 = new Vector2D(-2, -1);
		assertEquals(v.distance(v2), Math.sqrt(32), EPSILON);
		assertEquals(v.distance(v3), 2, EPSILON);
		assertEquals(v.distance(v4), Math.sqrt(13), EPSILON);
		assertEquals(v.distance(v), 0, EPSILON);
		assertEquals(v3.distance(v4), 5, EPSILON);
	}
	
	@Test
	public void testMove() {
		Vector2D v = new Vector2D(1, 1);
		v.move(3, 5);
		assertEquals(new Vector2D(3, 5), v);
	}
	
	@Test
	public void testMoveTo() {
		Vector2D v = new Vector2D(1, 1);
		Vector2D v2 = new Vector2D(1, 5);
		Vector2D v3 = new Vector2D(5, 5);
		assertEquals(v2, v.moveTo(v2, 4));
		assertEquals(new Vector2D(1, 2), v.moveTo(v2, 1));
		assertEquals(v.moveTo(v3, Math.sqrt(32)), v3);
		assertEquals(new Vector2D(2, 2), v.moveTo(v3, Math.sqrt(2)));
		assertEquals(new Vector2D(11, 11), v.moveTo(v3, 10*Math.sqrt(2)));
	}
	
	@Test
	public void testGetAngle() {
		Vector2D v = new Vector2D(1, 0);
		Vector2D v2 = new Vector2D(1, 1);
		Vector2D v3 = new Vector2D(5, -5);
		assertEquals(v.getAngle(), 0, EPSILON);
		assertEquals(v2.getAngle(), 45, EPSILON);
		assertEquals(v3.getAngle(), 315, EPSILON);
	}
	
	@Test
	public void testGetAngleRad() {
		Vector2D v = new Vector2D(1, 0);
		Vector2D v2 = new Vector2D(1, 1);
		Vector2D v3 = new Vector2D(5, -5);
		assertEquals(v.getAngleRad(), 0, EPSILON);
		assertEquals(v2.getAngleRad(), Math.PI/4, EPSILON);
		assertEquals(v3.getAngleRad(), Math.PI*7/4, EPSILON);
	}
	
	@Test
	public void testGetAngleDeltaTo() {
		Vector2D v = new Vector2D(1, 0);
		Vector2D v2 = new Vector2D(1, 1);
		Vector2D v3 = new Vector2D(5, -5);
		assertEquals(v.getAngleDeltaTo(v), 0, EPSILON);
		assertEquals(v.getAngleDeltaTo(v2), 45, EPSILON);
		assertEquals(v2.getAngleDeltaTo(v), 45, EPSILON);
		assertEquals(v.getAngleDeltaTo(v3), 45, EPSILON);
		assertEquals(v2.getAngleDeltaTo(v3), 90, EPSILON);
	}
	
	@Test
	public void testVectorTo() {
		Vector2D v = new Vector2D(1, 0);
		Vector2D v2 = new Vector2D(1, 1);
		Vector2D v3 = new Vector2D(5, 5);
		assertEquals(new Vector2D(0, 0), v.vectorTo(v));
		assertEquals(new Vector2D(0, 1), v.vectorTo(v2));
		assertEquals(new Vector2D(4, 5), v.vectorTo(v3));
		assertEquals(new Vector2D(4, 4), v2.vectorTo(v3));
		assertEquals(new Vector2D(-4, -4), v3.vectorTo(v2));
	}
	
	@Test
	public void testIsInRange() {
		Vector2D p = new Vector2D(0, 0);
		Vector2D v2 = new Vector2D(1, 1);
		Vector2D v3 = new Vector2D(3, 4);
		assertTrue(p.isInRange(v2, 2));
		assertTrue(p.isInRange(v2, Math.sqrt(2)));
		assertFalse(p.isInRange(v2, 0.5));
		assertFalse(p.isInRange(v2, Math.sqrt(2)-EPSILON));
		assertTrue(p.isInRange(v3, 42));
		assertTrue(p.isInRange(v3, 5));
		assertFalse(p.isInRange(v3, 3));
		assertFalse(p.isInRange(v3, 5-EPSILON));
		//inRange checks for p != this
		assertFalse(p.isInRange(p, EPSILON));
		assertFalse(p.isInRange(p, 1));
		assertFalse(p.isInRange(p, 1e10));
	}
	
}