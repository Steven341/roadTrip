import java.util.PriorityQueue;
import java.util.Queue;

/**
 * GraphAdjMatrix
 * 
 * An Adjacency matrix implementation of Graphs for topological sorting.
 * This is a directed, unweighted graph. (The graph should be acyclic, but that's not checked.)
 * 
 * Uses the Queue class we developed earlier. (Hooray for reuse!)
 * 
 * @author David Guy Brizan
 *
 */
public class GraphAdjMatrix {
	
	private int [][] adjMatrix;
	
	
	/**
	 * Constructor
	 * @param vertices in this graph. May not be changed after construction.
	 */
	public GraphAdjMatrix (int vertices) {
		adjMatrix = new int[vertices][vertices]; // By default, these values are 0.
	}
	
	
	/**
	 * addEdge: Adds an edge from source to target
	 * @param source -- source vertex
	 * @param target -- target vertex
	 * @return true if edge was added; false if not (eg. if (source|target) vertex does not exist).
	 */
	public boolean addEdge (int source, int target) {
		if (source < adjMatrix.length && target < adjMatrix.length) {
			adjMatrix[source][target] = 1;
			return true;
		}
		return false;
	}
	
	/**
	 * noIncidents -- determines the first (lowest numbered) vertex with no
	 * incoming edges.
	 * @param incident = the incident array for the graph
	 * @return a vertex with no incoming edges.
	 */
	protected int noIncidents (int [] incident) {
		for (int i = 0; i < incident.length; i++) {
			if (incident[i] == 0)
				return i;
		}
		return -1;
	}
	
	
	/**
	 * getSchedule Determines a feasible schedule for the graph.
	 * @return the schedule
	 * @throws Exception
	 */
	public Queue<Integer> getSchedule () throws Exception {
		Queue<Integer> schedule = new PriorityQueue<>();
		
		// Build incident array
		int [] incident = new int [adjMatrix.length];
		for (int i = 0; i < adjMatrix.length; i++)
			for (int j = 0; j < adjMatrix.length; j++)
				incident[i] += adjMatrix[j][i];
		
		// Use the incident array to create a feasible schedule
		for (int i = 0; i < adjMatrix.length; i++) {
			int v = noIncidents(incident);
			// Sometimes it goes sideways
			if (v == -1)
				throw new Exception("No unused vertices with zero incoming edges?");
			schedule.remove(v);
			incident[v] = -1; // Flag this vertex as used.
			for (int j = 0; j < adjMatrix.length; j++) {
				incident[j] -= adjMatrix[v][j];   // Like "if (adjMatrix[v][j] == 1) incident[j] -= 1;" but cuter
			}
		}
		
		return schedule;
	}
	
	
	public static void main (String [] args) {
		// Test of the topological sorting algorithm
		
		GraphAdjMatrix graph = new GraphAdjMatrix(6);
		
		// This is the graph from the slides... which came from GeeksForGeeks
		graph.addEdge(5, 2);
		graph.addEdge(5, 0);
		graph.addEdge(4, 0);
		graph.addEdge(4, 1);
		graph.addEdge(2, 3);
		graph.addEdge(3, 1);
		System.out.println(graph.toString());
		// Get and print the schedule.
		try {
			System.out.print("Schedule: ");
			Queue<Integer> schedule = graph.getSchedule();
			while (! schedule.isEmpty()) {
				System.out.print(schedule.remove() + " ");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	
}
