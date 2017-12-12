package net.jfabricationgames.linear_algebra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CircleTest {

	@Test
	public void testIsPointInArea() {
		Circle c = new Circle(new Vector2D(0, 0), 5);
		Circle c2 = new Circle(new Vector2D(2, 3), 4);
		
		assertTrue(c.isPointInArea(new Vector2D(0, 0)));
		assertTrue(c.isPointInArea(new Vector2D(1, 2)));
		assertTrue(c2.isPointInArea(new Vector2D(1, 1)));
		assertTrue(c2.isPointInArea(new Vector2D(6, 3)));

		assertFalse(c.isPointInArea(new Vector2D(6, 0)));
		assertFalse(c.isPointInArea(new Vector2D(9, 3)));
		assertFalse(c2.isPointInArea(new Vector2D(4.5, 6.5)));
		assertFalse(c2.isPointInArea(new Vector2D(-1, -1)));
	}

	@Test
	public void testGetIntercectionPoints() {
		Circle c = new Circle(new Vector2D(0, 0), 4);
		Circle c2 = new Circle(new Vector2D(0, 0), 7);
		Circle c3 = new Circle(new Vector2D(4, 0), 4);
		Circle c4 = new Circle(new Vector2D(6, 0), 1);
		
		//no intersection
		assertNull(c.getIntercectionPoints(c4));
		assertNull(c.getIntercectionPoints(c2));
		
		//empty vector (same circles)
		assertEquals(0, c.getIntercectionPoints(c).length);
		
		//correct intersection points
		List<Vector2D> intersection = Arrays.asList(c.getIntercectionPoints(c3));
		//TODO
	}

	@Test
	public void testCalculateIntersectingDiamond() {
		fail("Not yet implemented");
	}

}
