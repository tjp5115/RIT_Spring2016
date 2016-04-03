import java.awt.*;
import edu.rit.util.PriorityQueue;
import edu.rit.util.PriorityQueue.Item;
import java.io.*;
import java.util.*;
import edu.rit.numeric.Histogram;
import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.XYSeries;
import edu.rit.numeric.plot.Dots;
import edu.rit.numeric.plot.Plot;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;


/**
 * Created by Tyler Paulsen on 2/20/2016.
 * Class to compute various network science algorithms for a given graph.
 */
public class GraphAnalysis {
    private HashMap<Integer, Vertex> graph;
    private String graphfile;
    private int V;

    public static void main(String args[]) throws Exception{
        if (args.length < 2 || args.length > 6) usage();
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
                case "plotGraph":
                    ga.plotGraph();
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
    private void construct_graph(File fileName)throws IOException{
        // The name of the file to open.
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String splitLine[] = bufferedReader.readLine().split(" ");

            while(!splitLine[0].equals("g")) {
                splitLine = bufferedReader.readLine().split(" ");
            }

            V = Integer.parseInt(splitLine[1]);
            int E = Integer.parseInt(splitLine[2]);

            for (int i = 0; i < V; ++i)
                graph.put(i, new Vertex(i));

            int v1,v2;
            float x,y,w;
            Vertex v;

            String line;
            //parse the weights
            while((line = bufferedReader.readLine()) != null) {
                splitLine = line.split(" ");
                //System.out.println(Arrays.toString(splitLine));
                if (splitLine[0].equals("v")) {
                    v1 = Integer.parseInt(splitLine[1]);
                    w = Float.parseFloat(splitLine[2]);
                    v = graph.get(v1);
                    v.w = w;
                }else if (splitLine[0].equals("d")) {
                    v1 = Integer.parseInt(splitLine[1]);
                    x = Float.parseFloat(splitLine[2]);
                    y = Float.parseFloat(splitLine[3]);
                    v = graph.get(v1);
                    v.x = x;
                    v.y = y;
                }else if(splitLine[0].equals("e")) {
                    v1 = Integer.parseInt(splitLine[1]);
                    v2 = Integer.parseInt(splitLine[2]);
                    graph.get(v1).addAdjacent(v2);
                    graph.get(v2).addAdjacent(v1);
                }
            }
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file: " + fileName);
            throw new FileNotFoundException();
        }
        catch(Exception ex) {
            System.out.println("Error parsing file: " + fileName);
            throw new IOException();
        }
    }

