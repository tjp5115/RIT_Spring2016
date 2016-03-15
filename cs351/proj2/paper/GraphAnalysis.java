import java.io.*;
import java.util.*;
import edu.rit.numeric.Histogram;
import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.XYSeries;
import edu.rit.numeric.plot.Dots;
import edu.rit.numeric.plot.Plot;

import java.io.File;
import java.text.DecimalFormat;


/**
 * Created by Tyler Paulsen on 2/20/2016.
 * Class to compute various network science algorithms for a given graph.
 */
public class GraphAnalysis {
    private HashMap<Integer, HashSet<Integer>> graph;
    private String graphfile;
    private int V;

    public static void main(String args[]) throws Exception{
        if (args.length < 2 || args.length > 5) usage();
        GraphAnalysis ga = new GraphAnalysis(args[0]);

        for(int i = 1; i < args.length; ++i){
            String arg = args[i];
            switch (arg){
                case "degreeDistribution":
                    ga.degreeDistribution();
                    break;
                case "connectedComponents":
                    ga.connectedComponents();
                    break;
                case "closenessCentrality":
                    ga.closenessCentrality();
                    break;
                case "degreeCentrality":
                    ga.degreeCentrality();
                    break;
                default:
                    usage();
            }
        }
    }

    GraphAnalysis(String f) throws IOException{
        graphfile = new String(f);
        graph = new HashMap();
        construct_graph(new File(f));
        //System.out.println(graph);
    }

