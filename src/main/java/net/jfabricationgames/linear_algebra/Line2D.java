package net.jfabricationgames.linear_algebra;

public class Line2D {
	
	protected Vector2D start;
	protected Vector2D direction;
	
	public Line2D(Vector2D start, Vector2D direction) {
		this.start = start;
		this.direction = direction;
		if (direction.equals(Vector2D.NULL_VEC)) {
			throw new LinearAlgebraException("A Line's direction vector can't be a null-vector.");
		}
	}
	
	/**
	 * Calculate the crossing point of two lines.
	 * 
	 * @param line
	 * 		The line which's crossing-point with this line is calculated.
	 * 
	 * @return
	 * 		The crossing point of the two lines.
	 * 		If there is no crossing point because of parallel lines or there are infinite crossing points because the lines are equal all coordinates are Double.NaN.
	 */
	public Vector2D calculateCrosspoint(Line2D line) {
		/*double a1 = direction.y-start.y;
		double b1 = start.x-direction.x;
		double c1 = a1 * start.x - b1 * start.y;
		
		double a2 = line.direction.y-line.start.y;
		double b2 = line.start.x-line.direction.x;
		double c2 = a2 * line.start.x - b1 * line.start.y;
		
		double denom = a1*b2 - a2*b1;
		if (Math.abs(denom) < 1e-5) {
			//the two lines are parallel
			return Vector2D.NAN_VEC;
		}
		
		double x = (b2*c1 - b1*c2)/denom;
		double y = (a1*c2 - a2*c1)/denom;
		
		return new Vector2D(x, y);*/
		
		//http://www-cs.ccny.cuny.edu/~wolberg/capstone/intersection/Intersection%20point%20of%20two%20lines.html
		double x1 = start.x;
		double x2 = start.x + direction.x;
		double x3 = line.start.x;
		double x4 = line.start.x + line.direction.x;

		double y1 = start.y;
		double y2 = start.y + direction.y;
		double y3 = line.start.y;
		double y4 = line.start.y + line.direction.y;
		
		double ua = ((x4-x3)*(y1-y3) - (y4-y3)*(x1-x3)) / ((y4-y3)*(x2-x1) - (x4-x3)*(y2-y1));
		
		double x = x1 + ua * (x2-x1);
		double y = y1 + ua * (y2-y1);
		
		return new Vector2D(x, y);
	}
	
	/**
	 * Calculate the minimum distance between a point and this line.
	 * 
	 * @param point
	 * 		The point.
	 * 
	 * @return
	 * 		The minimum distance between the point and this line.
	 */
	public double calculateMinDistToPoint(Vector2D point) {
        /*try {
            //find the orthogonal vector on line
        	double[] normVec = new double[] {-direction.y * 100.0 / direction.x, 100.0};//define the second part as 100
        	//calculate the parameters
        	double t = (start.y - point.y + (normVec[1]/normVec[0]) * (point.x - start.x)) / (direction.x * (normVec[1]/normVec[0]) - direction.y);
        	double a = (start.x + t*direction.x - point.x) / normVec[0];
        	//calculate the intersection point of the two lines
        	double[] inter = new double[] {point.x + a*normVec[0], point.y + a*normVec[1]};
        	return Math.hypot(inter[0]-point.x, inter[1]-point.y);
        }
        catch (Exception e) {
            return Double.NaN;
        }*/
		
		//https://www.topcoder.com/community/data-science/data-science-tutorials/geometry-concepts-basic-concepts/
		
		//project the vector from a point of the line on the line; then calculate the distance between the two points
		Vector2D toPoint = start.vectorTo(point);
		Vector2D projected = direction.project(toPoint);
		Vector2D p2 = start.add(projected);
		return point.distance(p2);
    }
	
	/**
	 * Check whether a point is on this line.
	 * 
	 * @param point
	 * 		The checked point.
	 * 
	 * @return
	 * 		True if the point is on the line. False otherwise.
	 */
	public boolean isPointOnLine(Vector2D point) {
		//the calculation of the parameter t is equal in all cases (x and y).
		double t1 = direction.x == 0 ? 0 : -((start.x - point.x) / direction.x);
		double t2 = direction.y == 0 ? 0 : -((start.y - point.y) / direction.y);
		if (direction.x == 0) {
			return Math.abs(start.x - point.x) < 1e-8;
		}
		else if (direction.y == 0) {
			return Math.abs(start.y - point.y) < 1e-8;
		}
		return Math.abs(t1-t2) < 1e-5;
	}
	
	/**
	 * Calculate the parameter t which describes how often the vector direction is to be added to the start vector to get to the point.
	 * 
	 * @param point
	 * 		The checked point.
	 * 
	 * @return
	 * 		If the point is on the line the parameter t is returned. Double.NaN otherwise.
	 */
	public double calculateT(Vector2D point) {
		if (isPointOnLine(point)) {
			if (direction.x != 0) {
				return -((start.x - point.x) / direction.x);				
			}
			else {
				return -((start.y - point.y) / direction.y);
			}
			//if direction.x and direction.y is 0 it's no line...
		}
		return Double.NaN;
	}
	
	public Vector2D getStart() {
		return start;
	}
	public Vector2D getDirection() {
		return direction;
	}
}