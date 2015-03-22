package com.gmail.at.ivanehreshi.main.panels.graphinfo;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.gmail.at.ivanehreshi.graph.GraphAlgotithms;
import com.gmail.at.ivanehreshi.graph.GraphAlgotithms.DFSValueComputer;
import com.gmail.at.ivanehreshi.interfaces.GraphicManipulator;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;
import com.gmail.at.ivanehreshi.main.panels.GraphViewer;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;

public class DFSPanel extends JPanel implements QueuedUpdatable{
	
	GraphicUIApp app;
	DFSVisualization dfsVisualization;
	
	boolean needToUpdate = false;
	private JScrollPane scrollPane;
	private JTable table;
	private DFSValueComputer dfs; 
	
	public DFSPanel(GraphicUIApp app){
		this.app = app;
		
		setLayout(new FlowLayout());
		
		initTable();
	
		dfsVisualization = new DFSVisualization(null, null);
		app.graphViewer.graphicManipulators.add(dfsVisualization);
	}
	
	private void initTable() {
		removeAll();
		
		ArrayList<ArrayList<String>> data;
		
		
		dfs = new GraphAlgotithms.DFSValueComputer(app.graph);
		ArrayList<String[]> stackFingerPrint = new ArrayList<String[]>();
		dfs.computeAndSaveStack(stackFingerPrint , 0, true);
		
		String[][] rows = new String[stackFingerPrint.size()][];
		for(int i = 0; i < stackFingerPrint.size(); i++){
			rows[i] = new String[1];
			rows[i][0] = stackFingerPrint.get(i)[0];
		}
		
		table = new JTable( rows,new String[] {" "});
		
		scrollPane = new JScrollPane(table);
		table.setFillsViewportHeight(true);
		add(scrollPane);
		
	}

	public class DFSVisualization implements GraphicManipulator{

		//GraphAlgotithms.DFSValueComputer dfs;
		
		public static final long frameTime = 400;
		public long currentFrameTime = 0;
		public Integer pointerToCurrent = null;
		public Integer pointerToLast = null;
		public Color lastColor;
		
		public DFSVisualization(GraphAlgotithms.DFSValueComputer dfs, GraphViewer graphViewer){
			//this.dfs = dfs;
			//this.graphViewer = graphViewer;
		}
		
		@Override
		public void update(long deltaTime) {
			System.out.println(deltaTime);
			currentFrameTime+=deltaTime;
			if(currentFrameTime > frameTime){
				currentFrameTime = 0;
				
				if(pointerToCurrent == null)
					pointerToCurrent = 1;
				

				
				int v = dfs.orderedVertices.get(pointerToCurrent);
				if(v == -1)
					return;
				app.graphViewer.verticesUI.get(v).hover = true;
				lastColor = app.graphViewer.verticesUI.get(v).mainColor;
				app.graphViewer.verticesUI.get(v).mainColor = new Color(150, 0, 255);
				
				if(pointerToLast != null){
					Integer u = dfs.orderedVertices.get(pointerToLast);
					if(u != null){
						 app.graphViewer.verticesUI.get(u).hover = false;
						 app.graphViewer.verticesUI.get(u).mainColor = lastColor;
					}
				}
				
				pointerToLast = new Integer(pointerToCurrent);
				pointerToCurrent++;
				
				if(dfs.orderedVertices.get(pointerToCurrent) == -1){
					pointerToCurrent = 1;
				}
			}
		}

		
	}

	@Override
	public void queryUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateIfNeeded() {
		// TODO Auto-generated method stub
		
	}
	
}
