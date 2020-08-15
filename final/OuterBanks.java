import java.util.*;

public class OuterBanks {
    // Optional: your code here

    private static void DFS(Graph g, Stack<Integer> fringe, HashSet<Integer> visited) {
        while (!fringe.empty()) {
            Integer vertex = fringe.pop();
            if (!visited.contains(vertex)) {
                visited.add(vertex);
                for (Integer e : g.neighbors(vertex)) {
                    if (!visited.contains(e)) {
                        fringe.add(e);
                    }
                }
            }
        }
    }

    public static int getMinLocations(Graph g) {
        ArrayList<Integer> notVisited = new ArrayList<>();
        for(int i = 0; i<g.numVertices; i++) {
            notVisited.add(i);
        }
        Stack<Integer> fringe = new Stack<>();
        //fringe.add(0);
        HashSet<Integer> visited = new HashSet<>();
        int counter = 0;
        while(!notVisited.isEmpty()) {
            Integer next = notVisited.get(0);
            /*for(int i = 0; i<g.numVertices;i++) {
                if(!g.neighbors(i).isEmpty()&&notVisited.contains(i)) {
                    next = i;
                    break;
                }
            }*/
            fringe.add(next);
            DFS(g, fringe, visited);
            for(Integer x: visited) {
                notVisited.remove(x);
            }
            counter++;
        }
        return counter;
    }

    //Optional: your code here
}