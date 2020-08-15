import org.junit.Test;

import static org.junit.Assert.*;

public class MeasurementTest {
    @Test
    public void constructorsTest() {
        // TODO: stub for first test
        Measurement x = new Measurement();
        assertTrue(x.getFeet()==0);
        assertTrue(x.getInches()==0);
        Measurement y = new Measurement(5);
        assertTrue(y.getFeet()==5);
        assertTrue(y.getInches()==0);
        Measurement z = new Measurement(5, 9);
        assertTrue(z.getFeet()==5);
        assertTrue(z.getInches()==9);
    }

    // TODO: Add additional JUnit tests for Measurement.java here.
    @Test
    public void plusTest() {
        Measurement x = new Measurement(2, 7);
        Measurement y = new Measurement(5, 9);
        Measurement plus = x.plus(y);
        assertTrue(plus.getFeet()==8);
        assertTrue(plus.getInches()==4);
        Measurement w = new Measurement(2, 7);
        Measurement z = new Measurement(5, 2);
        Measurement plus2 = w.plus(z);
        assertTrue(plus2.getFeet()==7);
        assertTrue(plus2.getInches()==9);
    }

    @Test
    public void minusTest() {
        Measurement x = new Measurement(2, 7);
        Measurement y = new Measurement(5, 9);
        Measurement minus = y.minus(x);
        assertTrue(minus.getFeet()==3);
        assertTrue(minus.getInches()==2);
        Measurement w = new Measurement(2, 7);
        Measurement z = new Measurement(5, 2);
        Measurement minus2 = z.minus(w);
        assertTrue(minus2.getFeet()==2);
        assertTrue(minus2.getInches()==7);
    }

    @Test
    public void multipleTest() {
        Measurement x = new Measurement(2, 7);
        Measurement y = new Measurement(0, 9);
        Measurement multipleX = x.multiple(3);
        assertTrue(multipleX.getFeet()==7);
        assertTrue(multipleX.getInches()==9);
        Measurement multipleY = y.multiple(3);
        assertTrue(multipleY.getFeet()==2);
        assertTrue(multipleY.getInches()==3);
    }

    @Test
    public void stringTest() {
        Measurement x = new Measurement(2, 7);
        Measurement y = new Measurement(5);
        assertTrue(x.toString().equals("2\'7\""));
        assertTrue(y.toString().equals("5\'0\""));
    }
}