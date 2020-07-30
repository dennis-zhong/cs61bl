import org.junit.Test;
import static org.junit.Assert.*;

public class PandaTest {
    @Test
    public void testComparing() {
        int[] arr = {20, 25, 70};
        PandaBear bear = new PandaBear(arr, 5);
        int[] arr2 = {20, 40, 55, 65};
        PandaBear panda = new PandaBear(arr2, 5);
        assertTrue(bear.compareTo(panda) < 0);
        int[] arr3 = {5};
        PandaBear newBear = new PandaBear(arr3, 20);
        assertTrue(newBear.compareTo(panda) > 0);
    }
    @Test
    public void simpleTest() {
        int[] pandaArr = {20, 25, 70};
        PandaBear panda = new PandaBear(pandaArr,5);
        PandaTree tree = new PandaTree(panda);
        int[] bearArr = {20, 40, 55, 65};
        PandaBear bear = new PandaBear(bearArr,5);
        tree.add(bear);
        assertEquals(tree.mostAtRisk(), panda);
        assertEquals(tree.leastAtRisk(), bear);
        int[] newArr = {5};
        PandaBear newBear = new PandaBear(newArr,20);
        tree.add(newBear);
        assertEquals(tree.mostAtRisk(), panda);
        assertEquals(tree.leastAtRisk(), newBear);
    }
}