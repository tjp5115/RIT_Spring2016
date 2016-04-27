import edu.rit.numeric.Series;
import edu.rit.sim.Simulation;
import edu.rit.util.Random;

/**
 * Class DatabaseSim is the Database serverCluster simulation main program.
 *
 * @author  Tyler Paulsen
 * @author  Alan Kaminsky - Templated from : https://cs.rit.edu/~ark/351/sim/java2html.php?file=6
 * @version 18-Apr-2014
 */
public class DatabaseSim {
	private static double tproc;
	private static double treq;
	private static int nreq;
	private static long seed;
	private static int V;
	private static int k;
	private static float p;
	private static Random prng;
	private static Simulation sim;
	private static ServerCluster serverCluster;
	private static Generator generator;
	private static boolean transcript = false;
	private static final int MONTE_CARLO_RUNS = 2000;

	/**
	 * Main program.
	 */
	public static void main(String[] args) {
		// Parse command line arguments.
		switch(args[0]){
			case "sweep_p":
				parse(args);
				sweep_p();
				break;
			case "sweep_k":
				parse(args);
				sweep_k();
				break;
			case "single":
				parse(args);
				single();
				break;
			default:
				usage();
		}

	}

	private static void sweep_k(){
		System.out.println("k\tmean");
		int seed_count = 0;
		for(int i = 1; i < k; ++i) {
			float mean = 0.0f;
			for(int T = 0; T < MONTE_CARLO_RUNS; ++T) {
				// Set up pseudorandom number generator.
				prng = new Random(seed + seed_count++);
				// Set up simulation.
				sim = new Simulation();
				// Set up one serverCluster.
				//System.out.println(T);
				serverCluster = ServerCluster.generateCluster(sim, tproc, V, i, p, prng);
				//serverCluster = new ServerCluster(sim, tproc, V, i, p, prng);
				serverCluster.transcript = transcript;
				// Set up request generator and generate first request.
				generator = new Generator(sim, treq, nreq, prng, serverCluster);
				// Run the simulation.
				sim.run();
				//gather stats
				Series.Stats stats = generator.responseTimeStats();
				mean += stats.mean;
				//System.out.println(T);
			}
			System.out.printf ("%d\t%.3f%n", i, mean/MONTE_CARLO_RUNS);
		}
	}

	private static void sweep_p(){

		System.out.println("prob\tmean");
		int seed_count = 0;
		for(float i = 0; i < p; i += 0.01f) {
			float mean = 0.0f;
			for(int T = 0; T < MONTE_CARLO_RUNS; ++T) {
				// Set up pseudorandom number generator.
				prng = new Random(seed + seed_count++);
				// Set up simulation.
				sim = new Simulation();
				// Set up one serverCluster.
				//serverCluster = new ServerCluster(sim, tproc, V, k, i, prng);
				serverCluster = ServerCluster.generateCluster(sim, tproc, V, k, i, prng);
				serverCluster.transcript = transcript;
				// Set up request generator and generate first request.
				generator = new Generator(sim, treq, nreq, prng, serverCluster);
				// Run the simulation.
				sim.run();
				//gather stats
				Series.Stats stats = generator.responseTimeStats();
				mean += stats.mean;
			}
			System.out.printf ("%.02f\t%.3f%n", i, mean/MONTE_CARLO_RUNS);
		}
	}

	private static void single(){
		// Set up pseudorandom number generator.
		prng = new Random (seed);

		// Set up simulation.
		sim = new Simulation();

		// Set up one serverCluster.
		serverCluster = ServerCluster.generateCluster(sim, tproc, V, k, p, prng);
		serverCluster.transcript = transcript;

		// Set up request generator and generate first request.
		generator = new Generator (sim, treq, nreq, prng, serverCluster);

		// Run the simulation.
		sim.run();

		// Print the response time mean and standard deviation.
		Series.Stats stats = generator.responseTimeStats();
		System.out.printf ("Response time mean   = %.3f%n", stats.mean);
		System.out.printf ("Response time stddev = %.3f%n", stats.stddev);
	}

	/**
	 * parses the args for a single simulation
	 * @param args
	 */
	private static void parse(String args[]){
		if (args.length < 8 || args.length	> 9) usage();
		try {
			tproc = Double.parseDouble(args[1]);
			treq = Double.parseDouble(args[2]);
			nreq = Integer.parseInt(args[3]);
			V = Integer.parseInt(args[4]);
			k = Integer.parseInt(args[5]);
			p = Float.parseFloat(args[6]);
			seed = Long.parseLong(args[7]);
			if (args.length == 9)
				transcript = Boolean.parseBoolean(args[8]);
		} catch (Exception e) {
			e.printStackTrace();
			usage();
		}
	}
	/**
	 * Print a usage message and exit.
	 */
	private static void usage() {
		System.err.println ("Usage: java DatabaseSim <type> <tproc> <treq> <nreq> <V> <k> <p> <seed>");
		System.err.println ("<type> = Type of the project. " +
				"\n\t sweep_p : sweeps the p variable" +
				"\n\t sweep_k : sweeps the k variable" +
				"\n\t single : run a single simulation");
		System.err.println ("<tproc> = Mean request processing time");
		System.err.println ("<treq> = Mean request interarrival time");
		System.err.println ("<nreq> = Number of requests");
		System.err.println ("<V> = Number of vertices");
		System.err.println ("<k> = Density parameter" +
				"\n\t sweep_k : sweeps up to the k value given");
		System.err.println ("<p> = rewiring probability" +
				"\n\t sweep_p : sweeps up to the p value given");
		System.err.println ("<seed> = Random seed");
		System.exit (1);
	}
}
