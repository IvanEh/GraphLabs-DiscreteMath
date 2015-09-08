package com.gmail.at.ivanehreshi.main.panels.graphinfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.gmail.at.ivanehreshi.graph.EuclideanTSPSolver;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;
import com.gmail.at.ivanehreshi.main.panels.graphinfo.GraphInfo.GraphInfoTab;
import com.gmail.at.ivanehreshi.utility.PointD2D;

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
		layout.putConstraint(SpringLayout.NORTH, costTooltip, 10, SpringLayout.SOUTH, path);
		add(costTooltip);
		
		layout.putConstraint(SpringLayout.WEST, cost, 0, SpringLayout.WEST, path);
		layout.putConstraint(SpringLayout.NORTH, cost, 0, SpringLayout.NORTH, costTooltip);
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
			ArrayList<PointD2D> points = new ArrayList<>();
			if(val == JFileChooser.APPROVE_OPTION){
				filePath.setText(fileChooser.getSelectedFile().getPath());
				try {
					Scanner sc = new Scanner(fileChooser.getSelectedFile());
					for(int i = 0; sc.hasNext(); i++){
						double x = sc.nextDouble();
						double y = sc.nextDouble();
						PointD2D p = new PointD2D(x, y);
						points.add(p);
						
					}
					EuclideanTSPSolver solver = new EuclideanTSPSolver(points);
					solver.compute();
					StringBuilder s = new StringBuilder("<html><body>");
					for(int i = 0; i < solver.getPath().size(); i++){
						s.append(i + 1);
						s.append("\t");
						double x = points.get(solver.getPath().get(i)).x;
						double y = points.get(solver.getPath().get(i)).y;
						s.append("P");
						s.append(solver.getPath().get(i) + 1);
						s.append(" (");
						s.append(x);
						s.append(";");
						s.append(y);
						s.append(" )");
						s.append("<br>");
					}
					s.append("</body></html>");
					
					path.setText(s.toString());
					cost.setText(String.valueOf(solver.getDist()));
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			
		}
		
	}
	
}
