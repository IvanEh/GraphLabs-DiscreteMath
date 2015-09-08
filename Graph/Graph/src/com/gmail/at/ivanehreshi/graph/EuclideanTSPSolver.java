package com.gmail.at.ivanehreshi.graph;

import java.awt.Component;
import java.util.ArrayList;

import com.gmail.at.ivanehreshi.utility.PointD2D;
import com.gmail.at.ivanehreshi.utility.Ref;

public class EuclideanTSPSolver {
	

	private ArrayList<PointD2D> points;
	private Double distance;
	private ArrayList<Integer> path;

	public EuclideanTSPSolver(ArrayList<PointD2D> points){
		this.points = points;
	}
	
	public double dist(int i, int j){
		return Math.sqrt((points.get(i).x - points.get(j).x)*(points.get(i).x - points.get(j).x) 
				+  (points.get(i).y - points.get(j).y)*(points.get(i).y - points.get(j).y));
	}
	
	public void compute(){
	
		ArrayList<Integer> indices = new ArrayList<>();
		indices.add(0);
		Ref<ArrayList<Integer>> resultPath = new Ref<ArrayList<Integer>>(new ArrayList<>());
		Ref<Double> resultDist = new Ref<Double>(Double.MAX_VALUE/2);
		Double currentDist = (double) 0;
		boolean succ = track(indices, resultPath, resultDist, currentDist);
		this.path = resultPath.field;
		this.distance = resultDist.field;
	}

	private boolean track(ArrayList<Integer> indices,
			Ref<ArrayList<Integer>> resultPath, Ref<Double> resultDist,
			double currentDist) {

		if(indices.size() == points.size() + 1){
			if(currentDist < resultDist.field){
				resultPath.field = indices;
				resultDist.field = currentDist;
				return true;
			}
		}
		
		if(currentDist > resultDist.field){
			return false;
		}
		
		int added = 0;
		for(int i = 0; i < points.size(); i++){
			if(!indices.contains(i)){
				added++;
				ArrayList<Integer> newIndices = new ArrayList<>(indices);
				newIndices.add(i);
				currentDist += dist(indices.get(indices.size()-1), i);
				boolean succ = track(newIndices, resultPath, resultDist, currentDist);
			}else{
				if(indices.size() == points.size()){
					if(indices.get(0) == i){
						ArrayList<Integer> newIndices = new ArrayList<>(indices);
						newIndices.add(i);
						currentDist += dist(indices.get(indices.size()-1), i);
						boolean succ = track(newIndices, resultPath, resultDist, currentDist);
					}
				}
			}
		}
		
		
		return false;
	}

	public ArrayList<Integer> getPath() {
		// TODO Auto-generated method stub
		return path;
	}

	public double getDist() {
		// TODO Auto-generated method stub
		return distance;
	}
	
}
