package net.jfabricationgames.algorithm;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.jfabricationgames.linear_algebra.Vector2D;

class KMeansTest {
	
	@Test
	public void test2Clusters_2Points() {
		List<Vector2D> points = Arrays.asList(new Vector2D(1, 1), new Vector2D(3, 3));
		List<Vector2D> initialCenters = Arrays.asList(new Vector2D(2, 1), new Vector2D(3, 4));
		KMeans<Vector2D> kMeans = new KMeans<>(2, points, initialCenters, v -> v);
		
		Map<Vector2D, Set<Vector2D>> clusters = kMeans.findClusters();
		
		//both points are centers
		assertTrue(clusters.keySet().contains(points.get(0)));
		assertTrue(clusters.keySet().contains(points.get(1)));
		//all clusters are made of only one point
		assertTrue(clusters.values().stream().allMatch(set -> set.size() == 1));
	}
	
	@Test
	public void test2Clusters_5Points() {
		List<Vector2D> points = Arrays.asList(new Vector2D(1, 1), new Vector2D(3, 3), new Vector2D(2, 1), new Vector2D(1, 2), new Vector2D(2, 2));
		List<Vector2D> initialCenters = Arrays.asList(new Vector2D(2, 1), new Vector2D(3, 4));
		KMeans<Vector2D> kMeans = new KMeans<>(2, points, initialCenters, v -> v);
		
		Map<Vector2D, Set<Vector2D>> clusters = kMeans.findClusters();
		
		assertTrue(clusters.keySet().contains(new Vector2D(1.5, 1.5)));
		assertTrue(clusters.keySet().contains(points.get(1)));
		
		assertTrue(clusters.get(points.get(1)).size() == 1);
		assertTrue(clusters.get(new Vector2D(1.5, 1.5)).size() == 4);
	}
	
	@Test
	public void test4Clusters_ManyPoints() {
		List<Vector2D> points = Arrays.asList(new Vector2D(1, 1), new Vector2D(3, 3), new Vector2D(2, 1), new Vector2D(1, 2), new Vector2D(2, 2),
				new Vector2D(5, 1), new Vector2D(6, 1), new Vector2D(1, 6), new Vector2D(1, 7), new Vector2D(1, 8));
		List<Vector2D> initialCenters = Arrays.asList(new Vector2D(2, 1), new Vector2D(3, 4), new Vector2D(4, 1), new Vector2D(1, 4));
		KMeans<Vector2D> kMeans = new KMeans<>(4, points, initialCenters, v -> v);
		
		Map<Vector2D, Set<Vector2D>> clusters = kMeans.findClusters();
		
		assertTrue(clusters.keySet().contains(new Vector2D(1.5, 1.5)));
		assertTrue(clusters.keySet().contains(new Vector2D(3, 3)));
		assertTrue(clusters.keySet().contains(new Vector2D(5.5, 1)));
		assertTrue(clusters.keySet().contains(new Vector2D(1, 7)));
		
		assertTrue(clusters.get(new Vector2D(1.5, 1.5)).size() == 4);
		assertTrue(clusters.get(new Vector2D(3, 3)).size() == 1);
		assertTrue(clusters.get(new Vector2D(5.5, 1)).size() == 2);
		assertTrue(clusters.get(new Vector2D(1, 7)).size() == 3);
	}
	
	@Test
	public void testFindTrivialAnswer() {
		List<Vector2D> points = Arrays.asList(new Vector2D(1, 1), new Vector2D(3, 3), new Vector2D(2, 1));
		KMeans<Vector2D> kMeans = new KMeans<>(3, points, null, v -> v);
		
		Map<Vector2D, Set<Vector2D>> clusters = kMeans.findTrivialAnswer();
		assertTrue(clusters.keySet().contains(points.get(0)));
		assertTrue(clusters.keySet().contains(points.get(1)));
		assertTrue(clusters.keySet().contains(points.get(2)));

		assertTrue(clusters.get(points.get(0)).size() == 1);
		assertTrue(clusters.get(points.get(1)).size() == 1);
		assertTrue(clusters.get(points.get(2)).size() == 1);
	}
}