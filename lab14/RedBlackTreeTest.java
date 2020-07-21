import org.junit.Test;

import static org.junit.Assert.*;

public class RedBlackTreeTest {

    @Test
    public void insert() {
        RedBlackTree<Integer> intTree = new RedBlackTree<Integer>();
        intTree.insert(1);
        intTree.insert(2);
        intTree.insert(3);
        for(int i = 4; i < 100; i++) {
            intTree.insert(i);
        }
    }
}