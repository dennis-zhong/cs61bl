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
        assertTrue(set.sizeOf(0) == 2);
        assertTrue(set.parent(1) == 0);
        assertTrue(set.sizeOf(2) == 2);
        assertTrue(set.parent(3) == 2);
        assertTrue(set.sizeOf(4) == 1);
        set.union(0, 2);
    }
}