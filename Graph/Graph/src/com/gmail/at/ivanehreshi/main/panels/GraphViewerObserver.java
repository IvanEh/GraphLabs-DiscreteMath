package com.gmail.at.ivanehreshi.main.panels;

import java.util.Observable;
import java.util.Observer;

public class GraphViewerObserver implements Observer {
	GraphViewer graphViewer;
	
	public GraphViewerObserver(GraphViewer grp) {
		// TODO Auto-generated constructor stub
		graphViewer = grp;
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
