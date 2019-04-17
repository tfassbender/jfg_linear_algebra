package net.jfabricationgames.linear_algebra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class Line2DTest {
	
	public static final double EPSILON = 1e-8;
	
	@Test
	public void testCalculateCrosspoint() {
		Line2D l1 = new Line2D(new Vector2D(0, 0), new Vector2D(1, 1));
		Line2D l2 = new Line2D(new Vector2D(0, 0), new Vector2D(1, 2));
		Line2D l3 = new Line2D(new Vector2D(2, 0), new Vector2D(-1, 1));
		
		assertEquals(new Vector2D(0, 0), l1.calculateCrosspoint(l2));
		assertEquals(new Vector2D(0, 0), l2.calculateCrosspoint(l1));
		assertEquals(new Vector2D(1, 1), l1.calculateCrosspoint(l3));
		assertEquals(new Vector2D(1, 1), l3.calculateCrosspoint(l1));
		assertEquals(new Vector2D(2.0/3.0, 4.0/3.0), l2.calculateCrosspoint(l3));
	}
	
	@Test
	public void testCalculateMinDistToPoint() {
		Line2D l1 = new Line2D(new Vector2D(0, 0), new Vector2D(1, 1));
		Line2D l2 = new Line2D(new Vector2D(0, 1), new Vector2D(1, 0));
		
		//point on line
		assertEquals(0, l1.calculateMinDistToPoint(new Vector2D(0, 0)), EPSILON);
		assertEquals(0, l1.calculateMinDistToPoint(new Vector2D(1, 1)), EPSILON);
		
		//line: y = 1
		assertEquals(1, l2.calculateMinDistToPoint(new Vector2D(0, 2)), EPSILON);
		assertEquals(4, l2.calculateMinDistToPoint(new Vector2D(3, 5)), EPSILON);
		
		//line: y = x
		assertEquals(Math.sqrt(2), l1.calculateMinDistToPoint(new Vector2D(0, 2)), EPSILON);
		assertEquals(Math.sqrt(2)*0.5, l1.calculateMinDistToPoint(new Vector2D(1, 0)), EPSILON);
	}
	
	@Test
	public void testIsPointOnLine() {
		Line2D l1 = new Line2D(new Vector2D(0, 0), new Vector2D(1, 1));
		Line2D l2 = new Line2D(new Vector2D(0, 1), new Vector2D(1, 0));
		
		assertTrue(l1.isPointOnLine(new Vector2D(0, 0)));
		assertTrue(l1.isPointOnLine(new Vector2D(4, 4)));
		assertTrue(l2.isPointOnLine(new Vector2D(0, 1)));
		assertTrue(l2.isPointOnLine(new Vector2D(5, 1)));

		assertFalse(l1.isPointOnLine(new Vector2D(1, 0)));
		assertFalse(l1.isPointOnLine(new Vector2D(0, 1)));
		assertFalse(l2.isPointOnLine(new Vector2D(0, 5)));
		assertFalse(l2.isPointOnLine(new Vector2D(3, 4)));
	}
	
	@Test
	public void testCalculateT() {
		Line2D l1 = new Line2D(new Vector2D(0, 0), new Vector2D(1, 1));
		Line2D l2 = new Line2D(new Vector2D(0, 1), new Vector2D(1, 0));
		
		assertEquals(0, l1.calculateT(new Vector2D(0, 0)), EPSILON);
		assertEquals(4, l1.calculateT(new Vector2D(4, 4)), EPSILON);
		assertEquals(0, l2.calculateT(new Vector2D(0, 1)), EPSILON);
		assertEquals(5, l2.calculateT(new Vector2D(5, 1)), EPSILON);

		assertEquals(Double.NaN, l1.calculateT(new Vector2D(1, 0)), EPSILON);
		assertEquals(Double.NaN, l2.calculateT(new Vector2D(0, 5)), EPSILON);
	}
	
}
