package com.gmail.at.ivanehreshi.main.panels.graphinfo;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;

import com.gmail.at.ivanehreshi.graph.GraphAlgotithms;
import com.gmail.at.ivanehreshi.graph.GraphAlgotithms.BFSValueComputer;
import com.gmail.at.ivanehreshi.interfaces.GraphicManipulator;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;
import com.gmail.at.ivanehreshi.main.panels.GraphViewer;
import com.gmail.at.ivanehreshi.main.panels.VertexUI;
import com.gmail.at.ivanehreshi.main.panels.graphinfo.GraphInfo.GraphInfoTab;

public class BFSPanel extends JPanel implements QueuedUpdatable, GraphInfoTab{
	
	GraphicUIApp app;
	BFSVisualization bfsVisualization;
	
	boolean needToUpdate = false;
	private JScrollPane scrollPane;
	private JTable table;
	private BFSValueComputer bfs; 
	private int startVertex = 0;
	
	public BFSPanel(GraphicUIApp app){
		this.app = app;
		
		for(VertexUI vui: app.graphViewer.verticesUI ){
			vui.mainColor = Color.BLUE;
			vui.hover = false;
		}
		
		setLayout(new FlowLayout());
	}
	
	public void GUIInit(){
		String inputBoxStr = 
				JOptionPane.showInputDialog(app, "Введіть вершину з якої слід почати пошук");
		
		int value = Integer.valueOf(inputBoxStr);
		value--;
		if(value>= 0 && value < app.graph.verticesCount)
			startVertex = value;
		
		init();
	}
	
	private void init() {
		removeAll();
		
		ArrayList<ArrayList<String>> data;
		
		
		bfs = new GraphAlgotithms.BFSValueComputer(app.graph);
		ArrayList<String[]> protocol = new ArrayList<String[]>();
		
		bfs.computeAndSaveProtocol(startVertex, protocol, true);
		
		String[][] rows = new String[protocol.size()][];
		for(int i = 0; i < protocol.size(); i++){
			rows[i] = new String[1];
			rows[i][0] = protocol.get(i)[0];
		}
		
		table = new JTable( rows,new String[] {" "});
		
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		add(scrollPane);
		
		if (app.graphViewer.graphicManipulators.contains(bfsVisualization))
			app.graphViewer.graphicManipulators.remove(bfsVisualization);
		
		app.graphViewer.graphicManipulators.clear();
		bfsVisualization = new BFSVisualization();
		app.graphViewer.graphicManipulators.add(bfsVisualization);
	}

	public class BFSVisualization implements GraphicManipulator{
		
		public static final long frameTime = 400;
		public long currentFrameTime = 0;
		public Integer pointerToCurrent = null;
		public Integer pointerToLast = null;
		public Color lastColor;
		
		@Override
		public void update(long deltaTime) {
			Container p = getParent();
			Object parent = null;
			
			if(p instanceof JViewport){
				parent = ((JViewport) p).getParent();
			}
			
			if(parent instanceof GraphInfo){
				parent = ((JViewport) parent).getParent();
			}
		
			if (parent instanceof GraphInfo) {
				GraphInfo ginfo = (GraphInfo) p;
				if(! (ginfo.getSelectedComponent() instanceof BFSPanel) 
						|| ((BFSPanel)ginfo.getSelectedComponent()) != BFSPanel.this){
					
						if(pointerToLast != null){
							int u = bfs.orderedVertices.get(pointerToLast);
							 app.graphViewer.verticesUI.get(u).hover = false;
							 app.graphViewer.verticesUI.get(u).mainColor = lastColor;
						}
						return;
				}
			}
			
			currentFrameTime+=deltaTime;
			if(currentFrameTime > frameTime){
				currentFrameTime = 0;
				
				if(pointerToCurrent == null)
					pointerToCurrent = 1;
				

				
				int v = bfs.orderedVertices.get(pointerToCurrent);
				if(v == -1)
					return;
				app.graphViewer.verticesUI.get(v).hover = true;
				lastColor = app.graphViewer.verticesUI.get(v).mainColor;
				app.graphViewer.verticesUI.get(v).mainColor = new Color(150, 0, 255);
				
				if(pointerToLast != null){
					Integer u = bfs.orderedVertices.get(pointerToLast);
					if(u != null){
						 app.graphViewer.verticesUI.get(u).hover = false;
						 app.graphViewer.verticesUI.get(u).mainColor = lastColor;
					}
				}
				
				pointerToLast = new Integer(pointerToCurrent);
				pointerToCurrent++;
				
				if(bfs.orderedVertices.get(pointerToCurrent) == -1){
					pointerToCurrent = 1;
				}
			}
		}

		
	}

	@Override
	public void queryUpdate() {
		// TODO Auto-generated method stub
		needToUpdate = true;
	}

	@Override
	public void updateIfNeeded() {
		// TODO Auto-generated method stub
		GUIInit();
		if(needToUpdate){
			needToUpdate = false;
		}
	}

	@Override
	public void onActivated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeactivated() {
		// TODO Auto-generated method stub
		app.graphViewer.graphicManipulators.clear();
		for(VertexUI vui: app.graphViewer.verticesUI ){
			vui.mainColor = Color.BLUE;
			vui.hover = false;
		}
	}
	
}
