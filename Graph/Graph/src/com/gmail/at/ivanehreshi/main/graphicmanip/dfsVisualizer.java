package com.gmail.at.ivanehreshi.main.graphicmanip;

import com.gmail.at.ivanehreshi.graph.GraphAlgotithms;
import com.gmail.at.ivanehreshi.interfaces.GraphicManipulator;
import com.gmail.at.ivanehreshi.main.panels.GraphViewer;

public class dfsVisualizer implements GraphicManipulator {

	GraphAlgotithms.DFSValueComputer dfs;
	GraphViewer graphViewer;
	
	public static final long frameTime = 1000;
	public long currentFrameTime = 0;
	
	public dfsVisualizer(GraphAlgotithms.DFSValueComputer dfs, GraphViewer graphViewer){
		this.dfs = dfs;
		this.graphViewer = graphViewer;
	}
	
	@Override
	public void update(long deltaTime) {
		currentFrameTime+=deltaTime;
		if(currentFrameTime > frameTime){
			
		}
	}

}
