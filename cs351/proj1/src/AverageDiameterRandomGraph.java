import edu.rit.pj2.LongLoop;
import edu.rit.pj2.Task;
import edu.rit.pj2.vbl.DoubleVbl;
import edu.rit.util.Random;
import java.util.*;

/**
 * Class AverageDiameterRandomGraphSmp is a multicore parallel program that measures
 * the average diameter distribution of a series of random graph.
 * <P>
 * Usage: <TT>java pj2 [threads=<I>K</I>] DegreeDistRandomGraphSmp <I>seed</I>
 * <I>V</I> <I>T</I> </TT>
 * <BR><TT><I>K</I></TT> = Number of parallel threads
 * <BR><TT><I>seed</I></TT> = Random seed
 * <BR><TT><I>V</I></TT> = Number of vertices
 * <BR><TT><I>T</I></TT> = Number of trials
 *
 * @author Tyler Paulsen
 *          -- Templated from Alan Kaminsky java file:
 *                  DegreeDistRandomGraphSmp
 *                  (https://cs.rit.edu/~ark/351/monte/java2html.php?file=2)
 *
 *
 * @version 20-Feb-2016
 */
public class AverageDiameterRandomGraph extends Task {
    // Command line arguments.
    long seed;
    int V,V_total;
    double P_inc,P;
    long T;
    DoubleVbl avg_diameter;

    // Main program.
    public void main(String[] args) throws Exception {
        // Parse command line arguments, print provenance.
        if (args.length != 3) usage();
        seed = Long.parseLong(args[0]);
        V = Integer.parseInt(args[1]);
        T = Long.parseLong(args[2]);
        V_total = V;
        P_inc = 0.05;
        System.out.printf ("$ java pj2 AverageDiameterRandomGraphSmp");
        for (String arg : args) System.out.printf (" %s", arg);
        System.out.println();

        // set up the first simulation Probability Vs. Average Diameter. Sweeps the vertices around the main simulation
        int V_inc = V_total/5;
        for( V = V_inc; V <= V_total; V+= V_inc) {
            System.out.println("# Vert\tProb.\tAvg. Diam.");
            for (P = P_inc; P <= 1.0; P += P_inc){
                seed += 1;
                monte_carlo_simulation();
            }
            System.out.println();
        }
        /*
        // set up the second simulation Vertices Vs. Average Diameter. Sweeps the Probability around the main simulation
        P_inc = .2;
        for( P = P_inc; P <= 1.0; P+= P_inc) {
            System.out.println("# Vert\tProb.\tAvg. Diam.");
            for (V = 2; V <= V_total; V += 5) {
                monte_carlo_simulation();
            }
            System.out.println();
        }
        */
    }


    /**
     * monte carlo simulation for the average diameter.
     */
    void monte_carlo_simulation(){

        avg_diameter = new DoubleVbl.Sum(0);
        parallelFor(0, T - 1).exec(new LongLoop() {
            // Per-thread variables.
            Random prng;
            HashMap<Integer, HashSet<Integer>> graph;
            DoubleVbl thr_avg_diameter;

            public void start() {
                prng = new Random(seed + rank());
                graph = new HashMap();
                thr_avg_diameter = threadLocal(avg_diameter);
            }

            public void run(long t) {

                for (int i = 0; i < V; ++i)
                    graph.put(i, new HashSet());

                for (int a = 0; a < V - 1; ++a) {
                    for (int b = a + 1; b < V; ++b) {
                        if (prng.nextDouble() < P) {
                            graph.get(a).add(b);
                            graph.get(b).add(a);
                        }
                    }
                }
                int hops, max_hops = 0;
                for (int a = 0; a < V; ++a) {
                    for (int b = 0; b < V; ++b) {
                        if (a != b && (hops = BFS(graph, a, b)) != 0) {
                            if (max_hops < hops)
                                max_hops = hops;
                        }
                    }
                }
                thr_avg_diameter.item += max_hops;
            }

            /**
             * Breadth first search for a given graph.
             * @param graph - graph to perform the search on.
             * @param start - What node to start from
             * @param dest - What node we are looking for.
             * @return
             */
            int BFS(HashMap<Integer, HashSet<Integer>> graph, int start, int dest) {
                HashSet<Integer> seen = new HashSet();
                LinkedList<Path> queue = new LinkedList();
                Path A = new Path(start);
                queue.addLast(A);
                seen.add(A.id);
                while (!queue.isEmpty()) {
                    A = queue.poll();
                    if (graph.get(A.id).contains(dest)) {
                        return A.dist + 1;
                    }
                    for (Integer B : graph.get(A.id)) {
                        if (!seen.contains(B)) {
                            seen.add(B);
                            queue.addLast(new Path(B, A));
                        }
                    }
                }
                return 0;
            }


            // Class used in the breadth first search.
            class Path {
                int dist, id;
                Path parent;

                //constructor, keeps tract of the distance by adding one to the previous.
                Path(int _id, Path _parent) {
                    id = _id;
                    parent = _parent;
                    dist = parent.dist + 1;
                }
                //initial constructor
                Path(int _id) {
                    id = _id;
                    dist = 0;
                }
            }
        });
        System.out.printf("%d\t%f\t%f\n", V, P, avg_diameter.item/T);
    }
    // Print a usage message and exit.
    private static void usage()
    {
        System.err.println ("Usage: java pj2 [threads=<K>] AverageDiameterRandomGraphSmp <seed> <V> <P> <T>");
        System.err.println ("<K> = Number of parallel threads");
        System.err.println("<seed> = Random seed");
        System.err.println ("<V> = Number of vertices");
        System.err.println("<T> = Number of trials");
        throw new IllegalArgumentException();
    }
}



