/**
 * Created by Crystal on 3/29/2016.
 */
public class testMain {
    public static void main(String args[]) throws Exception{
        //GraphAnalysis ga = new GraphAnalysis("rome-cities.txt");
        //GraphAnalysis ga = new GraphAnalysis("graphfile.txt");
        GraphAnalysis ga = new GraphAnalysis("egypt-cities.txt");
        //GraphAnalysis ga = new GraphAnalysis("graphFile.txt");
        ga.minDistPrim();
        ga.plotGraph();
        ga.createGraphFile();
        ga.betweenCentrality();
    }
}
