package com.gmail.at.ivanehreshi.graph;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

public class EulerPathFinder {
	enum EulerPathType {PATH, CYCLE, NEXIST};
	
	OrientedGraph graph;
	OrientedGraph copy;
	
	LinkedList<Integer> eulerPathCache;
	EulerPathType state;
	
	boolean isComputed = false;
	
	public EulerPathFinder(OrientedGraph graph){
		this.graph = graph;
		copy = new OrientedGraph(this.graph);
		eulerPathCache = new LinkedList<Integer>();
	}
	
	public boolean isPath(){
		if(!isComputed)
			throw new RuntimeException();
		
		return state == EulerPathType.PATH;
	}
	
	public boolean isCycle(){
		if(!isComputed)
			throw new RuntimeException();
		
		
		return state == EulerPathType.CYCLE;
	}

	
	public boolean existPath(){
		if(!isComputed)
			throw new RuntimeException();
		
		return state == EulerPathType.NEXIST;
	}
	

	public boolean compute(){
		if(isComputed)
			return existPath();

		int first = -1;
		int last = -1;
		int evenDegreeCount = 0;
		boolean fail = false;
		for(int i = 0; i < graph.verticesCount; i++){
			int in = graph.inDegree(i);
			int out = graph.outDegree(i);
			
			if(in == out + 1){
				last = i;
			}
			if(out == in + 1){
				first = i;
			}
			if(in == out){
				evenDegreeCount++;
			}
		}
		
		boolean pathCond = evenDegreeCount == graph.verticesCount - 2 
				&& first != -1 && last != -1;
		boolean cycleCond = evenDegreeCount == graph.verticesCount;
		
		state = EulerPathType.NEXIST;
		
		if(pathCond){
			state = EulerPathType.PATH;
		}else{
			if(cycleCond){
				state = EulerPathType.CYCLE;
			}else{
				return false;
			}
		}
		
		LinkedList<Integer> stack = new LinkedList<>();
		stack.push(0);
		
		while(!stack.isEmpty()){
			int v = stack.getFirst();
			if(!copy.adjacencyList.get(v).isEmpty()){
				int u = copy.adjacencyList.get(v).pop().to;
				stack.push(u);
			}else{
				eulerPathCache.push(stack.pop());
			}
		}
		
		isComputed = true;
		return existPath();
	}
}
