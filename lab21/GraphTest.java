import org.junit.Test;

import static org.junit.Assert.*;

public class GraphTest {

    @Test
    public void prims() {
        Graph g = Graph.loadFromText("./inputs/graphTestNormal.in");
        Graph t = g.prims(0);
        assertTrue(g.prims(0).equals(g.prims(2)));
        assertTrue(g.prims(0).equals(g.prims(1)));
        assertTrue(g.prims(1).equals(g.prims(2)));
        assertTrue(t.spans(g));

        g = Graph.randomGraph(10, 20, 10);
        /*for(int i = 0; i<10; i++) {
            Graph t1 = g.prims(i);
            for(int j = i+1; j<10; j++) {
                Graph t2 = g.prims(j);
                t1.equals(t2);
                assertTrue(t1.equals(t2));
            }
        }*/
    }

    @Test
    public void prims2() {
        Graph g = new Graph();
        g.addVertex(1); g.addVertex(2); g.addVertex(3); g.addVertex(4); g.addVertex(5);
        g.addVertex(6); g.addVertex(7); g.addVertex(8); g.addVertex(9);
        g.addEdge(1, 6, 9001); g.addEdge(6, 1, 9001);
        g.addEdge(5, 1, 5); g.addEdge(1, 5, 5);
        g.addEdge(1, 3, 2); g.addEdge(3, 1, 2);
        g.addEdge(1, 9, 3); g.addEdge(9, 1, 3);
        g.addEdge(9, 3, 1); g.addEdge(3, 9, 1);
        g.addEdge(9, 2, 1); g.addEdge(2, 9, 1);
        g.addEdge(5, 4, 4); g.addEdge(4, 5, 4);
        g.addEdge(3, 4, 8); g.addEdge(4, 3, 8);
        g.addEdge(4, 2, 3); g.addEdge(2, 4, 3);
        g.addEdge(7, 4, 11); g.addEdge(4, 7, 11);
        g.addEdge(2, 7, 5); g.addEdge(7, 2, 5);
        g.addEdge(8, 2, 6); g.addEdge(2, 8, 6);
        g.addEdge(7, 8, 7); g.addEdge(8, 7, 7);
        g.addEdge(9, 8, 8); g.addEdge(8, 9, 8);
        Graph t = g.prims(6);
    }

    @Test
    public void kruskals() {
        Graph g = Graph.loadFromText("./inputs/graphTestAllDisjoint.in");
        Graph t = g.kruskals();
        Graph x = g.prims(0);
        //assertTrue(x.equals(t));
        //assertTrue(t.spans(g));
    }
}