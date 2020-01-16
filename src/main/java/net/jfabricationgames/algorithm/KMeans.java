package net.jfabricationgames.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import net.jfabricationgames.linear_algebra.Vector2D;

public class KMeans<T> {
	
	private int k;
	private List<T> points;
	private Function<T, Vector2D> vector2Dconverter;
	private List<Vector2D> initialCenters;
	
	/**
	 * Maximum distance for two center points to be treated as equal
	 */
	private double distanceThresholdForEqualCenters = 1e-5;
	
	public KMeans(int k, List<T> points, Function<T, Vector2D> vector2Dconverter, List<Vector2D> initialCenters) {
		this.k = k;
		this.points = points;
		this.vector2Dconverter = vector2Dconverter;
		this.initialCenters = initialCenters;
	}
	
	/**
	 * Find clusters using a k-means algorithm
	 * 
	 * @return Returns a map of center points to all points that are classified to the center (a set of T)
	 */
	public Map<Vector2D, Set<T>> findClusters() {
		if (points.isEmpty()) {
			throw new IllegalArgumentException("The list of fields mussn't be empty");
		}
		if (k < 2) {
			throw new IllegalArgumentException("The parameter k must be at least 2");
		}
		
		Vector2D min = new Vector2D();//upper left corner
		Vector2D max = new Vector2D();//lower right corner
		
		min.x = points.stream().map(vector2Dconverter).mapToDouble(v -> v.getX()).min()
				.orElseThrow(() -> new IllegalStateException("No coordinates were found"));
		min.y = points.stream().map(vector2Dconverter).mapToDouble(v -> v.getY()).min()
				.orElseThrow(() -> new IllegalStateException("No coordinates were found"));
		
		max.x = points.stream().map(vector2Dconverter).mapToDouble(v -> v.getX()).max()
				.orElseThrow(() -> new IllegalStateException("No coordinates were found"));
		max.y = points.stream().map(vector2Dconverter).mapToDouble(v -> v.getY()).max()
				.orElseThrow(() -> new IllegalStateException("No coordinates were found"));
		
		List<Vector2D> centers;
		if (initialCenters == null) {
			//initialize the centers with random values
			centers = new ArrayList<Vector2D>(k);
			for (int i = 0; i < k; i++) {
				double x = Math.random() * (max.x - min.x) + min.x;
				double y = Math.random() * (max.y - min.y) + min.y;
				centers.add(new Vector2D(x, y));
			}
		}
		else {
			if (initialCenters.size() != k) {
				throw new IllegalArgumentException("the number of initial centers has to be equal to k");
			}
			centers = new ArrayList<Vector2D>(initialCenters);
		}
		
		boolean centersChanged;//repeat until the centers are not changing anymore
		Map<Vector2D, Set<T>> finalClassification = null;
		
		do {
			Map<Vector2D, Set<T>> classifications = new HashMap<Vector2D, Set<T>>();
			//initialize the classification map
			centers.forEach(center -> classifications.put(center, new HashSet<T>()));
			
			//add all fields to the centers they belong to (the ones with the shortest distance)
			for (T field : points) {
				//start with the first center
				Vector2D center = centers.get(0);
				double centerDistance = center.distance(vector2Dconverter.apply(field));
				for (int i = 1; i < centers.size(); i++) {
					double distance = centers.get(i).distance(vector2Dconverter.apply(field));
					//found a center that is nearer to this field
					if (distance < centerDistance) {
						center = centers.get(i);
						centerDistance = distance;
					}
				}
				
				//add the field to the center that is the nearest to this field
				classifications.get(center).add(field);
			}
			
			//update the centers to the center of mass of all fields that are classified to the center
			List<Vector2D> newCenters = new ArrayList<Vector2D>(k);
			for (Set<T> classifiedFields : classifications.values()) {
				//calculate the center of the classified fields
				Vector2D center;
				if (!classifiedFields.isEmpty()) {
					//calculate center of mass
					center = classifiedFields.stream().map(vector2Dconverter).reduce((v1, v2) -> v1.add(v2))
							.orElseThrow(() -> new IllegalStateException("new center couldn't be found"));
					center = center.mult(1d / classifiedFields.size());
				}
				else {
					//use the minimum if there are no fields classified to this center (because then this center is useless now)
					center = min.clone();
				}
				
				newCenters.add(center);
			}
			
			centersChanged = false;
			
			for (Vector2D newCenter : newCenters) {
				boolean included = false;
				for (Vector2D oldCenter : centers) {
					included |= newCenter.distance(oldCenter) < distanceThresholdForEqualCenters;
				}
				//if one of the new centers is not included in the old center list the centers have changed
				centersChanged &= !included;
			}
			
			//repeat until the centers are not changing anymore
			centers = newCenters;
			
			if (!centersChanged) {
				finalClassification = classifications;
			}
		} while (centersChanged);
		
		return finalClassification;
	}
	
	public double getDistanceThresholdForEqualCenters() {
		return distanceThresholdForEqualCenters;
	}
	/**
	 * Set the distance that is the maximum for two center points to be treated as equal
	 */
	public void setDistanceThresholdForEqualCenters(double distanceThresholdForEqualCenters) {
		this.distanceThresholdForEqualCenters = distanceThresholdForEqualCenters;
	}
}