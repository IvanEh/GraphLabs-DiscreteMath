package com.gmail.at.ivanehreshi.graph;

public class GraphEvent {
	public enum EventType {
		VERTICES_CONNECTED, 
		VERTICES_DELETED,
		GRAPH_CREATED
	}
	
	public EventType eventType;
	public int from = -1, to = -1;
	
	public GraphEvent setEventType(EventType t){
		eventType = t;
		return this;
	}
}
