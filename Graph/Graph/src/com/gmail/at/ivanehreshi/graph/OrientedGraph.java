package com.gmail.at.ivanehreshi.graph;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Stack;
import java.util.Vector;

import com.gmail.at.ivanehreshi.graph.GraphEvent.EventType;
import com.gmail.at.ivanehreshi.utility.Pair;
import com.gmail.at.ivanehreshi.utility.VertexDegree;


public class OrientedGraph extends Observable{
	
	/**
	 * Maximum vertices numerical number. Suggest all vertices from 0 to N exists.
	 */
	public int verticesCount = 0;
	
	/**
	 * This fields represents the graph in different ways
	 * all the fields has constant size initialized by the constructor
	 */
	 
	/**
	 * Oriented graph could be weighted and not weighted.This <br>
	 * variable indicates this feature 
	 */
	public boolean isWeighted = false;
	public ArrayList<LinkedList<EdgeTo>> adjacencyList ;
	public ArrayList<ArrayList<Integer>> adjacencyMatrix;
	/**
	 * I could be deprecated
	 */
	public ArrayList<ArrayList<Integer>> incidenceMatrix 
			= new ArrayList<ArrayList<Integer>>();

	public boolean zeroWeightedFlag = false;
	
	public OrientedGraph(int vertices_count)
	{
		this.verticesCount = vertices_count;

		// Adjacency list init
		adjacencyList = new ArrayList<LinkedList<EdgeTo>>(vertices_count);
		
		for (int i = 0; i < vertices_count; i++){
			adjacencyList.add(new LinkedList<EdgeTo>()); 
		}

		// Adjacency matrix init
		adjacencyMatrix = new ArrayList<ArrayList<Integer>>(verticesCount);
		for (int i = 0; i < vertices_count; i++){
			adjacencyMatrix.add(new ArrayList<Integer>(verticesCount));
			for(int j = 0; j < verticesCount; j++)
				adjacencyMatrix.get(i).add(0);
		}
		
		setChanged();
		notifyObservers(new GraphEvent().setEventType(GraphEvent.EventType.GRAPH_CREATED));
	}
	
	public OrientedGraph(OrientedGraph g){
		this.verticesCount = g.verticesCount;
		
		adjacencyList = new ArrayList<LinkedList<EdgeTo>>(g.adjacencyList);

		adjacencyMatrix = new ArrayList<ArrayList<Integer>>(g.adjacencyMatrix);
		
		setChanged();
		notifyObservers(new GraphEvent().setEventType(GraphEvent.EventType.GRAPH_CREATED));
	}
	
//    @Override
//    protected synchronized void clearChanged() {
//        
//    }

	/**
	 * check if the vertex could possible exist
	 */
	public boolean vertexExists(int vertex){
		if (vertex >= verticesCount || vertex < 0)
			return false;

		return true;
	}

	public boolean connect(int from, int to){
		if(isWeighted)
			return connect(from, to, 0);
		else
			return connect(from, to, 1);
	}

	public boolean connect(int from, int to, int w){
		if (!vertexExists(from) || !vertexExists(to))
			return false;

		if(w < 0)
			zeroWeightedFlag = true;
		
		EdgeTo edge = new EdgeTo(to, w);
		adjacencyList.get(from).push(edge);

		adjacencyMatrix.get(from).set(to, 
				adjacencyMatrix.get(from).get(to)+w);
		
		if (from == to)
		{
			ArrayList<Integer> vertices =new ArrayList<Integer>(
					Collections.nCopies(verticesCount, 0));
			
			vertices.set(from, 2);
	
			incidenceMatrix.add(vertices);
		}
		else
		{
			ArrayList<Integer> vertices = new ArrayList<Integer>(
					Collections.nCopies(verticesCount, 0));
			vertices.set(from, -1);
			vertices.set(to, -1);
	
			incidenceMatrix.add(vertices);
		}
		
		GraphEvent event = new GraphEvent();
		event.eventType = EventType.VERTICES_CONNECTED;
		event.from = from;
		event.to = to;
		event.w = w;
		
		setChanged();
		notifyObservers(event);
		
		return true;
	}
	
	/**
	 * Simple connected vertices test. Could not work.
	 * @return true if vertices connected
	 */
	public boolean isConnected(int from, int to){
		if (!vertexExists(from) || !vertexExists(to))
			return false;

		if(adjacencyMatrix.get(from).get(to) != 0)
			return true;
		else
			return false;
	}

	/**
	 * slow method of checking vertices connection. Use only for testing
	 */
	@Deprecated
	public boolean safeIsConnected(int from, int to){
		boolean connected = true;
		connected = isConnected(from, to);
		
		return connected;
	}
	
	/**
	 * compute vertex power of all the vertices using adjacency list
	 */
	public ArrayList<VertexDegree> computeVerticesPower(){
		
		ArrayList<VertexDegree> result =
				new ArrayList<VertexDegree>(adjacencyList.size());
		

		for (int i = 0; i < adjacencyList.size(); i++)
		{

			for (EdgeTo x : adjacencyList.get(i)) {
				VertexDegree vd = result.get(i);
				vd.first++;
				
				vd = result.get(x.to);
				vd.second++;
			}

		}

		return result;
	}

