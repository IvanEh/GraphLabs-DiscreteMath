package com.gmail.at.ivanehreshi.main.panels.graphinfo;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.gmail.at.ivanehreshi.graph.EulerPathFinder;
import com.gmail.at.ivanehreshi.graph.HamiltonianCircuitFinder;
import com.gmail.at.ivanehreshi.graph.HamiltonianPathFinder;
import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;
import com.gmail.at.ivanehreshi.main.panels.graphinfo.GraphInfo.GraphInfoTab;

public class Lab6Tab extends JPanel implements QueuedUpdatable, GraphInfoTab{

	ButtonGroup group;
	JRadioButton hamiltPathRadio;
	JRadioButton hamiltCycleRadio;

	JRadioButton eulerRadio;
	Button computeButton;
	JLabel result = new JLabel("<>");
	
	SpringLayout layout = new SpringLayout();
	private GraphicUIApp app;
	private boolean needToUpdate;
	
	public Lab6Tab(GraphicUIApp a){
		this.app = a;
		createGUI();
		manageListeners();
		updateData();
	}
	
	private void updateData() {
		if(group.getSelection() == hamiltPathRadio.getModel()){
			hamiltonPathCompute();
		}else{
			eulerCompute();
		}
	}

	private void manageListeners() {
		computeButton.addActionListener(new TaskSelectionAction());
	}

	private void createGUI() {
		group = new ButtonGroup();
		
		hamiltPathRadio = new JRadioButton("Гамільтонів шлях");
		hamiltCycleRadio = new JRadioButton("Гамільтонів цикл");
		eulerRadio  =new JRadioButton("Ейлерів цикл(шлях)");
		group.add(hamiltPathRadio);
		group.add(hamiltCycleRadio);
		group.add(eulerRadio);
		
		computeButton = new Button("Обчислити");
		
		setLayout(layout);
		
		layout.putConstraint(SpringLayout.WEST, hamiltPathRadio, 60, SpringLayout.WEST, this);
		layout.putConstraint(SpringLayout.NORTH, hamiltPathRadio, 20, SpringLayout.NORTH, this);
		add(hamiltPathRadio);

		layout.putConstraint(SpringLayout.WEST, hamiltCycleRadio, 0, SpringLayout.WEST, hamiltPathRadio);
		layout.putConstraint(SpringLayout.NORTH, hamiltCycleRadio, 20, SpringLayout.NORTH, hamiltPathRadio);
		add(hamiltCycleRadio);
		
		layout.putConstraint(SpringLayout.WEST, eulerRadio, 0, SpringLayout.WEST, hamiltCycleRadio);
		layout.putConstraint(SpringLayout.NORTH, eulerRadio, 20, SpringLayout.NORTH, hamiltCycleRadio);
		add(eulerRadio);
		
		Box titleBox = Box.createVerticalBox();
		titleBox.setBorder(new TitledBorder("ЗАДАЧA"));
		layout.putConstraint(SpringLayout.WEST, titleBox, -10, SpringLayout.WEST, hamiltPathRadio);
		layout.putConstraint(SpringLayout.NORTH, titleBox, 5, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.EAST, titleBox, 25, SpringLayout.EAST, hamiltPathRadio);
		layout.putConstraint(SpringLayout.SOUTH, titleBox, 10, SpringLayout.SOUTH, eulerRadio);
		add(titleBox);
		
		
		layout.putConstraint(SpringLayout.EAST, computeButton, 0, SpringLayout.EAST, titleBox);
		layout.putConstraint(SpringLayout.NORTH, computeButton, 10, SpringLayout.SOUTH, titleBox);
		add(computeButton);

		layout.putConstraint(SpringLayout.EAST, result, 10, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.NORTH, result, 20, SpringLayout.SOUTH, titleBox);
		layout.putConstraint(SpringLayout.WEST, result, 10, SpringLayout.WEST, this);
		result.setHorizontalAlignment(SwingConstants.CENTER);
		add(result);
	}

	@Override
	public void onActivated() {
		// TODO Auto-generated method stub
		updateData();
	}

	@Override
	public void onDeactivated() {
		
	}

	@Override
	public void queryUpdate() {
		needToUpdate = true;
	}

	@Override
	public void updateIfNeeded() {
		// TODO Auto-generated method stub
		if(needToUpdate){
			needToUpdate = false;
			updateData();
		}
		
	}

	private class TaskSelectionAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(group.getSelection() == hamiltPathRadio.getModel()){
				hamiltonPathCompute();
			}else{
				if(group.getSelection() == eulerRadio.getModel())
					eulerCompute();
				else
					hamiltonCircuitCompute();
			}
		}
	}

	public void hamiltonPathCompute() {
		HamiltonianPathFinder finder = new HamiltonianPathFinder(app.graph);
		finder.compute();
		StringBuilder mess = new StringBuilder();
		if(finder.existPath()){
			mess.append("Гамільтонів шлях: ");
			mess.append("< ");
			for(int i = 0; i < finder.getPath().size(); i++){
				mess.append(finder.getPath().get(i) + 1);
				mess.append(" ");
			}
			mess.append(">");
		}else{
			mess = new StringBuilder("Не існує Гамільтонового шляху");
		}
		result.setText(mess.toString());
	}

	public void hamiltonCircuitCompute() {
		HamiltonianCircuitFinder finder = new HamiltonianCircuitFinder(app.graph);
		finder.compute();
		StringBuilder mess = new StringBuilder();
		if(finder.existPath()){
			mess.append("Гамільтонів цикл: ");

			mess.append("< ");
			for(int i = 0; i < finder.getPath().size(); i++){
				mess.append(finder.getPath().get(i) + 1);
				mess.append(" ");
			}
			mess.append(">");
		}else{
			mess = new StringBuilder("Не існує гамільтонового циклу");
		}
		result.setText(mess.toString());
		
	}

	public void eulerCompute() {
		EulerPathFinder finder = new EulerPathFinder(app.graph);
		finder.compute();
		StringBuilder mess = new StringBuilder();
		if(finder.existPath()){
			mess.append("Ейлерів ");
			if(finder.isCycle())
				mess.append(" цикл");
			else
				mess.append(" шлях");
			
			mess.append(": ");
			mess.append("< ");
			for(int i = 0; i < finder.getPath().size(); i++){
				mess.append(finder.getPath().get(i) + 1);
				mess.append(" ");
			}
			mess.append(">");
		}else{
			mess = new StringBuilder("Не існує ейлерового циклу");
		}
		result.setText(mess.toString());
	}
	
}
