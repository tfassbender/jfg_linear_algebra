package net.jfabricationgames.linear_algebra;

import static org.junit.Assert.*;

import org.junit.Test;

public class Matrix2DTest {
	
	public static final double EPSILON = 1e-8;

	@Test
	public void testMatrix2DOrientationVector3DArray() {
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), 
				new Vector3D(4, 5, 6), new Vector3D(7, 8, 9));
		Matrix2D m2 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), 
				new Vector3D(4, 5, 6), new Vector3D(7, 8, 9));
		
		assertEquals(1, m.at(0,  0), EPSILON);
		assertEquals(3, m.at(2,  0), EPSILON);
		assertEquals(8, m.at(1,  2), EPSILON);
		
		assertEquals(1, m2.at(0,  0), EPSILON);
		assertEquals(3, m2.at(0,  2), EPSILON);
		assertEquals(8, m2.at(2,  1), EPSILON);
	}

	@Test
	public void testMatrix2DIntInt() {
		Matrix2D m = new Matrix2D(2, 3);
		Matrix2D m2 = new Matrix2D(4, 3);
		
		assertArrayEquals(new int[] {2, 3}, m.getDimensions());
		assertArrayEquals(new int[] {4, 3}, m2.getDimensions());
	}

	@Test
	public void testClone() {
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), 
				new Vector3D(4, 5, 6), new Vector3D(7, 8, 9));
		Matrix2D m2 = m.clone();
		
		assertEquals(m.at(0, 0), m2.at(0,  0), EPSILON);
		assertEquals(m.at(0, 2), m2.at(0,  2), EPSILON);
		assertEquals(m.at(1, 0), m2.at(1,  0), EPSILON);
		
		m2.set(0, 0, 42);
		m2.set(1, 2, 42);

		assertEquals(42, m2.at(0,  0), EPSILON);
		assertEquals(42, m2.at(1,  2), EPSILON);
		assertEquals(1, m.at(0,  0), EPSILON);
		assertEquals(8, m.at(1,  2), EPSILON);
	}

	@Test
	public void testAt() {
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), 
				new Vector3D(4, 5, 6), new Vector3D(7, 8, 9));
		Matrix2D m2 = Matrix2D.getUnitMatrix(3);
		
		assertEquals(1, m.at(0,  0), EPSILON);
		assertEquals(3, m.at(2,  0), EPSILON);
		assertEquals(8, m.at(1,  2), EPSILON);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				assertEquals((i == j ? 1 : 0), m2.at(i, j), EPSILON);
			}
		}
	}

	@Test
	public void testSet() {
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), 
				new Vector3D(4, 5, 6), new Vector3D(7, 8, 9));
		
		m.set(0, 0, 42);
		m.set(2, 1, 42);
		m.set(1, 1, 42);
		
		//not changed
		assertEquals(3, m.at(2,  0), EPSILON);
		assertEquals(8, m.at(1,  2), EPSILON);
		
		//changed to 42
		assertEquals(42, m.at(0,  0), EPSILON);
		assertEquals(42, m.at(2,  1), EPSILON);
		assertEquals(42, m.at(1,  1), EPSILON);
	}

	@Test
	public void testTranspose() {
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), 
				new Vector3D(4, 5, 6), new Vector3D(7, 8, 9));
		Matrix2D m2 = m.transpose();
		
		Matrix2D m3 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3));
		Matrix2D m4 = m3.transpose();

		assertEquals(1, m2.at(0,  0), EPSILON);
		assertEquals(3, m2.at(2,  0), EPSILON);
		assertEquals(8, m2.at(1,  2), EPSILON);
		assertEquals(m.at(0, 0), m2.at(0,  0), EPSILON);
		assertEquals(m.at(0, 2), m2.at(2,  0), EPSILON);
		assertEquals(m.at(1, 0), m2.at(0,  1), EPSILON);
		
		assertEquals(2, m4.at(1, 0), EPSILON);
		assertEquals(3, m4.at(2, 0), EPSILON);
		
	}

	@Test
	public void testGetUnitMatrix() {
		Matrix2D m = Matrix2D.getUnitMatrix(1);
		Matrix2D m2 = Matrix2D.getUnitMatrix(3);
		Matrix2D m3 = Matrix2D.getUnitMatrix(42);
		
		assertArrayEquals(new int[] {1, 1}, m.getDimensions());
		assertArrayEquals(new int[] {3, 3}, m2.getDimensions());
		assertArrayEquals(new int[] {42, 42}, m3.getDimensions());
		
		assertEquals(1, m.at(0, 0), EPSILON);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				assertEquals((i == j ? 1 : 0), m2.at(i, j), EPSILON);
			}
		}
		for (int i = 0; i < 42; i++) {
			for (int j = 0; j < 42; j++) {
				assertEquals((i == j ? 1 : 0), m3.at(i, j), EPSILON);
			}
		}
	}

	@Test
	public void testGetDimensions() {
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), 
				new Vector3D(4, 5, 6), new Vector3D(7, 8, 9));
		Matrix2D m2 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), 
				new Vector3D(4, 5, 6), new Vector3D(7, 8, 9));
		
		Matrix2D m3 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3));
		Matrix2D m4 = m3.transpose();
		
		assertArrayEquals(new int[] {3, 3}, m.getDimensions());
		assertArrayEquals(new int[] {3, 3}, m2.getDimensions());
		assertArrayEquals(new int[] {3, 1}, m3.getDimensions());
		assertArrayEquals(new int[] {1, 3}, m4.getDimensions());
	}

}
