package com.gmail.at.ivanehreshi.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Stack;

import com.gmail.at.ivanehreshi.utility.Pair;

public class GraphAlgotithms {
	
	/**
	 * graph connectivity type. Suggesting that the distanceMatrix is valid
	 */
	public static GraphConnectivityType computeConnectivityType(OrientedGraph g, Integer[][] distanceMatrix){
		
		boolean flag = true;
		
		for(int i = 0; i < distanceMatrix.length && flag; i++){
			for(int j = 0; j< distanceMatrix[i].length; j++){
				if(distanceMatrix[i][j] == 0){
					flag = false;
					break;
				}
			}
		}
		
		if(flag)
			return GraphConnectivityType.STRONGLY_CONNECTED;
		
		flag = true;
		for(int i = 0; i < distanceMatrix.length && flag; i++){
			for(int j = 0; j< distanceMatrix[i].length; j++){
				if(i == j)
					continue;
				if((distanceMatrix[i][j] < 1? 0: 1) + (distanceMatrix[j][i] < 1? 0: 1) == 0){
					flag = false;
					break;
				}		
			}
		}
		
		if(flag)
			return GraphConnectivityType.SIDED;
		
		flag = true;
		
		int[][] A = new int[distanceMatrix.length][distanceMatrix.length],
				B = new int[distanceMatrix.length][distanceMatrix.length],
				Delta = new int[distanceMatrix.length][distanceMatrix.length];
		
		for(int i = 0; i < distanceMatrix.length; i++){
			for(int j = 0; j < distanceMatrix.length; j++){
				Delta[i][j] = g.adjacencyMatrix.get(i).get(j) + g.adjacencyMatrix.get(j).get(i) + ((i == j)? 1: 0);
				A[i][j] = Delta[i][j];
			}
		}
		
		for(int k = 2;  k < distanceMatrix.length; k++){
			
			for(int i = 0; i < distanceMatrix.length; i++){
				for(int j = 0; j < distanceMatrix.length; j++){
					B[i][j] = 0;
					for(int u = 0; u < distanceMatrix.length; u++){
						B[i][j] += A[i][u]*Delta[u][j];  
					}
				}
			}
			
			for(int i = 0; i < distanceMatrix.length; i++){
				for(int j = 0; j < distanceMatrix.length; j++){
					A[i][j] = B[i][j];
				}
			}
		}
		
		for(int i = 0; i < distanceMatrix.length && flag; i++){
			for(int j = 0; j< distanceMatrix[i].length; j++){
				if(A[i][j] == 0){
					flag = false;
					break;
				}		
			}
		}
		
		
		
		if(flag)
			return GraphConnectivityType.WEAKLY_CONNECTED;
		
		return GraphConnectivityType.NO_CONNECTIVITY_TYPE;
	}
	
	public static class StronglyConnectedComponentsFinder{
	
		
		OrientedGraph g;
		private ArrayList<ArrayList<Integer>> adj;
		private ArrayList<ArrayList<Integer>> tAdj;
		public DFSValue[] dfs1, dfs2;
		private DFSColors colors[];
		private int time = 0;
		
		public ArrayList<ArrayList<Integer>> scc = new ArrayList<ArrayList<Integer>>();
		public Integer[] sccArr;
		private int sccCounter = 0;
		
		public StronglyConnectedComponentsFinder(OrientedGraph g) {
			this.g = g;
			adj = g.adjacencyMatrix;
			
			colors = new DFSColors[g.verticesCount];
			for(int i = 0; i < g.verticesCount; i++)
				colors[i] = DFSColors.WHITE;
			
			dfs1 = new DFSValue[g.verticesCount];
			dfs2 = new DFSValue[g.verticesCount];
			
			for(int i = 0; i < g.verticesCount; i++){
				dfs1[i] = new DFSValue();
				dfs2[i] = new DFSValue();
			}
			
			
			
		
			tAdj = new ArrayList<ArrayList<Integer>>(g.verticesCount);
			for (int i = 0; i < g.verticesCount; i++){
				tAdj.add(new ArrayList<Integer>(g.verticesCount));
				for(int j = 0; j < g.verticesCount; j++)
					tAdj.get(i).add(0);
			}
			
			for(int i = 0; i < g.verticesCount; i++){
				for(int j = 0; j < g.verticesCount; j++){
					int conn = adj.get(i).get(j);
					if(conn > 0){
						tAdj.get(j).set(i, conn);
					}	
				}
			}
			
			sccArr = new Integer[g.verticesCount];
		}
	
		
		public void compute(){
			dfsModLoop();
		}
		
		public boolean isCyclic(){
			
			for(int i = 0; i < scc.size(); i++){
				if(scc.get(i) != null){
					return true;
				}
			}
			
			return false;
		}
		
