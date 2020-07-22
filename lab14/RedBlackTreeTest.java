import org.junit.Test;

import static org.junit.Assert.*;

public class RedBlackTreeTest {

    @Test
    public void insert() {
        RedBlackTree<Integer> intTree = new RedBlackTree<Integer>();
        for(int i = 0; i < 5; i++) {
            intTree.insert(i);
        }
        intTree.insert(10);
        intTree.insert(12);
        intTree.insert(5);
        intTree.insert(1);
        intTree.insert(3);

    }
}