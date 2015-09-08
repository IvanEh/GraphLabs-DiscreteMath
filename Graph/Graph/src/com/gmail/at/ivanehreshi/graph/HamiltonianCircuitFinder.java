package com.gmail.at.ivanehreshi.graph;

import java.util.LinkedList;

import com.gmail.at.ivanehreshi.utility.Ref;

public class HamiltonianCircuitFinder extends HamiltonianPathFinder {
	
	//enum HamiltonianPathType{CYCLE, PATH, NEXIST};
	
//	HamiltonianPathType state;

	public HamiltonianCircuitFinder(OrientedGraph graph){
		super(graph);
	}
	
	public boolean isCycle(){
		if(!isComputed)
			throw new RuntimeException();
		
		
		return !HamiltonianPathCahce.isEmpty();
//		return state == HamiltonianPathType.CYCLE;
	}

	

	
	private boolean track(LinkedList<Integer> curr, Ref<LinkedList<Integer>> result, int len){
		if(curr.size() == len+1){
			result.field = curr;
			return true;
		}
		int v = curr.getLast();
		for(EdgeTo u: graph.adjacencyList.get(v)){
			if(!curr.contains(u.to)){
				LinkedList<Integer> next = new LinkedList<>(curr);
				next.add(u.to);
				boolean solution = track(next, result, len);
				if(solution){
					return true;
				}
			}else{
				if(curr.size() == len
						&& u.to == curr.getFirst()){
					curr.add(u.to);
					if(track(curr, result, len)){
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean compute(){
		if(isComputed){
			return existPath();
		}
		
		int actualCount = graph.usedVertices();
		
		LinkedList<Integer> result = new LinkedList<>();
		Ref<LinkedList<Integer>> resultRef = new Ref<>(result);
		boolean succ = false;
		for(int i = 0; i < graph.verticesCount; i++){
			LinkedList<Integer> first = new LinkedList<>();
			first.add(i);
			succ = track(first, resultRef, actualCount);
			if(succ)
				break;
		}
		isComputed = true;
		HamiltonianPathCahce = resultRef.field;
		
		if(succ == false){
			state = HamiltonianPathType.NEXIST;
		}else{
			state = HamiltonianPathType.CYCLE;
		}
		
		
		
		return succ;
		
	}

	public LinkedList<Integer> getPath() {
		return HamiltonianPathCahce;
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
