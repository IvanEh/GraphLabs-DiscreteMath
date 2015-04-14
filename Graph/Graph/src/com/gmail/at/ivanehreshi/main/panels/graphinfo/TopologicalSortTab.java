package com.gmail.at.ivanehreshi.main.panels.graphinfo;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import com.gmail.at.ivanehreshi.graph.GraphAlgotithms.StronglyConnectedComponentsFinder;
import com.gmail.at.ivanehreshi.graph.GraphAlgotithms.TopologicalSort;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;
import com.gmail.at.ivanehreshi.main.panels.VertexUI;
import com.gmail.at.ivanehreshi.main.panels.graphinfo.GraphInfo.GraphInfoTab;
import com.sun.corba.se.pept.transport.Selector;

public class TopologicalSortTab extends JPanel implements QueuedUpdatable, GraphInfoTab {

	private JTable table; 
	private final JScrollPane scrollPane;
	private GraphicUIApp app;
	private TopologicalSort topolSort;
	private boolean needToUpdate;
	
	public TopologicalSortTab(GraphicUIApp app){
		this.app = app;
		//setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		table = new JTable();
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		add(scrollPane);
		table.getSelectionModel().addListSelectionListener(new SelectionListener());
		init();
	}
	
	
	private void init() {
		StronglyConnectedComponentsFinder finder = new 
				StronglyConnectedComponentsFinder(app.graph);
		finder.compute();
		if(finder.isCyclic()){
			table.setEnabled(false);
		}else{
			table.setEnabled(true);
			topolSort = new TopologicalSort(app.graph);
			topolSort.compute();	
			table.setModel(new TopologicalModel(topolSort));
		}
	}



	@Override
	public void onActivated() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onDeactivated() {
		// TODO Auto-generated method stub
		app.graphViewer.unhoverAll();
	}

	@Override
	public void queryUpdate() {
		// TODO Auto-generated method stub
		needToUpdate = true;
		JTabbedPane pane = (JTabbedPane)this.getParent();
		if(pane.getSelectedComponent() == this)
			updateIfNeeded();
	}

	@Override
	public void updateIfNeeded() {
		// TODO Auto-generated method stub
		if(needToUpdate){
			init();
			needToUpdate = false;
		}
	}

	
	private void hover(int middle, int a, int b){
		ArrayList<VertexUI> verticesUI = app.graphViewer.verticesUI;
		app.graphViewer.unhoverAll();
		
		int size = verticesUI.size();
		verticesUI.get(middle).hover = true;

		if(app.graph.isConnected(a, middle))
			verticesUI.get(a).hover = true;
		if(app.graph.isConnected(middle, b))
			verticesUI.get(b).hover = true;
	}
	
	
	class SelectionListener implements ListSelectionListener{

		@Override
		public void valueChanged(ListSelectionEvent e) {
			int column = table.getSelectedColumn();
			int row = table.getSelectedRow();
			int v = (int) table.getValueAt(row, column) -1;
			int a = v;
			int b = a;
			int c = a;
			
			if(row != 0)
				b = (int) table.getValueAt(row - 1, column) -1;
			if(row+1 < table.getRowCount())
				c =  (int) table.getValueAt(row + 1, column) -1;
			
			hover(a, b, c);
		}
		
	}
}


class TopologicalModel extends AbstractTableModel{

	public TopologicalSort sort;
	
	public TopologicalModel(TopologicalSort sort){
		this.sort = sort;
	}
	
	@Override
	 public String getColumnName(int column) {
		 return String.valueOf(column + 1);
	 }
	
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		int max =0;

		return sort.bags.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		if(columnIndex >= sort.bags.size())
			return "";
		Vector<Integer> vec = sort.bags;
		if(rowIndex >= vec.size())
			return "";
		
		return vec.get(rowIndex)+1;
	}
	
}