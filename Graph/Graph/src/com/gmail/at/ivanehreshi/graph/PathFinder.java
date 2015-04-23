package com.gmail.at.ivanehreshi.graph;

import java.util.ArrayList;

public interface PathFinder {
	boolean findAll();
	boolean relax(int u, int v, int w);
	public ArrayList<Integer> getPath(int to);
	public int[] getDist();
}
