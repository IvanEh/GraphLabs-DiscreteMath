package com.gmail.at.ivanehreshi.main.panels;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.gmail.at.ivanehreshi.graph.OrientedGraph;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;

public class ReachibilityMatrixPanel extends JPanel implements QueuedUpdatable {

	public JTable table;
	
	public OrientedGraph graph;
	JScrollPane scrollPane = new JScrollPane();
	DistanceModel distanceModel;

	private GraphicUIApp app;
	
	private boolean needToUpdate = true;
	
	public ReachibilityMatrixPanel(GraphicUIApp app, DistanceModel d) {
		
		this.app = app;
		distanceModel = d;
		
		
		
		setLayout(new FlowLayout());
		
	}

	public void initTable(){
		removeAll();
		table = new JTable(distanceModel.rowDataForReachibility, distanceModel.columnNames);
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		add(scrollPane);
	}
	
	@Override
	public void updateIfNeeded() {
		// TODO Auto-generated method stub
		if(needToUpdate)
		{
			initTable();
			needToUpdate = false;
		}
	}

	@Override
	public void queryUpdate() {
		// TODO Auto-generated method stub
		needToUpdate = true;
	}

}
