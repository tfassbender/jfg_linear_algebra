package net.jfabricationgames.linear_algebra;

import static org.junit.Assert.*;

import org.junit.Test;

public class GaussTest {
	
	public static final double EPSILON = 1e-8;
	
	@Test
	public void testCalculateGauss() {
		//start with a very simple system
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.COL, new Vector2D(1, 2), new Vector2D(1, 3));
		double[] b = new double[] {1, 2};
		Gauss gauss = Gauss.calculateGauss(m, b);
		
		assertEquals(1, gauss.getM().at(0, 0), EPSILON);
		assertEquals(2, gauss.getM().at(1, 0), EPSILON);
		assertEquals(0, gauss.getM().at(0, 1), EPSILON);
		assertEquals(1, gauss.getM().at(1, 1), EPSILON);
		assertEquals(1, gauss.getB()[0], EPSILON);
		assertEquals(1, gauss.getB()[1], EPSILON);
		
		//a simple 3x3 system
		//1 2 3 | 6          1 2 3 | 6                1
		//1 3 5 | 9     =>   0 1 2 | 3      =>    b = 1
		//1 4 8 | 13         0 0 1 | 1                1
		Matrix2D m2 = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(1, 4, 8));
		double[] b2 = new double[] {6, 9, 13};
		Gauss gauss2 = Gauss.calculateGauss(m2, b2);
		
		//entries are ordered different...
		assertArrayEquals(new double[] {1, 0, 0}, gauss2.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {2, 1, 0}, gauss2.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {3, 2, 1}, gauss2.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {6, 3, 1}, gauss2.getB(), EPSILON);
		
		//a not so simple 3x3 system
		Matrix2D m3 = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 3, 5), new Vector3D(3, -3, 9), new Vector3D(-1, 7, 2));
		double[] b3 = new double[] {3, 7, 2};
		Gauss gauss3 = Gauss.calculateGauss(m3, b3);
		
		assertArrayEquals(new double[] {1, 0, 0}, gauss3.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {3, -12, 0}, gauss3.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {5, -6, 2}, gauss3.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {3, -2, 3.33333333}, gauss3.getB(), EPSILON);
		
		//solution calculated by internet gauss solver
		Matrix2D m4 = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(5, 7, 13), new Vector3D(7, 9, 2), new Vector3D(4, 8, 8));
		double[] b4 = new double[] {2, 11, 8};
		Gauss gauss4 = Gauss.calculateGauss(m4, b4);
		
		assertArrayEquals(new double[] {5, 0, 0}, gauss4.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {7, -4.0/5.0, 0}, gauss4.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {13, -81.0/5.0, -51.0}, gauss4.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {2, 8.2, 31}, gauss4.getB(), EPSILON);
		
		//a solution where the lines have to be switched
		Matrix2D m5 = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), new Vector3D(1, 2, 5), new Vector3D(3, 4, 6));
		double[] b5 = new double[] {10, 3, 9};
		Gauss gauss5 = Gauss.calculateGauss(m5, b5);
		
		//entries are ordered different...
		assertArrayEquals(new double[] {1, 0, 0}, gauss5.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {2, -2, 0}, gauss5.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {3, -3, 2}, gauss5.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {10, -21, -7}, gauss5.getB(), EPSILON);
	}
	
	@Test
	public void testRollback() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetXVec() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testGetSolutions() {
		fail("Not yet implemented");
	}
	
}
