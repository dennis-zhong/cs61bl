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
    }

    @Test
    public void kruskals() {
        Graph g = Graph.loadFromText("./inputs/graphTestNormal.in");
        Graph t = g.kruskals();
        Graph x = g.prims(0);
        assertTrue(x.equals(t));
        assertTrue(t.spans(g));
    }
}