package com.gmail.at.ivanehreshi.interfaces;

import java.awt.Dimension;

import javax.swing.JPanel;

public abstract class GraphicObject extends JPanel implements GraphicManipulator{ 
	
	public abstract void update(double deltaTime);
	
	@Override
	public Dimension getMinimumSize(){
		if (getParent() == null)
			return null;
		
		return getParent().getSize();
		
	}

	@Override
	public boolean isOpaque(){
		return false;
	}
	
}
