import java.util.*;

public class BTree<T extends Comparable<T>> {
    Node<T> root;

    static class Node<T> {
        private List<T> items;
        private List<Node<T>> children;

        private List<T> helper(List<T> lst) {
            int i;
            for (i = 0; i < items.size(); i++) {
                if (children.get(i) == null) {
                    continue;
                }
                lst.addAll(i, children.get(i).items);
            }
            if (i<children.size()) {
                lst.addAll(children.get(i).helper(children.get(i).items));
            }
            return lst;
        }

        Node(List<T> items, List<Node<T>> children) {
            this.items = items;
            this.children = children;
        }
    }

    public List<T> inorderTraversal() {
        if (root != null) {
            return root.helper(root.items);
        }
        return null;
    }
}