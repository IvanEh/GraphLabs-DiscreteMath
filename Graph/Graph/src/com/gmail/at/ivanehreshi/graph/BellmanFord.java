package com.gmail.at.ivanehreshi.graph;

import java.util.ArrayList;
import java.util.Comparator;

public class BellmanFord implements PathFinder{
	public final int INF = Integer.MAX_VALUE - 100;
	
	private int source;
	private int[] dist;
	private Integer[] pred;
	private OrientedGraph graph;
	
	public BellmanFord(OrientedGraph graph, int source) throws Exception{
		if(!graph.isWeighted)
			throw new Exception();
		
		this.source = source;
		this.graph = graph;
		dist = new int[graph.verticesCount];
		pred = new Integer[graph.verticesCount];
	}
	
	
	
	public boolean findAll(){
		init();
		
		dist[source]= 0;
		
		boolean changes = true;
		for(int i = 0; i < graph.verticesCount-1 && changes; i++){
			changes = false;
			for(int j = 0; j < graph.verticesCount; j++){
				int u = j;
				for(EdgeTo edge: graph.adjacencyList.get(j)){
					int v = edge.to;
					int w = edge.w;
					changes |= relax(u, v, w);
				}
			}
		}
		
		changes = false;
		for(int j = 0; j < graph.verticesCount; j++){
			int u = j;
			for(EdgeTo edge: graph.adjacencyList.get(j)){
				int v = edge.to;
				int w = edge.w;
				changes |= relax(u, v, w);
			}
		}
		
		
		return changes;
	}

	public boolean relax(int u, int v, int w) {
		int newWeight =dist[u] + w; 
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
		if(to < 0)
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



	public int[] getDist() {
		return dist;
	}
}
