import org.junit.Test;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class BSTTest {

    @Test
    public void BSTTreeTest() {
        LinkedList<String> arr = new LinkedList<>();
        arr.add("D");
        arr.add("A");
        arr.add("C");
        arr.add("G");
        arr.add("H");
        arr.add("L");
        arr.add("P");
        BST<String> bst = new BST<String>(arr);
        bst.print();
    }
}