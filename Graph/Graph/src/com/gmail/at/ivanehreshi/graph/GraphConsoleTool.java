package com.gmail.at.ivanehreshi.graph;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Observable;

public class GraphConsoleTool{
	
	public static void print_adjacency_matrix(OrientedGraph graph, PrintStream out){
		out.println( "Матириця сумiжностi" );

		out.printf("%3s", " ");

		for (int i = 0; i < graph.verticesCount; i++)
		{
			out.printf("%3d", i+1);
		}

		out.println();

		for (int i = 0; i < graph.verticesCount; i++)
		{
			out.printf("%3d", i+1);
			for (int j = 0; j < graph.verticesCount; j++)
			{
		
				if (!graph.vertexExists(i) || !graph.vertexExists(j))
					out.printf("%3d", 0);
				else
					out.printf("%3d", 
							graph.adjacencyMatrix.get(i).get(j));
			}

			out.println();
		}

	}
}
