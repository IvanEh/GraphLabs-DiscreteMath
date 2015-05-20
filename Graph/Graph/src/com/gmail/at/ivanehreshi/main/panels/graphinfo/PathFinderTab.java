package com.gmail.at.ivanehreshi.main.panels.graphinfo;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.gmail.at.ivanehreshi.graph.BellmanFord;
import com.gmail.at.ivanehreshi.graph.PathFinder;
import com.gmail.at.ivanehreshi.graph.GraphAlgotithms.DijkstraPath;
import com.gmail.at.ivanehreshi.interfaces.GraphicManipulator;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;
import com.gmail.at.ivanehreshi.main.panels.graphinfo.GraphInfo.GraphInfoTab;

public class PathFinderTab extends JPanel implements QueuedUpdatable, GraphInfoTab {

	private GraphicUIApp app;
	private PathFinder pathFinder = null;
	private final JLabel label1 = new JLabel("Початок");
	private final JLabel label2 = new JLabel("Кінець");
	private final JLabel label3 = new JLabel("Довжина");
	private final JLabel res = new JLabel("");
	private final JLabel protocolLabel1 = new JLabel("Вершини: ");
	private final JLabel protocolLabel2 = new JLabel();
	private final JLabel algLabel = new JLabel("Алгоритм: ");
	private boolean neetToUpdate = true;
	GraphicManipulator manip;
	
	private JComboBox<Integer> fromVertLst = new JComboBox<Integer>();
	private JComboBox<Integer> toVertLst = new JComboBox<Integer>();
	private JComboBox<String> algorithm = new JComboBox<String>();
	private SpringLayout layout;
	
	public PathFinderTab(GraphicUIApp app) {
		this.app = app;
		algorithm.setModel(new DefaultComboBoxModel<String>(new String[]{"Dijkstra", "Bellman-Ford"}));
		
		initGUI();
		init();
		manageListeners();
	}



	private void initGUI() {
		layout = new SpringLayout();
		setLayout(layout);
		
		// fix alignment
		label2.setPreferredSize(label3.getPreferredSize()); 
		
		add(label1);
		layout.putConstraint(SpringLayout.WEST, label1, 30, SpringLayout.WEST, this);
		
		add(fromVertLst);
		layout.putConstraint(SpringLayout.WEST, fromVertLst, 50, SpringLayout.EAST, label1);

		add(label2);
		layout.putConstraint(SpringLayout.WEST, label2, 30, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, label2, 20, SpringLayout.SOUTH, label1);
		
		add(toVertLst);
		layout.putConstraint(SpringLayout.WEST, toVertLst, 50, SpringLayout.EAST, label2);
		layout.putConstraint(SpringLayout.NORTH, toVertLst, 20, SpringLayout.SOUTH, label1);
		
		add(algLabel);
		layout.putConstraint(SpringLayout.WEST, algLabel, 30, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, algLabel, 20, SpringLayout.SOUTH, label2);
		
		add(algorithm);
		layout.putConstraint(SpringLayout.WEST, algorithm, 50, SpringLayout.EAST, algLabel);
		layout.putConstraint(SpringLayout.NORTH, algorithm, 20, SpringLayout.SOUTH, label2);
		
		add(label3);
		layout.putConstraint(SpringLayout.WEST, label3, 30, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, label3, 20, SpringLayout.SOUTH, algLabel);

		add(res);
		layout.putConstraint(SpringLayout.WEST, res, 50, SpringLayout.EAST, label3);
		layout.putConstraint(SpringLayout.NORTH, res, 20, SpringLayout.SOUTH, algLabel);

		add(protocolLabel1);
		layout.putConstraint(SpringLayout.WEST, protocolLabel1, 30, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, protocolLabel1, 20, SpringLayout.SOUTH, label3);

		add(protocolLabel2);
		layout.putConstraint(SpringLayout.WEST, protocolLabel2, 50, SpringLayout.EAST, protocolLabel1);
		layout.putConstraint(SpringLayout.NORTH, protocolLabel2, 20, SpringLayout.SOUTH, label3);

		Dimension dim = label1.getPreferredSize();
		for(Component comp: this.getComponents()){
				Dimension dimCurr = comp.getPreferredSize();
				if(dimCurr.getWidth() > dim.getWidth())
					dim = dimCurr;
		}
		
		for(Component comp: this.getComponents()){
				comp.setPreferredSize(dim);
		}
	}
	
	
	
