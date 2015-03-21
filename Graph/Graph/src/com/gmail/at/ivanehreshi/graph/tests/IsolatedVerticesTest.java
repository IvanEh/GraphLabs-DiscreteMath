package com.gmail.at.ivanehreshi.graph.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.gmail.at.ivanehreshi.graph.OrientedGraph;

@RunWith(Parameterized.class)
public class IsolatedVerticesTest {
	
	@Parameterized.Parameters
	public static List<Object[]> data(){
		return Arrays.asList(new Object[][]{
//				{3, Arrays.asList(new Integer[]{0, 1,  1, 2, 2, 0}), 0, 0, 1 },
//				{6, Arrays.asList(new Integer[]{0, 1,  1, 2, 2, 0, 1, 3, 3, 4, 4, 5, 5, 3}), 0, 0, 2 },
				{6, Arrays.asList(new Integer[]{0, 1,  1, 2, 2, 0, 1, 3, 3, 4, 4, 5, 5, 3, 4, 2}), 0, 0, 3 },
//				{4, Arrays.asList(new Integer[]{1, 2,  1, 3}), 2, 1, 0 }
		});
	}
	
	public OrientedGraph graph;
	public List<Integer> edges;
	public int hangingVerticesCount;
	public int isolatedVertices;
	public int cyclesCount;
	
	public IsolatedVerticesTest(int verticesCount, List<Integer> edges, int hangingVerticesCount, 
			int isolatedVertices, int cyclesCount) {
		// TODO Auto-generated constructor stub
		graph =  new OrientedGraph(verticesCount);
		this.edges = edges;
		
		for(Iterator<Integer> i= edges.iterator(); i.hasNext();){
			graph.connect(i.next(), i.next());
		}
		
		this.hangingVerticesCount = hangingVerticesCount;
		this.isolatedVertices = isolatedVertices;
		
		this.cyclesCount = cyclesCount;
	}
	
	@Test
	public void test1() {
		
		for(Iterator<Integer> i= edges.iterator(); i.hasNext();){
			int v1 = i.next();
			int v2 = i.next();
			assertTrue(graph.isConnected(v1, v2)
					|| !graph.vertexExists(v1)
					|| !graph.vertexExists(v2));
		}
		
	}

	
	@Test
	public void test2() {
		
		int count = 0;
	
		for(int i = 0; i < graph.verticesCount; i++){
			if(graph.isHanging(i))
				count++;
		}
		
		assertTrue(count == hangingVerticesCount);
		
	}
	
	@Test
	public void test3() {
		
		int count = 0;
	
		for(int i = 0; i < graph.verticesCount; i++){
			if(graph.isIsolated(i))
				count++;
		}
		
		assertTrue(count == isolatedVertices);
		
	}
	
	@Test
	public void test4(){
		int count = 0;
		
		graph.findAllCycles();
	}
	
}
