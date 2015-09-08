package com.gmail.at.ivanehreshi.graph;

import java.util.ArrayList;

public class GraphColoring {
	int chromaticNumber = 0;
	boolean computed = false;
	OrientedGraph graph;
	int[] coloring;
	
	GraphColoring(OrientedGraph graph){
		this.graph = graph;
		coloring = new int[graph.verticesCount];
	}
	
	public void compute(){
		if(computed)
			return;
		
		for(int i = 1; i <= graph.verticesCount; i++){
			ArrayList<Integer> tempColoring = new ArrayList<Integer>();
//			findColoring(tem,i)
		}
	}
}
