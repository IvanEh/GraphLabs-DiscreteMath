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
		
		/**
		 * <div>
		 * Icons made by
		 *  <a href="http://www.flaticon.com/authors/freepik" title="Freepik">Freepik</a> 
		 *  from <a href="http://www.flaticon.com" title="Flaticon">www.flaticon.com</a> 
		 *  is licensed by <a href="http://creativecommons.org/licenses/by/3.0/" title="Creative Commons BY 3.0">CC BY 3.0</a>
		 *  </div>
		 */
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
