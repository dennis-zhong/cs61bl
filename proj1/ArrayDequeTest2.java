import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest2 {

    @Test
    public void add_remove_IsEmpty_SizeTest() {
        System.out.println("Running add/remove/isEmpty/Size test.");

        ArrayDeque<String> lld1 = new ArrayDeque<>();

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

            lld1.removeLast();

        } finally {
            // The deque will be printed at the end of this test
            // or after the first point of failure.
            System.out.println("Printing out deque: ");
            System.out.println(lld1.size());
            System.out.println(lld1.get(4));
            lld1.printDeque();
        }

    }

    @Test
    public void  resizeTest() {
        System.out.println("Running resize test.");

        ArrayDeque<Integer> lld1 = new ArrayDeque<>();

        // Java will try to run the below code.
        // If there is a failure, it will jump to the finally block before erroring.
        // If all is successful, the finally block will also run afterwards.
        try {
            lld1.addFirst(0);
            lld1.removeFirst();
            lld1.removeLast();
            lld1.addFirst(1);
            lld1.addFirst(0);
            for (int i = 2; i < 8; i++){
                lld1.addLast(i);
            }

            for (int i = 8; i < 16; i++){
                lld1.addLast(i);
            }

            for (int i = 0 ; i < 13; i++){
                lld1.removeLast();
            }

        } finally {
            // The deque will be printed at the end of this test
            // or after the first point of failure.
            System.out.println("Printing out deque: ");
            System.out.println(lld1.size());
            System.out.println(lld1.get(2));
            lld1.printDeque();
        }

    }
}
