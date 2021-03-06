package com.gmail.at.ivanehreshi.listeners;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.gmail.at.ivanehreshi.graph.GraphEvent;
import com.gmail.at.ivanehreshi.graph.OrientedGraph;
import com.gmail.at.ivanehreshi.graph.GraphEvent.EventType;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;

public class InputGraphAction implements ActionListener {
	private GraphicUIApp frame;
	
	public InputGraphAction(OrientedGraph graph, GraphicUIApp frame){
		this.frame = frame;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
		int paramVertCount = 0;
		int paramEdgeCount = 0;
		
		frame.graphInfo.disabledUpdate = true;
		
		 int mode = JOptionPane.showConfirmDialog((Component)frame, "������ ����� ����", "",
				 JOptionPane.YES_NO_OPTION);
		 
		 if(mode == 0){
		 	String strVertCount = JOptionPane.showInputDialog(frame, "������ ������� ������");
		 	paramVertCount = Integer.valueOf(strVertCount);
		 	frame.graph = new OrientedGraph(paramVertCount);
		 	frame.manageObservers();
			frame.graph.notifyAllAboutCreating();
		 }
		 
		 String strEdgeCount = JOptionPane.showInputDialog(frame, "������ ������� �����");
		 
		 paramEdgeCount = Integer.valueOf(strEdgeCount);
		 
		 for(int i = 0; i < paramEdgeCount; i++){
			String parse = JOptionPane.showInputDialog(frame, "������ ������� ����� ����� + e" + i);
			String[] num = parse.split(" ");
			if((num.length != 2 && frame.graph.isWeighted) ||
					(num.length != 3 && !frame.graph.isWeighted))
				continue;
			
			int v1 = Integer.valueOf(num[0])-1;
			int v2 = Integer.valueOf(num[1])-1;
			
			if(num.length == 3){
				int w = Integer.valueOf(num[2]);
				frame.graph.connect(v1,  v2, w);
			}else{
				frame.graph.connect(v1, v2);
			}
			
		 }
		 
		 frame.graphInfo.disabledUpdate = false;
		 frame.graph.notifyObservers(new GraphEvent(EventType.GRAPH_CREATED), true);
	}

}
