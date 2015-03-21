package com.gmail.at.ivanehreshi.main.panels;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.ColorModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import com.gmail.at.ivanehreshi.graph.GraphEvent;
import com.gmail.at.ivanehreshi.graph.OrientedGraph;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;

public class DistanceMatrixPanel extends JPanel implements Observer, QueuedUpdatable{

	public JTable table;
	
	public OrientedGraph graph;
	public boolean needToUpdate = true;
	//boolean needToChange = false;
	JScrollPane scrollPane = new JScrollPane();
	//Integer[][] rowData;
	//String[] columnNames;
	DistanceModel distanceModel;

	private GraphicUIApp app;
	
	
	public DistanceMatrixPanel(GraphicUIApp app, DistanceModel d){
		this.app = app;
		distanceModel = d;
		
//		columnNames = new String[graph.verticesCount];
//		rowData =  g.computeDistanceMatrix();
//		for(int i = 0; i < g.verticesCount; i++){
//			columnNames[i] = String.valueOf(i+1);
//		}
//		
//		table = new JTable(rowData, columnNames);
//		
//		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		TableColumnModel columnModel = table.getColumnModel();
//		for(int i = 0; i < columnModel.getColumnCount(); i++){
//			
//		}
		
		setLayout(new FlowLayout());
		
		initTable();
		
		
	}


	public void initTable(){
		removeAll();
		
		table = new JTable(distanceModel.rowDataForDistance, distanceModel.columnNames);
		
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		add(scrollPane);
	}
	
	@Override
	public void update(Observable arg0, Object arg1) {
		needToUpdate = true;
	}

	@Override
	public void updateIfNeeded() {
		if(needToUpdate){
			needToUpdate = false;
			distanceModel.updateAll();
			initTable();
		}
	}
	
	public void forceUpdateIfNeeded() {
		if(needToUpdate){
			needToUpdate = false;
			distanceModel.updateAll();
			initTable();
		}
	}


	@Override
	public void queryUpdate() {
		needToUpdate = true;
	}
	
	
	
		
}
