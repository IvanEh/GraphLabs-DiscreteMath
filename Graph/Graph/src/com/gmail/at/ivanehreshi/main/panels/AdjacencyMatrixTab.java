package com.gmail.at.ivanehreshi.main.panels;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.gmail.at.ivanehreshi.graph.OrientedGraph;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;

public class AdjacencyMatrixTab extends JPanel implements QueuedUpdatable{
	public JTable table;
	String[] columnsName;
	Integer[][] cellValue;
	JScrollPane scrollPane = new JScrollPane();
	private GraphicUIApp app;
	private boolean needToUpdate = true;
	
	public AdjacencyMatrixTab(GraphicUIApp app){
		this.app = app;
		
		initTable();
		
		setLayout(new FlowLayout());
		
	
	}

	private void initTable() {
		// TODO Auto-generated method stub
		removeAll();
		
		columnsName = new String[app.graph.verticesCount];
		for(int i = 0; i < columnsName.length; i++){
			columnsName[i] = String.valueOf(i+1);
		}
		
		
		cellValue = new Integer[app.graph.verticesCount][];
		for(int i = 0; i < cellValue.length; i++){
			cellValue[i] = new Integer[cellValue.length];
		}
		
		for(int i = 0; i < app.graph.verticesCount; i++){
			for(int j = 0; j < app.graph.verticesCount; j++){
				cellValue[i][j] =app.graph.adjacencyMatrix.get(i).get(j);
			}
		}
		
		table = new JTable(cellValue, columnsName);
		
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		add(scrollPane);
	}

	@Override
	public void updateIfNeeded() {
		// TODO Auto-generated method stub
		if(needToUpdate )
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
