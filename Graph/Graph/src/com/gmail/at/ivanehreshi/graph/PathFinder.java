package com.gmail.at.ivanehreshi.graph;

import java.util.ArrayList;

public interface PathFinder {
	public static int INF = Integer.MAX_VALUE - 1000;
	boolean findAll();
	boolean relax(int u, int v, double w);
	public ArrayList<Integer> getPath(int to);
	public double[] getDist();
}