	/**
	 * TODO: test for correct work
	 */
	public final boolean isIsolated(int vertex){	
		
		if (adjacencyList.get(vertex).iterator().hasNext())
			return false;

		for (int i = 0; i < adjacencyMatrix.size(); i++)
		{
			if(adjacencyMatrix.get(i).get(vertex) != 0)
				return false;
		}

		return true;
	}
	
	public int outDegree(int v){
		int degree = 0;

		degree = adjacencyList.get(v).size();
		
		return degree;
	}
	
	public int inDegree(int v){
		int degree = 0;
		for (int i = 0; i < adjacencyMatrix.size(); i++)
		{
			if(adjacencyMatrix.get(i).get(v) != 0)
				degree++;
		}
		
		return degree;
	}

	public final boolean isHanging(int vertex) 
	{
		if (!vertexExists(vertex))
			return false;

		int degree = 0;
		
		for (int i = 0; i < adjacencyMatrix.size(); i++)
		{
			
			
			if(adjacencyMatrix.get(vertex).get(i) == 1) 
				degree++;
			if(adjacencyMatrix.get(i).get(vertex) == 1) 
				degree++;
			
			if(degree > 1)
				return false;
		}

		return degree == 1 ;
	}

	public Integer[][] computeDistanceMatrix(){
		
		Integer[][] distanceMatrix = new Integer[verticesCount][];
		Integer[][] A = new Integer[verticesCount][],
				B = new Integer[verticesCount][];
		Integer[][] X = new Integer[verticesCount][];
		
		for(int i = 0; i < verticesCount; i++){
			distanceMatrix[i] = new Integer[verticesCount];
			A[i] = new Integer[verticesCount];
			
			X[i] = new Integer[verticesCount];
			B[i] = new Integer[verticesCount];
			
			Arrays.fill(distanceMatrix[i], -1);
			
			for(int j = 0; j < verticesCount; j++){
				A[i][j] = adjacencyMatrix.get(i).get(j);
				X[i][j] = adjacencyMatrix.get(i).get(j);
//				A[i][j] = adjacencyMatrix.get(i).get(j);
				if( i != j){
					if(adjacencyMatrix.get(i).get(j) == 1)
						distanceMatrix[i][j] = adjacencyMatrix.get(i).get(j);
				}
				else{
					distanceMatrix[i][j] = 0;
				}
			}			

			
		}
		
		
		
		for(int k = 2;  k <= verticesCount; k++){
			
			for(int i = 0; i < verticesCount; i++){
				for(int j = 0; j < verticesCount; j++){
					B[i][j] = 0;
					for(int u = 0; u < verticesCount; u++){
						B[i][j] += A[i][u]*X[u][j];  
					}
				}
			}
			
			for(int i = 0; i < verticesCount; i++){
				for(int j = 0; j < verticesCount; j++){
					A[i][j] = B[i][j];
					if(A[i][j] != 0 && distanceMatrix[i][j] < 1)
						distanceMatrix[i][j] = k;
				}
			}
			
		}
			
		for(int i = 0; i < verticesCount; i++){
			distanceMatrix[i][i] = 0;
		}
		
		return distanceMatrix;
		
	}
	
	public void findAllCycles(){
		int[] from = new int[verticesCount];
		int[] dfs_values = new int[verticesCount];
		for(int i = 0; i < verticesCount; i++){
			if(dfs_values[i] == 0){
				dfs_cycles(i, dfs_values, from);
			}
		}
	}
	
	
	private void addCycle(Pair<Integer> cyclePos, int[] from){
		ArrayList<Integer> cycle = new ArrayList<Integer>();
		
		cycle.add(cyclePos.first);
		for(int v = cyclePos.second; v != cyclePos.first; v = from[v]){
			cycle.add(v);
		}
		
		Collections.reverse(cycle);
		
		System.out.println(cycle.toString());
	}
	
	private void dfs_cycles(int v, int[] dfs_values, int[] from){
		
		Pair<Integer> cycle = new Pair<Integer>();
		
		if(dfs_values == null){
			dfs_values = new int[verticesCount];
			Arrays.fill(dfs_values, 0);
		}
		
		if(from == null){
			from = new int[verticesCount];
		}
		
		ArrayList<ArrayList<Integer>> cycles = new ArrayList<ArrayList<Integer>>();
				
		boolean cycleFound = false;
		
		int cycleStart = -1;
		int cycleEnd = -1;
		
		dfs_values[v] = 1;
		
		for(EdgeTo e: adjacencyList.get(v)){
			if(dfs_values[e.to] == 0){
				
				from[e.to] = v;
				 dfs_cycles(e.to, dfs_values, from);
			}else{
				if(dfs_values[e.to] == 1){
					cycle.first = e.to;
					cycle.second = v;
					addCycle(cycle, from);
				}
			}
		}
			
		dfs_values[v] = 2;
		
	}

	public void notifyAllAboutCreating(){
		setChanged();
		notifyObservers(new GraphEvent().setEventType(EventType.GRAPH_CREATED));
	}
	
	public void notifyObservers(GraphEvent e, boolean force){
		if(force)
			setChanged();
		
		notifyObservers(e);
	}

	public int w(int u, int v) {
		if(!isWeighted)
			return 0;
		return adjacencyMatrix.get(u).get(v);
	}
	
	public void add(int v){
		
	}

}
