package comp361;
import java.util.*;
import java.util.Random;
public class Main {

	private static final String[] cities = {"VA", "NV", "WV", "BU", "RM", "SR", "NW", "DT", "LG", "AB", "CW", "HP", "MS"};
	private static final double[][] graph = new double[cities.length][cities.length];
	private static final double[][] heuristics = new double[cities.length][cities.length];
	private static int[] distances = new int[cities.length];
	private static double[] distances1 = new double[cities.length];
	
	// This is an adjacency matrix that shows the connections between different cities:
	private static final int[][] connections = {
			{0, 1, 1, 1, 1,0,0,0,0,0,0,0,0}, 
			{1, 0, 1, 0, 0, 0, 0,0,0,0,0,0,0},
			{1, 1, 0, 0, 0, 0, 0,0,0,0,0,0,0},
			{1, 0, 0, 0, 0, 1, 1,0,0,0,0,0,0},
			{1, 0, 0, 0, 0, 0, 1,1,0,0,0,0,0},
			{0, 0, 0, 1, 0, 0, 1,1,1,0,0,0,0},
			{0, 0, 0, 1, 1, 1, 0,1,0,0,0,0,0},
			{0, 0, 0, 0, 1, 1, 1,0,0,0,0,0,0},
			{0, 0, 0, 0, 0, 1, 0,0,0,1,0,0,0},
			{0, 0, 0, 0, 0, 0, 0,0,1,0,1,0,1},
			{0, 0, 0, 0, 0, 0, 0,0,0,1,0,1,0},
			{0, 0, 0, 0, 0, 0, 0,0,0,0,1,0,0},
			{0, 0, 0, 0, 0, 0, 0,0,0,1,0,0,0}};
	
	static Map<String[], String> inconsistentCities;
	
	public static void main(String[] args) {
		initializeGraph();
		System.out.println("The graph below shows the distances between different cities: ");
		printGraph();
		calculateHeuristics();
		System.out.println();
		System.out.println();
		System.out.println("The heuristics are as follows (before consistency check): ");
		printHeuristics();
		System.out.println();
		System.out.println();
		checkConsistency();
		System.out.println("Inconsistent value Key: (Start, intermediate) -> Value: (Goal) ");
		printInconsistentCities();
		System.out.println();
		System.out.println();
		correctHeuristics();
		System.out.println();
		System.out.println();
		
		
		System.out.println("The heuristics are as follows (after consistency check): ");
		printHeuristics();
		System.out.println();
		System.out.println();
		System.out.println("Inconsistent value Key: (Start, intermediate) -> Value: (Goal) ");
		printInconsistentCities();
		
		System.out.println();
		
		String start = "WV";
		String goal = "DT";
		
		System.out.println();
		System.out.println("The starting city is: " + start);
		System.out.println("The final city is: " + goal);
		System.out.println();
		String[] parents = pathFinder(start, goal);
		
		System.out.println("Parents for A*: ");
		System.out.println();
		for(int i = 0; i <  parents.length; i++) {
			System.out.println(cities[i] + " -> " + parents[i]);
		}
		
		System.out.println()
;
		System.out.println("The path is as follows (in the order) for A*: ");
		ArrayList<String> path = findPath(parents, start, goal);
		for(int i = 0; i < path.size(); i++) {
			if(i == path.size()-1) {
				System.out.print(path.get(i));	
			}
			else {
			System.out.print(path.get(i) + " -> ");
		}
		}
		
		String[] parents_Grassfire = PathFinder_GrassFire(start, goal);
		
		System.out.println();
		System.out.println();
		System.out.println("Parents for Grassfire Algorithm: ");
		for(int i = 0; i <  parents_Grassfire.length; i++) {
			System.out.println(cities[i] + " -> " +parents_Grassfire[i]);
		}
		
		System.out.println();
		System.out.println("The distances array is as follows: ");
		for(int k = 0; k < cities.length; k++) {
			System.out.print( cities[k] + " ");
		}
		System.out.println();
		for(int i = 0; i < distances.length; i++) {
			System.out.print(distances[i] +  " ");
		}
		
		System.out.println();
		System.out.println();
		System.out.println("The path is as follows (in the order) for Grassfire: ");
		ArrayList<String> path_grassfire = findPath_Grassfire(parents_Grassfire, start, goal);
		
		for(int i = 0; i < path_grassfire.size(); i++) {
			if(i == path_grassfire.size()-1) {
				System.out.print(path_grassfire.get(i));	
			}
			else {
			System.out.print(path_grassfire.get(i) + " -> ");
		}
		}
		
	String[] parents_dikstra = PathFinder_Dijkstra(start, goal);
		
		System.out.println();
		System.out.println();
		System.out.println("Parents for Dijkstra's Algorithm: ");
		for(int i = 0; i <  parents_Grassfire.length; i++) {
			System.out.println(cities[i] + " -> " +parents_dikstra[i]);
		}
		
		System.out.println();
		System.out.println();
		System.out.println("The path is as follows (in the order) for Dijkstra: ");
		ArrayList<String> path_dijkstra = findPath(parents_dikstra, start, goal);
		
		for(int i = 0; i < path_grassfire.size(); i++) {
			if(i == path_grassfire.size()-1) {
				System.out.print(path_dijkstra.get(i));	
			}
			else {
			System.out.print(path_dijkstra.get(i) + " -> ");
		}
		}
		
		
		
	}
	
