package net.jfabricationgames.linear_algebra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

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
		Circle c4 = new Circle(new Vector2D(6, 2), 2);
		Circle c5 = new Circle(new Vector2D(6, 0), 2);
		
		//no intersection
		assertNull(c.getIntercectionPoints(c4));
		assertNull(c.getIntercectionPoints(c2));
		
		//empty vector (same circles)
		assertEquals(0, c.getIntercectionPoints(c).length);
		
		//single intersection point
		assertEquals(1, c.getIntercectionPoints(c5).length);
		assertEquals(new Vector2D(4, 0), c.getIntercectionPoints(c5)[0]);
		
		//correct intersection points (check manually by calculating the distance to both centers...) 
		List<Vector2D> intersection = Arrays.asList(c.getIntercectionPoints(c3));
		assertTrue(intersection.contains(new Vector2D(2, 3.4641016151)));
		assertTrue(intersection.contains(new Vector2D(2, -3.4641016151)));
		
		intersection = Arrays.asList(c3.getIntercectionPoints(c4));
		assertTrue(intersection.contains(new Vector2D(5.1771243445, 3.82287565553)));
		assertTrue(intersection.contains(new Vector2D(7.8228756555, 1.17712434446)));
	}

	@Test
	public void testCalculateIntersectingDiamond() {
		Circle c = new Circle(new Vector2D(0, 0), 4);
		Circle c2 = new Circle(new Vector2D(0, 0), 7);
		Circle c3 = new Circle(new Vector2D(4, 0), 4);
		Circle c4 = new Circle(new Vector2D(6, 2), 2);
		Circle c5 = new Circle(new Vector2D(6, 0), 2);
		
		//no intersection
		assertNull(c.calculateIntersectingKite(c4));
		assertNull(c.calculateIntersectingKite(c2));
		
		//empty vector (same circles; points on the circle used)
		assertNotNull(c.calculateIntersectingKite(c));
		Kite2D diamond = c.calculateIntersectingKite(c);
		assertTrue(diamond.getPoints().contains(new Vector2D(4, 0)));
		assertTrue(diamond.getPoints().contains(new Vector2D(0, 4)));
		assertTrue(diamond.getPoints().contains(new Vector2D(-4, 0)));
		assertTrue(diamond.getPoints().contains(new Vector2D(0, -4)));
		
		//single intersection point
		assertNull(c.calculateIntersectingKite(c5));
		
		//correct intersection points (check manually by calculating the distance to both centers...) 
		diamond = c.calculateIntersectingKite(c3);
		//the two circle intersection points...
		assertTrue(diamond.getPoints().contains(new Vector2D(2, 3.4641016151)));
		assertTrue(diamond.getPoints().contains(new Vector2D(2, -3.4641016151)));
		//...and the two other points on the circles (on the vector from middle to middle; and on the circle)
		assertTrue(diamond.getPoints().contains(c.getMiddle().add(c.getMiddle().vectorTo(c3.getMiddle()).setLength(c.radius))));
		assertTrue(diamond.getPoints().contains(c3.getMiddle().add(c3.getMiddle().vectorTo(c.getMiddle()).setLength(c3.radius))));
		
		diamond = c3.calculateIntersectingKite(c4);
		//the two circle intersection points...
		assertTrue(diamond.getPoints().contains(new Vector2D(5.1771243445, 3.82287565553)));
		assertTrue(diamond.getPoints().contains(new Vector2D(7.8228756555, 1.17712434446)));
		//...and the two other points on the circles (on the vector from middle to middle; and on the circle)
		assertTrue(diamond.getPoints().contains(c3.getMiddle().add(c3.getMiddle().vectorTo(c4.getMiddle()).setLength(c3.radius))));
		assertTrue(diamond.getPoints().contains(c4.getMiddle().add(c4.getMiddle().vectorTo(c3.getMiddle()).setLength(c4.radius))));
	}
}