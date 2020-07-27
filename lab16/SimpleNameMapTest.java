import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleNameMapTest {

    @Test
    public void SimpleTest() {
        SimpleNameMap map = new SimpleNameMap();
        map.put("Bob", "Joe");
        assertTrue(map.containsKey("Bob"));
        assertFalse(map.containsKey("Joe"));
        map.put("Bob", "Moe");
        assertTrue(map.get("Bob").equals("Moe"));
        map.put("Dennis", "Zhong");
        map.put("Small", "Man");
        assertTrue(map.size() == 3);
        map.remove("Yoink");
        assertTrue(map.size() == 3);
        assertEquals(map.remove("Bob"), "Moe");
        assertTrue(map.size() == 2);
        System.out.println(map.loadFactor());
        map.put("Moe", "Moe");
        map.put("Sto", "Toe");
        map.put("Toe", "Toe");
        map.put("Beer", "Teer");
        map.put("Kai", "Ley");
        map.put("And", "Tor");
        System.out.println(map.loadFactor());
    }
}