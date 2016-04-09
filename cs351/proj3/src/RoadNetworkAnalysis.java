import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.plot.Dots;
import edu.rit.numeric.plot.Plot;
import edu.rit.util.PriorityQueue;
import edu.rit.util.PriorityQueue.Item;

import java.awt.*;
import java.io.*;
import java.util.*;


/**
 * Created by Tyler Paulsen on 4/9/2016.
 * Class to compute various network science algorithms for a given graph.
 */
public class RoadNetworkAnalysis {
    private HashMap<Integer, Vertex> graph;
    private String graphfile;
    private int V;

    public static void main(String args[]) throws Exception{
        if (args.length < 2 ) usage();
        RoadNetworkAnalysis ga = new RoadNetworkAnalysis(args[0]);

        for(int i = 1; i < args.length; ++i){
            String arg = args[i];
            switch (arg){
                case "minSpanningTree":
                    ga.minSpanningTree();
                    break;
                case "plotGraph":
                    ga.plotGraph();
                    break;
                case "createGraphFile":
                    ga.createGraphFile();
                    break;
                case "betweenCentrality":
                    ga.betweenCentrality();
                    break;
                default:
                    usage();
            }
        }
    }

    RoadNetworkAnalysis(String f) throws IOException{
        graphfile = f;
        graph = new HashMap<>();
        construct_graph(new File(f));
        //System.out.println(graph);
    }

    /**
     * creates a graphfile called: graphFile.txt using the graph formatted specified in:
     * https://cs.rit.edu/~ark/351/analysis/graphfile.shtml
     */
    public void createGraphFile(){
        try {
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream("graphFile.txt", false),
                            "utf-8")
            );
            int E = 0;
            int V = 0;
            LinkedList<String> lines = new LinkedList<>();

            HashSet<HashSet<Integer>> edge = new HashSet<>();
            String l;
            //write the g tag <V> <E>
            HashSet<Integer> hs;
            for(Vertex v: graph.values()) {
                V += 1;
                lines.addFirst("d " + v.n + " " + v.x + " " + v.y);
                for(Integer e: v.edges) {
                    l = "e " + v.n + " " + e;
                    hs = new HashSet<>();
                    hs.add(v.n);
                    hs.add(e);
                    if(!edge.contains(hs)) {
                        E += 1;
                        lines.addLast(l);
                        edge.add(hs);
                    }
                }
            }
            lines.addFirst("g " + V + " " + E);

            for(String line: lines) {
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
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
     *  shortest path
     * @param src node to start from
     * @param dest node to find shortest path too
     */
    private Path BFS(int src, int dest ){
        HashSet<Integer> seen = new HashSet<>();
        LinkedList<Path> queue = new LinkedList<>();
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
    public void minSpanningTree(){
        createCompleteGraph();
        PriorityQueue queue = new PriorityQueue();
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
                    D[W].predecessor = V.n;
                    D[W].distance = distance(graph.get(W),graph.get(V.n));
                    D[W].increasePriority();
                }
            }
        }
        double total_distance = 0;
        //clear all the edges from the current graph, so we can add new ones.
        for(int i = 0; i < this.V; ++i) {
            graph.get(i).edges.clear();
            total_distance += D[i].distance;
        }
        System.out.printf("Total Distance: %f%n",total_distance);
        Vertex v1,v2;
        for(int i=0; i < this.V; ++i){
            if (D[i].predecessor == -1) continue;
            v1 = graph.get(i);
            v2 = graph.get(D[i].predecessor);
            v1.edges.add(D[i].predecessor);
            v2.edges.add(i);
        }

    }

    /**
     * calculated the betweenness centrality
     */
    public void betweenCentrality(){
        TreeMap<Double,LinkedList<Integer>> results = new TreeMap<>(Collections.reverseOrder());
        int between[] = new int[V];
        for( int i = 0; i < V; ++i){
            between[i] = 0;
        }
        Path path;
        int total = 0;
        for( int src = 0; src < V; ++src){
            for(int dest = src+1; dest < V; ++dest) {
                if ( ( path = BFS(src, dest) ) == null) continue;

                HashSet<Integer> p = path.getPath(src);
                for(Integer i: p){
                    between[i] += 1;
                }
                total += 1;
            }
        }
        double result;
        LinkedList<Integer> ll;
        for(int i = 0; i < V; ++i){
            result = between[i] / (double)total;
            if(results.containsKey(result)){
                results.get(result).add(i);
            }else{
                ll = new LinkedList<>();
                ll.add(i);
                results.put(result, ll);
            }
        }
        System.out.println("Rank\tVertex\tbetweenness");
        int rank = 1;
        for(Map.Entry<Double,LinkedList<Integer>> entry : results.entrySet()) {
            for ( Integer vertex : entry.getValue()) {
                System.out.printf("%d\t%d\t%f%n", rank++, vertex, entry.getKey());
                graph.get(vertex).betweeness = entry.getKey();
                total += entry.getKey();
            }
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
     * get the euclidean distance for two points
     * @param a x,y cords of vertex
     * @param b x,y cords of vertex
     * @return distance
     */
    private double distance(Vertex a, Vertex b){
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    private class Vertex{
        int n;
        float x,y,w;
        double betweeness = -1;
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
        public String toString(){
            return n + " : " + x + " , " + y;
        }
    }
    // Print a usage message and exit.
    private static void usage()
    {
        System.err.println ("Usage: java RoadNetworkAnalysis <graphfile> <graph measurement>");
        System.err.println ("<graphfile> = graph file name");
        System.err.println ("<graph measurement> = [ minSpanningTree | plotGraph | " +
                "createGraphFile | betweenCentrality");
        throw new IllegalArgumentException();
    }

}