	private void init(){
		releaseVis();
		if(!this.app.graph.isWeighted){
			this.setEnabled(false);
			return;
		}
		Integer[] vertices = new Integer[app.graph.verticesCount];
		for(int i = 0; i < app.graph.verticesCount; i++){
			vertices[i] = i+1;
		}
		fromVertLst.setModel(new DefaultComboBoxModel<Integer>(vertices));
		toVertLst.setModel(new DefaultComboBoxModel<Integer>(vertices));
				
		initFinder();
	}
	
	private void initFinder(){
		try {
			int source = fromVertLst.getSelectedIndex();
			int algChoose = algorithm.getSelectedIndex();
			if(algChoose == 0)
				pathFinder = new DijkstraPath(this.app.graph, source);
			else
				pathFinder = new BellmanFord(this.app.graph, source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		boolean dijkstraFail = pathFinder instanceof DijkstraPath;
		dijkstraFail &= app.graph.zeroWeightedFlag;
		fromVertLst.setEnabled(!dijkstraFail);
		toVertLst.setEnabled(!dijkstraFail);
		label3.setEnabled(!dijkstraFail);
		res.setEnabled(!dijkstraFail);
		protocolLabel1.setEnabled(!dijkstraFail);
		label1.setEnabled(!dijkstraFail);
		label2.setEnabled(!dijkstraFail);
		label3.setEnabled(!dijkstraFail);

		if(dijkstraFail){
			return;
		}
			
		
		boolean fail = pathFinder.findAll();
		int to =(int) toVertLst.getSelectedItem()-1;
		int res = pathFinder.getDist()[to];
		String resStr; 
		if(res == PathFinder.INF)
			resStr = "∞";
		else
			if(fail && pathFinder instanceof BellmanFord)
				resStr = "-∞";
			else
				resStr = String.valueOf(res);
		
		StringBuffer pathStr = new StringBuffer("[ "); 
		ArrayList<Integer> path = pathFinder.getPath(to);
		if(path != null){
			for(int i = path.size()-1; i >= 0 ; i--){
				pathStr.append((path.get(i)+1) + " ");
			}
			pathStr.append("]");
			this.protocolLabel2.setText(pathStr.toString());
		}
		
		this.res.setText(resStr);
	}
	
	private void manageListeners(){
		fromVertLst.addActionListener(new LstActionListener());
		toVertLst.addActionListener(new LstActionListener());
		algorithm.addActionListener(new LstActionListener());
	}
	
	@Override
	public void onActivated() {
//		if(neetToUpdate){
			init();
			initVis();
			neetToUpdate = false;
//		}else{
//			if(manip != null)
//				app.graphViewer.graphicManipulators.add(manip);

//		}
	}

	private void initVis() {
		releaseVis();
		if(pathFinder == null)
			return;
		int to = toVertLst.getSelectedIndex() ;
		releaseVis();
		manip = new PathVisualizer(to);
		app.graphViewer.graphicManipulators.add(manip);
	}

	@Override
	public void onDeactivated() {
		releaseVis();
	}

	private void releaseVis() {
		app.graphViewer.graphicManipulators.remove(manip);
		app.graphViewer.unhoverAll();
	}

	
	
	@Override
	public void queryUpdate() {
		neetToUpdate = true;
	}

	@Override
	public void updateIfNeeded() {
		if(neetToUpdate){
			neetToUpdate = false;
			init();
		}
			
	}

	private class LstActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			initFinder();
			initVis();
		}
		
	}
	
	private class PathVisualizer implements GraphicManipulator {


		private ArrayList<Integer> path;
		private int active;
		private int time = 0;
		private int transTime = 250;
		public PathVisualizer(int to) {
			this.path = pathFinder.getPath(to);
			active = 0;
		}
		
		private int index(int i){
			if(i >= path.size())
				return 0;
			if(i < 0)
				return path.size()-1;
			return i;
		}
		
		@Override
		public void update(long deltaTime) {
			if(path == null)
				return;
			if(path.size() == 1)
				return;
			time+=deltaTime;
			if(time > transTime ){
				int prev = index(active-1);
				int ths = index(active);
				app.graphViewer.hover(path.get(path.size() - prev - 1), false);
				
				prev = index(active);
				active = index(++active);
				
				app.graphViewer.hover(path.get(path.size() - prev - 1), true);
				app.graphViewer.hover(path.get(path.size() - ths - 1), true);
				time = 0;
			}
		}
		
	}
}


