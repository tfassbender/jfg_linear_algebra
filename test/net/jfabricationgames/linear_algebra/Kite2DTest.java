package net.jfabricationgames.linear_algebra;

import static org.junit.Assert.*;

import org.junit.Test;

public class Kite2DTest {
	
	@Test
	public void testKite2DVector2DArray() {
		//just create some kites to test the constructor
		//squares are kites
		new Kite2D(new Vector2D(0, 0), new Vector2D(2, 0), new Vector2D(2, 2), new Vector2D(0, 2));
		new Kite2D(new Vector2D(2, 2), new Vector2D(3, 2), new Vector2D(3, 3), new Vector2D(2, 3));
		//diamonds are also kites
		new Kite2D(new Vector2D(0, 0), new Vector2D(1, 1), new Vector2D(2, 1), new Vector2D(1, 0));
		new Kite2D(new Vector2D(1, 1), new Vector2D(2, 3), new Vector2D(3, 1), new Vector2D(2, -1));
		//other kites
		new Kite2D(new Vector2D(1, 1), new Vector2D(3, 2), new Vector2D(3, 3), new Vector2D(2, 3));
		new Kite2D(new Vector2D(1, 1), new Vector2D(2, 5), new Vector2D(3, 1), new Vector2D(2, 3));
	}
	
	@Test(expected = LinearAlgebraException.class)
	public void testKite2DVector2DArrayException() {
		//create wrong kites to cause an exception
		new Kite2D(new Vector2D(1, 1), new Vector2D(2, 3), new Vector2D(3, 1), new Vector2D(3, 0));
	}
	
	@Test
	public void testGetCenterOfGravity() {
		//square
		Kite2D kite = new Kite2D(new Vector2D(0, 0), new Vector2D(2, 0), new Vector2D(2, 2), new Vector2D(0, 2));
		assertEquals(new Vector2D(1, 1), kite.getCenterOfGravity());
		
		//diamond
		kite = new Kite2D(new Vector2D(1, 1), new Vector2D(2, 3), new Vector2D(3, 1), new Vector2D(2, -1));
		assertEquals(new Vector2D(2, 1), kite.getCenterOfGravity());
		
		//other kites
		kite = new Kite2D(new Vector2D(1, 1), new Vector2D(3, 2), new Vector2D(3, 3), new Vector2D(2, 3));
		assertEquals(new Vector2D(3, 2).add(new Vector2D(-1, 1).mult(0.5)), kite.getCenterOfGravity());
		
		//non-convex kite
		kite = new Kite2D(new Vector2D(1, 1), new Vector2D(2, 5), new Vector2D(3, 1), new Vector2D(2, 3));
		assertEquals(new Vector2D(2, 3), kite.getCenterOfGravity());
		
		kite = new Kite2D(new Vector2D(1, 1), new Vector2D(2, 0), new Vector2D(3, 1), new Vector2D(2, -1));
		assertEquals(new Vector2D(2, 0), kite.getCenterOfGravity());
	}
}