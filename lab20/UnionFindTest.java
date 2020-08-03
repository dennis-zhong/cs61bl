import org.junit.Test;

import static org.junit.Assert.*;

public class UnionFindTest {

    @Test
    public void union() {
        UnionFind set = new UnionFind(5);
        for(int i = 0; i<5; i++) {
            assertTrue(set.sizeOf(i) == 1);
        }
        set.union(0, 1);
        set.union(2, 3);
        set.union(0, 2);
        assertTrue(set.connected(3, 1));
        assertTrue(set.connected(2, 1));
        assertTrue(set.connected(3, 0));
        assertFalse(set.connected(3, 4));

        UnionFind set2 = new UnionFind(6);
        set2.union(0, 1);
        set2.union(0, 2);
        set2.union(3, 4);
        set2.union(3, 0);
        set2.find(3);

    }
}