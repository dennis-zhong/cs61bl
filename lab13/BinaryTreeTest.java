import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class BinaryTreeTest {
    @Test
    public void treeFormatTest() {
        BinarySearchTree<String> x = new BinarySearchTree<String>();
        x.add("C");
        x.add("A");
        x.add("E");
        x.add("B");
        x.add("D");
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(outContent));
        BinaryTree.print(x, "x");
        System.setOut(oldOut);
        assertEquals("x in preorder\nC A B E D \nx in inorder\nA B C D E \n\n".trim(),
                     outContent.toString().trim());
    }

    @Test
    public void treeContainsAddTest() {
        BinarySearchTree<String> x = new BinarySearchTree<String>();
        assertFalse(x.contains("X"));
        x.add("X");
        x.add("X");
        x.add("X");
        x.add("X");
        assertTrue(x.contains("X"));
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(outContent));
        BinaryTree.print(x, "x");
        System.setOut(oldOut);
        assertEquals("x in preorder\nX \nx in inorder\nX \n\n".trim(),
                outContent.toString().trim());
        x.delete("X");
        assertFalse(x.contains("X"));
    }
}