import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DBTableTest {
    @Test
    public void getWhitelisted() {
        List<User> users = Arrays.asList(
                new User(2, "Matt", ""),
                new User(4, "Zoe", ""),
                new User(5, "Alex", ""),
                new User(1, "Shreya", ""),
                new User(1, "Connor", ""));
        List<String> whiteListedNames = Arrays.asList("Connor", "Shreya");
        DBTable<User> t = new DBTable<>(users);
        List<User> whiteListedUsers = t.getWhitelisted(User::getName, whiteListedNames);
        assertEquals(whiteListedUsers.get(0).getName(), "Shreya");
        assertEquals(whiteListedUsers.get(1).getName(), "Connor");
    }

    @Test
    public void getSubtableOf() {
        List<User> users = Arrays.asList(
                new User(2, "Matt", ""),
                new User(4, "Zoe", ""),
                new User(5, "Alex", ""),
                new User(1, "Shreya", ""),
                new User(1, "Connor", ""));
        DBTable<User> t = new DBTable<>(users);
        DBTable<String> names = t.getSubtableOf(User::getName);
        names.getEntries().forEach(System.out::println);
    }
}