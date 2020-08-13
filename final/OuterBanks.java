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
        fringe.add(0);
        HashSet<Integer> visited = new HashSet<>();
        int counter = 0;
        while(!notVisited.isEmpty()) {
            fringe.add(notVisited.get(0));
            DFS(g, fringe, visited);
            for(int x: visited) {
                notVisited.remove(new Integer(x));
            }
            counter++;
        }
        return counter;
    }
}