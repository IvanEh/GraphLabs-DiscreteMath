package com.gmail.at.ivanehreshi.graph;

import java.util.ArrayList;
import java.util.Comparator;

public class BellmanFord implements PathFinder{	
	private int source;
	private double[] dist;
	private Integer[] pred;
	private OrientedGraph graph;
	private boolean failed = false; 
	
	public BellmanFord(OrientedGraph graph, int source){
		
		this.source = source;
		this.graph = graph;
		dist = new double[graph.verticesCount];
		pred = new Integer[graph.verticesCount];
	}
	
	
	
	public boolean findAll(){
		init();
		
		dist[source]= 0;
		
		boolean changes = true;
		int i;
		for(i = 1; i <= graph.verticesCount-1; i++){
			for(int j = 0; j < graph.verticesCount; j++){
				int u = j;
				for(EdgeTo edge: graph.adjacencyList.get(j)){
					int v = edge.to;
					double w = edge.w;
					relax(u, v, w);
				}
			}
		}
		
		changes = false;
		for(int j = 0; j < graph.verticesCount; j++){
			int u = j;
			for(EdgeTo edge: graph.adjacencyList.get(j)){
				int v = edge.to;
				double w = edge.w;
				changes = changes || relax(u, v, w);
			}
		}
		
		failed = changes;
		return changes;
	}

	public boolean relax(int u, int v, double w) {
		double newWeight =dist[u] + w; 
		if(dist[v] > newWeight){
			dist[v] = newWeight;
			pred[v] = u;
			return true;
		}
		return false;
	}

	/**
	 * get the path from source to <b>to</b> in reversed order
	 * @param to
	 * @return 
	 */
	public ArrayList<Integer> getPath(int to){
		if(to < 0 || failed)
			return null;
		ArrayList<Integer> path = new ArrayList<Integer>();
		Integer x = to;
		while(x != null){
			path.add(x);
			x = pred[x];
		}
		return path;
	}
	
	private void init() {
		for(int i = 0; i < graph.verticesCount; i++){
			dist[i] = INF;
			pred[i] = null;
		}
		pred = new Integer[graph.verticesCount];
		
	}



	public double[] getDist() {
		return dist;
	}
}
