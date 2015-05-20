package com.gmail.at.ivanehreshi.main.panels.graphinfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.stage.FileChooser;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;
import com.gmail.at.ivanehreshi.main.panels.graphinfo.GraphInfo.GraphInfoTab;
import com.sun.org.apache.bcel.internal.generic.F2D;

/**
 * @author Ivan
 *
 */
public class TSPTab extends JPanel implements QueuedUpdatable, GraphInfoTab{


	JButton button;
	private GraphicUIApp app;
	SpringLayout layout = new SpringLayout();
	JLabel pathTooltip = new JLabel("Маршут");
	JLabel path = new JLabel("1");
	JLabel costTooltip = new JLabel("Відстань");
	JLabel cost = new JLabel("1");
	JLabel filePath = new JLabel("1");
	
	public TSPTab(GraphicUIApp app){
		this.app = app;
		initGUI();
		button.addActionListener(new LoadFileAction());
	}
	
	private void initGUI() {
		setLayout(layout);
		
		button = new JButton("Обзор");
		
		layout.putConstraint(SpringLayout.WEST, button,20, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, button, 20, SpringLayout.NORTH, this);
		add(button);
		
		layout.putConstraint(SpringLayout.WEST, filePath, 70, SpringLayout.EAST, button);
		layout.putConstraint(SpringLayout.NORTH, filePath, 5, SpringLayout.NORTH, button);
		add(filePath);
		
		layout.putConstraint(SpringLayout.WEST, pathTooltip, 0, SpringLayout.WEST, button);
		layout.putConstraint(SpringLayout.NORTH, pathTooltip, 10, SpringLayout.SOUTH, button);
		add(pathTooltip);
		
		layout.putConstraint(SpringLayout.WEST, path, 0, SpringLayout.WEST, filePath);
		layout.putConstraint(SpringLayout.NORTH, path, 10, SpringLayout.SOUTH, button);
		add(path);
		
		layout.putConstraint(SpringLayout.WEST, costTooltip, 0, SpringLayout.WEST, pathTooltip);
		layout.putConstraint(SpringLayout.NORTH, costTooltip, 10, SpringLayout.SOUTH, pathTooltip);
		add(costTooltip);
		
		layout.putConstraint(SpringLayout.WEST, cost, 0, SpringLayout.WEST, path);
		layout.putConstraint(SpringLayout.NORTH, cost, 10, SpringLayout.SOUTH, pathTooltip);
		add(cost);
	}

	@Override
	public void onActivated() {
		// TODO Auto-generated method stub
		app.graphViewer.setVisible(false);
	}

	@Override
	public void onDeactivated() {
		// TODO Auto-generated method stub
		app.graphViewer.setVisible(true);
	}

	@Override
	public void queryUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateIfNeeded() {
		// TODO Auto-generated method stub
		
	}

	
	private class LoadFileAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int val = fileChooser.showOpenDialog(app);
			if(val == JFileChooser.APPROVE_OPTION){
				filePath.setText(fileChooser.getSelectedFile().getPath());
			}
		}
		
	}
	
}
