package com.gmail.at.ivanehreshi.main.panels.graphinfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gmail.at.ivanehreshi.graph.GraphAlgotithms.DijkstraPath;
import com.gmail.at.ivanehreshi.interfaces.GraphicManipulator;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;
import com.gmail.at.ivanehreshi.main.panels.graphinfo.GraphInfo.GraphInfoTab;

public class Dijkstra extends JPanel implements QueuedUpdatable, GraphInfoTab {

	private GraphicUIApp app;
	private DijkstraPath pathFinder = null;
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
	
	public Dijkstra(GraphicUIApp app) {
		this.app = app;
		algorithm.setModel(new DefaultComboBoxModel<String>(new String[]{"Dijkstra", "Bellman-Ford"}));
		
		add(label1);
		add(label2);
		add(fromVertLst);
		add(toVertLst);
		add(label3);
		add(res);
		add(protocolLabel1);
		add(protocolLabel2);
		add(algLabel);
		add(algorithm);
		init();
		manageListeners();
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
			pathFinder = new DijkstraPath(this.app.graph, source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		pathFinder.findAll();
		int to =(int) toVertLst.getSelectedItem()-1;
		int res = pathFinder.getDist()[to];
		String resStr;
		if(res == pathFinder.INF)
			resStr = "∞";
		else
			resStr = String.valueOf(res);
		
		this.res.setText(resStr);
	}
	
	private void manageListeners(){
		fromVertLst.addActionListener(new LstActionListener());
		toVertLst.addActionListener(new LstActionListener());
	}
	
	@Override
	public void onActivated() {
		init();
		initVis();
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


