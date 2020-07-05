import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    public void addAndExpandTest() {
        Deque<Integer> arrD = new ArrayDeque<>();
        arrD.printDeque();
        System.out.println("printed empty");
        arrD.addFirst(4);
        assertTrue(arrD.size() == 1);
        arrD.addFirst(3);
        arrD.addFirst(2);
        arrD.addFirst(1);
        arrD.addLast(5);
        arrD.addLast(6);
        arrD.addLast(7);
        arrD.addLast(8);
        arrD.printDeque();
        for(int i = 9; i<=16; i++) {//should expand correctly
            arrD.addLast(i);
        }
        arrD.printDeque();
        assertTrue(arrD.size() == 16);
        for(int i = 0; i>=-15; i--) {//should expand again
            arrD.addFirst(i);
        }
        arrD.printDeque();
        for(int i = -16; i>=-100; i--) {//expand
            arrD.addFirst(i);
        }
        int j = 0;
        for(int i = -100; i<17; i++) {
            assertTrue(i==arrD.get(j));//check if everything inside is right
            j++;
        }
        for(int i = 17; i<=10000; i++) {
            arrD.addLast(i);//expand alot
        }
        j = 0;
        for(int i = -100; i<10000; i++) {
            assertTrue(i==arrD.get(j));//check if expand worked
            j++;
        }
        arrD.printDeque();//if expand didn't work there should be some null pointer exceptions
    }

    @Test
    public void removeAndDownsizeTest() {
        Deque<Integer> arrD = new ArrayDeque<>();
        System.out.println(arrD.get(0));
        for(int i = 9; i<=16; i++) {
            arrD.addLast(i);
        }
        for(int i = 8; i>=-15; i--) {
            arrD.addFirst(i);
        }
        int j = 0;
        for(int i = -15; i<17; i++) {
            assertTrue(i==arrD.get(j));
            j++;
        }
        for(int x = 0; x<10; x++) {
            arrD.removeFirst();
        }
        for(int i = -6; i>=-10; i--) {//check if nexts are messedup from removefirst
            arrD.addFirst(i);
        }
        for(int x = 0; x<10; x++) {
            arrD.removeLast();
        }
        for(int i = 7; i<=100; i++) {//check if nexts are messed up from removelast
            arrD.addLast(i);
        }
        //arrD.printDeque(); //extend arr to preparing for downsizing
        j = 0;
        for(int i = -10; i<101; i++) {//check if indexs are in right order
            assertTrue(i==arrD.get(j));
            j++;
        }
        for(int x = 0; x<47; x++) {
            arrD.removeLast();
        }
        arrD.printDeque();//check if it downsizes, it prob shouldnt
        for(int x = 0; x<32; x++) {
            arrD.removeLast();
        }
        arrD.printDeque(); //now it should downsize
        for(int x = 0; x<31; x++) {
            arrD.removeLast();
        }
        arrD.printDeque();//shouldn't downsize past 8
        ArrayDeque<Integer> arrD2 = new ArrayDeque<>();
        for(int i = 1; i<=100; i++) {
            arrD2.addLast(i);
        }
        for(int i = 1; i<=50; i++) {
            arrD2.removeFirst();
        }
        for(int i = 101; i<=150; i++) {
            arrD2.addLast(i);
        }
        for(int i = 1; i<=30; i++) {
            arrD2.removeFirst();
        }
        for(int i = 1; i<=50; i++) {
            arrD2.removeLast();
        }
        arrD2.printDeque(); //checking downsize when front>end
        for(int i = 101; i<=209; i++) {//check if expand when front>end works
            arrD2.addLast(i);
        }
        arrD2.printDeque();
    }

    @Test
    public void fuzzTest() {
        LinkedListDeque<Double> lld = new LinkedListDeque<>();
        ArrayDeque<Double> arrD = new ArrayDeque<>();
        Random rd = new Random();
        double rand;
        double rand2;
        double rand3;
        for(int i = 0; i < 1000; i++) {
            rand = rd.nextDouble();
            rand2 = rd.nextDouble();
            if(rand<.3){
                if(rand2<.5) {
                    lld.removeFirst();
                    arrD.removeFirst();
                } else {
                    lld.removeLast();
                    arrD.removeLast();
                }
            } else {
                rand3 = rd.nextDouble();
                if(rand2<.5) {
                    lld.addFirst(rand3);
                    arrD.addFirst(rand3);
                } else {
                    lld.addLast(rand3);
                    arrD.addLast(rand3);
                }
            }
        }
        for(int i = 0; i < 2000; i++) {
            assertEquals(arrD.get(i), lld.get(i));
        }
        arrD.printDeque();
        lld.printDeque();
    }
}
