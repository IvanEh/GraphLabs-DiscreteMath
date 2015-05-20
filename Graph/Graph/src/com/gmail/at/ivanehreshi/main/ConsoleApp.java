package com.gmail.at.ivanehreshi.main;

import java.util.Scanner;

import com.gmail.at.ivanehreshi.graph.EulerPathFinder;
import com.gmail.at.ivanehreshi.graph.GraphAlgotithms;
import com.gmail.at.ivanehreshi.graph.GraphConsoleTool;
import com.gmail.at.ivanehreshi.graph.HamiltonianPathFinder;
import com.gmail.at.ivanehreshi.graph.OrientedGraph;

public class ConsoleApp {
	
	
	public static void main(String[] arg){
		OrientedGraph graph = new OrientedGraph(7);
		int v, u;
		Scanner sc = new Scanner(System.in);
		while(sc.hasNextInt()){
			v = sc.nextInt();
			u = sc.nextInt();
			graph.connect(v, u);
		}
//		graph.connect(0, 1);
//		graph.connect(1, 0);
//		graph.connect(1, 2);
//		graph.connect(2, 3);
//		graph.connect(3, 0);
		GraphConsoleTool.print_adjacency_matrix(graph, System.out);
		graph.computeDistanceMatrix();
		
		GraphAlgotithms.StronglyConnectedComponentsFinder finder = 
				new GraphAlgotithms.StronglyConnectedComponentsFinder(graph);
		
		finder.compute();
		
		HamiltonianPathFinder finder3 = new HamiltonianPathFinder(graph);
		finder3.compute();
		
		EulerPathFinder finder2 = new EulerPathFinder(graph);
		finder2.compute();
	}
	
//	0 1
//	1 0
//	1 2
//	2 3
//	3 0
//	2 4
//	4 8
//	8 3
//	4 5
//	5 6
//	6 7
//	7 5
//	d	
}
