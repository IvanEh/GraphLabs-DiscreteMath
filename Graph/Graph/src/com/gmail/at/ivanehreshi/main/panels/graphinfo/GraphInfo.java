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
	private ConnectedComponents connectedComponents;
	private PathFinderTab pathFinderTab;
	private AllPathsTab allPathsTab;
	private Lab6Tab lab6Tab;
	private TSPTab tspTab;
	
	public GraphInfo(OrientedGraph g, GraphicUIApp app){
		super();
		
		this.app = app;
		
		DistanceModel distanceModel = new DistanceModel(app);
		
		distanceMatrixPanel = new DistanceMatrixPanel(app, distanceModel);;
		add("������� ��������",distanceMatrixPanel);
		
		addTab("������� ���������", new ReachibilityMatrixPanel(app, distanceModel));
		addTab("������� ��������", new AdjacencyMatrixTab(app));
		
		cycleTab = new  CycleTab(app);
		addTab("�����", cycleTab);
		
		addTab("��������� ����������",new  AdditionalInformation(app));
		
//		dfsPanel =  new DFSPanel(app);
//		addTab("����� � �������",new JScrollPane(dfsPanel)  );
//		setTabComponentAt(5, new UpdateButtonForSearch("DFS ����� � �������", dfsPanel));
		
//		bfsPanel = new BFSPanel(app);
//		addTab("����� � ������", new JScrollPane(bfsPanel));
//		setTabComponentAt(6, new UpdateButtonForSearch("BFS ����� � ������", bfsPanel));
		
		connectedComponents = new ConnectedComponents(app);
		addTab("������ ��'���� ����������", connectedComponents);
		
		TopologicalSortTab topologicalSortTab = new TopologicalSortTab(app);
		addTab("���������� ����������", topologicalSortTab);
		
		pathFinderTab = new PathFinderTab(this.app);
		addTab("����������� ����", pathFinderTab);
		
		allPathsTab = new AllPathsTab(this.app);
		addTab("�� ���������� �����", allPathsTab);
		
		lab6Tab = new Lab6Tab(app);
		addTab("��� 6", lab6Tab);
		
		tspTab = new TSPTab(this.app);
		addTab("������ ����������", tspTab);
		
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
				
				if(c instanceof JScrollPane){
					c = ((JScrollPane) c).getViewport().getComponent(0);
				}
				
				if(c instanceof QueuedUpdatable){
					((QueuedUpdatable)c).updateIfNeeded();
				}
				
				
				Object x = source.getComponentAt(lastActiveTab);
				if(x instanceof JScrollPane){
					x = ((JScrollPane) x).getViewport().getComponent(0);
				}
				
				
				if (x instanceof GraphInfoTab) {
					((GraphInfoTab) x).onDeactivated();
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
			if(c instanceof JScrollPane){
				c = ((JScrollPane) c).getViewport().getComponent(0);
			}
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
