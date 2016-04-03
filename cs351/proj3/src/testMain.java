/**
 * Created by Crystal on 3/29/2016.
 */
public class testMain {
    public static void main(String args[]) throws Exception{
        GraphAnalysis ga = new GraphAnalysis("rome-roads.txt");
        //ga.minDistPrim();
//        ga.plotGraph();
        ga.betweenCentrality();
    }
}
