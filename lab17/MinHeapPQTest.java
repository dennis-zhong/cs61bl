import org.junit.Test;
import static org.junit.Assert.*;

public class MinHeapPQTest {

    @Test
    public void test1() {
        MinHeapPQ<Character> h = new MinHeapPQ<>();
        h.insert('f', 100);
        h.insert('h', 1);
        h.insert('d', 10);
        h.insert('b', 1000);
        h.insert('c', 10000);
        System.out.println(h.toString());
        h.poll();
        h.poll();
        System.out.println(h.toString());
    }
}
