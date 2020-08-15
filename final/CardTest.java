import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class CardTest {
    @Test
    public void basicTest() {
        Pattern pattern = Card.pattern();

        Matcher match = pattern.matcher("2 of spades");
        assertTrue(match.matches());
        match.reset();

        match = pattern.matcher("quEen of hearts");
        assertTrue(match.matches());
        match.reset();

        match = pattern.matcher("one of clubs");
        assertFalse(match.matches());
        match.reset();

        match = pattern.matcher("jack ofdiamonds");
        assertFalse(match.matches());
        match.reset();
    }
    public class A {
        public int num;

        public A(int n) {
            num = n;
        }

        public void print() {
            System.out.println("A");
        }
    }
    public class B extends A {
        public int num;

        public B(int n) {
            super(n);
        }
    }
    public class C extends B {
        public C(int n) {
            super(n);
        }

        public void print(String s) {
            System.out.println(s);
        }
    }

    @Test
    public void test() {
        C see = new C(5);
        see.print();
    }
}