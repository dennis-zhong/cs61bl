import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * A DLList is a list of integers. Like SLList, it also hides the terrible
 * truth of the nakedness within, but with a few additional optimizations.
 */
public class DLList<Item> implements Iterable<Item> {

    public static void main(String[] args) {
        LinkedList<Integer> a = new LinkedList<>();
        a.addLast(1);
        a.addLast(2);
        a.addLast(3);
        Iterator<Integer> b = a.iterator();
        b.next();
        b.next();
        b.next();
        System.out.println(b.hasNext());
        System.out.println(b.next());
        System.out.println(b.hasNext());
    }
    private class Node {
        public Node prev;
        public Item item;
        public Node next;

        public Node(Item i, Node p, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    /* The first item (if it exists) is at sentinel.next. */
    private Node sentinel;
    private int size;

    /** Creates an empty DLList. */
    public DLList() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    /** Returns a DLList consisting of the given values. */
    public static <Item> DLList<Item> of(Item... values) {
        DLList<Item> list = new DLList<>();
        for (Item value : values) {
            list.addLast(value);
        }
        return list;
    }

    /** Returns the size of the list. */
    public int size() {
        return size;
    }

    /** Adds item to the back of the list. */
    public void addLast(Item item) {
        Node n = new Node(item, sentinel.prev, sentinel);
        n.next.prev = n;
        n.prev.next = n;
        size += 1;
    }

    @Override
    public String toString() {
        String result = "";
        Node p = sentinel.next;
        boolean first = true;
        while (p != sentinel) {
            if (first) {
                result += p.item.toString();
                first = false;
            } else {
                result += " " + p.item.toString();
            }
            p = p.next;
        }
        return result;
    }

    /** Returns whether this and the given list or object are equal. */
    public boolean equals(Object o) {
        DLList other = (DLList) o;
        if (size() != other.size()) {
            return false;
        }
        Node op = other.sentinel.next;
        for (Node p = sentinel.next; p != sentinel; p = p.next) {
            if (!(p.item.equals(op.item))) {
                return false;
            }
            op = op.next;
        }
        return true;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DLListIterator();
    }

    private class DLListIterator implements Iterator<Item> {

        int counter = 0;
        Node pointer = sentinel;
        final int modcheck = size;

        @Override
        public boolean hasNext() {
            if (counter < size()) {
                return true;
            }
            return false;
        }

        @Override
        public Item next() {
            if(size() != modcheck) {
                throw new ConcurrentModificationException();
            }
            pointer = pointer.next;
            counter++;
            return pointer.item;
        }
    }
}
