import java.io.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Created by Crystal on 2/20/2016.
 */
public class Graph {
    public HashMap<Integer, HashSet<Integer>> graph;
    int V;
    Graph(String f){
        graph = new HashMap();
        construct_graph(new File(f));
        System.out.println(graph);
    }

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

    int diameter() {
        int hops, max_hops = 0;
        for (int a = 0; a < V; ++a) {
            for (int b = 0; b < V; ++b) {
                if (a != b && (hops = BFS(graph, a, b)) != 0) {
                    if (max_hops < hops)
                        max_hops = hops;
                }
            }
        }
        return max_hops;
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
}
