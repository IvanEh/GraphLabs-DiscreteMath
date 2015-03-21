package com.gmail.at.ivanehreshi.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Scanner;

import javax.swing.JFileChooser;

import com.gmail.at.ivanehreshi.graph.OrientedGraph;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;

public class FromFileAction implements ActionListener{

	GraphicUIApp app;
	

	public  FromFileAction(GraphicUIApp p){
		app = p;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int val = fileChooser.showOpenDialog(app);
		
		if(val == JFileChooser.APPROVE_OPTION){
			readFromFile(fileChooser.getSelectedFile());
		}
		
	}

	private void readFromFile(File selectedFile) {
		// TODO Auto-generated method stub
		try {
			Scanner sc = new Scanner(selectedFile);
			int vertCount = sc.nextInt();
			
			app.graph = new OrientedGraph(vertCount);
			
			for(int i = 0;  sc.hasNextInt(); i+=1 ){
				int v1 = sc.nextInt()-1;
				int v2 = sc.nextInt()-1;
				app.graph.connect(v1 , v2 );
			}
			sc.close();
			
		 	app.manageObservers();
			app.graph.notifyAllAboutCreating();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
