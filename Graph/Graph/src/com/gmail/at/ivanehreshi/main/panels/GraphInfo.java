package com.gmail.at.ivanehreshi.main.panels;

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

public class GraphInfo extends JTabbedPane implements Observer {
	
	GraphicUIApp app;
	DistanceMatrixPanel distanceMatrixPanel;
	
	public GraphInfo(OrientedGraph g, GraphicUIApp app){
		super();
		
		this.app = app;
		
		DistanceModel distanceModel = new DistanceModel(app);
		
		distanceMatrixPanel = new DistanceMatrixPanel(app, distanceModel);
		
		addTab("Матриця відстаней", distanceMatrixPanel);
		addTab("Матриця досяжності", new ReachibilityMatrixPanel(app, distanceModel));
		addTab("Матриця суміжності", new AdjacencyMatrixTab(app));
		addTab("Цикли",new  CycleTab(app));
		addTab("Додаткова інформація",new  AdditionalInformation(app));
		
		
		addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				JTabbedPane source = (JTabbedPane) e.getSource();
				
				int index = source.getSelectedIndex();
				Object c =  source.getComponentAt(index);
				
				
				if(c instanceof QueuedUpdatable)
					((QueuedUpdatable)c).updateIfNeeded();
			}
		});
		
		
	}

	
	@Override
	public void update(java.util.Observable o, Object arg) {
		// TODO Auto-generated method stub
		for(int i = 0; i < getTabCount(); i++){
			Component c = getComponentAt(i);
			if(c instanceof QueuedUpdatable){
				
				if(c instanceof DistanceMatrixPanel){
					((DistanceMatrixPanel) c).queryUpdate();
					if(i == getSelectedIndex())
						((DistanceMatrixPanel) c).updateIfNeeded();
				}else
				{
					((QueuedUpdatable) c).updateIfNeeded();
				}
			
				
			}
		}
	}
}
