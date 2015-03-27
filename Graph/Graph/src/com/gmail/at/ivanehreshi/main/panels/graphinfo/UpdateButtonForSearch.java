package com.gmail.at.ivanehreshi.main.panels.graphinfo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.gmail.at.ivanehreshi.interfaces.QueuedUpdatable;

public class UpdateButtonForSearch extends JPanel {
	JLabel title;
	JButton button;
	QueuedUpdatable comp;
	
	public UpdateButtonForSearch(String title, QueuedUpdatable comp){
		super();
		this.title = new JLabel(title);
		this.button = new JButton();
		this.comp = comp;
		
		initGUI();
		setEventListeners();
	}
	
	private void initGUI(){
		setOpaque(false);
		
		button.setBorder(BorderFactory.createEmptyBorder());
		button.setContentAreaFilled(false);
		ImageIcon defaultIcon = new ImageIcon(
				this.getClass().getResource("refresh.png"));
		button.setIcon(defaultIcon );
		
		add(this.title);
		add(this.button);
	}
	
	
	private void setEventListeners(){
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				comp.queryUpdate();
				comp.updateIfNeeded();
			}
		});
	}
}
