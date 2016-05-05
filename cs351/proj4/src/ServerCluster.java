import edu.rit.numeric.ExponentialPrng;
import edu.rit.sim.Event;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

import java.util.*;

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
	public boolean debug = false ;
	public boolean hasError;
	Node[] graph;

	/**
	 * True to print transcript, false to omit transcript.
	 */
	public boolean transcript = false;

	/**
	 * Construct a new server. The server's request processing time is
	 * exponentially distributed with the given mean.
	 *
	 * @param  sim    Simulation.
	 * @param  tproc  Mean request processing time.
	 * @param  prng   Pseudorandom number generator.
	 */
	private ServerCluster(Simulation sim, double tproc, int V, int k, float p, Random prng) {
		this.sim = sim;
		this.tprocPrng = new ExponentialPrng (prng, 1.0/tproc);
		this.V = V;
		this.k = k;
		this.p = p;
		this.prng = prng;
		graph = new Node[V];
		hasError = false;
		for(int i = 0; i < V; ++i)
			graph[i] = new Node(i);
		generateSmallGraph();
	}

	/**
	 * generates a server cluster
	 * @param sim sumulation
	 * @param tproc = Mean request processing time
	 * @param V number of vertices
	 * @param k density parameter
	 * @param p rewiring probability
	 * @param prng random number generator
	 * @return
	 */
	public static ServerCluster generateCluster(Simulation sim, double tproc, int V, int k, float p, Random prng){
		ServerCluster sc = new ServerCluster(sim,tproc, V, k, p, prng);
		//System.out.println(sc.hasError);
		//stem.out.println(isConnectedGraph(sc));
		while(sc.hasError || !isConnectedGraph(sc)){
			//stem.out.println("error)=:::);");
			sc = new ServerCluster(sim,tproc, V, k, p, prng);
		}
		return sc;
	}

	/**
	 * checks to see if the graph is connected or not
	 * @param sc - the graph
	 * @return true if graph is connected else false.
	 */
	public static boolean isConnectedGraph(ServerCluster sc){
		Node graph[] = sc.graph;
		HashSet<Integer> seen = new HashSet();
		LinkedList<Integer> queue = new LinkedList();
		int A = 0;
		queue.addLast(A);
		seen.add(A);
		while (!queue.isEmpty()) {
			A = queue.poll();
			for (Node B : graph[A].nodes) {
				if (!seen.contains(B.id)) {
					seen.add(B.id);
					queue.addLast(B.id);
				}
			}
		}
		return seen.size() == sc.getV();
	}


	/**
	 * Add the given request to this server's queue.
	 *
	 * @param  request  Request.
	 */
	public void add(Request request) {
		if (transcript)
			System.out.printf("%.3f %s added curr=%d dest=%d%n", sim.time(),
					request,
					request.currentDatabase,
					request.databaseNeeded);
		startProcessing(request);
	}

	/**
	 * Start processing the request.
	 */
	private void startProcessing(Request request) {
		if (transcript)
			System.out.printf ("%.3f %s starts processing curr=%d dest=%d%n", sim.time(),
					request,
					request.currentDatabase,
					request.databaseNeeded);

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
			System.out.printf ("%.3f %s finishes processing curr=%d dest=%d%n", sim.time(),
					request,
					request.currentDatabase,
					request.databaseNeeded);
		request.finish();
	}

	// start of Small World Graph cluster structure.
	/**
	 * print the graph. Used to debug.
	 */
	private void printGraph(){
		if(debug)System.out.println("\nGraph:");
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
		int a;
		for(int i = 0; i < V; ++i){
			A = graph[i];
			for(int j = 1; j <= k; ++j){
				B = graph[(i+j) % V];
				if(prng.nextFloat() < p){
					C = graph[prng.nextInt(V)];
					while(C.equals(A) || C.equals(B) || C.isAdjacent(A) ) {
						if (completeNode(A,B)) {
							hasError = true;
							return;
						}
						C = graph[prng.nextInt(V)];
					}
					B = C;
				}
				A.addNeighbor(B);
			}
		}
		printGraph();
	}
	private boolean completeNode(Node A, Node B){
		return (!A.nodes.contains(B.id) && (A.nodes.size() + 2) >= V);
	}
	/**
	 * represents a node in the graph.
	 */
	class Node{
		private HashSet<Node> nodes;
		int id;
		Node(int id){
			this.id = id;
			nodes = new HashSet<>();
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
			ArrayList<Node> random = new ArrayList(nodes);
			Collections.shuffle(random);
			try {
				int ret = random.iterator().next().id;
				while (ret == curr) {
					Collections.shuffle(random);
					ret = random.iterator().next().id;
				}
				return ret;
			}catch(NoSuchElementException nsee){
				System.out.println(random);
				System.out.println(nodes);
			}
			return -1;
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
		public String toString() {
			String ret = id + " : ";
			for (Node n : nodes)
				ret += n.id + " ";
			return ret;
		}

	}

}
