import org.junit.Test;
import static org.junit.Assert.*;

/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    //my own tests
    @Test
    public void test() {
        LinkedListDeque<Integer> t = new LinkedListDeque<>();
        assertTrue(t.isEmpty());
        t.addFirst(new Integer(5));
        LinkedListDeque<Integer> t2 = new LinkedListDeque<>(5);
        t.printDeque();
        assertTrue(t2.equals(t));
        t.size();
        assertEquals(1, t.size());
        t2.addLast(new Integer(7));
        t2.printDeque();
        t2.addLast(new Integer(7));
        t2.printDeque();
        assertEquals(3, t2.size());
        t2.addLast(new Integer(3));
        t2.printDeque();
        t2.addLast(new Integer(9));
        t2.printDeque();
    }

    /** Adds a few things to the deque, checking isEmpty() and size() are correct,
     * finally printing the results. */
    @Test
    public void addIsEmptySizeTest() {
        System.out.println("Running add/isEmpty/Size test.");
        LinkedListDeque<String> lld1 = new LinkedListDeque<>();

        // Java will try to run the below code.
        // If there is a failure, it will jump to the finally block before erroring.
        // If all is successful, the finally block will also run afterwards.
        try {

            assertTrue(lld1.isEmpty());

            lld1.addFirst("front");
            assertEquals(1, lld1.size());
            assertFalse(lld1.isEmpty());

            lld1.addLast("middle");
            assertEquals(2, lld1.size());

            lld1.addLast("back");
            assertEquals(3, lld1.size());

        } finally {
            // The deque will be printed at the end of this test
            // or after the first point of failure.
            System.out.println("Printing out deque: ");
            lld1.printDeque();
        }
    }

    /** Adds an item, then removes an item, and ensures that deque is empty afterwards. */
    @Test
    public void addRemoveTest() {
        System.out.println("Running add/remove test.");
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

        try {
            assertTrue(lld1.isEmpty());

            lld1.addFirst(10);
            assertFalse(lld1.isEmpty());

            lld1.removeFirst();
            assertTrue(lld1.isEmpty());
        } finally {
            System.out.println("Printing out deque: ");
            lld1.printDeque();
        }
    }

    @Test
    public void removeTest() {
        System.out.println("Running remove test.");
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

        try {
            assertTrue(lld1.isEmpty());
            lld1.addFirst(5);
            lld1.addFirst(4);
            lld1.addFirst(3);
            lld1.addFirst(2);
            lld1.addFirst(1);
            assertFalse(lld1.isEmpty());
            lld1.printDeque();
            lld1.fill(new Integer(6), new Integer(7),
                    new Integer(8), new Integer(9));
            LinkedListDeque<Integer> lld2 = new LinkedListDeque<>();
            lld2.fill(new Integer(1), new Integer(2), new Integer(3), new Integer(4), new Integer(5), new Integer(6), new Integer(7), new Integer(8), new Integer(9));
            assertTrue(lld1.equals(lld2));
            lld1.removeLast();
            lld1.removeLast();
            lld1.removeLast();
            assertTrue(lld1.size()==6);
            lld1.removeFirst();
            lld1.removeFirst();
        } finally {
            System.out.println("Printing out deque: ");
            lld1.printDeque();
        }
    }

    @Test
    public void getTest() {
        System.out.println("Running get test.");
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();

        try {
            assertTrue(lld1.isEmpty());
            lld1.addFirst(5);
            lld1.addFirst(4);
            lld1.addFirst(3);
            lld1.addFirst(2);
            lld1.addFirst(1);
            for(int i = 0; i < lld1.size(); i++) {
                System.out.println(lld1.get(i));
            }
        } finally {
            System.out.println("Printing out deque: ");
            lld1.printDeque();
        }
    }
}
