public class RedBlackTree<T extends Comparable<T>> {

    /* Root of the tree. */
    RBTreeNode<T> root;

    /* Creates an empty RedBlackTree. */
    public RedBlackTree() {
        root = null;
    }

    /* Creates a RedBlackTree from a given BTree (2-3-4) TREE. */
    public RedBlackTree(BTree<T> tree) {
        Node<T> btreeRoot = tree.root;
        root = buildRedBlackTree(btreeRoot);
    }

    /* Builds a RedBlackTree that has isometry with given 2-3-4 tree rooted at
       given node R, and returns the root node. */
    RBTreeNode<T> buildRedBlackTree(Node<T> r) {
        if (r == null) {
            return null;
        }

        if (r.getItemCount() == 1) {//just becomes a black node
            // TODO: Replace with code to create a 2 node equivalent
            RBTreeNode<T> root = new RBTreeNode<T>(true, r.getItemAt(0));
            root.left = buildRedBlackTree(r.getChildAt(0));
            root.right = buildRedBlackTree(r.getChildAt(1));
            return root;
        } else if (r.getItemCount() == 2) {
            // TODO: Replace with code to create a 3 node equivalent
            RBTreeNode<T> root = new RBTreeNode<T>(true, r.getItemAt(1));
            RBTreeNode<T> root2 = new RBTreeNode<T>(false, r.getItemAt(0));
            root.left = root2;
            root.left.left = buildRedBlackTree(r.getChildAt(0));
            root.left.right = buildRedBlackTree(r.getChildAt(1));
            root.right = buildRedBlackTree(r.getChildAt(2));
            return root;
        } else {
            // TODO: Replace with code to create a 4 node equivalent
            RBTreeNode<T> root = new RBTreeNode<T>(true, r.getItemAt(1));
            RBTreeNode<T> root2 = new RBTreeNode<T>(false, r.getItemAt(0));
            RBTreeNode<T> root3 = new RBTreeNode<T>(false, r.getItemAt(2));
            root.left = root2;
            root.right = root3;
            root.left.left = buildRedBlackTree(r.getChildAt(0));
            root.left.right = buildRedBlackTree(r.getChildAt(1));
            root.right.left = buildRedBlackTree(r.getChildAt(2));
            root.right.right = buildRedBlackTree(r.getChildAt(3));
            return root;
        }
    }

    /* Flips the color of NODE and its children. Assume that NODE has both left
       and right children. */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    /* Rotates the given node NODE to the right. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateRight(RBTreeNode<T> node) {
        // TODO: YOUR CODE HERE
        RBTreeNode<T> temp = node;
        RBTreeNode<T> temp2 = node.left.right;
        node = node.left;
        node.isBlack = temp.isBlack;
        node.right = temp;
        node.right.isBlack = false;
        node.right.left = temp2;
        return node;
    }

    /* Rotates the given node NODE to the left. Returns the new root node of
       this subtree. */
    RBTreeNode<T> rotateLeft(RBTreeNode<T> node) {
        // TODO: YOUR CODE HERE
        RBTreeNode<T> temp = node;
        RBTreeNode<T> temp2 = node.right.left;
        node = node.right;
        node.isBlack = temp.isBlack;
        node.left = temp;
        node.left.isBlack = false;
        node.left.right = temp2;
        return node;
    }

    public void insert(T item) {   
        root = insert(root, item);
        root.isBlack = true;    
    }

    private RBTreeNode<T> insert(RBTreeNode<T> node, T item) {
        // TODO: YOUR CODE HERE
        if(node == null) {
            return new RBTreeNode<>(false, item);
        } else if(item.compareTo(node.item)<0) {
            node.left = insert(node.left, item);
        } else {
            node.right = insert(node.right, item);
        }
        return restore(node, item);
    }

    private RBTreeNode<T> restore(RBTreeNode<T> node, T item) {
        try {
            if(node.left == null && node.right == null) {
                return node;
            } else if (node.isBlack && node.left == null
                    && item.equals(node.right.item)) {
                return rotateLeft(node);
            } else if (node.isBlack && node.left != null
                    && item.equals(node.right.item)) {
                flipColors(node);
                return node;
            } else if (node.isBlack && !node.left.isBlack
                    && item.equals(node.left.left.item)) {
                return restore(rotateRight(node), item);
            } else if (node.isBlack && !node.left.isBlack
                    && item.equals(node.left.right.item)) {
                return restore(rotateLeft(node), item);
            } else if (node.item.compareTo(item)<0){
                return restore(node.right, item);
            } else {
                return restore(node.left, item);
            }
        } catch (NullPointerException e) {
            return node;
        }
    }

    /* Returns whether the given node NODE is red. Null nodes (children of leaf
       nodes are automatically considered black. */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    static class RBTreeNode<T> {

        final T item;
        boolean isBlack;
        RBTreeNode<T> left;
        RBTreeNode<T> right;

        /* Creates a RBTreeNode with item ITEM and color depending on ISBLACK
           value. */
        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        /* Creates a RBTreeNode with item ITEM, color depending on ISBLACK
           value, left child LEFT, and right child RIGHT. */
        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left,
                   RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

}
