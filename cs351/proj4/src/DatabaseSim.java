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

	/**
	 * Main program.
	 */
	public static void main(String[] args) {
		// Parse command line arguments.
		if (args.length != 7) usage();
		tproc = Double.parseDouble (args[0]);
		treq = Double.parseDouble(args[1]);
		nreq = Integer.parseInt(args[2]);
		V = Integer.parseInt(args[3]);
		k = Integer.parseInt(args[4]);
		p = Float.parseFloat(args[5]);
		seed = Long.parseLong(args[6]);


		// Set up pseudorandom number generator.
		prng = new Random (seed);

		// Set up simulation.
		sim = new Simulation();

		// Set up one serverCluster.
		serverCluster = new ServerCluster(sim, tproc, V, k, p, prng);

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
	 * Print a usage message and exit.
	 */
	private static void usage() {
		System.err.println ("Usage: java DatabaseSim <tproc> <treq> <nreq> <V> <k> <p> <seed>");
		System.err.println ("<tproc> = Mean request processing time");
		System.err.println ("<treq> = Mean request interarrival time");
		System.err.println ("<nreq> = Number of requests");
		System.err.println ("<V> = Number of vertices");
		System.err.println ("<k> = Density parameter");
		System.err.println ("<p> = rewiring probability");
		System.err.println ("<seed> = Random seed");
		System.exit (1);
	}
}
