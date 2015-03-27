package com.gmail.at.ivanehreshi.main.panels.graphinfo;

import java.awt.Component;
import java.awt.Frame;
import java.util.Observer;

import javafx.beans.Observable;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.gmail.at.ivanehreshi.graph.OrientedGraph;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;
import com.gmail.at.ivanehreshi.main.panels.DistanceModel;

public class GraphInfo extends JTabbedPane implements Observer {
	
	GraphicUIApp app;
	DistanceMatrixPanel distanceMatrixPanel;
	java.util.Observable observable;
	private CycleTab cycleTab;
	int lastActiveTab;
	public boolean disabledUpdate = false;
	private DFSPanel dfsPanel;
	private BFSPanel bfsPanel;
	
	public GraphInfo(OrientedGraph g, GraphicUIApp app){
		super();
		
		this.app = app;
		
		DistanceModel distanceModel = new DistanceModel(app);
		
		distanceMatrixPanel = new DistanceMatrixPanel(app, distanceModel);;
		add("Матриця відстаней", distanceMatrixPanel);
		
		addTab("Матриця досяжності", new ReachibilityMatrixPanel(app, distanceModel));
		addTab("Матриця суміжності", new AdjacencyMatrixTab(app));
		
		cycleTab = new  CycleTab(app);
		addTab("Цикли", cycleTab);
		
		addTab("Додаткова інформація",new  AdditionalInformation(app));
		
		dfsPanel =  new DFSPanel(app);
		addTab("Пошук в глибину",dfsPanel );
		setTabComponentAt(5, new UpdateButtonForSearch("DFS пошук в глибину", dfsPanel));
		
		bfsPanel = new BFSPanel(app);
		addTab("Пошук в ширину", bfsPanel);
		setTabComponentAt(6, new UpdateButtonForSearch("BFS пошук в ширину", bfsPanel));
		
		lastActiveTab = 0;
		
		addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				if(disabledUpdate)
					return;
				
				JTabbedPane source = (JTabbedPane) e.getSource();
				
				int index = source.getSelectedIndex();
				Object c =  source.getComponentAt(index);
				
				
				if(c instanceof QueuedUpdatable){
					((QueuedUpdatable)c).updateIfNeeded();
				}
				
				if (source.getComponentAt(lastActiveTab) instanceof GraphInfoTab) {
					((GraphInfoTab)source.getComponentAt(lastActiveTab)).onDeactivated();
				}
				
				if(source.getComponentAt(index) instanceof GraphInfoTab){
					((GraphInfoTab)source.getComponentAt(index)).onActivated();
				}
			
				lastActiveTab = index;
			}
		});
		
		
	}

	
	@Override
	public void update(java.util.Observable o, Object arg) {
		if(disabledUpdate)
			return;
		
		for(int i = 0; i < getTabCount(); i++){
			Component c = getComponentAt(i);
			if(c instanceof QueuedUpdatable){
				
				((QueuedUpdatable) c).queryUpdate();
				
					if(i == getSelectedIndex())
						((QueuedUpdatable) c).updateIfNeeded();
			
				
			}
		}
	}
	
	static interface GraphInfoTab {
		public void onActivated();
		public void onDeactivated();
	}
}
