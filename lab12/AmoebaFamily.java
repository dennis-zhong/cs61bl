import java.util.*;

/* An AmoebaFamily is a tree, where nodes are Amoebas, each of which can have
   any number of children. */
public class AmoebaFamily implements Iterable<AmoebaFamily.Amoeba> {

    /* ROOT is the root amoeba of this AmoebaFamily */
    private Amoeba root = null;

    /* Creates an AmoebaFamily, where the first Amoeba's name is NAME. */
    public AmoebaFamily(String name) {
        root = new Amoeba(name, null);
    }

    /* Adds a new Amoeba with CHILDNAME to this AmoebaFamily as the youngest
       child of the Amoeba named PARENTNAME. This AmoebaFamily must contain an
       Amoeba named PARENTNAME. */
    public void addChild(String parentName, String childName) {
        if (root != null) {
            root.addChildHelper(parentName, childName);
        }
    }

    /* Prints the name of all Amoebas in this AmoebaFamily in preorder, with
       the ROOT Amoeba printed first. Each Amoeba should be indented four spaces
       more than its parent. */
    public void print() {
        if(root != null) {
            root.print(root, 0);
        }
    }

    /* Returns the length of the longest name in this AmoebaFamily. */
    public int longestNameLength() {
        if (root != null) {
            return root.longestNameLengthHelper();
        }
        return 0;
    }

    /* Returns the longest name in this AmoebaFamily. */
    public String longestName() {
        // TODO: YOUR CODE HERE
        return "";
    }

    /* Returns an Iterator for this AmoebaFamily. */
    public Iterator<Amoeba> iterator() {
        return new AmoebaBFSIterator();
    }

    /* Creates a new AmoebaFamily and prints it out. */
    public static void main(String[] args) {
        AmoebaFamily family = new AmoebaFamily("Amos McCoy");
        family.addChild("Amos McCoy", "mom/dad");
        family.addChild("Amos McCoy", "auntie");
        family.addChild("mom/dad", "me");
        family.addChild("mom/dad", "Fred");
        family.addChild("mom/dad", "Wilma");
        family.addChild("me", "Mike");
        family.addChild("me", "Homer");
        family.addChild("me", "Marge");
        family.addChild("Mike", "Bart");
        family.addChild("Mike", "Lisa");
        family.addChild("Marge", "Bill");
        family.addChild("Marge", "Hilary");
        System.out.println("Here's the family:");
        family.print();

        for(Amoeba a: family) {
            System.out.println(a.toString());
        }
    }

    /* An Amoeba is a node of an AmoebaFamily. */
    public static class Amoeba {

        private String name;
        private Amoeba parent;
        private ArrayList<Amoeba> children;

        public Amoeba(String name, Amoeba parent) {
            this.name = name;
            this.parent = parent;
            this.children = new ArrayList<Amoeba>();
        }

        public String toString() {
            return name;
        }

        public Amoeba getParent() {
            return parent;
        }

        public ArrayList<Amoeba> getChildren() {
            return children;
        }

        /* Adds child with name CHILDNAME to an Amoeba with name PARENTNAME. */
        public void addChildHelper(String parentName, String childName) {
            if (name.equals(parentName)) {
                Amoeba child = new Amoeba(childName, this);
                children.add(child);
            } else {
                for (Amoeba a : children) {
                    a.addChildHelper(parentName, childName);
                }
            }
        }

        /* Returns the length of the longest name between this Amoeba and its
           children. */
        public int longestNameLengthHelper() {
            int maxLengthSeen = name.length();
            for (Amoeba a : children) {
                maxLengthSeen = Math.max(maxLengthSeen,
                                         a.longestNameLengthHelper());
            }
            return maxLengthSeen;
        }

        // TODO: ADD HELPER FUNCTIONS HERE
        public void print(Amoeba root, int level) {
            for (int i = 0; i < level * 4; i++) {
                System.out.print(" ");
            }
            if(root.children.isEmpty()) {
                System.out.println(root.name);
            } else {
                System.out.println(root.name);
                level++;
                for (Amoeba a : root.children) {
                    a.print(a, level);
                }
            }
        }

    }

    /* An Iterator class for the AmoebaFamily, running a DFS traversal on the
       AmoebaFamily. Complete enumeration of a family of N Amoebas should take
       O(N) operations. */
    public class AmoebaDFSIterator implements Iterator<Amoeba> {

        // TODO: IMPLEMENT THE CLASS HERE

        private Stack<Amoeba> fringe = new Stack<>();

        /* AmoebaDFSIterator constructor. Sets up all of the initial information
           for the AmoebaDFSIterator. */
        public AmoebaDFSIterator() {
            fringe.push(root);
        }

        /* Returns true if there is a next element to return. */
        public boolean hasNext() {
            if(fringe.isEmpty()) {
                return false;
            }
            return true;
        }

        /* Returns the next element. */
        public Amoeba next() {
            Amoeba a = fringe.pop();
            ArrayList<Amoeba> lst = a.getChildren();
            for(int i = lst.size()-1; i >=0; i--) {
                fringe.push(lst.get(i));
            }
            return a;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }//1 5 9 11 10 6 8 7 2 4 3

    /* An Iterator class for the AmoebaFamily, running a BFS traversal on the
       AmoebaFamily. Complete enumeration of a family of N Amoebas should take
       O(N) operations. */
    public class AmoebaBFSIterator implements Iterator<Amoeba> {

        // TODO: IMPLEMENT THE CLASS HERE
        LinkedList<Amoeba> queue = new LinkedList<>();


        /* AmoebaBFSIterator constructor. Sets up all of the initial information
           for the AmoebaBFSIterator. */
        public AmoebaBFSIterator() {
            queue.add(root);
        }

        /* Returns true if there is a next element to return. */
        public boolean hasNext() {
            if(queue.isEmpty()) {
                return false;
            }
            return true;
        }

        /* Returns the next element. */
        public Amoeba next() {
            Amoeba a = queue.removeFirst();
            for(Amoeba am: a.getChildren()) {
                queue.add(am);
            }
            return a;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
