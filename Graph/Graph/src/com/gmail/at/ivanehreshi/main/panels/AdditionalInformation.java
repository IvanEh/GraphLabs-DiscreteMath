package com.gmail.at.ivanehreshi.main.panels;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;

import com.gmail.at.ivanehreshi.graph.GraphAlgotithms;
import com.gmail.at.ivanehreshi.graph.GraphConnectivityType;
import com.gmail.at.ivanehreshi.graph.GraphEvent;
import com.gmail.at.ivanehreshi.graph.OrientedGraph;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;

public class AdditionalInformation extends JPanel  implements Observer, QueuedUpdatable{
	
	public boolean needToUpdate = true;
	
	private SpringLayout layout = new SpringLayout();
	
	GraphicUIApp app;
	
	
	JLabel verticesCountLabel = new JLabel("Кількість вершин: ");
	JLabel verticesCountValueLabel = new JLabel("1");
	
	JLabel edgeCountLabel = new JLabel("Кількість ребер: ");
	JLabel edgeCountValueLabel = new JLabel("1");
	
	JLabel connectivityTypeLabel = new JLabel("Тип зв'язності");
	JLabel connectivityTypeValueLabel = new JLabel("1");
	
	public AdditionalInformation(GraphicUIApp app){
	
		this.app = app;
		
		setLayout(layout);
	
		createGUI();
		
		updateLabels();
		
	
		
	}
	
	public void updateLabels(){

		verticesCountValueLabel.setText(String.valueOf(app.graph.verticesCount));
		
		edgeCountValueLabel.setText(String.valueOf(app.graph.incidenceMatrix.size()));
	
		GraphConnectivityType connectivityType = 
				GraphAlgotithms.computeConnectivityType(app.graph, app.graph.computeDistanceMatrix());
		
		switch (connectivityType) {
		case  NO_CONNECTIVITY_TYPE:
			connectivityTypeValueLabel.setText("-");
			break;
		case  STRONGLY_CONNECTED:
			connectivityTypeValueLabel.setText("Сильно зв'язаний");
			break;
		case  WEAKLY_CONNECTED:
			connectivityTypeValueLabel.setText("Слабо зв'язаний");
			break;
		case  SIDED:
			connectivityTypeValueLabel.setText("Однобічно зв'язаний");
			break;
		default:
			connectivityTypeValueLabel.setText("незв'язаний");
			break;
		}
		
	}
	
	private void createGUI(){
		add(verticesCountLabel);
		layout.putConstraint(SpringLayout.WEST, verticesCountLabel, 10, SpringLayout.WEST, this);
		verticesCountLabel.setVisible(true);
		
		add(verticesCountValueLabel);
		layout.putConstraint(SpringLayout.WEST, verticesCountValueLabel, 40, SpringLayout.EAST, verticesCountLabel);
		verticesCountLabel.setVisible(true);
		
		add(edgeCountLabel);
		layout.putConstraint(SpringLayout.WEST, edgeCountLabel, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, edgeCountLabel, 20, SpringLayout.NORTH, verticesCountLabel);
		verticesCountLabel.setVisible(true);
		
		add(edgeCountValueLabel);
		layout.putConstraint(SpringLayout.WEST, edgeCountValueLabel, 40, SpringLayout.EAST, edgeCountLabel);
		layout.putConstraint(SpringLayout.NORTH, edgeCountValueLabel, 20, SpringLayout.NORTH, verticesCountLabel);
		verticesCountLabel.setVisible(true);
		
		add(connectivityTypeLabel);
		layout.putConstraint(SpringLayout.WEST, connectivityTypeLabel, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, connectivityTypeLabel, 20, SpringLayout.NORTH, edgeCountLabel);
		verticesCountLabel.setVisible(true);
		
		add(connectivityTypeValueLabel);
		layout.putConstraint(SpringLayout.WEST, connectivityTypeValueLabel, 40, SpringLayout.EAST, connectivityTypeLabel);
		layout.putConstraint(SpringLayout.NORTH, connectivityTypeValueLabel, 20, SpringLayout.NORTH, edgeCountLabel);
		verticesCountLabel.setVisible(true);
	}

	@Override
	public void update(Observable o, Object e) {
		OrientedGraph g;
		GraphEvent event;
		
		if(e instanceof GraphEvent && o instanceof OrientedGraph ){
			g = (OrientedGraph) o;
			event = (GraphEvent) e;
		}
		
		needToUpdate = true;
	}

	@Override
	public void updateIfNeeded() {
		updateLabels();
	}
}
