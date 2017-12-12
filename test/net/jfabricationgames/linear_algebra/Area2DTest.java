package net.jfabricationgames.linear_algebra;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class Area2DTest {
	
	public static final double EPSILON = 1e-8;
	
	@Test(expected = LinearAlgebraException.class)
	public void testGetSize() {
		Area2D area = new Area2D(new Vector2D(2, 3), new Vector2D(4, 5), new Vector2D(3, 7));//a triangle
		Area2D area2 = new Area2D(new Vector2D(0, 0), new Vector2D(5, 0), new Vector2D(5, 5), new Vector2D(0, 5));//a rectangle
		Area2D area3 = new Area2D(new Vector2D(0, 0), new Vector2D(5, 0), new Vector2D(1, 1), new Vector2D(0, 5));//non convex
		
		assertEquals(3, area.getSize(), EPSILON);
		assertEquals(25, area2.getSize(), EPSILON);
		assertEquals(42, area3.getSize(), EPSILON);//expect exception here
	}
	
	@Test
	public void testIsConvex() {
		Area2D area = new Area2D(new Vector2D(2, 3), new Vector2D(4, 5), new Vector2D(3, 7));//a triangle
		Area2D area2 = new Area2D(new Vector2D(0, 0), new Vector2D(5, 0), new Vector2D(5, 5), new Vector2D(0, 5));//a rectangle
		Area2D area3 = new Area2D(new Vector2D(0, 0), new Vector2D(5, 0), new Vector2D(1, 1), new Vector2D(0, 5));//non convex
		Area2D area4 = new Area2D(new Vector2D(1, 0), new Vector2D(5, 0), new Vector2D(1, 3), new Vector2D(0, 5));//non convex
		//area4 with reverse order
		Area2D area5 = new Area2D(new Vector2D(0, 5), new Vector2D(1, 3), new Vector2D(5, 0), new Vector2D(1, 0));
		
		assertTrue(area.isConvex());
		assertTrue(area2.isConvex());
		assertFalse(area3.isConvex());
		assertFalse(area4.isConvex());
		assertFalse(area5.isConvex());
	}

	@Test(expected = LinearAlgebraException.class)
	public void testIsPointInArea() {
		Area2D area = new Area2D(new Vector2D(0, 0), new Vector2D(10, 0), new Vector2D(10, 10), new Vector2D(0, 10));
		
		assertTrue(area.isPointInArea(new Vector2D(1, 1)));
		assertTrue(area.isPointInArea(new Vector2D(5, 5)));
		assertTrue(area.isPointInArea(new Vector2D(3, 7)));
		
		assertTrue(area.isPointInArea(new Vector2D(0, 0)));
		assertTrue(area.isPointInArea(new Vector2D(10, 10)));
		assertTrue(area.isPointInArea(new Vector2D(5, 0)));
		
		assertFalse(area.isPointInArea(new Vector2D(11, 0)));
		assertFalse(area.isPointInArea(new Vector2D(5, 15)));
		assertFalse(area.isPointInArea(new Vector2D(30, -2)));
		
		//non-convex polygon area
		Area2D area2 = new Area2D(new Vector2D(0, 0), new Vector2D(5, 0), new Vector2D(1, 1), new Vector2D(0, 5));
		area2.isPointInArea(new Vector2D(0, 0));//expect exception here
	}

	@Test
	public void testPointInWay() {
		//rectangle from (0|0) to (10|0) with a width of 2
		assertTrue(Area2D.pointInWay(new Vector2D(0, 0), new Vector2D(10, 0), 2, new Vector2D(1, 1)));
		//one point in way one not (same rectangle)
		assertTrue(Area2D.pointInWay(new Vector2D(0, 0), new Vector2D(10, 0), 2, new Vector2D(1, 1), new Vector2D(3, 3)));
		//none in way
		assertFalse(Area2D.pointInWay(new Vector2D(0, 0), new Vector2D(10, 0), 2, new Vector2D(1, 5), new Vector2D(3, 3)));
		//test edges
		assertTrue(Area2D.pointInWay(new Vector2D(0, 0), new Vector2D(10, 0), 2, new Vector2D(3, 2)));
		
		//other rectangle: from (0|0) to (5|5), width sqrt(2)
		assertTrue(Area2D.pointInWay(new Vector2D(0, 0), new Vector2D(5, 5), Math.sqrt(2), new Vector2D(2, 2)));
		assertTrue(Area2D.pointInWay(new Vector2D(0, 0), new Vector2D(5, 5), Math.sqrt(2), new Vector2D(1, 1.5)));
		//edges
		assertTrue(Area2D.pointInWay(new Vector2D(0, 0), new Vector2D(5, 5), Math.sqrt(2), new Vector2D(1, -1)));
		assertTrue(Area2D.pointInWay(new Vector2D(0, 0), new Vector2D(5, 5), Math.sqrt(2), new Vector2D(5, 5)));
		//not in rectangle
		assertFalse(Area2D.pointInWay(new Vector2D(0, 0), new Vector2D(5, 5), Math.sqrt(2), new Vector2D(-1, -1)));
		assertFalse(Area2D.pointInWay(new Vector2D(0, 0), new Vector2D(5, 5), Math.sqrt(2), new Vector2D(5, 7)));
	}

	@Test
	public void testIsPointInRectangle() {
		Vector2D[] rectangle = Area2D.calculateWayRectangle(new Vector2D(0, 0), new Vector2D(10, 0), 2);
		Vector2D[] rectangle2 = Area2D.calculateWayRectangle(new Vector2D(0, 0), new Vector2D(5, 5), Math.sqrt(2));
		//test the same points as in testPointInWay()
		
		assertTrue(Area2D.isPointInRectangle(rectangle, new Vector2D(1, 1)));
		//none in way
		assertFalse(Area2D.isPointInRectangle(rectangle, new Vector2D(3, 3)));
		//test edges
		assertTrue(Area2D.isPointInRectangle(rectangle, new Vector2D(3, 2)));
		
		//other rectangle: from (0|0) to (5|5), width sqrt(2)
		assertTrue(Area2D.isPointInRectangle(rectangle2, new Vector2D(2, 2)));
		assertTrue(Area2D.isPointInRectangle(rectangle2, new Vector2D(1, 1.5)));
		//edges
		assertTrue(Area2D.isPointInRectangle(rectangle2, new Vector2D(1, -1)));
		assertTrue(Area2D.isPointInRectangle(rectangle2, new Vector2D(5, 5)));
		//not in rectangle
		assertFalse(Area2D.isPointInRectangle(rectangle2, new Vector2D(-1, -1)));
		assertFalse(Area2D.isPointInRectangle(rectangle2, new Vector2D(5, 7)));
	}

	@Test
	public void testCalculateWayRectangle() {
		Vector2D[] rectangle = Area2D.calculateWayRectangle(new Vector2D(0, 0), new Vector2D(10, 0), 2);
		List<Vector2D> points = Arrays.asList(rectangle);//create a set because the order of the points is not relevant
		
		assertTrue(points.contains(new Vector2D(0, 2)));
		assertTrue(points.contains(new Vector2D(10, 2)));
		assertTrue(points.contains(new Vector2D(10, -2)));
		assertTrue(points.contains(new Vector2D(0, -2)));
		
		rectangle = Area2D.calculateWayRectangle(new Vector2D(0, 0), new Vector2D(5, 5), Math.sqrt(2));
		points = Arrays.asList(rectangle);

		assertTrue(points.contains(new Vector2D(-1, 1)));
		assertTrue(points.contains(new Vector2D(1, -1)));
		assertTrue(points.contains(new Vector2D(4, 6)));
		assertTrue(points.contains(new Vector2D(6, 4)));
	}

	@Test
	public void testCalculateTriangleArea() {
		Vector2D[] triangle1 = new Vector2D[] {new Vector2D(0, 0), new Vector2D(0, 3), new Vector2D(4, 0)};
		Vector2D[] triangle2 = new Vector2D[] {new Vector2D(1, 1), new Vector2D(0, 1), new Vector2D(0, 2)};
		Vector2D[] triangle3 = new Vector2D[] {new Vector2D(2, 3), new Vector2D(4, 5), new Vector2D(3, 7)};
		//same points in another order
		Vector2D[] triangle4 = new Vector2D[] {new Vector2D(3, 7), new Vector2D(2, 3), new Vector2D(4, 5)};
		Vector2D[] triangle5 = new Vector2D[] {new Vector2D(3, 7), new Vector2D(4, 5), new Vector2D(2, 3)};
		
		assertEquals(6, Area2D.calculateTriangleArea(triangle1), EPSILON);
		assertEquals(0.5, Area2D.calculateTriangleArea(triangle2), EPSILON);
		assertEquals(3, Area2D.calculateTriangleArea(triangle3), EPSILON);
		assertEquals(Area2D.calculateTriangleArea(triangle3), Area2D.calculateTriangleArea(triangle4), EPSILON);
		assertEquals(Area2D.calculateTriangleArea(triangle3), Area2D.calculateTriangleArea(triangle5), EPSILON);
	}
}