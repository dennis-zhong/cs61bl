import com.sun.source.tree.Tree;

public class BinaryTree<T> {

    TreeNode<T> root;

    public BinaryTree() {
        root = null;
    }

    public BinaryTree(TreeNode<T> t) {
        root = t;
    }

    public TreeNode<T> getRoot() {
        return root;
    }

    /* Returns the height of the tree. */
    public int height() {
        // TODO: YOUR CODE HERE
        if (this.root == null) {
            return 0;
        } else {
            return 1 + Math.max(root.left.height(), root.right.height());
        }
    }

    /* Returns true if the tree's left and right children are the same height
       and are themselves completely balanced. */
    public boolean isCompletelyBalanced() {
        // TODO: YOUR CODE HERE
        if(root == null) {
            return true;
        } else {
            return root.left.height() == root.right.height() && root.left.isCompletelyBalanced() && root.right.isCompletelyBalanced();
        }
    }

    /* Returns a BinaryTree representing the Fibonacci calculation for N. */
    public static BinaryTree<Integer> fibTree(int N) {
        BinaryTree<Integer> result = new BinaryTree<Integer>(new TreeNode<Integer>(0));
        if(N==0) {
            result.root.setItem(0);
        } else if(N==1) {
            result.root.setItem(1);
        } else {
            result.root.setLeft(TreeNode.fibTree(N-1));
            result.root.setRight(TreeNode.fibTree(N-2));
            result.root.setItem(result.root.getLeft().getItem()+result.root.getRight().getItem());
        }
        return result;
    }

    /* Print the values in the tree in preorder: root value first, then values
       in the left subtree (in preorder), then values in the right subtree
       (in preorder). */
    public void printPreorder() {
        if (root == null) {
            System.out.println("(empty tree)");
        } else {
            root.printPreorder();
            System.out.println();
        }
    }

    /* Print the values in the tree in inorder: values in the left subtree
       first (in inorder), then the root value, then values in the first
       subtree (in inorder). */
    public void printInorder() {
        if (root == null) {
            System.out.println("(empty tree)");
        } else {
            root.printInorder();
            System.out.println();
        }
    }

    /* Prints out the contents of a BinaryTree with a description in both
       preorder and inorder. */
    private static void print(BinaryTree t, String description) {
        System.out.println(description + " in preorder");
        t.printPreorder();
        System.out.println(description + " in inorder");
        t.printInorder();
        System.out.println();
    }

    /* Fills this BinaryTree with values a, b, and c. DO NOT MODIFY. */
    public void sampleTree1() {
        root = new TreeNode("a", new TreeNode("b"), new TreeNode("c"));
    }

    /* Fills this BinaryTree with values a, b, and c, d, e, f. DO NOT MODIFY. */
    public void sampleTree2() {
        root = new TreeNode("a",
                  new TreeNode("b", new TreeNode("d", new TreeNode("e"),
                  new TreeNode("f")), null), new TreeNode("c"));
    }

    /* Fills this BinaryTree with the values a, b, c, d, e, f. DO NOT MODIFY. */
    public void sampleTree3() {
        root = new TreeNode("a", new TreeNode("b"), new TreeNode("c",
               new TreeNode("d", new TreeNode("e"), new TreeNode("f")), null));
    }

    /* Fills this BinaryTree with the same leaf TreeNode. DO NOT MODIFY. */
    public void sampleTree4() {
        TreeNode leafNode = new TreeNode("c");
        root = new TreeNode("a", new TreeNode("b", leafNode, leafNode),
                                 new TreeNode("d", leafNode, leafNode));
    }

    /* Creates two BinaryTrees and prints them out in inorder. */
    public static void main(String[] args) {
        BinaryTree t;
        t = new BinaryTree();
        print(t, "the empty tree");
        System.out.println(t.height());
        System.out.println(t.isCompletelyBalanced());

        t.sampleTree1();
        print(t, "sample tree 1");
        System.out.println(t.height());
        System.out.println(t.isCompletelyBalanced());

        t.sampleTree2();
        print(t, "sample tree 2");
        System.out.println(t.height());
        System.out.println(t.isCompletelyBalanced());

        t.sampleTree4();
        print(t, "sample tree 4");
        System.out.println(t.height());
        System.out.println(t.isCompletelyBalanced());

        print(fibTree(5), "fib tree");
    }
    //A B C D E
    //B D E C A
    //B A D C E

    //A C
    //D E

    /* Note: this class is public in this lab for testing purposes. However,
       in professional settings as well as the rest of your labs and projects,
       we recommend that you keep your inner classes private. */
    static class TreeNode<T> {

        private T item;
        private TreeNode left;
        private TreeNode right;

        TreeNode(T obj) {
            item = obj;
            left = null;
            right = null;
        }

        TreeNode(T obj, TreeNode<T> left, TreeNode<T> right) {
            item = obj;
            this.left = left;
            this.right = right;
        }

        public T getItem() {
            return item;
        }

        public TreeNode<T> getLeft() {
            return left;
        }

        public TreeNode<T> getRight() {
            return right;
        }

        void setItem(T item) {
            this.item = item;
        }

        void setLeft(TreeNode<T> left) {
            this.left = left;
        }

        void setRight(TreeNode<T> right) {
            this.right = right;
        }

        private void printPreorder() {
            System.out.print(item + " ");
            if (left != null) {
                left.printPreorder();
            }
            if (right != null) {
                right.printPreorder();
            }
        }

        private void printInorder() {
            if (left != null) {
                left.printInorder();
            }
            System.out.print(item + " ");
            if (right != null) {
                right.printInorder();
            }
        }

        private int height() {
            if (left == null && right == null) {
                return 1;
            } else if (left != null && right == null) {
                return 1 + left.height();
            } else if (left == null && right != null) {
                return 1 + right.height();
            } else {
                return 1 + Math.max(left.height(), right.height());
            }
        }

        private boolean isCompletelyBalanced() {
            if(left == right) {
                return true;
            } else {
                return left.height() == right.height() && left.isCompletelyBalanced()
                        && right.isCompletelyBalanced();
            }
        }

        private static TreeNode<Integer> fibTree(int n) {
            TreeNode<Integer> result = new TreeNode<>(0);
            if(n == 0) {
                return result;
            } else if(n == 1) {
                result.setItem(1);
                return result;
            } else {
                result.setLeft(fibTree(n-1));
                result.setRight(fibTree(n-2));
                result.setItem(result.getLeft().getItem()+result.getRight().getItem());;
            }
            return result;
        }
    }
}
