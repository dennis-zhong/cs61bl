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

    //Optional: your code here

    public static class Graph {

        private LinkedList<Edge>[] adjLists;
        public int numVertices;

        /* Initializes a graph with NUMVERTICES vertices and no Edges. */
        public Graph(int numVertices) {
            adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
            for (int k = 0; k < numVertices; k++) {
                adjLists[k] = new LinkedList<Edge>();
            }
            this.numVertices = numVertices;
        }

        /* Adds a directed Edge (V1, V2) to the graph. */
        public void addEdge(int v1, int v2) {
            addEdge(v1, v2, 0);
        }

        /* Adds an undirected Edge (V1, V2) to the graph. */
        public void addUndirectedEdge(int v1, int v2) {
            addUndirectedEdge(v1, v2, 0);
        }

        /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
           Edge already exists, replaces the current Edge with a new Edge with
           weight WEIGHT. */
        public void addEdge(int v1, int v2, int weight) {
            Edge in = new Edge(v1, v2, weight);
            boolean contains = false;
            for(int i = 0; i < adjLists[v1].size(); i++) {
                Edge edge = adjLists[v1].get(i);
                if(edge.from == v1 && edge.to == v2) {
                    adjLists[v1].set(i, in);
                    contains = true;
                }
            }
            if(!contains) {
                adjLists[v1].addLast(in);
            }
        }

        /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
           Edge already exists, replaces the current Edge with a new Edge with
           weight WEIGHT. */
        public void addUndirectedEdge(int v1, int v2, int weight) {
            addEdge(v1, v2, weight);
            addEdge(v2, v1, weight);
        }

        /* Returns a list of all the vertices u such that the Edge (V, u)
           exists in the graph. */
        public List<Integer> neighbors(int v) {
            ArrayList<Integer> lst = new ArrayList<>();
            for(Edge curr: adjLists[v]) {
                lst.add(curr.to);
            }
            return lst;
        }

        private class Edge {

            private int from;
            private int to;
            private int weight;

            Edge(int from, int to, int weight) {
                this.from = from;
                this.to = to;
                this.weight = weight;
            }

            public String toString() {
                return "(" + from + ", " + to + ", weight = " + weight + ")";
            }

        }
    }
}