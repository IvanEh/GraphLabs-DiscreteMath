package com.gmail.at.ivanehreshi.main.panels.graphinfo;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SpringLayout;
import javax.swing.table.AbstractTableModel;

import com.gmail.at.ivanehreshi.graph.BellmanFord;
import com.gmail.at.ivanehreshi.graph.GraphAlgotithms.TopologicalSort;
import com.gmail.at.ivanehreshi.graph.PathFinder;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;
import com.gmail.at.ivanehreshi.main.panels.graphinfo.GraphInfo.GraphInfoTab;

public class AllPathsTab extends JPanel implements QueuedUpdatable, GraphInfoTab {
	JComboBox<String> algorithm;
	JLabel algorithmLabel = new JLabel("Алгоритм: ");
	JPanel settings;
	JPanel path;
	SpringLayout layout = new SpringLayout();
	JComboBox<Integer> from, to;
	JTable table;
	private GraphicUIApp app;
	private JScrollPane content;
	private boolean needToUpdate;
	private JLabel pathLabel;
	private boolean fail = false; 
	
	public AllPathsTab(GraphicUIApp app) {
		this.app = app;
		
		initGUI();
		init();
	}

	void init() {
		from.addActionListener(new LstActionListener());
		to.addActionListener(new LstActionListener());
		
		
		Integer[] vertices = new Integer[app.graph.verticesCount];
		for(int i = 0; i < app.graph.verticesCount; i++){
			vertices[i] = i+1;
		}
		
		if(!app.graph.isWeighted){
			disableAll();
			
			return;
		}

		
		table.setVisible(true);
		algorithm.setEnabled(true);
		algorithmLabel.setEnabled(true);
		
		
		setPath();

		
		table.setModel(new TableModel());
		from.setModel(new DefaultComboBoxModel<Integer>(vertices));
		to.setModel(new DefaultComboBoxModel<Integer>(vertices));
	}

	private void disableAll() {
		table.setVisible(false);
		algorithm.setEnabled(false);
		algorithmLabel.setEnabled(false);
	}

	void setPath() {
		// TODO Auto-generated method stub
		if(from.getSelectedItem() != null){
			int ifrom = (int) from.getSelectedItem();
			int ito = (int) to.getSelectedItem();
			PathFinder finder = new BellmanFord(this.app.graph, ifrom-1);
			finder.findAll();
			ArrayList<Integer> path = finder.getPath(ito-1);
			StringBuilder pathStr = new StringBuilder("[");
			if(path != null){
				for(int i = path.size()-1; i >= 0 ; i--){
					pathStr.append((path.get(i)+1) + " ");
				}
				pathStr.append("]");
				
			}
			this.pathLabel.setText(pathStr.toString());
		}
		
	}

	private void initGUI() {
		setLayout(layout);
		
		table = new JTable();
		content = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		add(content);
		
		algorithm = new JComboBox<String>();
		algorithm.setModel(new DefaultComboBoxModel<String>(new String[]{"Флойда-Уоршела","Джонсона"}));
		
		from = new JComboBox<>();
		to = new JComboBox<>();
		pathLabel = new JLabel();
		
		settings = new JPanel();
		path = new JPanel();
		
		add(settings);
		layout.putConstraint(SpringLayout.WEST, settings, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, settings, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.SOUTH, settings, 30, SpringLayout.NORTH, this);
		
		add(content);
		layout.putConstraint(SpringLayout.WEST, content, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, content, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, content, 10, SpringLayout.SOUTH, settings);
		layout.putConstraint(SpringLayout.SOUTH, content, -30, SpringLayout.SOUTH, this);
		
		add(path);
		layout.putConstraint(SpringLayout.WEST, path, 0, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, path, 0, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, path, 0, SpringLayout.SOUTH, content);
		layout.putConstraint(SpringLayout.SOUTH, path, 0, SpringLayout.SOUTH, this);
		
		path.add(from);
		path.add(to);
		path.add(pathLabel);
		
		settings.add(algorithmLabel);
		settings.add(algorithm);
		
	}

	@Override
	public void onActivated() {
		// TODO Auto-generated method stub
		init();
	}

	@Override
	public void onDeactivated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void queryUpdate() {
		// TODO Auto-generated method stub
		needToUpdate = true;
	}

	@Override
	public void updateIfNeeded() {
		// TODO Auto-generated method stub
		if(needToUpdate){
			init();
			needToUpdate = false;
		}
	}

	private class TableModel extends AbstractTableModel{
		
		public TableModel(){
			
		}
		
		@Override
		public String getColumnName(int column) {
			if(column == 0)
				return "";
			
			return String.valueOf(column );
		}
		
		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return app.graph.verticesCount;
		}
		
		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return app.graph.verticesCount + 1;
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if(columnIndex == 0)
				return String.valueOf(rowIndex+1);
		
			BellmanFord finder = new BellmanFord(app.graph, rowIndex);
			boolean fail = finder.findAll();
		
			if(fail){
				disableAll();
			}
			
			
			double value = finder.getDist()[columnIndex-1];
			if(value >=( finder.INF-100))
				return "∞";
			
			return value;
		}
		
	}
	
	class LstActionListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent e) {
			setPath();
		}
		
	}
}
