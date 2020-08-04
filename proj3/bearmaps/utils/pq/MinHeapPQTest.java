package bearmaps.utils.pq;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MinHeapPQTest {

    @Test
    public void SimpleTest() {
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
        h.changePriority('b', 1);
        System.out.println(h.toString());
        assertTrue(h.contains('b'));
    }

    @Test
    public void ExtremeTest() {
        double myHeapTime = 0;
        double naiveHeapTime = 0;
        for(int a = 0; a<1; a++) {
            MinHeapPQ<Double> h = new MinHeapPQ<>();
            NaiveMinPQ<Double> bot = new NaiveMinPQ<>();
            ArrayList<Double> collection = new ArrayList<>();
            for(int i = 0; i < 10000; i++) {
                double currVal = Math.random()*1000;
                double priorVal = Math.random()*10000;
                collection.add(currVal);
                Stopwatch watch1 = new Stopwatch();
                h.insert(currVal, priorVal);
                myHeapTime+=watch1.elapsedTime();
                Stopwatch watch2 = new Stopwatch();
                bot.insert(currVal, priorVal);
                naiveHeapTime+=watch2.elapsedTime();
            }

            for(int i = 0; i < 100; i++) {
                double decider = Math.random();
                if(decider<0.15) {
                    Stopwatch watch1 = new Stopwatch();
                    double hPoll = h.poll();
                    myHeapTime+=watch1.elapsedTime();
                    Stopwatch watch2 = new Stopwatch();
                    double naivePoll = bot.poll();
                    naiveHeapTime+=watch2.elapsedTime();
                    collection.remove(hPoll);
                    assertTrue(hPoll == naivePoll);
                } else if(decider<.40) {
                    double rand = Math.random()*1000;
                    Stopwatch watch1 = new Stopwatch();
                    boolean hContains = h.contains(rand);
                    myHeapTime+=watch1.elapsedTime();
                    Stopwatch watch2 = new Stopwatch();
                    boolean naiveContains = bot.contains(rand);
                    naiveHeapTime+=watch2.elapsedTime();
                    assertTrue(hContains == naiveContains);
                } else if(decider<.90) {
                    double rand = Math.random()*collection.size();
                    double randPrior = Math.random()*10000;
                    double randVal = collection.get((int) rand);
                    Stopwatch watch1 = new Stopwatch();
                    h.changePriority(randVal, randPrior);
                    myHeapTime+=watch1.elapsedTime();
                    Stopwatch watch2 = new Stopwatch();
                    bot.changePriority(randVal, randPrior);
                    naiveHeapTime+=watch2.elapsedTime();
                } else {
                    assertTrue(h.size()==bot.size());
                }

            }
        }
        System.out.println("My time: "+myHeapTime);
        System.out.println("Naive time: "+naiveHeapTime);
    }
}