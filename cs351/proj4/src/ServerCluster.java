import edu.rit.numeric.ExponentialPrng;
import edu.rit.sim.Event;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Database server.
 * @author  Tyler Paulsen
 * @author  Alan Kaminsky - templated from https://cs.rit.edu/~ark/351/sim/java2html.php?file=6
 * @version 23-Apr-2015
 */
public class ServerCluster {
	private ExponentialPrng tprocPrng;
	private Simulation sim;
	private int V, k;
	private float p;
	private Random prng;
	private final boolean debug = false;
	Node[] graph;

	/**
	 * True to print transcript, false to omit transcript.
	 */
	public boolean transcript = true;

	/**
	 * Construct a new server. The server's request processing time is
	 * exponentially distributed with the given mean.
	 *
	 * @param  sim    Simulation.
	 * @param  tproc  Mean request processing time.
	 * @param  prng   Pseudorandom number generator.
	 */
	public ServerCluster(Simulation sim, double tproc, int V, int k, float p, Random prng) {
		this.sim = sim;
		this.tprocPrng = new ExponentialPrng (prng, 1.0/tproc);
		this.V = V;
		this.k = k;
		this.p = p;
		this.prng = prng;
		graph = new Node[V];
		for(int i = 0; i < V; ++i)
			graph[i] = new Node(i);
		generateSmallGraph();
	}

	/**
	 * Add the given request to this server's queue.
	 *
	 * @param  request  Request.
	 */
	public void add(Request request) {
		if (transcript)
			System.out.printf("%.3f %s added%n", sim.time(), request);
		startProcessing(request);
	}

	/**
	 * Start processing the request.
	 */
	private void startProcessing(Request request) {
		if (transcript)
			System.out.printf ("%.3f %s starts processing%n", sim.time(), request);

		if(request.foundDatabase()) {
			sim.doAfter(tprocPrng.next(), new Event() {
				public void perform() {
					finishProcessing(request);
				}
			});
		}else{
			sim.doAfter(tprocPrng.next(), new Event() {
				public void perform() {
					int d = request.currentDatabase;
					request.currentDatabase = graph[d].getRandomNeighbor(request.currentDatabase);
					startProcessing(request);
				}
			});
		}
	}

	/**
	 * Finish processing the first request in this server's queue.
	 */
	private void finishProcessing(Request request) {
		if (transcript)
			System.out.printf ("%.3f %s finishes processing%n", sim.time(), request);
		request.finish();
	}

	// start of Small World Graph cluster structure.
	/**
	 * print the graph. Used to debug.
	 */
	private void printGraph(){
		for(int i=0; i < V & debug; ++i)
			System.out.println(graph[i]);
	}

	/**
	 * @return number of vertices.
	 */
	public int getV(){
		return V;
	}
	/**
	 * generate a small world graph.
	 *
	 * Assumes the current graph is a regular graph.
	 */
	private void generateSmallGraph(){
		Node A,B,C;
		for(int i = 0; i < V; ++i){
			A = graph[i];
			for(int j = 1; j <= k; ++j){
				B = graph[(i+j) % V];
				if(prng.nextFloat() < p){
					C = graph[prng.nextInt(V)];
					while(C.equals(A) || C.equals(B) || C.isAdjacent(A))
						C = graph[prng.nextInt(V)];
					B = C;
				}
				A.addNeighbor(B);
			}
		}
		printGraph();
	}

	/**
	 * represents a node in the graph.
	 */
	class Node{
		private ArrayList<Node> nodes;
		int id;
		Node(int id){
			this.id = id;
			nodes = new ArrayList<>();
		}
		@Override
		public boolean equals(Object o){
			return ((Node)o).id == id;
		}

		/**
		 * finds and returns the nth neighbor
		 * @param curr
		 * @return
		 */
		public int getRandomNeighbor(int curr){
			int ret = nodes.get(prng.nextInt(graph[curr].nodes.size())).id;
			while(ret == curr)
				ret = nodes.get(prng.nextInt(graph[curr].nodes.size())).id;
			return ret;
		}
		/**
		 * remove the link between two nodes
		 * @param n
		 */
		public void remove(Node n){
			nodes.remove(n);
			n.removeSingle(this);
		}

		/**
		 * remove a single node
		 * @param n
		 */
		private void removeSingle(Node n){
			nodes.remove(n);
		}
		/**
		 * add an edge between nodes.
		 * @param n
		 */
		public void addNeighbor(Node n){
			nodes.add(n);
			n.addNeighborSingle(this);
		}

		/**
		 * add a single link.
		 * @param n
		 */
		private void addNeighborSingle(Node n){
			nodes.add(n);
		}

		/**
		 * is adjacent
		 * @param n - node
		 * @return
		 */
		public boolean isAdjacent(Node n){
			return nodes.contains(n);
		}

		//to string
		public String toString(){
			String ret = id + " : ";
			for(Node n: nodes)
				ret += n.id + " ";
			return ret;
		}

	}

}
