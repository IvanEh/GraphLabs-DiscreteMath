package com.gmail.at.ivanehreshi.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.MenuItem;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

import com.gmail.at.ivanehreshi.graph.GraphEvent;
import com.gmail.at.ivanehreshi.graph.GraphEvent.EventType;
import com.gmail.at.ivanehreshi.graph.OrientedGraph;
import com.gmail.at.ivanehreshi.listeners.FromFileAction;
import com.gmail.at.ivanehreshi.listeners.InputGraphAction;
import com.gmail.at.ivanehreshi.main.panels.GraphViewer;
import com.gmail.at.ivanehreshi.main.panels.VertexUI;
import com.gmail.at.ivanehreshi.main.panels.graphinfo.GraphInfo;

public class GraphicUIApp extends JFrame implements Observer{
	public OrientedGraph graph;
	public GraphViewer graphViewer;
	private GraphInfo graphInfo;
	
	public GraphicUIApp(OrientedGraph graph){
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setTitle("Lab");
		setSize(800, 600);
		
		getContentPane().setLayout(new GridLayout(2, 1, 10, 0));
		
		this.graph = graph;
		initGUI();
		manageObservers();
		
		setVisible(true);
	}
	
	public void manageObservers(){
		graph.addObserver(this);
		graph.addObserver(graphViewer);
		graph.addObserver(graphInfo);
	}
	
	private void initGUI() {		
		graphViewer = new GraphViewer(this);
		graphViewer.setLocation(0, 0);
		graphViewer.setSize(400, 200);
		getContentPane().add(graphViewer, BorderLayout.CENTER);
		
		
		graphInfo = new GraphInfo(graph, this);
		graphInfo.setMinimumSize(new Dimension(300, 500));
		getContentPane().add(graphInfo, BorderLayout.PAGE_END);
		
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menu = new JMenu("Граф");
		menuBar.add(menu);
		
		JMenuItem inputGraph = new JMenuItem("Ввести граф");
		menu.add(inputGraph);
		inputGraph.addActionListener(new InputGraphAction(graph, this));
		
		JMenuItem fromFile = new JMenuItem("Зчитати з файлу");
		menu.add(fromFile);
		fromFile.addActionListener(new FromFileAction(this));
		
		setJMenuBar(menuBar);
//		VertexUI v3 = new VertexUI();
//		v3.setLocation(150, 150);
//		v3.setSize(50, 50);
//		graphViewer.add(v3);
	}

	public static void main(String[] arg){
		
		OrientedGraph graph = new OrientedGraph(6);
//		graph.connect(5, 6);
//		graph.connect(6, 5);
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {	
				GraphicUIApp app = new GraphicUIApp(graph);
			}
		});	
		
	
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		GraphEvent e = (GraphEvent) arg;
		
		if(e.eventType == EventType.GRAPH_CREATED)
			manageObservers();
	}
}
