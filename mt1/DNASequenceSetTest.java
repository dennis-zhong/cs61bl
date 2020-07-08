import org.junit.Test;
import static org.junit.Assert.*;

public class DNASequenceSetTest {
    @Test
    public void testSingleBase() {
        DNASequenceSet set = new DNASequenceSet();
        set.add(new int[]{0});
        assertTrue(set.contains(new int[]{0}));
        assertFalse(set.contains(new int[]{1}));
    }

    @Test
    public void testAllSameTwo() {
        DNASequenceSet set = new DNASequenceSet();
        set.add(new int[]{0,0});
        assertTrue(set.contains(new int[]{0,0}));
        assertFalse(set.contains(new int[]{0}));
    }

    @Test
    public void testMultipleOverlapping1() {
        DNASequenceSet set = new DNASequenceSet();
        set.add(new int[]{0,1});
        assertFalse(set.contains(new int[]{0}));
        assertTrue(set.contains(new int[]{0,1}));

        set.add(new int[]{3,2,1,0});
        assertFalse(set.contains(new int[]{0}));
        assertTrue(set.contains(new int[]{0,1}));
        assertFalse(set.contains(new int[]{3}));
        assertFalse(set.contains(new int[]{3,2}));
        assertFalse(set.contains(new int[]{3,2,1}));
        assertTrue(set.contains(new int[]{3,2,1,0}));
    }
}