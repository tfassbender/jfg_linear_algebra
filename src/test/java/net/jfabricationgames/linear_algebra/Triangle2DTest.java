package net.jfabricationgames.linear_algebra;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class Triangle2DTest {
	
	public static final double EPSILON = 1e-8;

	@Test
	public void testGetSize() {
		Triangle2D t = new Triangle2D(new Vector2D(0, 0), new Vector2D(1, 1), new Vector2D(1, 0));
		Triangle2D t2 = new Triangle2D(new Vector2D(0, 0), new Vector2D(2, 2), new Vector2D(1, 1));
		Triangle2D t3 = new Triangle2D(new Vector2D(0, 0), new Vector2D(2, 1), new Vector2D(2, 0));
		
		assertEquals(0.5, t.getSize(), EPSILON);
		assertEquals(0, t2.getSize(), EPSILON);
		assertEquals(1, t3.getSize(), EPSILON);
	}

	@Test
	public void testTriangle2D() {
		new Triangle2D(new Vector2D(0, 0), new Vector2D(1, 1), new Vector2D(1, 0));
		new Triangle2D(new Vector2D(0, 0), new Vector2D(2, 2), new Vector2D(1, 1));
		List<Vector2D> p = new ArrayList<Vector2D>();
		p.add(new Vector2D(0, 0));
		p.add(new Vector2D(3, 4));
		p.add(new Vector2D(6, 2));
		new Triangle2D(p);
	}
	
	@Test()
	public void testTriangle2DException() {
		assertThrows(LinearAlgebraException.class, () -> new Triangle2D(new Vector2D(0, 0), new Vector2D(1, 1)));
		assertThrows(LinearAlgebraException.class, () -> new Triangle2D(new Vector2D(0, 0), new Vector2D(2, 2), new Vector2D(1, 1), new Vector2D(1, 0)));
	}
}