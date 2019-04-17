package net.jfabricationgames.linear_algebra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LineSegment2DTest {
	
	public static final double EPSILON = 1e-8;

	@Test
	public void testCalculateCrosspointLine2D() {
		LineSegment2D l1 = new LineSegment2D(new Vector2D(-1, -1), new Vector2D(2, 2));
		LineSegment2D l2 = new LineSegment2D(new Vector2D(0, 0), new Vector2D(0.5, 1));
		Line2D l3 = new Line2D(new Vector2D(2, 0), new Vector2D(-1, 1));
		
		assertEquals(new Vector2D(0, 0), l1.calculateCrosspoint(l2));
		assertEquals(new Vector2D(1, 1), l1.calculateCrosspoint(l3));
		assertEquals(new Vector2D(1, 1), l1.calculateCrosspoint(l3));
		assertNull(l2.calculateCrosspoint(l3));
	}

	@Test
	public void testCalculateMinDistToPoint() {
		LineSegment2D l1 = new LineSegment2D(new Vector2D(-1, -1), new Vector2D(2, 2));
		
		assertEquals(0, l1.calculateMinDistToPoint(new Vector2D(1, 1)), EPSILON);
		assertEquals(Math.sqrt(2), l1.calculateMinDistToPoint(new Vector2D(2, 2)), EPSILON);
		assertEquals(1, l1.calculateMinDistToPoint(new Vector2D(2, 1)), EPSILON);
		
		assertEquals(Math.sqrt(2)*0.5, l1.calculateMinDistToPoint(new Vector2D(1, 0)), EPSILON);
	}

	@Test
	public void testIsPointOnLine() {
		LineSegment2D l1 = new LineSegment2D(new Vector2D(-1, -1), new Vector2D(2, 2));
		
		assertTrue(l1.isPointOnLine(new Vector2D(0, 0)));
		assertTrue(l1.isPointOnLine(new Vector2D(1, 1)));
		assertFalse(l1.isPointOnLine(new Vector2D(3, 3)));
		assertFalse(l1.isPointOnLine(new Vector2D(0, 1)));
	}

	@Test
	public void testCalculateCrosspointLineSegment2D() {
		LineSegment2D l1 = new LineSegment2D(new Vector2D(-1, -1), new Vector2D(2, 2));
		LineSegment2D l2 = new LineSegment2D(new Vector2D(0, 0), new Vector2D(0.5, 1));
		LineSegment2D l3 = new LineSegment2D(new Vector2D(2, 0), new Vector2D(-1, 1));
		
		assertEquals(new Vector2D(0, 0), l1.calculateCrosspoint(l2));
		assertEquals(new Vector2D(1, 1), l1.calculateCrosspoint(l3));
		assertEquals(new Vector2D(1, 1), l1.calculateCrosspoint(l3));
		assertNull(l2.calculateCrosspoint(l3));
	}
}