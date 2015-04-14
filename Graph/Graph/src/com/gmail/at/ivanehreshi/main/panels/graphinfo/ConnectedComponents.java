package com.gmail.at.ivanehreshi.main.panels.graphinfo;

import java.awt.LayoutManager;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.SpringLayout;
import javax.swing.SpringLayout.Constraints;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.gmail.at.ivanehreshi.graph.GraphAlgotithms.StronglyConnectedComponentsFinder;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;
import com.gmail.at.ivanehreshi.main.panels.graphinfo.GraphInfo.GraphInfoTab;

public class ConnectedComponents extends JPanel implements QueuedUpdatable, GraphInfoTab {
		
	private boolean needToUpdate;
	private GraphicUIApp app;
	private StronglyConnectedComponentsFinder finder;
	private SpringLayout layout;
	private final JLabel label1 = new JLabel("Кількість компонент зв'язності: ");
	private final JLabel label1Ans = new JLabel("0");
	private final JSlider slider = new JSlider();
	private final JSpinner spinner = new JSpinner();
	private final JLabel protocol = new JLabel();
	
	public ConnectedComponents(GraphicUIApp app){
		this.app = app;
		init();
		layout = new SpringLayout();
		setLayout(layout);
		
		initGUI();
		
		setListeners();
	}

	private void setListeners() {
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				init();
				hoverVertices();
			}
		});
	}

	private void initGUI() {
		
		add(label1);
		layout.putConstraint(SpringLayout.WEST, label1, 20, SpringLayout.WEST, this);
		
		add(label1Ans);
		layout.putConstraint(SpringLayout.WEST, label1Ans, 20, SpringLayout.EAST, label1);
		
		add(slider);
		layout.putConstraint(SpringLayout.WEST, slider, 50, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, slider, 50, SpringLayout.SOUTH, label1);
		layout.putConstraint(SpringLayout.EAST, slider, -50, SpringLayout.EAST, this);
		
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		slider.setMajorTickSpacing(1);
		
		add(protocol);
		protocol.setHorizontalAlignment(JLabel.CENTER);
		layout.putConstraint(SpringLayout.NORTH, protocol, 20, SpringLayout.SOUTH, slider);
		layout.putConstraint(SpringLayout.WEST, protocol, 10, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.EAST, protocol, -10, SpringLayout.EAST, this);
		
		
	}

	private void init(){
		finder = new StronglyConnectedComponentsFinder(app.graph);
		finder.compute();
		
		label1Ans.setText(String.valueOf(finder.scc.size()));
		
		if(finder.scc.isEmpty()){
			slider.setEnabled(false);
			protocol.setText("");
		}else{
			slider.setEnabled(true);
			slider.setMinimum(1);
			slider.setMaximum(finder.scc.size());
			
			StringBuilder str = new StringBuilder("");
			int size = finder.scc.get(slider.getValue() - 1).size();
			for(int i = 0; i < size; i++){
				str.append("" + (finder.scc.get(slider.getValue()-1).get(i) + 1) + " ");
			}
			protocol.setText(str.toString());
		}
		
	}
	
	private void hoverVertices() {
		if(finder.scc == null || finder.scc.isEmpty())
			return;
		
		app.graphViewer.unhoverAll();
		
		ArrayList<Integer> comp = finder.scc.get(slider.getValue() - 1);
		for(int i = 0; i < comp.size(); i++){
			app.graphViewer.verticesUI.get(comp.get(i)).hover = true;
		}
	}

	@Override
	public void onActivated() {
		// TODO Auto-generated method stub
		init();
		hoverVertices();
	}

	@Override
	public void onDeactivated() {
		app.graphViewer.unhoverAll();
	}

	@Override
	public void queryUpdate() {
		if(needToUpdate){
			init();
			hoverVertices();
			needToUpdate = false;
		}
		
	}

	@Override
	public void updateIfNeeded() {
		needToUpdate = true;
	}

}