		private void dfsModLoop( ){
			
			for(int i = 0; i < g.verticesCount; i++){
				if(colors[i] == DFSColors.WHITE)
					dfsr(i, adj, dfs1, null);
			}
			
			time = 0;
			
			ArrayList<Pair<Integer>> verticesOrdered = new ArrayList<Pair<Integer> >(g.verticesCount);
			
			for(int i = 0; i < g.verticesCount; i++){
				Pair<Integer> p = new Pair<Integer>();
				p.first = i;
				p.second = dfs1[i].finishing;
				verticesOrdered.add(p);
			}
			
			Collections.sort(verticesOrdered, new Comparator<Pair<Integer>>() {

				@Override
				public int compare(Pair<Integer> o1, Pair<Integer> o2) {
					// TODO Auto-generated method stub
					return  o2.second - o1.second;
				}
		
			});
		
			for(int i = 0; i < g.verticesCount; i++)
				colors[i] = DFSColors.WHITE;
			
			for(Pair<Integer> x: verticesOrdered){
				if(colors[x.first] == DFSColors.WHITE){
					ArrayList<Integer> comp = new ArrayList<Integer>();
					dfsr(x.first, tAdj, dfs2, comp);
					if(comp.size() != 1){
						sccCounter++;
						scc.add(comp);
					}
				}
			}
			
			for(int i = 0; i < g.verticesCount; i++)
				colors[i] = DFSColors.WHITE;
		}

		private void dfsr(int v, ArrayList<ArrayList<Integer>> adj, DFSValue[] dfsv, ArrayList<Integer> scc ) {
			time++;
			dfsv[v].discovery = time;
			
			if(scc != null){
				scc.add(v);
				sccArr[v] = sccCounter;
			}
			
			colors[v] = DFSColors.GREY;
			for(int u = 0; u < g.verticesCount; u++ ){
				if(adj.get(v).get(u) == 0)
					continue;
				
				if(colors[u] == DFSColors.WHITE){
					dfsv[u].pred = v;
					dfsr(u, adj, dfsv, scc);
				}
				
			}
			
			colors[v] = DFSColors.BLACK;
			time++;
			dfsv[v].finishing = time;
		}
		
		public void dfsrFindCycle(int v, int begin, int level){
			colors[v] = DFSColors.GREY;
			System.out.println(v + " ");
			
			for(int u: g.adjacencyList.get(v)){
				if(adj.get(v).get(u) == 0 || sccArr[u] != level)
					continue;
				
				if(u == begin){
					System.out.println(u + " ");
					return;
				}
				
				if(colors[u] == DFSColors.WHITE){
					dfsrFindCycle(u, begin, level);
				}
				
			}
			colors[v] = DFSColors.BLACK;
		}
		
	}
	
	public static class DFSValue {
		public Integer pred = null;
		public int discovery = 0;
		public int finishing = 0;
	}
	
	enum DFSColors {
		WHITE,
		GREY,
		BLACK
	}
	
	public static class DFSValueComputer {
		OrientedGraph g;
		public DFSValue[] dfs;
		private DFSColors colors[];
		private int time = 0;
		public ArrayList<Integer> orderedVertices;
		
		private void init(){
			colors = new DFSColors[g.verticesCount];
			for(int i = 0; i < g.verticesCount; i++)
				colors[i] = DFSColors.WHITE;
			
			dfs = new DFSValue[g.verticesCount];
			for(int i = 0; i < g.verticesCount; i++){
				dfs[i] = new DFSValue();
			}
		}

		public DFSValueComputer(OrientedGraph g){
				this.g = g;
				
				init();
		}
	
		public void computeAndSaveStack(ArrayList<String[]> stackFingerPrint, int v, boolean user){
			
			StringBuilder fingerPrint = new StringBuilder("");
			Stack<Integer> stack = new Stack<Integer>();
			orderedVertices = new ArrayList<Integer>(g.verticesCount);
			for(int i = 0; i <= g.verticesCount; i++)
				orderedVertices.add(-1);
		
			int deltaUser = user ? 1: 0;
			
			stack.push(v + deltaUser);
			time++;
			dfs[v].discovery = time;
			colors[v] = DFSColors.GREY;
			
			stackFingerPrint.add(new String[]{"["+String.valueOf(v) + "]"});
			orderedVertices.set(time, v);
			
			while(!stack.isEmpty()){
				int vert =  stack.lastElement() - deltaUser;
				
				int count = 0;
				for(int u: g.adjacencyList.get(vert)){
					if(colors[u] == DFSColors.WHITE){
						stack.push(u+deltaUser);
						time++;
						count++;
						
						colors[u] = DFSColors.GREY;
						dfs[u].discovery = time;
						orderedVertices.set(time, u);
						
						stackFingerPrint.add(new String[]{stack.toString()});
					}
				}
				if(count == 0){
					time++;
					int finv = stack.pop() - deltaUser;
					colors[finv] = DFSColors.BLACK;
					dfs[finv].finishing = time;
					stackFingerPrint.add(new String[]{stack.toString()});
				}
			}
			System.out.print(1);
		}
		
	}
	
}
