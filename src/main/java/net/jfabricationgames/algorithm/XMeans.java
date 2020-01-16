package net.jfabricationgames.algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import net.jfabricationgames.linear_algebra.Vector2D;

public class XMeans<T> {
	
	private List<T> points;
	private int kMin;
	private int kMax;
	private Function<T, Vector2D> vector2Dconverter;
	
	/**
	 * The distance to use when splitting a new center (if the default split strategy is used)
	 */
	private double splitDistanceAsAverageToCenters = 0.05;//5% of the average distance to other centers
	
	/**
	 * The strategy that is used to split a new center from one of the current centers
	 */
	private BiFunction<List<Vector2D>, Vector2D, Vector2D> splitStrategy = (centers, split) -> {
		//use 5% of the average distance between this center and all other centers as distance for the split
		double averageDistanceToCenters = centers.stream().mapToDouble(center -> center.distance(split)).sum() / (centers.size() - 1);
		Vector2D randomDirectionVector = new Vector2D(Math.random() * 360);
		randomDirectionVector = randomDirectionVector.setLength(averageDistanceToCenters * splitDistanceAsAverageToCenters);
		return split.add(randomDirectionVector);
	};
	
	/**
	 * A better solution is assumed if the average distance to all points in the cluster is improved by at least this amount (as percent value)
	 */
	private double improvementNeededToAcceptTheNewSolutionInPercent = 0.15;
	
	public XMeans(List<T> points, int kMin, int kMax, Function<T, Vector2D> vector2Dconverter) {
		this.points = points;
		this.kMin = kMin;
		this.kMax = kMax;
		this.vector2Dconverter = vector2Dconverter;
	}
	
	/**
	 * Find clusters using a (simplified) x-means algorithm
	 * 
	 * Algorithm:
	 * <ul>
	 * <li>Starting with a simple k means algorithm with k = kMin</li>
	 * <li>Split one of the clusters into two and check whether this is a better solution</li>
	 * <li>Better solutions is assumed if the average distance to all points in the cluster is now at least 15% lower (can be customized)</li>
	 * <li>Repeat until either kMax is reached or all clusters decline to be split</li>
	 * </ul>
	 */
	public Map<Vector2D, Set<T>> findClusters() {
		//start with simple k means algorithm with k = kMin
		Map<Vector2D, Set<T>> initialKMeans = findClusters_K_Means(points, null, kMin);
		Map<Vector2D, Set<T>> bestResult = initialKMeans;
		double bestResultAverageDistToAllPoints = calculateAverageDistanceFromCentersToClusterPoints(bestResult);
		
		int k = kMin;
		boolean clusterSplitted = true;
		List<Vector2D> centers = new ArrayList<Vector2D>(initialKMeans.keySet());
		
		//repeat until kMax is reached or all clusters decline to be split
		while (k < kMax && clusterSplitted) {
			clusterSplitted = false;
			k++;
			
			//try to split all centers (one after another)
			for (int i = 0; i < centers.size(); i++) {
				List<Vector2D> splitCenters = new ArrayList<Vector2D>(centers);
				Vector2D split = centers.get(i);
				
				//add a new center that was split from one of the current centers
				splitCenters.add(splitStrategy.apply(centers, split));
				
				//run a k-means with the new centers
				Map<Vector2D, Set<T>> newKMeansResult = findClusters_K_Means(points, splitCenters, k);
				double newAverageDistToAllPoints = calculateAverageDistanceFromCentersToClusterPoints(newKMeansResult);
				
				//use the new result if the average distance to all points is more than 15% reduced (customizable)
				if (newAverageDistToAllPoints < bestResultAverageDistToAllPoints * (1 - improvementNeededToAcceptTheNewSolutionInPercent)) {
					bestResult = newKMeansResult;
					bestResultAverageDistToAllPoints = newAverageDistToAllPoints;
					clusterSplitted = true;
					//don't split another cluster but restart the outer (while) loop
					break;
				}
			}
		}
		
		return bestResult;
	}
	
	private Map<Vector2D, Set<T>> findClusters_K_Means(List<T> points, List<Vector2D> initialCenters, int k) {
		KMeans<T> kMeans = new KMeans<>(k, points, vector2Dconverter, initialCenters);
		return kMeans.findClusters();
	}
	
	private double calculateAverageDistanceFromCentersToClusterPoints(Map<Vector2D, Set<T>> classification) {
		double avgDist = 0;
		for (Vector2D key : classification.keySet()) {
			avgDist += classification.get(key).stream().map(vector2Dconverter).mapToDouble(v -> v.distance(key)).sum()
					/ classification.get(key).size();
		}
		avgDist /= classification.size();
		
		return avgDist;
	}
	
	public double getSplitDistanceAsAverageToCenters() {
		return splitDistanceAsAverageToCenters;
	}
	public void setSplitDistanceAsAverageToCenters(double splitDistanceAsAverageToCenters) {
		this.splitDistanceAsAverageToCenters = splitDistanceAsAverageToCenters;
	}
	
	public BiFunction<List<Vector2D>, Vector2D, Vector2D> getSplitStrategy() {
		return splitStrategy;
	}
	public void setSplitStrategy(BiFunction<List<Vector2D>, Vector2D, Vector2D> splitStrategy) {
		this.splitStrategy = splitStrategy;
	}
	
	public double getImprovementNeededToAcceptTheNewSolutionInPercent() {
		return improvementNeededToAcceptTheNewSolutionInPercent;
	}
	public void setImprovementNeededToAcceptTheNewSolutionInPercent(double improvementNeededToAcceptTheNewSolutionInPercent) {
		this.improvementNeededToAcceptTheNewSolutionInPercent = improvementNeededToAcceptTheNewSolutionInPercent;
	}
}