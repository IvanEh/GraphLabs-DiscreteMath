package com.gmail.at.ivanehreshi.graphv2;

import java.util.LinkedList;
import java.util.Stack;
import java.util.Vector;

import com.gmail.at.ivanehreshi.graph.EdgeTo;
import com.gmail.at.ivanehreshi.graph.OrientedGraph;

public class FloydWarshall {
    private boolean hasNegativeCycle;  // is there a negative cycle?
    private int[][] distTo;  // distTo[v][w] = length of shortest v->w path
    Vector<Integer> path = new Vector<>();
	private OrientedGraph graph;   
    
    public FloydWarshall(OrientedGraph G) {
        this.graph = G;

        for (int v = 0; v < G.verticesCount; v++) {
            for (int w = 0; w < G.verticesCount; w++) {
                distTo[v][w] = 1021039;
            }
        }

        for (int v = 0; v < graph.verticesCount; v++) {
            for (LinkedList<EdgeTo> e : graph.adjacencyList) {
                for(EdgeTo u: e){
                	distTo[v][u.to] = u.w;
                }
            }
        }

        for (int i = 0; i < graph.verticesCount; i++) {
            for (int v = 0; v < graph.verticesCount; v++) {
                for (int w = 0; w < graph.verticesCount; w++) {
                    if (distTo[v][w] > distTo[v][i] + distTo[i][w]) {
                        distTo[v][w] = distTo[v][i] + distTo[i][w];
                        distTo[v][w] = distTo[i][w];
                    }
                }
                // від'ємні цикли
                if (distTo[v][v] < 0) {
                    hasNegativeCycle = true;
                    return;
                }
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


    private boolean check() {
        return hasNegativeCycle;
    }

}