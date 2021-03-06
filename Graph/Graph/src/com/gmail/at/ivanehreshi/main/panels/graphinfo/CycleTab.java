package com.gmail.at.ivanehreshi.main.panels.graphinfo;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.gmail.at.ivanehreshi.graph.GraphAlgotithms;
import com.gmail.at.ivanehreshi.graph.GraphAlgotithms.StronglyConnectedComponentsFinder;
import com.gmail.at.ivanehreshi.graph.OrientedGraph;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;
import com.gmail.at.ivanehreshi.main.panels.graphinfo.GraphInfo.GraphInfoTab;

public class CycleTab extends JPanel implements QueuedUpdatable, GraphInfoTab{
	GraphAlgotithms.StronglyConnectedComponentsFinder finder;
	
	
	public final String cyclic = "���� ��������";
	public final String notcyclic = "���� ���������";
	public JLabel hasCycleLabel = new JLabel(notcyclic);
	
	private SpringLayout layout = new SpringLayout();
	private GraphicUIApp app;


	private boolean needToUpdate = true;
	
	public CycleTab(GraphicUIApp app){
		this.app = app;
		
		setLayout(layout);
		
		add(hasCycleLabel);
		layout.putConstraint(SpringLayout.WEST, hasCycleLabel, 10, SpringLayout.WEST, this);
		hasCycleLabel.setVisible(true);

		updateInfo();
		
		setVisible(true);
	}
	
	public void updateInfo(){
		
		finder = new StronglyConnectedComponentsFinder(app.graph);
	
		finder.compute();
	
		if(!finder.isCyclic()){
			hasCycleLabel.setText(notcyclic);
		}else{
			hasCycleLabel.setText(cyclic);
		}
		
	//	finder.dfsrFindCycle(
	}
	
	public void getNextCycle(){

		if(finder.isCyclic()){
			for(int i = 0; i < finder.scc.size(); i++){
				ArrayList<Integer> V = finder.scc.get(0);
				for(int x: V){
					app.graphViewer.verticesUI.get(x).hover = true;
					app.graphViewer.verticesUI.get(x).repaint();
				}
			}
		}
	}
	
	public void clearCycles(){
		if(finder.isCyclic()){
			for(int i = 0; i < finder.scc.size(); i++){
				ArrayList<Integer> V = finder.scc.get(0);
				for(int x: V){
					app.graphViewer.verticesUI.get(x).hover = false;
					app.graphViewer.verticesUI.get(x).repaint();
				}
			}
		}
	}

	@Override
	public void updateIfNeeded() {
		// TODO Auto-generated method stub
		if(needToUpdate )
		{
			updateInfo();
			getNextCycle();
			needToUpdate = false;
		}
	}

	@Override
	public void queryUpdate() {
		// TODO Auto-generated method stub
		needToUpdate = true;
	}

	@Override
	public void onActivated() {
		// TODO Auto-generated method stub
		getNextCycle();
	}

	@Override
	public void onDeactivated() {
		// TODO Auto-generated method stub
		clearCycles();
	}

	
}
