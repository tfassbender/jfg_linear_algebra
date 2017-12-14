package net.jfabricationgames.linear_algebra;

import static org.junit.Assert.*;

import org.junit.Test;

public class GaussTest {
	
	public static final double EPSILON = 1e-8;
	
	@Test
	public void testCalculateGauss() {
		//start with a very simple system
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.ROW, new Vector2D(1, 2), new Vector2D(1, 3));
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
		//1 3 5 | 9     =>   0 1 2 | 3      =>    x = 1
		//1 4 8 | 13         0 0 1 | 1                1
		Matrix2D m2 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(1, 4, 8));
		double[] b2 = new double[] {6, 9, 13};
		Gauss gauss2 = Gauss.calculateGauss(m2, b2);
		
		//entries are ordered different...
		assertArrayEquals(new double[] {1, 0, 0}, gauss2.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {2, 1, 0}, gauss2.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {3, 2, 1}, gauss2.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {6, 3, 1}, gauss2.getB(), EPSILON);
		
		//a not so simple 3x3 system
		Matrix2D m3 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 3, 5), new Vector3D(3, -3, 9), new Vector3D(-1, 7, 2));
		double[] b3 = new double[] {3, 7, 2};
		Gauss gauss3 = Gauss.calculateGauss(m3, b3);
		
		assertArrayEquals(new double[] {1, 0, 0}, gauss3.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {3, -12, 0}, gauss3.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {5, -6, 2}, gauss3.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {3, -2, 3.33333333}, gauss3.getB(), EPSILON);
		
		//solution calculated by internet gauss solver
		Matrix2D m4 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(5, 7, 13), new Vector3D(7, 9, 2), new Vector3D(4, 8, 8));
		double[] b4 = new double[] {2, 11, 8};
		Gauss gauss4 = Gauss.calculateGauss(m4, b4);
		
		assertArrayEquals(new double[] {5, 0, 0}, gauss4.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {7, -4.0/5.0, 0}, gauss4.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {13, -81.0/5.0, -51.0}, gauss4.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {2, 8.2, 31}, gauss4.getB(), EPSILON);
		
		//a solution where the lines have to be switched
		Matrix2D m5 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), new Vector3D(1, 2, 5), new Vector3D(3, 4, 6));
		double[] b5 = new double[] {10, 3, 9};
		Gauss gauss5 = Gauss.calculateGauss(m5, b5);
		
		//entries are ordered different...
		assertArrayEquals(new double[] {1, 0, 0}, gauss5.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {2, -2, 0}, gauss5.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {3, -3, 2}, gauss5.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {10, -21, -7}, gauss5.getB(), EPSILON);
		
		//a system where the swapping is not possible (but can be solved with multiple solutions with the given b-vector)
		Matrix2D m6 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), new Vector3D(1, 2, 5), new Vector3D(2, 4, 8));
		double[] b6 = new double[] {10, 3, 13};
		Gauss gauss6 = Gauss.calculateGauss(m6, b6);
		
		//entries are ordered different...
		assertArrayEquals(new double[] {1, 0, 0}, gauss6.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {2, 0, 0}, gauss6.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {3, 2, 2}, gauss6.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {10, -7, -7}, gauss6.getB(), EPSILON);
		
		//another system where the swapping is not possible (but can NOT be solved with the given b-vector; but the algorithm can be iterated)
		Matrix2D m7 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), new Vector3D(1, 2, 5), new Vector3D(2, 4, 8));
		double[] b7 = new double[] {10, 3, 0};
		Gauss gauss7 = Gauss.calculateGauss(m7, b7);
		
		//entries are ordered different...
		assertArrayEquals(new double[] {1, 0, 0}, gauss7.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {2, 0, 0}, gauss7.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {3, 2, 2}, gauss7.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {10, -7, -20}, gauss7.getB(), EPSILON);
		
		//a 4x3 system
		Matrix2D m8 = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(2, 8, 8), new Vector3D(3, 8, 5));
		double[] b8 = new double[] {1, 1, 1};
		Gauss gauss8 = Gauss.calculateGauss(m8, b8);
		
		//entries are ordered different...
		assertArrayEquals(new double[] {1, 0, 0}, gauss8.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {1, 1, 0}, gauss8.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {2, 4, -6}, gauss8.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {3, 2, -8}, gauss8.getM().entries[3], EPSILON);
		assertArrayEquals(new double[] {1, -1, 0}, gauss8.getB(), EPSILON);
		
		//a similar 4x3 system but this one can only be solved with two parameters (which this implementation can't calculate)
		Matrix2D m9 = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(2, 8, 14), new Vector3D(3, 8, 13));
		double[] b9 = new double[] {1, 1, 1};
		Gauss gauss9 = Gauss.calculateGauss(m9, b9);
		
		//entries are ordered different...
		assertArrayEquals(new double[] {1, 0, 0}, gauss9.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {1, 1, 0}, gauss9.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {2, 4, 0}, gauss9.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {3, 2, 0}, gauss9.getM().entries[3], EPSILON);
		assertArrayEquals(new double[] {1, -1, 0}, gauss9.getB(), EPSILON);
		
		//another similar 3x4 system but this one can NOT be solved (no parameters; it just has no solution)
		Matrix2D m10 = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(2, 8, 14), new Vector3D(3, 8, 13));
		double[] b10 = new double[] {1, 1, 2};
		Gauss gauss10 = Gauss.calculateGauss(m10, b10);
		
		//entries are ordered different...
		assertArrayEquals(new double[] {1, 0, 0}, gauss10.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {1, 1, 0}, gauss10.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {2, 4, 0}, gauss10.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {3, 2, 0}, gauss10.getM().entries[3], EPSILON);
		assertArrayEquals(new double[] {1, -1, 1}, gauss10.getB(), EPSILON);
		
		//a 3x2 system with a single solution
		Matrix2D m11 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector2D(1, 2), new Vector2D(3, 8), new Vector2D(4, 10));
		double[] b11 = new double[] {1, 2, 3};
		Gauss gauss11 = Gauss.calculateGauss(m11, b11);
		
		//entries are ordered different...
		assertArrayEquals(new double[] {1, 0, 0}, gauss11.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {2, 2, 0}, gauss11.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {1, -1, 0}, gauss11.getB(), EPSILON);
		
		//a similar 3x2 system with no solution
		Matrix2D m12 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector2D(1, 2), new Vector2D(3, 8), new Vector2D(4, 10));
		double[] b12 = new double[] {1, 2, 4};
		Gauss gauss12 = Gauss.calculateGauss(m12, b12);
		
		//entries are ordered different...
		assertArrayEquals(new double[] {1, 0, 0}, gauss12.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {2, 2, 0}, gauss12.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {1, -1, 1}, gauss12.getB(), EPSILON);
	}
	
	@Test
	public void testRollback() {
		//a simple 3x3 system
		//1 2 3 | 6          1 2 3 | 6                1
		//1 3 5 | 9     =>   0 1 2 | 3      =>    x = 1
		//1 4 8 | 13         0 0 1 | 1                1
		Matrix2D m2 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(1, 4, 8));
		double[] b2 = new double[] {6, 9, 13};
		Gauss gauss2 = Gauss.calculateGauss(m2, b2);
		
		//check before the calculation, after the calculation and after the rollback
		assertArrayEquals(new double[] {1, 0, 0}, gauss2.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {2, 1, 0}, gauss2.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {3, 2, 1}, gauss2.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {6, 3, 1}, gauss2.getB(), EPSILON);
		
		gauss2.getXVec();
		
		//values changed here because of the calculation
		assertArrayEquals(new double[] {1, 0, 0}, gauss2.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {0, 1, 0}, gauss2.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {0, 0, 1}, gauss2.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {1, 1, 1}, gauss2.getB(), EPSILON);
		
		gauss2.rollback();
		
		//values should be like before the calculation again
		assertArrayEquals(new double[] {1, 0, 0}, gauss2.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {2, 1, 0}, gauss2.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {3, 2, 1}, gauss2.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {6, 3, 1}, gauss2.getB(), EPSILON);
		

		//a 4x3 system
		Matrix2D m8 = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(2, 8, 8), new Vector3D(3, 8, 5));
		double[] b8 = new double[] {1, 1, 1};
		Gauss gauss8 = Gauss.calculateGauss(m8, b8);

		//check before the calculation and after the rollback (entries after calculation are not really necessary for a working rollback...)
		assertArrayEquals(new double[] {1, 0, 0}, gauss8.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {1, 1, 0}, gauss8.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {2, 4, -6}, gauss8.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {3, 2, -8}, gauss8.getM().entries[3], EPSILON);
		assertArrayEquals(new double[] {1, -1, 0}, gauss8.getB(), EPSILON);
		
		gauss8.getSolutions();
		
		gauss8.rollback();
		
		//values should be like before the calculation again
		assertArrayEquals(new double[] {1, 0, 0}, gauss8.getM().entries[0], EPSILON);
		assertArrayEquals(new double[] {1, 1, 0}, gauss8.getM().entries[1], EPSILON);
		assertArrayEquals(new double[] {2, 4, -6}, gauss8.getM().entries[2], EPSILON);
		assertArrayEquals(new double[] {3, 2, -8}, gauss8.getM().entries[3], EPSILON);
		assertArrayEquals(new double[] {1, -1, 0}, gauss8.getB(), EPSILON);
	}
	
	@Test
	public void testGetXVec() {
		//using the same systems as in the first test (calculateGauss) here
		
		//start with a very simple system
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.ROW, new Vector2D(1, 2), new Vector2D(1, 3));
		double[] b = new double[] {1, 2};
		Gauss gauss = Gauss.calculateGauss(m, b);
		
		assertArrayEquals(new double[] {-1, 1}, gauss.getXVec(), EPSILON);
		
		//a simple 3x3 system
		//1 2 3 | 6          1 2 3 | 6                1
		//1 3 5 | 9     =>   0 1 2 | 3      =>    x = 1
		//1 4 8 | 13         0 0 1 | 1                1
		Matrix2D m2 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(1, 4, 8));
		double[] b2 = new double[] {6, 9, 13};
		Gauss gauss2 = Gauss.calculateGauss(m2, b2);

		assertArrayEquals(new double[] {1, 1, 1}, gauss2.getXVec(), EPSILON);
		
		//a not so simple 3x3 system
		Matrix2D m3 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 3, 5), new Vector3D(3, -3, 9), new Vector3D(-1, 7, 2));
		double[] b3 = new double[] {3, 7, 2};
		Gauss gauss3 = Gauss.calculateGauss(m3, b3);

		assertArrayEquals(new double[] {-10d/3, -2d/3, 5d/3}, gauss3.getXVec(), EPSILON);
		
		//solution calculated by internet gauss solver
		Matrix2D m4 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(5, 7, 13), new Vector3D(7, 9, 2), new Vector3D(4, 8, 8));
		double[] b4 = new double[] {2, 11, 8};
		Gauss gauss4 = Gauss.calculateGauss(m4, b4);

		assertArrayEquals(new double[] {-46d/51, 35d/17, -31d/51}, gauss4.getXVec(), EPSILON);
		
		//a solution where the lines have to be switched
		Matrix2D m5 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), new Vector3D(1, 2, 5), new Vector3D(3, 4, 6));
		double[] b5 = new double[] {10, 3, 9};
		Gauss gauss5 = Gauss.calculateGauss(m5, b5);

		assertArrayEquals(new double[] {-11d, 63d/4, -7d/2}, gauss5.getXVec(), EPSILON);
		
		//a system where the swapping is not possible (but can be solved with multiple solutions with the given b-vector)
		Matrix2D m6 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), new Vector3D(1, 2, 5), new Vector3D(2, 4, 8));
		double[] b6 = new double[] {10, 3, 13};
		Gauss gauss6 = Gauss.calculateGauss(m6, b6);
		
		try {
			gauss6.getXVec();
			fail("A LinearAlgebraException was expected here...");
		}
		catch (LinearAlgebraException lae) {
			//do nothing here because the exception is expected
		}
		
		//another system where the swapping is not possible (but can NOT be solved with the given b-vector)
		Matrix2D m7 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), new Vector3D(1, 2, 5), new Vector3D(2, 4, 8));
		double[] b7 = new double[] {10, 3, 0};
		Gauss gauss7 = Gauss.calculateGauss(m7, b7);
		
		assertNull(gauss7.getXVec());
		
		//a 4x3 system
		Matrix2D m8 = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(2, 8, 8), new Vector3D(3, 8, 5));
		double[] b8 = new double[] {1, 1, 1};
		Gauss gauss8 = Gauss.calculateGauss(m8, b8);
		
		try {
			gauss8.getXVec();
			fail("A LinearAlgebraException was expected here...");
		}
		catch (LinearAlgebraException lae) {
			//do nothing here because the exception is expected
		}
		
		//a similar 4x3 system but this one can only be solved with two parameters (which this implementation can't calculate)
		Matrix2D m9 = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(2, 8, 14), new Vector3D(3, 8, 13));
		double[] b9 = new double[] {1, 1, 1};
		Gauss gauss9 = Gauss.calculateGauss(m9, b9);

		try {
			gauss9.getXVec();
			fail("A LinearAlgebraException was expected here...");
		}
		catch (LinearAlgebraException lae) {
			//do nothing here because the exception is expected
		}
		
		//another similar 3x4 system but this one can NOT be solved (no parameters; it just has no solution)
		Matrix2D m10 = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(2, 8, 14), new Vector3D(3, 8, 13));
		double[] b10 = new double[] {1, 1, 2};
		Gauss gauss10 = Gauss.calculateGauss(m10, b10);
		
		assertNull(gauss10.getXVec());
		
		//a 3x2 system with a single solution
		Matrix2D m11 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector2D(1, 2), new Vector2D(3, 8), new Vector2D(4, 10));
		double[] b11 = new double[] {1, 2, 3};
		Gauss gauss11 = Gauss.calculateGauss(m11, b11);
		
		//assertArrayEquals(new double[] {2, -1d/2}, gauss11.getXVec(), EPSILON);

		try {
			gauss11.getXVec();
			fail("A LinearAlgebraException was expected here...");
		}
		catch (LinearAlgebraException lae) {
			//do nothing here because the exception is expected
		}
		
		//a similar 3x2 system with no solution
		Matrix2D m12 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector2D(1, 2), new Vector2D(3, 8), new Vector2D(4, 10));
		double[] b12 = new double[] {1, 2, 4};
		Gauss gauss12 = Gauss.calculateGauss(m12, b12);
		
		assertNull(gauss12.getXVec());
	}
	
	@Test
	public void testGetSolutions() {
		//using the same systems as in the first test (calculateGauss) here
		
		//start with a very simple system
		Matrix2D m = new Matrix2D(Matrix2D.Orientation.ROW, new Vector2D(1, 2), new Vector2D(1, 3));
		double[] b = new double[] {1, 2};
		Gauss gauss = Gauss.calculateGauss(m, b);
		
		double[][] solutions = gauss.getSolutions();
		assertArrayEquals(new double[] {-1, 1}, solutions[0], EPSILON);
		assertArrayEquals(new double[] {0, 0}, solutions[1], EPSILON);
		
		//a simple 3x3 system
		//1 2 3 | 6          1 2 3 | 6                1
		//1 3 5 | 9     =>   0 1 2 | 3      =>    x = 1
		//1 4 8 | 13         0 0 1 | 1                1
		Matrix2D m2 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(1, 4, 8));
		double[] b2 = new double[] {6, 9, 13};
		Gauss gauss2 = Gauss.calculateGauss(m2, b2);
		
		double[][] solutions2 = gauss2.getSolutions();
		assertArrayEquals(new double[] {1, 1, 1}, solutions2[0], EPSILON);
		assertArrayEquals(new double[] {0, 0, 0}, solutions2[1], EPSILON);
		
		//a not so simple 3x3 system
		Matrix2D m3 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 3, 5), new Vector3D(3, -3, 9), new Vector3D(-1, 7, 2));
		double[] b3 = new double[] {3, 7, 2};
		Gauss gauss3 = Gauss.calculateGauss(m3, b3);
		
		double[][] solutions3 = gauss3.getSolutions();
		assertArrayEquals(new double[] {-10d/3, -2d/3, 5d/3}, solutions3[0], EPSILON);
		assertArrayEquals(new double[] {0, 0, 0}, solutions3[1], EPSILON);
		
		//solution calculated by internet gauss solver
		Matrix2D m4 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(5, 7, 13), new Vector3D(7, 9, 2), new Vector3D(4, 8, 8));
		double[] b4 = new double[] {2, 11, 8};
		Gauss gauss4 = Gauss.calculateGauss(m4, b4);
		
		double[][] solutions4 = gauss4.getSolutions();
		assertArrayEquals(new double[] {-46d/51, 35d/17, -31d/51}, solutions4[0], EPSILON);
		assertArrayEquals(new double[] {0, 0, 0}, solutions4[1], EPSILON);
		
		//a solution where the lines have to be switched
		Matrix2D m5 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), new Vector3D(1, 2, 5), new Vector3D(3, 4, 6));
		double[] b5 = new double[] {10, 3, 9};
		Gauss gauss5 = Gauss.calculateGauss(m5, b5);
		
		double[][] solutions5 = gauss5.getSolutions();
		assertArrayEquals(new double[] {-11d, 63d/4, -7d/2}, solutions5[0], EPSILON);
		assertArrayEquals(new double[] {0, 0, 0}, solutions5[1], EPSILON);
		
		//a system where the swapping is not possible (but can be solved with multiple solutions with the given b-vector)
		Matrix2D m6 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), new Vector3D(1, 2, 5), new Vector3D(2, 4, 8));
		double[] b6 = new double[] {10, 3, 13};
		Gauss gauss6 = Gauss.calculateGauss(m6, b6);
		
		double[][] solutions6 = gauss6.getSolutions();
		assertArrayEquals(new double[] {41d/2, 0, -7d/2}, solutions6[0], EPSILON);
		assertArrayEquals(new double[] {-2, 1, 0}, solutions6[1], EPSILON);//x2 is the free parameter
		
		//another system where the swapping is not possible (but can NOT be solved with the given b-vector)
		Matrix2D m7 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector3D(1, 2, 3), new Vector3D(1, 2, 5), new Vector3D(2, 4, 8));
		double[] b7 = new double[] {10, 3, 0};
		Gauss gauss7 = Gauss.calculateGauss(m7, b7);
		
		assertNull(gauss7.getSolutions());
		
		//a 4x3 system
		Matrix2D m8 = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(2, 8, 8), new Vector3D(3, 8, 5));
		double[] b8 = new double[] {1, 1, 1};
		Gauss gauss8 = Gauss.calculateGauss(m8, b8);

		double[][] solutions8 = gauss8.getSolutions();
		assertArrayEquals(new double[] {2, -1, 0, 0}, solutions8[0], EPSILON);
		assertArrayEquals(new double[] {-11d/3, 10d/3, -4d/3, 1}, solutions8[1], EPSILON);
		
		//a similar 4x3 system but this one can only be solved with two parameters (which this implementation can't calculate)
		Matrix2D m9 = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(2, 8, 14), new Vector3D(3, 8, 13));
		double[] b9 = new double[] {1, 1, 1};
		Gauss gauss9 = Gauss.calculateGauss(m9, b9);

		try {
			gauss9.getSolutions();
			fail("A LinearAlgebraException was expected here...");
		}
		catch (LinearAlgebraException lae) {
			//do nothing here because the exception is expected
		}
		
		//another similar 3x4 system but this one can NOT be solved (no parameters; it just has no solution)
		Matrix2D m10 = new Matrix2D(Matrix2D.Orientation.COL, new Vector3D(1, 2, 3), new Vector3D(1, 3, 5), new Vector3D(2, 8, 14), new Vector3D(3, 8, 13));
		double[] b10 = new double[] {1, 1, 2};
		Gauss gauss10 = Gauss.calculateGauss(m10, b10);
		
		assertNull(gauss10.getSolutions());
		
		//a 3x2 system with a single solution
		Matrix2D m11 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector2D(1, 2), new Vector2D(3, 8), new Vector2D(4, 10));
		double[] b11 = new double[] {1, 2, 3};
		Gauss gauss11 = Gauss.calculateGauss(m11, b11);
		
		double[][] solutions11 = gauss11.getSolutions();
		assertArrayEquals(new double[] {2, -1d/2}, solutions11[0], EPSILON);
		assertArrayEquals(new double[] {0, 0}, solutions11[1], EPSILON);
		
		//a similar 3x2 system with no solution
		Matrix2D m12 = new Matrix2D(Matrix2D.Orientation.ROW, new Vector2D(1, 2), new Vector2D(3, 8), new Vector2D(4, 10));
		double[] b12 = new double[] {1, 2, 4};
		Gauss gauss12 = Gauss.calculateGauss(m12, b12);
		
		assertNull(gauss12.getSolutions());
	}
}