    /**
     * Breadth first search for a given graph anc compute the average distance.
     * @param src - What node to start from
     * @return  double : the average distance for a given node.
     */
    private double BFS_avgDist(int src) {
        HashSet<Integer> seen = new HashSet();
        LinkedList<Path> queue = new LinkedList();
        Path A = new Path(src);
        queue.addLast(A);
        seen.add(A.id);
        int total_dist = 0;
        int count = 0;
        while (!queue.isEmpty()) {
            A = queue.poll();
            for (Integer B : graph.get(A.id).edges) {
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
     * between centrality BFS
     * @param src
     * @param between - keeps track of the betweeness for each vertex
     */
    private void BFS_between(int src, Between between[]){
        HashSet<Integer> seen = new HashSet();
        LinkedList<Path> queue = new LinkedList();
        Path A = new Path(src);
        queue.addLast(A);
        seen.add(A.id);
        while (!queue.isEmpty()) {
            A = queue.poll();
            for (Integer B : graph.get(A.id).edges) {
                if (!seen.contains(B)) {
                    seen.add(B);
                    queue.addLast(new Path(B, A));
                    for(Integer i: queue.getLast().getPath(src)){
                        between[i].total_paths += 1;
                        between[i].total_distance += A.dist + 1;
                    }
                }
            }
        }
    }

    /**
     *  shortest path
     * @param src
     * @param dest
     */
    private Path BFS(int src, int dest ){
        HashSet<Integer> seen = new HashSet();
        LinkedList<Path> queue = new LinkedList();
        Path A = new Path(src);
        queue.addLast(A);
        seen.add(A.id);
        while (!queue.isEmpty()) {
            A = queue.poll();
            for (Integer B : graph.get(A.id).edges) {
                if(B == dest) return new Path(B,A);
                if (!seen.contains(B)) {
                    seen.add(B);
                    queue.addLast(new Path(B, A));
                }
            }
        }
        return null;
    }

    /**
     * BFS to find a component for a given node.
     * @param start - What node to start from
     * @return
     */
    private HashSet<Integer> BFS_component(int start) {
        HashSet<Integer> seen = new HashSet();
        LinkedList<Path> queue = new LinkedList();
        Path A = new Path(start);
        queue.addLast(A);
        seen.add(A.id);
        while (!queue.isEmpty()) {
            A = queue.poll();
            for (Integer B : graph.get(A.id).edges) {
                if (!seen.contains(B)) {
                    seen.add(B);
                    queue.addLast(new Path(B, A));
                }
            }
        }
        return seen;
    }

    // Class used in the breadth first search.
    private class Path {
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
        public HashSet<Integer> getPath(int dest){
            HashSet<Integer> path = new HashSet<>();
            if(dest != parent.id) {
                getPath(path, parent, dest);
            }
            return path;
        }
        private void getPath(HashSet<Integer> path, Path vertex, int dest){
            path.add(vertex.id);
            if(vertex.parent.id == dest)
                return;
            getPath(path, vertex.parent, dest);
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

    /**h
     * @return List of vertices in the large
     * get the largest component of the grapst component.
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

    /**
     * plots the graph.
     */
    public void plotGraph(){

        ListXYSeries results = new ListXYSeries();
        Vertex src,dest;
        for (int i = 0; i < graph.size(); ++i) {
            src = graph.get(i);
            for(Integer A: src.edges){
                dest = graph.get(A);
                results.add(src.x, src.y);
                results.add(dest.x, dest.y);
            }
        }

        Plot plot = new Plot()
                .plotTitle("Graph Plot, graphfile = " + graphfile)
                .xAxisLength(650)
                .yAxisLength(650)
                .seriesDots(Dots.circle(5))
                .seriesColor(Color.RED)
                .segmentedSeries(results);
        plot.getFrame().setVisible(true);
    }

    /**
     * Creates a complete graph of the current graph
     */
    private void createCompleteGraph(){
        for(int i = 0; i < V; ++i){
            for( int k = i; k < V; ++k){
                graph.get(i).edges.add(k);
                graph.get(k).edges.add(i);
            }
        }
    }

    /**
     * uses Prim's algorithm to create a minimum spanning tree on the current graph.
     */
    public void minDistPrim(){
        createCompleteGraph();
        edu.rit.util.PriorityQueue queue = new PriorityQueue();
        Distance D[] = new Distance[V];
        for(int i = 0; i<V; ++i){
            D[i] = new Distance(i, -1, Double.MAX_VALUE);
            queue.add(D[i]);
        }
        D[0].distance = 0.0f;
        Distance V;
        while( !queue.isEmpty()){
            V = (Distance)queue.remove();
            for(Integer W: graph.get(V.n).edges){
                if(D[W].enqueued() && distance(graph.get(W),graph.get(V.n)) < D[W].distance){
                    D[W].predecessor = V.n;
                    D[W].distance = distance(graph.get(W),graph.get(V.n));
                    D[W].increasePriority();
                }
            }
        }
        double total_distance = 0;
        for(int i = 0; i < this.V; ++i) {
            graph.get(i).edges.clear();
            total_distance += D[i].distance;
        }
        System.out.printf("Total Distance: %f%n",total_distance);
        Vertex v1;
        for(int i=0; i < this.V; ++i){
            if (D[i].predecessor == -1) continue;
            v1 = graph.get(i);
            v1.edges.add(D[i].predecessor);
        }

    }

    public void betweenCentrality(){
        TreeMap<Double,Integer> results = new TreeMap<>(Collections.reverseOrder());
        int between[] = new int[V];
        for( int i = 0; i < V; ++i){
            between[i] = 0;
        }
        Path path;
        int total = 0;
        for( int src = 0; src < V; ++src){
            for(int dest = src+1; dest < V; ++dest) {
                path = BFS(src, dest);
                HashSet<Integer> p = path.getPath(src);
                if (p.size() != path.dist){
                    System.out.println(p.size() + " " + path.dist);
                }
                for(Integer i: p){
                    between[i] += 1;
                }
                total += 1;
            }
        }
        for(int i = 0; i < V; ++i){
            results.put(between[i] / (double)total,i);
        }
        System.out.println("Rank\tVertex\tbetweeness");
        int rank = 1;
        for(Map.Entry<Double,Integer> vertex: results.entrySet()) {
            System.out.printf("%d\t%d\t%f%n", rank++, vertex.getValue(), vertex.getKey());
            if(rank > 40) break;
        }
    }
    /**
     * used in prim's algo to store a vertex and edge.
     */
    private class Distance extends Item{
        int n,predecessor;
        double distance;
        Distance(int n, int predecessor, double distance){
            this.n = n;
            this.predecessor = predecessor;
            this.distance = distance;
        }
        public boolean equals(Object obj){
            return (Integer)obj == n;
        }

        @Override
        public boolean comesBefore(Item item) {
            return this.distance < ((Distance)item).distance;
        }
        public String toString(){
            return "N="+n + " p=" + predecessor + " D=" + distance;
        }
    }

    /**
     * used to calculate the betweeness
     */
    class Between{
        int total_paths;
        int total_distance;
        Between(){
            total_distance = total_paths = 0;
        }
        double get_betweeness(){
            return (double)total_distance/total_paths;
        }
    }
    /**
     * get the euclidean distance for two points
     * @param a
     * @param b
     * @return distance
     */
    private double distance(Vertex a, Vertex b){
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    private class Vertex{
        int n;
        float x,y,w;
        HashSet<Integer> edges;
        Vertex(int n){
            this.n = n;
            edges = new HashSet<>();
            x = y = 0.0f;
            w = 1.0f;
        }
        void addAdjacent(int v){
            edges.add(v);
        }

        public boolean equals(Object obj){
            return (Integer)obj == n;
        }
        public int size(){
            return edges.size();
        }
        public boolean contains(int v){
            return edges.contains(v);
        }
        public String toString(){
            return n + " : " + x + " , " + y;
        }
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
