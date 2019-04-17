package net.jfabricationgames.linear_algebra;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class Diamond2DTest {
	
	public static final double EPSILON = 1e-8;
	
	@Test
	public void testGetSize() {
		//a square
		Diamond2D diamond = new Diamond2D(new Vector2D(0, 0), new Vector2D(2, 0), new Vector2D(2, 2), new Vector2D(0, 2));
		assertEquals(4, diamond.getSize(), EPSILON);
		
		diamond = new Diamond2D(new Vector2D(0, 0), new Vector2D(1, 1), new Vector2D(2, 1), new Vector2D(1, 0));
		assertEquals(1, diamond.getSize(), EPSILON);
	}

	@Test
	public void testDiamond2DVector2DArray() {
		//just create a view diamonds to test the constructor
		new Diamond2D(new Vector2D(0, 0), new Vector2D(2, 0), new Vector2D(2, 2), new Vector2D(0, 2));
		new Diamond2D(new Vector2D(0, 0), new Vector2D(1, 1), new Vector2D(2, 1), new Vector2D(1, 0));
		new Diamond2D(new Vector2D(1, 1), new Vector2D(2, 3), new Vector2D(3, 1), new Vector2D(2, -1));
	}
	
	@Test()
	public void testDiamond2DVector2DArrayException() {
		assertThrows(LinearAlgebraException.class, () -> new Diamond2D(new Vector2D(0, 0), new Vector2D(1, 1), new Vector2D(2, 1), new Vector2D(2, 0)));
	}
	
	@Test
	public void testGetMiddle() {
		Diamond2D diamond = new Diamond2D(new Vector2D(0, 0), new Vector2D(2, 0), new Vector2D(2, 2), new Vector2D(0, 2));
		assertEquals(new Vector2D(1, 1), diamond.getMiddle());
		
		diamond = new Diamond2D(new Vector2D(0, 0), new Vector2D(1, 1), new Vector2D(2, 1), new Vector2D(1, 0));
		assertEquals(new Vector2D(1, 0.5), diamond.getMiddle());
		
		diamond = new Diamond2D(new Vector2D(1, 1), new Vector2D(2, 3), new Vector2D(3, 1), new Vector2D(2, -1));
		assertEquals(new Vector2D(2, 1), diamond.getMiddle());
	}
	
	@Test
	public void testGetSpanVectors() {
		Diamond2D diamond = new Diamond2D(new Vector2D(0, 0), new Vector2D(2, 0), new Vector2D(2, 2), new Vector2D(0, 2));
		assertArrayEquals(new Vector2D[] {new Vector2D(2, 0), new Vector2D(0, 2)}, diamond.getSpanVectors());
		
		diamond = new Diamond2D(new Vector2D(0, 0), new Vector2D(1, 1), new Vector2D(2, 1), new Vector2D(1, 0));
		assertArrayEquals(new Vector2D[] {new Vector2D(1, 1), new Vector2D(1, 0)}, diamond.getSpanVectors());
	}
}