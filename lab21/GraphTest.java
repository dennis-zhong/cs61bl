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
    public void kruskals() {
        Graph g = Graph.loadFromText("./inputs/graphTestAllDisjoint.in");
        Graph t = g.kruskals();
        Graph x = g.prims(0);
        assertTrue(x.equals(t));
        assertTrue(t.spans(g));
    }
}