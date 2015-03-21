package com.gmail.at.ivanehreshi.main.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.border.StrokeBorder;

import com.gmail.at.ivanehreshi.graph.OrientedGraph;

public class VertexUI extends JPanel{
	public String caption = "1";
	public boolean hover = false;
	private boolean hoveredByMouse;
	
	private int vertex;
	private OrientedGraph graph;
	
	public  VertexUI(String caption) {
		super();
		this.caption = caption;
		
		
		
		addMouseListener(new MouseListener(this));
	}
	
	Point getCenter(){
		return new Point(getX()+getWidth()/2, getY()+getHeight()/2);
	}
	
	public boolean isHovered(){
		return hover || hoveredByMouse;
	}
	
	@Override
	 public boolean isOptimizedDrawingEnabled(){
		 return true;
	 }

	@Override
	public boolean isOpaque() {
		return false;
	};
	
	@Override
	public void paintComponent(Graphics g){
//		super.paintComponent(g);
		
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(g2d.getFont().deriveFont(0,getHeight()/2));
		
		FontMetrics metrics =  g2d.getFontMetrics();
		int thikness = 1;
		
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON));
		
		Stroke old = g2d.getStroke();
		
//		g2d.drawRect(0, 0, getWidth(), getHeight());
		
		if(isHovered()){
			thikness = 2;
			g2d.setStroke(new BasicStroke(thikness));
		}
		
		
		g2d.setColor(Color.RED);
		
		g2d.drawOval(0, 0, getWidth()-thikness, getHeight()-thikness);
	
		g2d.setStroke(old);
		
		
		Dimension captionDim = new Dimension(metrics.stringWidth(caption), metrics.getHeight());
		g2d.drawString(caption,
				(int)(getWidth()-captionDim.getWidth())/2,
				(int)(getHeight() + captionDim.getHeight()/2)/2 );
		
		
	}
	
	class MouseListener extends MouseAdapter{
		
		VertexUI v;
		
		MouseListener(VertexUI v){
			this.v = v;
		}
		
		@Override
		 public void mouseEntered(java.awt.event.MouseEvent event){
			v.hoveredByMouse = true;
			v.repaint();
		}
		
		@Override
		 public void mouseExited(java.awt.event.MouseEvent event){
			v.hoveredByMouse = false;
			v.repaint();
		}
	}
}
