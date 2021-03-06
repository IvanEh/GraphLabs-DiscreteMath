package com.gmail.at.ivanehreshi.main.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.gmail.at.ivanehreshi.graph.EdgeTo;
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
		
	private Timer timer;

	private Color WEIGHT_FONT_COLOR = new Color(10, 30, 190, 128);

	private Color CONECTION_COLOR = new Color(180, 20, 240,128);

	private Color BOLD_CONNECTION_COLOR = new Color(170, 10, 240,255);
	
	public  GraphViewer(GraphicUIApp app) {
		super();
		
		this.app = app;
		
		setLayout(this);
		setOpaque(false);
		setGraph(app.graph);
		
		timer = new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				repaint();
			}
		});
		timer.setRepeats(true);
		timer.start();
		
	}
	
	public void unhoverAll(){
		for(VertexUI vui: verticesUI){
			vui.hover =false;
		}
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
		
//		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
		
		long delta ;	
		if(lastTimeStamp == 0)
			delta = 0;
		else
			delta = System.currentTimeMillis() - lastTimeStamp;
		lastTimeStamp = System.currentTimeMillis();
		
		for(GraphicManipulator o: graphicManipulators){
			if(o == null)
				continue;
			
			o.update(delta);
			if (o instanceof GraphicObject) {
				GraphicObject GO = (GraphicObject) o;
				
				GO.paintComponents(g);		
			}
		}
		
		paintConnections(g2d); 
	}

	
	private void paintConnections(Graphics2D g){
		

		
		for(int i = 0; i < verticesUI.size(); i++)
		{
			Color oldColor;
			if(i >= app.graph.adjacencyList.size())
				break;
			VertexUI vertex = verticesUI.get(i);
			
			for(EdgeTo e: app.graph.adjacencyList.get(i) ){
				int to = e.to;
				if(to != i){		
					g.setColor(CONECTION_COLOR );
					
					double dx = verticesUI.get(to).getCenter().x - vertex.getCenter().x;
					double dy =  verticesUI.get(to).getCenter().y - vertex.getCenter().y ;
					double cosPhi = dx/Math.sqrt(dx*dx + dy*dy);
					double phi = Math.acos(cosPhi);
					double R = vertex.getSize().getHeight()/2;					
					
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
					
				
					Stroke old = g.getStroke();
							
					if(verticesUI.get(to).isHovered() && verticesUI.get(i).isHovered()){
						int thikness = 2;
						g.setStroke(new BasicStroke(thikness));
						g.setColor(BOLD_CONNECTION_COLOR = new Color(200, 130, 255,255));
					}

					oldColor = g.getColor();
					if(app.graph.isWeighted){
						int cx, cy;
						cx = (x1 + x2)/2;
						cy = (y1 + y2)/2;

						g.setFont(g.getFont().deriveFont(10f));
						g.setColor(WEIGHT_FONT_COLOR );
						
						int w = app.graph.w(i, to);
						
						g.drawString(String.valueOf(w), cx, cy);
						
					}
					g.setColor(oldColor);
					
					drawArrowLine(g,  x1, y1, x2,y2,4,4);
					
					g.setStroke(old);
					
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

	public void hover(int i, boolean b) {
		if(i >= verticesUI.size())
			return;
		if(verticesUI.size() < 0)
			return;
		
		verticesUI.get(i).hover = b;
	}
}