	public static ArrayList<String> findPath_Grassfire(String[] parents, String start, String goal){
		ArrayList<String> path = new ArrayList<String>();
		String current = start;
		while(!current.equals(goal)) {
			path.add(current);
			current = parents[indexOfCity(current)];
		}
		path.add(goal);
		return path;
	}
	
	public static ArrayList<String> findPath(String[] parents, String start, String goal){
		ArrayList<String> revPath = new ArrayList<String>();
		String current = goal;
		while(!current.equals(start)) {
			revPath.add(current);
			current = parents[indexOfCity(current)];
		}
		revPath.add(start);
		ArrayList<String> path = new ArrayList<String>();
		for(int i = 0; i < revPath.size(); i++) {
			path.add(revPath.get(revPath.size() - i - 1));
		}
		return path;
	}
	

	
	public static void printHeuristics() {
		for(int k = 0; k < cities.length; k++) {
			System.out.print("  " + cities[k] + " ");
		}
		System.out.println()
;		for(int i = 0; i < heuristics.length; i++) {
			System.out.print(cities[i] + " ");
			for(int j = 0; j < heuristics.length; j++) {
				System.out.print(heuristics[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public static void calculateHeuristics() {
		Random random = new Random();
		
		for(int i = 0; i < heuristics.length; i++) {
			for(int j = 0; j < heuristics.length; j++) {
				if(i == j) {
					heuristics[i][j] = 0;
				}
				else{
				int randInt = random.nextInt(6) ;
				heuristics[i][j] = Math.round((graph[i][j] - randInt) * 100.0) / 100.0;
				}
			}
		}
		
	}
	
	private static void initializeGraph() {
		Map<String[], Double> distances = new HashMap<>();
        distances.put(new String[]{"VA", "NV"}, 13.1);
        distances.put(new String[]{"VA", "WV"}, 12.1);
        distances.put(new String[]{"VA", "BU"}, 11.3);
        distances.put(new String[]{"VA", "RM"}, 14.2);
        distances.put(new String[]{"VA", "SR"}, 31.8);
        distances.put(new String[]{"VA", "NW"}, 23.2);
        distances.put(new String[]{"VA", "DT"}, 23.4);
        distances.put(new String[]{"VA", "LG"}, 43.6);
        distances.put(new String[]{"VA", "AB"}, 70.3);
        distances.put(new String[]{"VA", "CW"}, 117.0);
        distances.put(new String[]{"VA", "HP"}, 150.0);
        distances.put(new String[]{"VA", "MS"}, 76.7);
 
        
        distances.put(new String[]{"NV", "WV"}, 9.3);
        distances.put(new String[]{"NV", "BU"}, 16.7);
        distances.put(new String[]{"NV", "RM"}, 30.5);
        distances.put(new String[]{"NV", "SR"}, 29.7);
        distances.put(new String[]{"NV", "NW"}, 27.2);
        distances.put(new String[]{"NV", "DT"}, 37.4);
        distances.put(new String[]{"NV", "LG"}, 49.4);
        distances.put(new String[]{"NV", "AB"}, 70.3);
        distances.put(new String[]{"NV", "CW"}, 105.0);
        distances.put(new String[]{"NV", "HP"}, 155.0);
        distances.put(new String[]{"NV", "MS"}, 81.9); 
        
        distances.put(new String[]{"WV", "BU"}, 23.1);
        distances.put(new String[]{"WV", "RM"}, 24.3);
        distances.put(new String[]{"WV", "SR"}, 36.1);
        distances.put(new String[]{"WV", "NW"}, 29.5);
        distances.put(new String[]{"WV", "DT"}, 34.3);
        distances.put(new String[]{"WV", "LG"}, 55.8);
        distances.put(new String[]{"WV", "AB"}, 76.7);
        distances.put(new String[]{"WV", "CW"}, 112.0);
        distances.put(new String[]{"WV", "HP"}, 161.0);
        distances.put(new String[]{"WV", "MS"}, 83.5); 
        
        distances.put(new String[]{"BU", "RM"}, 21.6);
        distances.put(new String[]{"BU", "SR"}, 13.4);
        distances.put(new String[]{"BU", "NW"}, 6.9);
        distances.put(new String[]{"BU", "DT"}, 15.0);
        distances.put(new String[]{"BU", "LG"}, 32.9);
        distances.put(new String[]{"BU", "AB"}, 54.4);
        distances.put(new String[]{"BU", "CW"}, 89.2);
        distances.put(new String[]{"BU", "HP"}, 139.0);
        distances.put(new String[]{"BU", "MS"}, 61.1); 
        
        
        distances.put(new String[]{"RM", "SR"}, 30.7);
        distances.put(new String[]{"RM", "NW"}, 22.4);
        distances.put(new String[]{"RM", "DT"}, 15.7);
        distances.put(new String[]{"RM", "LG"}, 44.8);
        distances.put(new String[]{"RM", "AB"}, 72.9);
        distances.put(new String[]{"RM", "CW"}, 107.0);
        distances.put(new String[]{"RM", "HP"}, 157.0);
        distances.put(new String[]{"RM", "MS"}, 86.1); 
        
        distances.put(new String[]{"SR", "NW"}, 7.6);
        distances.put(new String[]{"SR", "DT"}, 34.3);
        distances.put(new String[]{"SR", "LG"}, 20.3);
        distances.put(new String[]{"SR", "AB"}, 41.9);
        distances.put(new String[]{"SR", "CW"}, 76.7);
        distances.put(new String[]{"SR", "HP"}, 127.0);
        distances.put(new String[]{"SR", "MS"}, 53.4); 
        
        distances.put(new String[]{"NW", "DT"}, 29.4);
        distances.put(new String[]{"NW", "LG"}, 27.4);
        distances.put(new String[]{"NW", "AB"}, 49.6);
        distances.put(new String[]{"NW", "CW"}, 85.4);
        distances.put(new String[]{"NW", "HP"}, 135.0);
        distances.put(new String[]{"NW", "MS"}, 57.3); 
        
        distances.put(new String[]{"DT", "LG"}, 32.1);
        distances.put(new String[]{"DT", "AB"}, 59.9);
        distances.put(new String[]{"DT", "CW"}, 94.7);
        distances.put(new String[]{"DT", "HP"}, 43.5);
        distances.put(new String[]{"DT", "MS"}, 79.4); 
        
        distances.put(new String[]{"LG", "AB"}, 26.0);
        distances.put(new String[]{"LG", "CW"}, 63.3);
        distances.put(new String[]{"LG", "HP"}, 113.0);
        distances.put(new String[]{"LG", "MS"}, 37.2);
        
        distances.put(new String[]{"AB", "CW"}, 37.2);
        distances.put(new String[]{"AB", "HP"}, 22.1);
        distances.put(new String[]{"AB", "MS"}, 17.5);
        
        distances.put(new String[]{"CW", "HP"}, 52.6);
        distances.put(new String[]{"CW", "MS"}, 46.5);
        
        distances.put(new String[]{"HP", "MS"}, 46.5);
        
        
        for(int i = 0; i < cities.length; i++) {
        	graph[i][i] = 0;
        }
        for (Map.Entry<String[], Double> entry : distances.entrySet()) {
            String city1 = entry.getKey()[0];
            String city2 = entry.getKey()[1];
            double distance = entry.getValue();
            int index1 = indexOfCity(city1);
            int index2 = indexOfCity(city2);
            graph[index1][index2] = graph[index2][index1] = distance;
        }
    }
	
	private static int indexOfCity(String city) {
		int i;
		for(i = 0; i < cities.length; i++) {
			if(cities[i].equals(city)) {
				return i;
			}
		}
		return i;
	}
	
	public static void printGraph() {
		for(int k = 0; k < cities.length; k++) {
			System.out.print("  " + cities[k] + " ");
		}
		System.out.println();
		for(int i = 0; i < cities.length; i++) {
			System.out.print(cities[i] + " ");
			for(int j = 0; j < cities.length; j++) {
				System.out.print(graph[i][j] + " ");
			}
			System.out.println();
		}
		
	}
	
	// Calculating if the heuristics are consistent:
	
	public static boolean checkConsistency() {
		inconsistentCities = new HashMap<>();
		for(int i = 0; i < cities.length; i++) {
			int goalIndex = i;
			for(int j = 0; j < cities.length; j++) {
				if(j == i) {
					break;
				}
				for(int k = 0; k < cities.length; k++) {
					if(k == i) {
						break;
					}
					if(connections[j][k] == 1) { // This means that j and k are connected cities or neighbors
						if(heuristics[j][goalIndex] - heuristics[k][goalIndex] > graph[j][k]) {
							inconsistentCities.put(new String[]{cities[j], cities[k]}, cities[i]);
						}
					}
				}
			}
		}
		if(inconsistentCities.isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static void printInconsistentCities() {
		Set<Map.Entry<String[], String>> entrySet = inconsistentCities.entrySet();
		
		for(Map.Entry<String[], String> entry: entrySet) {
			System.out.println("Key: " + entry.getKey()[0] + " " + entry.getKey()[1] + " Value: " + entry.getValue());
		}
	}
	
	public static void correctHeuristics() {
		int i = 0;
		while(!checkConsistency() && i < 1200) {
	    Set<Map.Entry<String[], String>> entrySet = inconsistentCities.entrySet();
	    Random random = new Random();
	    int maxIterations = 100;

	    for (Map.Entry<String[], String> entry : entrySet) {
	        String start = entry.getKey()[0];
	        String city2 = entry.getKey()[1];
	        String goal = entry.getValue();

	        int startIndex = indexOfCity(start);
	        int goalIndex = indexOfCity(goal);
	        int intermediateIndex = indexOfCity(city2);

	        for (int iteration = 0; iteration < maxIterations; iteration++) {
	            int Y = random.nextInt(6);
	            heuristics[startIndex][goalIndex] = graph[startIndex][goalIndex] - Y;
	            heuristics[intermediateIndex][goalIndex] = heuristics[startIndex][goalIndex] - graph[startIndex][intermediateIndex];

	            if (heuristics[startIndex][goalIndex] - heuristics[intermediateIndex][goalIndex] <= graph[startIndex][intermediateIndex]) {
	                // Correction successful
	                break;
	            }
	        }
	    }
	    i++;
	}
	}
	
	// Let us create a function to run the A* algorithm:
	
	public static String[] pathFinder(String start, String goal){
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>();
		String[] parents = new String[cities.length];
		double[] g_values = new double[cities.length];
		double[] f_values = new double[cities.length];
		for(int i = 0; i < g_values.length; i++ ) {
			g_values[i] = Integer.MAX_VALUE;
			f_values[i] = Integer.MAX_VALUE;
		}
		int startIndex = indexOfCity(start);
		int goalIndex = indexOfCity(goal);
		parents[startIndex] = null;
		g_values[startIndex] = 0;
		f_values[startIndex] = heuristics[startIndex][goalIndex];
		list.add(start);
		while(!list.isEmpty()) {
			String current = findMinfValueCity(list, f_values);
			path.add(current);
			list.remove(current);
			
			if(current.equals(goal)) {
				return parents;
			}
			ArrayList<String> neighbors = findNeighbors(current);
			for(String neighbor: neighbors) {
				if( g_values[indexOfCity(current)] + graph[indexOfCity(current)][indexOfCity(neighbor)]< g_values[indexOfCity(neighbor)]) {
				g_values[indexOfCity(neighbor)] = g_values[indexOfCity(current)] + graph[indexOfCity(current)][indexOfCity(neighbor)];
				f_values[indexOfCity(neighbor)]= g_values[indexOfCity(neighbor)] + heuristics[indexOfCity(neighbor)][indexOfCity(goal)];
				parents[indexOfCity(neighbor)] = current;
				if(!list.contains(neighbor)) {
					
					list.add(neighbor);
			}
				
				}
				
				
		}
		}
		return parents;
	}
	
	public static String findMinfValueCity(ArrayList<String> list, double[] f_values) {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(String element: list) {
			indexes.add(indexOfCity(element));
		}
		double min_fValue = f_values[indexes.get(0)];
		int minfvalue_index = indexes.get(0);
		for(int i = 1; i < indexes.size(); i++) {
			double f_value = f_values[indexes.get(i)];
			if(f_value < min_fValue) {
				min_fValue = f_value;
				minfvalue_index = indexes.get(i);
			}
		}
		return cities[minfvalue_index];
	}
	
	public static ArrayList<String> findNeighbors(String city){
		ArrayList<String> neighbors = new ArrayList<String>();
		int index = indexOfCity(city);
		for(int i = 0; i < cities.length; i++) {
			if(connections[index][i] == 1) {
				neighbors.add(cities[i]);
			}
		}
		return neighbors;
	}
	
	public static String[] PathFinder_GrassFire(String start, String goal) {
		// Let us create distances array:
		
		String[] parents = new String[cities.length];
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < cities.length; i++) {
			distances[i] = Integer.MAX_VALUE;
		}
		int startIndex = indexOfCity(start);
		int goalIndex = indexOfCity(goal);
		distances[goalIndex] = 0;
		list.add(goal);
		parents[goalIndex] = null;
		while(!list.isEmpty()) {
			String current = list.remove(0);
			ArrayList<String> neighbors = findNeighbors(current);
			for(String neighbor : neighbors) {
				int index = indexOfCity(neighbor);
				if(distances[index] > distances[indexOfCity(current)] + 1) {
					distances[index] = distances[indexOfCity(current)] + 1;
					parents[index] = current;
					list.add(neighbor);
				}
			}
		}
		return parents;
	}
	
	public static String[] PathFinder_Dijkstra(String start, String goal) {
		String[] parents = new String[cities.length];
		boolean[] visited = new boolean[cities.length];
		for(int i = 0; i < visited.length; i++) {
			visited[i] = false;
		}
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 0; i < cities.length; i++) {
			distances1[i] = Integer.MAX_VALUE;
		}
		int startIndex = indexOfCity(start);
		int goalIndex = indexOfCity(goal);
		distances1[startIndex] = 0;
		list.add(start);
		visited[startIndex] = true;
		while(!list.isEmpty() && !visited[goalIndex]) {
			String current = findMinDistanceNode(distances1,  list);
			if(current.equals(goal)) {
				return parents;
			}
		
			list.remove(current);
			visited[indexOfCity(current)] = true;
			ArrayList<String> neighbors = findNeighbors(current);
			for(String neighbor : neighbors) {
				int index = indexOfCity(neighbor);
				if(distances1[index] > distances1[indexOfCity(current)] + graph[index][indexOfCity(current)]) {
					distances1[index] = distances1[indexOfCity(current)] +  graph[index][indexOfCity(current)];
					parents[index] = current;
					if(visited[index] == false) {
					list.add(neighbor);
					}
				}
			}
		}
		return parents;
	}
	
	public static String findMinDistanceNode(double[] distances,  ArrayList<String> list) {
	    if (list.isEmpty()) {
	        return null; 
	    }

	    int minIndex = indexOfCity(list.get(0));
	    for (String element : list) {
	        int index = indexOfCity(element);
	        if (distances[index] < distances[minIndex]) {
	            minIndex = index;
	        }
	    }
	    return cities[minIndex];
	}

}
