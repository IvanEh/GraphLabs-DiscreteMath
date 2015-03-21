package com.gmail.at.ivanehreshi.main.panels.graphinfo;

import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.gmail.at.ivanehreshi.graph.GraphAlgotithms;
import com.gmail.at.ivanehreshi.interfaces.GraphicManipulator;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;

public class DFSPanel extends JPanel implements QueuedUpdatable{
	
	GraphicUIApp app;
	DFSVisualization dfsVisualization;
	
	boolean needToUpdate = false;
	private JScrollPane scrollPane;
	private JTable table; 
	
	public DFSPanel(GraphicUIApp app){
		this.app = app;
		
		setLayout(new FlowLayout());
		
		initTable();
		
		GraphAlgotithms.DFSValueComputer dfs = new GraphAlgotithms.DFSValueComputer(app.graph);
		ArrayList<String> stackFingerPrint = new ArrayList<String>();
		dfs.computeAndSaveStack(stackFingerPrint , 0);
		
		app.graphViewer.graphicManipulators.add(dfsVisualization);
	}
	
	private void initTable() {
		removeAll();
		
		ArrayList<ArrayList<String>> data;
		
		
		table = new JTable();
		
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		add(scrollPane);
		
	}

	public class DFSVisualization implements GraphicManipulator{
		
		@Override
		public void update(double deltaTime) {
			
		}
		
	}

	@Override
	public void queryUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateIfNeeded() {
		// TODO Auto-generated method stub
		
	}
	
}
