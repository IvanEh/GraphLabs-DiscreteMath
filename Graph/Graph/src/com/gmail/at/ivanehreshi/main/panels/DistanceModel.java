package com.gmail.at.ivanehreshi.main.panels;

import javax.swing.RowFilter;

import com.gmail.at.ivanehreshi.graph.OrientedGraph;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;

public class DistanceModel {
	Integer[][] rowDataForDistance;
	Integer[][] rowDataForReachibility;
	String[] columnNames;
	public GraphicUIApp app;
	boolean needToChange = false;
	
	public DistanceModel(GraphicUIApp app){
		this.app = app;
		updateAll();
	}
	
	public void updateAll(){
		updateColumnsNames(true);
		updateDataForDistance(true);
		updateDataForReachibility(true);
	}
	
	public void updateColumnsNames(boolean force ){
		if(!(force || needToChange))
			return;
		
		if(columnNames == null || columnNames.length != app.graph.verticesCount){
			columnNames = new String[app.graph.verticesCount];
		}
		for(int i = 0; i < app.graph.verticesCount; i++)
				columnNames[i] = String.valueOf(i+1);	
	}
	
	public void updateDataForDistance(boolean force){
		if(!(force || needToChange))
			return;
		
		rowDataForDistance = app.graph.computeDistanceMatrix();
	}
	
	public void updateDataForReachibility(boolean force){
		if(!(force || needToChange))
			return;
		
		rowDataForReachibility = new Integer[app.graph.verticesCount][app.graph.verticesCount];
		
		for(int i = 0; i < rowDataForDistance.length; i++){
			for(int j = 0; j < rowDataForDistance.length; j++){
				rowDataForReachibility[i][j] =( rowDataForDistance[i][j] > 0 || i == j)? 1: 0;
			}
		}
	}
}
