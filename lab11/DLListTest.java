import org.junit.Test;

import java.util.ConcurrentModificationException;

import static org.junit.Assert.*;

public class DLListTest {

    @Test
    public void testFor() {
        DLList<Integer> a = new DLList<>();
        a.addLast(1);
        a.addLast(2);
        a.addLast(3);
        int count = 0;
        for (Integer i : a) {
            count += i;
            System.out.println(i);
        }
        assertEquals(6, count);
    }

    @Test
    public void testException() {
        DLList<Integer> a = new DLList<>();
        a.addLast(1);
        a.addLast(2);
        a.addLast(3);
        int count = 0;
        try {
            for (Integer i : a) {
                count += i;
                a.addLast(0);
                System.out.println(i);
            }
        } catch (ConcurrentModificationException e) {
            System.out.println("caught change");
        }
        assertEquals(1, count);
    }
}
