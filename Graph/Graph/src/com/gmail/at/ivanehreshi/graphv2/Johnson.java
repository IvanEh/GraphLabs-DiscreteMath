package com.gmail.at.ivanehreshi.graphv2;

import java.util.LinkedList;
import java.util.Vector;

import com.gmail.at.ivanehreshi.graph.BellmanFord;
import com.gmail.at.ivanehreshi.graph.EdgeTo;
import com.gmail.at.ivanehreshi.graph.OrientedGraph;
import com.gmail.at.ivanehreshi.graph.GraphAlgotithms.DijkstraPath;

public class Johnson {
	 private boolean hasNegativeCycle;  // is there a negative cycle?
	    private double[][] distTo;  // distTo[v][w] = length of shortest v->w path
	    Vector<Integer> path = new Vector<>();
		private OrientedGraph graph;   
	    
	    public Johnson(OrientedGraph G) {
	        this.graph = G;

	        for (int v = 0; v < G.verticesCount; v++) {
	            for (int w = 0; w < G.verticesCount; w++) {
	                distTo[v][w] = 1021039;
	            }
	        }

	        graph.add(graph.verticesCount-1);
	        
	       BellmanFord finder = new BellmanFord(graph, graph.verticesCount-1);
	       hasNegativeCycle = finder.findAll();
	       	
	       double[] h = new double[graph.verticesCount];
	       
	       for(int i = 0; i < graph.verticesCount; i++){
	    	   h[i] = finder.getDist()[i];
	    	   setW();
	       }

	       for(int i = 0; i < graph.verticesCount; i++){
	    	   DijkstraPath finder2 = null;
			try {
				finder2 = new DijkstraPath(G, i);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
	    	   finder2.findAll();
	    	   for(int j = 0; j < graph.verticesCount; j++){
	    		   distTo[i][j] = graph.adjacencyMatrix.get(i).get(j).intValue() + h[i] - h[j];
	    	   }
	       }
	       
	    }


		public boolean hasNegativeCycle() {
	        return hasNegativeCycle;
	    }


	    public boolean hasPath(int s, int t) {
	        return distTo[s][t] < Double.POSITIVE_INFINITY;
	    }

	 
	    public double dist(int s, int t) {
	        if (hasNegativeCycle())
	            throw new UnsupportedOperationException("Negative cost cycle exists");
	        return distTo[s][t];
	    }

	    private void setW() {
	    	
	    }

	    private boolean check() {
	        return hasNegativeCycle;
	    }

}
