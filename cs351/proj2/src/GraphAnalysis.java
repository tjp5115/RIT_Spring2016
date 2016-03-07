import java.awt.*;
import java.io.*;
import java.util.*;


import edu.rit.numeric.Histogram;
import edu.rit.numeric.ListXYSeries;
import edu.rit.numeric.XYSeries;
import edu.rit.numeric.plot.Dots;
import edu.rit.numeric.plot.Plot;
import edu.rit.numeric.plot.Strokes;
import edu.rit.util.Random;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import static edu.rit.numeric.Mathe.*;

/**
 * Created by Crystal on 2/20/2016.
 */
public class GraphAnalysis {
    private HashMap<Integer, HashSet<Integer>> graph;
    private String graphfile;
    private int V;
    GraphAnalysis(String f){
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
    void construct_graph(File fileName){
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
                v1 = Integer.parseInt(line[1]);
                v2 = Integer.parseInt(line[2]);
                graph.get(v1).add(v2);
                graph.get(v2).add(v1);
            }
            // Always close files.
            bufferedReader.close();
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file '" +
                            fileName + "'");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file '"
                            + fileName + "'");
        }
    }

    /**
     * Get the diameter of a vertex.
     * @return diameter
     */
    int diameter() {
        int hops, max_hops = 0;
        for (int a = 0; a < V; ++a) {
            for (int b = 0; b < V; ++b) {
                if (a != b && (hops = BFS(a, b)) != 0) {
                    if (max_hops < hops)
                        max_hops = hops;
                }
            }
        }
        return max_hops;
    }


    /**
     * Breadth first search for a given graph.
     * @param start - What node to start from
     * @param dest - What node we are looking for.
     * @return
     */
    int BFS(int start, int dest) {
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

    public void degreeCentrility(){
        List list = new LinkedList(graph.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                HashSet hs1 = (HashSet) ((Map.Entry) (o1)).getValue();
                HashSet hs2 = (HashSet) ((Map.Entry) (o2)).getValue();
                return ((Comparable) hs2.size()).compareTo(hs1.size());
            }
        });

        Map result = new LinkedHashMap<>();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), ((HashSet) entry.getValue()).size());
        }
        System.out.println("Rank\tVertex\tDegCen");
        int count = 1;
        Iterator it = result.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry i = (Map.Entry) it.next();
            System.out.printf("%d\t%d\t%d%n",count++,i.getKey(),i.getValue());
            if(count > 40) break;
        }
    }

    public double averageDistance(int src){
        LinkedList<Integer> vertices= new LinkedList<>(graph.keySet());
        int count = 0;
        int total = 0;
        int dist;
        for(int i = 0; i < vertices.size(); ++i){
            if( src != i && (dist = BFS(src,i)) != 0 ){
                total += dist;
                count++;
            }
        }
        return (double)total / (double)count;
    }

    public void closenessCentrality(){
        TreeMap<Double,Integer> results = new TreeMap<>();
        LinkedList<Integer> vertices = largestComponent();
        int src,dest;
        for (int i = 0; i < vertices.size(); ++i){
            int count = 0;
            int total = 0;
            src = vertices.get(i);
            for(int j = 0; j < vertices.size() && j < 1000; j++) {
                dest = vertices.get(j);
                if(i != j){
                    total += BFS(src,dest);
                    count++;
                }
            }
            results.put((double)total/count, src);
            System.out.println(i + " of " + vertices.size());
        }
        System.out.println("Rank\tVertex\tCloCen");
        int rank = 1;
        for(Map.Entry<Double,Integer> entry : results.entrySet()) {
            System.out.printf("%d\t%d\t%f%n", rank++, entry.getValue(), entry.getKey());
            if ( rank > 40 )break;
        }
    }

    public LinkedList<Integer> largestComponent(){
        LinkedList<Integer> vertices= new LinkedList<>(graph.keySet());
        LinkedHashMap<Integer,HashSet<Integer>> component = new LinkedHashMap<>();
        int count = 0;
        while (!vertices.isEmpty()){
            //System.out.println(vertices.size());
            int src = vertices.poll();
            component.put(count,new HashSet<>());
            component.get(count).add(src);
            for(int i = 0; i < vertices.size(); ++i){
                int dest = vertices.get(i);
                if( BFS(src,dest) != 0){
                    vertices.remove(i--);
                    component.get(count).add(dest);
                }
            }
            count++;
        }
        HashSet largest = new HashSet();
        for(Map.Entry<Integer,HashSet<Integer>> entry : component.entrySet()){
            if (entry.getValue().size() > largest.size()) largest = entry.getValue();
        }
        return new LinkedList(largest);
    }

    /**
     * Find the number of calculated components for a given graph.
     */
    public void connectedComponents(){
        LinkedList<Integer> vertices= new LinkedList<>(graph.keySet());
        System.out.println("Comp\tSize");
        int comp = 0;
        int largest = -1;
        int smallest = V;
        while (!vertices.isEmpty()){
            //System.out.println(vertices.size());
            int src = vertices.poll();
            int count = 1;
            for(int i = 0; i < vertices.size(); ++i){
                int dest = vertices.get(i);
                if( BFS(src,dest) != 0){
                   count++;
                   vertices.remove(i--);
                }
            }
            System.out.printf("%d\t%d%n",comp++,count);
            if ( count < smallest ) smallest = count ;
            if ( count > largest ) largest = count;
        }

        System.out.println("Largest Component: " + largest);
        System.out.println("Smallest Component: " + smallest);

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
        System.err.println ("Usage: java pj2 GraphAnalysis <graphfile>");
        System.err.println ("<graphfile> = graph file name");
        throw new IllegalArgumentException();
    }

}
