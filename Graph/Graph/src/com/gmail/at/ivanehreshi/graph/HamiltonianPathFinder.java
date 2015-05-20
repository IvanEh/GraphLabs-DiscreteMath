package com.gmail.at.ivanehreshi.graph;

import java.util.LinkedList;

import com.gmail.at.ivanehreshi.utility.Ref;

public class HamiltonianPathFinder {
	
	enum HamiltonianPathType{PATH, CYCLE, NEXIST};
	
	OrientedGraph graph;
	HamiltonianPathType state;
	boolean isComputed = false;
	LinkedList<Integer> HamiltonianPathCahce = null;
	
	public HamiltonianPathFinder(OrientedGraph graph){
		this.graph = graph;
		state = HamiltonianPathType.NEXIST;
	}
	
	public boolean isPath(){
		if(!isComputed)
			throw new RuntimeException();
		
		return state == HamiltonianPathType.PATH;
	}
	
	public boolean isCycle(){
		if(!isComputed)
			throw new RuntimeException();
		
		
		return state == HamiltonianPathType.CYCLE;
	}

	
	public boolean existPath(){
		if(!isComputed)
			throw new RuntimeException();
		
		return state != HamiltonianPathType.NEXIST;
	}
	
	private boolean track(LinkedList<Integer> curr, Ref<LinkedList<Integer>> result){
		if(curr.size() == graph.verticesCount){
			result.field = curr;
			return true;
		}
		int v = curr.getLast();
		for(EdgeTo u: graph.adjacencyList.get(v)){
			if(!curr.contains(u.to)){
				LinkedList<Integer> next = new LinkedList<>(curr);
				next.add(u.to);
				boolean solution = track(next, result);
				if(solution){
					return true;
				}
			}
		}
		return false;
	}

	public boolean compute(){
		if(isComputed){
			return existPath();
		}
		
		LinkedList<Integer> result = new LinkedList<>();
		Ref<LinkedList<Integer>> resultRef = new Ref<>(result);
		boolean succ = false;
		for(int i = 0; i < graph.verticesCount; i++){
			LinkedList<Integer> first = new LinkedList<>();
			first.add(i);
			succ = track(first, resultRef);
			if(succ)
				break;
		}
		return succ;
		
	}
	
//	0 1
//	0 3
//	1 2
//	2 3
//	2 4
//	3 4
//	3 0
//	3 2
//	4 5
//	6 0
	
	
}
