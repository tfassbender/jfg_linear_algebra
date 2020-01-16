package net.jfabricationgames.linear_algebra;

public class Triangle3D extends Plane3D {
	
	private Vector3D[] vertices;
	
	public Triangle3D(Vector3D vertex1, Vector3D vertex2, Vector3D vertex3) {
		super(vertex1, vertex2, vertex3);
		vertices = new Vector3D[3];
		vertices[0] = vertex1;
		vertices[1] = vertex2;
		vertices[2] = vertex3;
	}
	
	/**
	 * Check whether the line segment intersects the triangle by projecting the 3D triangle to a 2D triangle by ignoring the z-axis. Note that this
	 * will only work if the triangle is NOT in x-z or y-z area.
	 * 
	 * @param lineSegment
	 *        The line segment that may intersect this triangle.
	 * 
	 * @return Returns true if the line segment intersects this triangle.
	 */
	public boolean intersectsUnchecked(LineSegment3D lineSegment) {
		return getIntersectionPointUnchecked(lineSegment) != null;
	}
	
	/**
	 * Calculate the intersection point between the line and this triangle (if any). Checking whether the line intersects is done by projecting the 3D
	 * triangle to a 2D triangle by ignoring the z-axis. Note that this will only work if the triangle is NOT in x-z or y-z area.
	 * 
	 * @param lineSegment
	 *        The line segment that may intersect this triangle.
	 * 
	 * @return Returns the intersection point or null, if there is no intersection point.
	 */
	public Vector3D getIntersectionPointUnchecked(LineSegment3D lineSegment) {
		Vector3D intersectionPoint = getIntersectionPointLineSegment(lineSegment);
		
		if (intersectionPoint != null) {
			//check whether the intersection point is inside the triangle by projecting on the x-y-axis and checking the 2d area
			Triangle2D triangle2d = new Triangle2D(new Vector2D(vertices[0].x, vertices[0].y), new Vector2D(vertices[1].x, vertices[1].y),
					new Vector2D(vertices[2].x, vertices[2].y));
			
			if (triangle2d.isPointInArea(new Vector2D(intersectionPoint.x, intersectionPoint.y))) {
				return intersectionPoint;
			}
		}
		
		return null;
	}
}