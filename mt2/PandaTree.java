public class PandaTree {
    private PandaNode root;
    private PandaNode leftMost;
    private PandaNode rightMost;
    // TODO?

    public PandaTree(PandaBear bear) {
        this.root = new PandaNode(bear, null, null);
        leftMost = root;
        rightMost = root;
    }

    // returns the most at risk bear
    public PandaBear mostAtRisk() {
        return leftMost.item;
    }

    // returns the least at risk bear
    public PandaBear leastAtRisk() {
        return rightMost.item;
    }

    // adds bear to tree if and only if bear not already in tree
    public void add(PandaBear bear) {
        add(bear, this.root);
        leftMost = findLeftMost(root);
        rightMost = findRightMost(root);
    }

    private PandaNode findLeftMost(PandaNode node) {
        if(node.left == null) {
            return node;
        } else {
            return findLeftMost(node.left);
        }
    }

    private PandaNode findRightMost(PandaNode node) {
        if(node.right == null) {
            return node;
        } else {
            return findRightMost(node.right);
        }
    }

    private PandaNode add(PandaBear bear, PandaNode node) {
        if (node == null) {
            return new PandaNode(bear, null, null);
        }
        if (node.item.compareTo(bear) > 0) {
            node.left = add(bear, node.left);
        } else if (node.item.compareTo(bear) < 0) {
            node.right = add(bear, node.right);
        }
        return node;
    }

    private class PandaNode {
        private PandaBear item;
        private PandaNode left;
        private PandaNode right;
        private PandaNode(PandaBear bear, PandaNode l, PandaNode r) {
            this.item = bear;
            this.left = l;
            this.right = r;
        }
    }

}