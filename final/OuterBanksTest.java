import org.junit.Test;

import static org.junit.Assert.*;

public class OuterBanksTest {
    @Test
    public void Test() {
        Graph g = new Graph(0);
        assertTrue(OuterBanks.getMinLocations(g)==0);

        Graph g2 = new Graph(1);
        assertTrue(OuterBanks.getMinLocations(g2)==1);

        Graph g3 = new Graph(2);
        OuterBanks.getMinLocations(g3);
        assertTrue(OuterBanks.getMinLocations(g3)==2);
        for(int i = 0; i<10; i++) {
            Graph g4 = new Graph(i);
            assertTrue(OuterBanks.getMinLocations(g4)==i);
        }
    }

    @Test
    public void Test2() {
        Graph g = new Graph(7);
        g.addEdge(1, 2);
        g.addEdge(3, 4);
        g.addEdge(4, 5);
        g.addEdge(5, 6);
        g.addEdge(6, 4);
        assertTrue(OuterBanks.getMinLocations(g)==3);
        Graph g2 = new Graph(7);
        g2.addUndirectedEdge(1, 2);
        g2.addUndirectedEdge(4, 5);
        g2.addUndirectedEdge(5, 6);
        g2.addUndirectedEdge(6, 4);
        System.out.println(OuterBanks.getMinLocations(g2));
        assertTrue(OuterBanks.getMinLocations(g2)==4);
    }

    @Test
    public void Test3() {
        Graph g = new Graph(3);
        g.addEdge(2, 1);
        g.addEdge(0, 1);
        System.out.println(OuterBanks.getMinLocations(g));
    }

    @Test
    public void fuzz() {
        int counter = 0;
        for(int i = 0; i<1000; i++) {
            Graph g = new Graph(100);
            for (int j = 0; j < 2; j++) {
                g.addUndirectedEdge((int) (Math.random() * 100), (int) (Math.random() * 100));
            }
            int x = OuterBanks.getMinLocations(g);
            if(x==100) {
                System.out.println(x);
                counter++;
            }
        }
        System.out.println(counter);
    }
}