    /**
     * Construct a graph from a file. Must conform to the spec: https://cs.rit.edu/~ark/351/analysis/graphfile.shtml
     *  If the spec is not follow, results may vary.
     * @param fileName - name of the graph file
     */
    void construct_graph(File fileName)throws IOException{
        // The name of the file to open.
        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line[] = bufferedReader.readLine().split(" ");
            V = Integer.parseInt(line[1]);
            int E = Integer.parseInt(line[2]);

            for (int i = 0; i < V; ++i)
                graph.put(i, new HashSet());
            int v1,v2;
            for (int i = 0; i < E; ++i){
                line = bufferedReader.readLine().split(" ");
                if(line[0].equals("e")) {
                    v1 = Integer.parseInt(line[1]);
                    v2 = Integer.parseInt(line[2]);
                    graph.get(v1).add(v2);
                    graph.get(v2).add(v1);
                }
            }
            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file: " + fileName);
            throw new FileNotFoundException();
        }
        catch(IOException ex) {
            System.out.println("Error reading file: " + fileName);
            throw new IOException();
        }
    }

    /**
     * Breadth first search for a given graph anc compute the average distance.
     * @param src - What node to start from
     * @return  double : the average distance for a given node.
     */
    double BFS_avgDist(int src) {
        HashSet<Integer> seen = new HashSet();
        LinkedList<Path> queue = new LinkedList();
        Path A = new Path(src);
        queue.addLast(A);
        seen.add(A.id);
        int total_dist = 0;
        int count = 0;
        while (!queue.isEmpty()) {
            A = queue.poll();
            for (Integer B : graph.get(A.id)) {
                if (!seen.contains(B)) {
                    seen.add(B);
                    queue.addLast(new Path(B, A));
                    total_dist += A.dist;
                    count += 1;
                }
            }
        }
        return (double)total_dist/count;
    }

    /**
     * Breadth first search for a given graph.
     * @param start - What node to start from
     * @param dest - What node we are looking for.
     * @return
     */
    int BFS(int start, int dest) {
        if(graph.get(start).contains(dest)) return 1;
        HashSet<Integer> seen = new HashSet();
        LinkedList<Path> queue = new LinkedList();
        Path A = new Path(start);
        queue.addLast(A);
        seen.add(A.id);
        while (!queue.isEmpty()) {
            A = queue.poll();

            for (Integer B : graph.get(A.id)) {
                if (graph.get(B).contains(dest)) return A.dist + 1;
                if (!seen.contains(B)) {
                    seen.add(B);
                    queue.addLast(new Path(B, A));
                }
            }
        }
        return 0;
    }


    /**
     * BFS to find a component for a given node.
     * @param start - What node to start from
     * @return
     */
    HashSet<Integer> BFS_component(int start) {
        HashSet<Integer> seen = new HashSet();
        LinkedList<Path> queue = new LinkedList();
        Path A = new Path(start);
        queue.addLast(A);
        seen.add(A.id);
        while (!queue.isEmpty()) {
            A = queue.poll();
            for (Integer B : graph.get(A.id)) {
                if (!seen.contains(B)) {
                    seen.add(B);
                    queue.addLast(new Path(B, A));
                }
            }
        }
        return seen;
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
            dist = 1;
        }
    }

    /**
     * compute the degree centrality of a graph.
     */
    public void degreeCentrality(){
        // < vert id, degree >
        TreeMap<Integer,LinkedList<Integer>> results = new TreeMap<>(Collections.reverseOrder());
        LinkedList<Integer> vertices = largestComponent();
        int src,degree;
        for (int i  = 0; i < vertices.size(); ++i){
            src = vertices.get(i);
            degree = graph.get(src).size();
            if (results.containsKey(degree))
                results.get(degree).add(src);
            else {
                LinkedList<Integer> v = new LinkedList();
                v.add(src);
                results.put(degree,v);
            }
        }

        System.out.println("Rank\tVertex\tDegCen");
        int rank = 1;
        for(Map.Entry<Integer,LinkedList<Integer>> entry : results.entrySet()) {
            for ( Integer vertex : entry.getValue())
                System.out.printf("%d\t%d\t%d%n", rank++, vertex, entry.getKey()) ;
            if(rank > 40) break;
        }
    }

    /**
     * compute the closeness centrality for the largest component in the graph.
     */
    public void closenessCentrality(){
        TreeMap<Double,LinkedList<Integer>> results = new TreeMap<>();
        LinkedList<Integer> vertices = largestComponent();
        int src;
        double avg;
        for (int i  = 0; i < vertices.size(); ++i){
            src = vertices.get(i);
            avg = BFS_avgDist(src);
            if (results.containsKey(avg))
                results.get(avg).add(src);
            else {
                LinkedList<Integer> v = new LinkedList();
                v.add(src);
                results.put(avg,v);
            }
        }
        System.out.println("Rank\tVertex\tCloCen");
        int rank = 1;
        for(Map.Entry<Double,LinkedList<Integer>> entry : results.entrySet()) {
            for ( Integer vertex : entry.getValue())
                System.out.printf("%d\t%d\t%f%n", rank++, vertex, entry.getKey());
            if(rank > 40) break;
        }
    }

    /**
     * Find the number of calculated components for a given graph.
     */
    public void connectedComponents(){
        System.out.println("Comp\tSize");
        int comp = 0;
        int largest = -1;
        int smallest = V;
        LinkedList<Integer> vertices= new LinkedList<>(graph.keySet());

        HashSet<Integer> seen;
        while (!vertices.isEmpty()) {
            int src = vertices.poll();
            seen = BFS_component(src);
            vertices.removeAll(seen);
            int size = seen.size();
            System.out.printf("%d\t%d%n", comp++, size);
            if ( size < smallest) smallest =  size;
            if ( size  > largest ) largest = size;
        }
        System.out.println("Largest Component: " + largest);
        System.out.println("Smallest Component: " + smallest);
    }

    /**
     * get the largest component of the graph
     * @return List of vertices in the largest component.
     */
    public LinkedList<Integer> largestComponent(){
        LinkedList<Integer> vertices= new LinkedList<>(graph.keySet());
        HashSet<Integer> largest = new HashSet<>();

        HashSet<Integer> seen;
        while (!vertices.isEmpty()) {
            int src = vertices.poll();
            seen = BFS_component(src);
            vertices.removeAll(seen);
            if ( largest.size() < seen.size() ) largest = seen;
        }
        return new LinkedList<Integer>(largest);
    }

    /**
     * calculate the degree distribution, and produce two plots of the outcome -- a Power Fn, and an Exponential Fn plot
     * @throws IOException
     */
    public void degreeDistribution() throws IOException{
        //set up histogram

        Histogram hist = new Histogram(V);
        // Update histogram.
        for(int i = 0; i < V; ++i ) {
                hist.accumulate(graph.get(i).size());
        }

        // Print results, gather plot data.
        ListXYSeries results = new ListXYSeries();
        ListXYSeries exponential = new ListXYSeries();
        ListXYSeries power = new ListXYSeries();
        System.out.printf("d\tcount\tpr\texpect%n");
        for (int d = 0; d < hist.size(); ++d) {
            long count = hist.count(d);
            if (count == 0) continue;
            double pr = hist.prob(d);
            double expect = hist.expectedProb(d);
            results.add(d, pr);
            exponential.add(d, Math.log(pr));
            power.add(Math.log(d), Math.log(pr));
            System.out.printf("%d\t%d\t%.4e\t%.4e%n", d, count, pr, expect);
        }

        XYSeries.Regression r = exponential.linearRegression();
        System.out.printf("exponential%na=%f\tb=%f\tcorr=%f%n", r.a, r.b, r.corr);
        Plot exponentialplot = new Plot()
            .plotTitle(String.format("Degree Distribution, Exponential fn, graphfile = %s", graphfile))
            .xAxisTitle("Degree <I>d</I> a=" + (float) r.a + " b=" + (float) r.b + " corr=" + (float) r.corr)
            .xAxisLength(460)
            .yAxisTitle("pr(<I>d</I>)")
            .yAxisKind(Plot.LOGARITHMIC)
            .yAxisTickFormat(new DecimalFormat("0.0E0"))
            .yAxisLength(300)
            .seriesDots(Dots.circle(5))
            .seriesStroke(null)
            .xySeries(results);
        exponentialplot.getFrame().setVisible(true);
        r = power.linearRegression();
        System.out.printf("Power%na=%f\tb=%f\tcorr=%f%n", r.a, r.b, r.corr);
        Plot powerplot = new Plot()
                .plotTitle(String.format("Degree Distribution, Power fn, graphfile = %s", graphfile))
                .xAxisTitle("Degree <I>d</I> a="+(float)r.a + " b=" + (float)r.b + " corr="+(float)r.corr)
                .xAxisLength(460)
                .yAxisTitle("pr(<I>d</I>)")
                .yAxisKind(Plot.LOGARITHMIC)
                .xAxisKind(Plot.LOGARITHMIC)
                .yAxisLength(300)
                .yAxisTickFormat(new DecimalFormat("0.0E0"))
                .xAxisTickFormat(new DecimalFormat("0.0E0"))
                .seriesDots(Dots.circle(5))
                .seriesStroke(null)
                .xySeries(results);

        powerplot.getFrame().setVisible(true);
    }

    // Print a usage message and exit.
    private static void usage()
    {
        System.err.println ("Usage: java pj2 GraphAnalysis <graphfile> <graph measurement>");
        System.err.println ("<graphfile> = graph file name");
        System.err.println ("<graph measurement> = [ degreeDistribution | connectedComponents | " +
                "closenessCentrality | degreeCentrality");
        throw new IllegalArgumentException();
    }

}
