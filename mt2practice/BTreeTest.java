import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class BTreeTest {

    @Test
    public void TestBTreeInOrder() {
        BTree<Integer> t = new BTree<>();

        // Initialize children nodes
        BTree.Node<Integer> c1 = new BTree.Node<>(List.of(1), Arrays.asList(null, null));
        BTree.Node<Integer> c2 = new BTree.Node<>(List.of(3), Arrays.asList(null, null));
        BTree.Node<Integer> c3 = new BTree.Node<>(List.of(6, 7), Arrays.asList(null, null, null));
        BTree.Node<Integer> c4 = new BTree.Node<>(List.of(9, 14), Arrays.asList(null, null, null));

        // Set root of tree
        t.root = new BTree.Node<>(List.of(2,4,8), List.of(c1, c2, c3, c4));
        t.inorderTraversal();
        assertEquals(List.of(1,2,3,4,6,7,8,9,14), t.inorderTraversal());
    }
}