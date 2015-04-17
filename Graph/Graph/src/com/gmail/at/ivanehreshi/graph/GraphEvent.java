package com.gmail.at.ivanehreshi.graph;

public class GraphEvent {
	public enum EventType {
		VERTICES_CONNECTED, 
		VERTICES_DELETED,
		GRAPH_CREATED
	}
	
	public EventType eventType = EventType.GRAPH_CREATED;
	public int from = -1, to = -1;
	public int w = 0;
	
	public GraphEvent(){};
	public GraphEvent(EventType t){
		eventType = t;
	}
	
	public GraphEvent setEventType(EventType t){
		eventType = t;
		return this;
	}
}
