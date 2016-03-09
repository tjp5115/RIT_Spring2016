/**
 * Created by Crystal on 3/6/2016.
 */
public class Main {
    public static void main(String args[]) throws Exception{
        GraphAnalysis ga = new GraphAnalysis("CA-HepTh-graph.txt");
        //GraphAnalysis ga = new GraphAnalysis("CA-GrQc-graph.txt");

        //ga.degreeDistribution();
        //ga.connectedComponents();
        //ga.degreeCentrility();
        ga.closenessCentrality();
    }
}
