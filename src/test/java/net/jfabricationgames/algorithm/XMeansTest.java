package net.jfabricationgames.algorithm;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import net.jfabricationgames.linear_algebra.Vector2D;

class XMeansTest {
	
	@Test
	public void test2Clusters_2Points() {
		List<Vector2D> points = Arrays.asList(new Vector2D(1, 1), new Vector2D(3, 3));
		List<Vector2D> initialCenters = Arrays.asList(new Vector2D(2, 1), new Vector2D(3, 4));
		XMeans<Vector2D> xMeans = new XMeans<>(points, 2, 2, initialCenters, v -> v);
		
		Map<Vector2D, Set<Vector2D>> clusters = xMeans.findClusters();
		
		assertEquals(2, clusters.size());
		//both points are centers
		assertTrue(clusters.keySet().contains(points.get(0)));
		assertTrue(clusters.keySet().contains(points.get(1)));
		//all clusters are made of only one point
		assertTrue(clusters.values().stream().allMatch(set -> set.size() == 1));
	}
	
	@Test
	public void test2To3Clusters_5Points() {
		List<Vector2D> points = Arrays.asList(new Vector2D(1, 1), new Vector2D(3, 3), new Vector2D(2, 1), new Vector2D(1, 2), new Vector2D(2, 2));
		List<Vector2D> initialCenters = Arrays.asList(new Vector2D(2, 1), new Vector2D(3, 4));
		XMeans<Vector2D> xMeans = new XMeans<>(points, 2, 3, initialCenters, v -> v);
		xMeans.setImprovementNeededToAcceptTheNewSolution(0.5);//set a high improvement needed to not split
		
		Map<Vector2D, Set<Vector2D>> clusters = xMeans.findClusters();
		
		assertTrue(clusters.keySet().contains(new Vector2D(1.5, 1.5)));
		assertTrue(clusters.keySet().contains(points.get(1)));
		
		assertTrue(clusters.get(points.get(1)).size() == 1);
		assertTrue(clusters.get(new Vector2D(1.5, 1.5)).size() == 4);
	}
	
	@Test
	public void test2To4Clusters_ManyPoints() {
		List<Vector2D> points = Arrays.asList(new Vector2D(1, 1), new Vector2D(3, 3), new Vector2D(2, 1), new Vector2D(1, 2), new Vector2D(2, 2),
				new Vector2D(5, 1), new Vector2D(6, 1), new Vector2D(1, 6), new Vector2D(1, 7), new Vector2D(1, 8));
		List<Vector2D> initialCenters = Arrays.asList(new Vector2D(4, 1), new Vector2D(1, 4));
		XMeans<Vector2D> xMeans = new XMeans<>(points, 2, 4, initialCenters, v -> v);
		
		Map<Vector2D, Set<Vector2D>> clusters = xMeans.findClusters();
		
		assertTrue(clusters.keySet().contains(new Vector2D(1.8, 1.8)));
		assertTrue(clusters.keySet().contains(new Vector2D(5, 1)));
		assertTrue(clusters.keySet().contains(new Vector2D(6, 1)));
		assertTrue(clusters.keySet().contains(new Vector2D(1, 7)));
		
		assertTrue(clusters.get(new Vector2D(1.8, 1.8)).size() == 5);
		assertTrue(clusters.get(new Vector2D(5, 1)).size() == 1);
		assertTrue(clusters.get(new Vector2D(6, 1)).size() == 1);
		assertTrue(clusters.get(new Vector2D(1, 7)).size() == 3);
	}
	
	@Test
	public void test2To3Clusters_ManyPoints() {
		List<Vector2D> points = Arrays.asList(new Vector2D(1, 1), new Vector2D(3, 3), new Vector2D(2, 1), new Vector2D(1, 2), new Vector2D(2, 2),
				new Vector2D(5, 1), new Vector2D(6, 1), new Vector2D(1, 6), new Vector2D(1, 7), new Vector2D(1, 8));
		List<Vector2D> initialCenters = Arrays.asList(new Vector2D(4, 1), new Vector2D(1, 4));
		XMeans<Vector2D> xMeans = new XMeans<>(points, 2, 3, initialCenters, v -> v);
		
		Map<Vector2D, Set<Vector2D>> clusters = xMeans.findClusters();
		
		assertTrue(clusters.keySet().contains(new Vector2D(1.8, 1.8)));
		assertTrue(clusters.keySet().contains(new Vector2D(5.5, 1)));
		assertTrue(clusters.keySet().contains(new Vector2D(1, 7)));
		
		assertTrue(clusters.get(new Vector2D(1.8, 1.8)).size() == 5);
		assertTrue(clusters.get(new Vector2D(5.5, 1)).size() == 2);
		assertTrue(clusters.get(new Vector2D(1, 7)).size() == 3);
	}
}