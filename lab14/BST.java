import java.util.LinkedList;
import java.util.Iterator;

public class BST<T> {

    BSTNode<T> root;

    public BST(LinkedList<T> list) {
        root = sortedIterToTree(list.iterator(), list.size());
    }

    /* Returns the root node of a BST (Binary Search Tree) built from the given
       iterator ITER  of N items. ITER will output the items in sorted order,
       and ITER will contain objects that will be the item of each BSTNode. */
    private BSTNode<T> sortedIterToTree(Iterator<T> iter, int N) {
        // TODO: YOUR CODE HERE
        if(N == 0) {
            return null;
        }
        return sortedIterToTreeHelper(iter, N, (N-1)/2, new BSTNode<>(null));
    }

    private BSTNode<T> sortedIterToTreeHelper(Iterator<T> iter, int N, int start, BSTNode<T> node) {
        if(N == 1) {
            return new BSTNode<>(iter.next());
        } else if(N == 2) {
            node.left = new BSTNode<>(iter.next());
            node.item = iter.next();
            return node;
        } else if(N == 3) {
            node.left = new BSTNode<>(iter.next());
            node.item = iter.next();
            node.right = new BSTNode<>(iter.next());
            return node;
        } else {
            node.left = sortedIterToTreeHelper(iter, N/2, N/4, new BSTNode<>(null));
            node.item = iter.next();
            node.right = sortedIterToTreeHelper(iter, (N-1)/2, 3*N/4, new BSTNode<>(null));
            return node;
        }
    }

    /* Prints the tree represented by ROOT. */
    public void print() {
        print(root, 0);
    }

    private void print(BSTNode<T> node, int d) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < d; i++) {
            System.out.print("  ");
        }
        System.out.println(node.item);
        print(node.left, d + 1);
        print(node.right, d + 1);
    }

    class BSTNode<T> {
        T item;
        BSTNode<T> left;
        BSTNode<T> right;

        BSTNode(T item) {
            this.item = item;
        }
    }
}
