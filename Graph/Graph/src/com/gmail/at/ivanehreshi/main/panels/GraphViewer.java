package com.gmail.at.ivanehreshi.main.panels;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import javax.swing.JPanel;

import com.gmail.at.ivanehreshi.graph.GraphEvent;
import com.gmail.at.ivanehreshi.graph.GraphEvent.EventType;
import com.gmail.at.ivanehreshi.graph.OrientedGraph;
import com.gmail.at.ivanehreshi.interfaces.GraphicManipulator;
import com.gmail.at.ivanehreshi.interfaces.GraphicObject;
import com.gmail.at.ivanehreshi.main.GraphicUIApp;

public class GraphViewer extends JPanel implements LayoutManager, Observer{
	private int verticesCount = 0; 
	
	GraphicUIApp app;
	public ArrayList<VertexUI> verticesUI = new ArrayList<VertexUI>();

	private boolean needToUpdate = true; 
	
	public long lastTimeStamp = 0;
	
	public ArrayList<GraphicManipulator> graphicManipulators =
			new ArrayList<GraphicManipulator>();
	
	public  GraphViewer(GraphicUIApp app) {
		super();
		
		this.app = app;
		
		setLayout(this);
		setOpaque(false);
		setGraph(app.graph);
	}
	
	public void setGraph(OrientedGraph graph){
		this.app.graph = graph;
		
		verticesUI = new ArrayList<VertexUI>();
		
		removeAll();
		
		for(int i = 0; i < graph.verticesCount; i++){
			VertexUI v = new VertexUI(String.valueOf(i+1));
			verticesUI.add(v);
			add(v);
		}
	}

	@Override
	public Component add(Component comp) {
		Component component = super.add(comp);
		
		if(comp instanceof VertexUI) {
			VertexUI vertex = (VertexUI) comp;

		}
		
		return component;
	};
	
	@Override
	public void addLayoutComponent(String name, Component comp) {

	}

	@Override
	public void layoutContainer(Container parent) {
		
		int verticesCount = parent.getComponentCount();
		Dimension dimm = parent.getSize();
		
		double alpha = 2*Math.PI/verticesCount;
		if(verticesCount == 0)
			alpha = 0;
		
		int minDim = (int) Math.min(dimm.getHeight(), dimm.getWidth());
		
		int circleSize = (int) (minDim/3/(verticesCount*0.22));
		
		int vertIter = 0;
		for(int i = 0; i < parent.getComponentCount(); i++)
		{
			Component comp = getComponent(i);
			VertexUI vertex;
			int x, y;
			vertIter = i;
			
			if(comp instanceof VertexUI){
				vertex = (VertexUI) comp;
			}else
				continue;

			
			
			double R = minDim/2.5;
			double phi = (i)*alpha;
			System.out.println(">" + R);
			
			x = (int) (  R*Math.cos(phi)  );
			y = (int) (  R*Math.sin(phi)  );
			
			int dx = (int) ((getWidth() - 2*R)/2);
			int dy = (int) ((getHeight() - 2*R)/2);
			
			x = (int) (x +R + dx) - circleSize/2;
			y = (int) (dy+R - y) - circleSize/2;
			
			
			vertex.setSize(circleSize, circleSize);
			vertex.setLocation(x, y);
		}
		
	}

	
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		// TODO Auto-generated method stub
		if(comp instanceof VertexUI)
			verticesCount--;
	}
	
	@Override
	public void paintComponent(Graphics g){
		if(app.graph == null)
			return; 
		
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, 
				RenderingHints.VALUE_ANTIALIAS_ON));
		
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
		paintConnections(g2d); 
	}

	
	private void paintConnections(Graphics2D g){
		
		long delta ;	
		if(lastTimeStamp == 0)
			delta = 0;
		else
			delta = System.currentTimeMillis() - lastTimeStamp;
		lastTimeStamp = System.currentTimeMillis();
		
		for(int i = 0; i < verticesUI.size(); i++)
		{
			VertexUI vertex = verticesUI.get(i);
			
			for(int to: app.graph.adjacencyList.get(i) ){
				if(to != i){		
					g.setColor(Color.BLACK);
					
					double dx = verticesUI.get(to).getCenter().x - vertex.getCenter().x;
					double dy =  verticesUI.get(to).getCenter().y - vertex.getCenter().y ;
					double cosPhi = dx/Math.sqrt(dx*dx + dy*dy);
					double phi = Math.acos(cosPhi);
					double R = vertex.getSize().getHeight()/2;
					System.out.println(phi/2/Math.PI*360);
					System.out.println();
					
					
					double xrho = R*Math.cos(phi);
					double yrho = R*Math.sin(phi);
					
					if(verticesUI.get(to).getCenter().y > vertex.getY() + R)
						yrho = -yrho;
						
					int x1 =(int) (vertex.getX()+R+ xrho);
					int y1 =(int) (vertex.getY() + R - yrho);
					
					phi = Math.PI - phi;
					phi = -phi;
					
					xrho = R*Math.cos(phi);
					yrho = R*Math.sin(phi);
					
					if(verticesUI.get(to).getCenter().y > vertex.getY() + R)
						yrho = -yrho;
					
					int x2 =(int) (verticesUI.get(to).getCenter().x  + xrho);
					int y2 =(int) (verticesUI.get(to).getCenter().y  - yrho);
					
					drawArrowLine(g,  x1, y1, x2,y2,4,4);
					
				}else{
					g.setColor(Color.green);
					
					int x = verticesUI.get(to).getCenter().x;
					int y = verticesUI.get(to).getCenter().y;
					
					y-= verticesUI.get(to).getHeight()/4;
					x+=  verticesUI.get(to).getWidth()/4;
					int dy = verticesUI.get(to).getHeight()/5;
					
					drawArrowLine(g, x, y, x, y+dy, 2, 2);
					x+=verticesUI.get(to).getWidth()/8;
					drawArrowLine(g, x, y+dy,x, y, 2, 2);
				}
			}
			
		}
		
		for(GraphicManipulator o: graphicManipulators){
			o.update(delta);
			if (o instanceof GraphicObject) {
				GraphicObject GO = (GraphicObject) o;
				
				GO.paintComponents(g);
				
			}
		}
		
	}
	
	  private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h){
	        int dx = x2 - x1, dy = y2 - y1;
	        double D = Math.sqrt(dx*dx + dy*dy);
	        double xm = D - d, xn = xm, ym = h, yn = -h, x;
	        double sin = dy/D, cos = dx/D;

	        x = xm*cos - ym*sin + x1;
	        ym = xm*sin + ym*cos + y1;
	        xm = x;

	        x = xn*cos - yn*sin + x1;
	        yn = xn*sin + yn*cos + y1;
	        xn = x;

	        int[] xpoints = {x2, (int) xm, (int) xn};
	        int[] ypoints = {y2, (int) ym, (int) yn};

	        g.drawLine(x1, y1, x2, y2);
	        g.fillPolygon(xpoints, ypoints, 3);
	        
	        
	  }

	@Override
	public void update(Observable o, Object arg) {

		GraphEvent event = (GraphEvent) arg;
		
		if(event.eventType == GraphEvent.EventType.GRAPH_CREATED){
			setGraph(app.graph);
		}else{
			if(event.eventType == EventType.VERTICES_CONNECTED){
				needToUpdate = true;
			}
		}
		
		repaint();
		validate();
	}
}
