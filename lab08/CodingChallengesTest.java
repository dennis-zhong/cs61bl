import org.junit.Test;
import static org.junit.Assert.*;

public class CodingChallengesTest {

    @Test
    public void missingNumberTest() {
        int[] arr = {0, 1, 2, 4, 5};
        assertEquals(CodingChallenges.missingNumber(arr), 3);
        int[] arr2 = {0, 1, 2, 3, 4, 5};
        assertEquals(CodingChallenges.missingNumber(arr2), 6);
        int[] arr3 = {1, 2, 3, 4, 5, 6};
        assertEquals(CodingChallenges.missingNumber(arr3), 0);
    }

    @Test
    public void sumToTest() {
        int[] arr = {0, 1, 2, 4, 5};
        assertFalse(CodingChallenges.sumTo(arr, 10));
        int[] arr2 = {0, 1, 2, 3, 4, 5};
        assertFalse(CodingChallenges.sumTo(arr2, 0));
        int[] arr3 = {1, 2, 3, 4, 5, 6};
        assertTrue(CodingChallenges.sumTo(arr3, 6));
    }

    @Test
    public void isPermutation() {
        String s = "big ups";
        String s2 = "sup big";
        assertTrue(CodingChallenges.isPermutation(s, s2));
        s2 = "beep be";
        assertFalse(CodingChallenges.isPermutation(s, s2));
    }
}