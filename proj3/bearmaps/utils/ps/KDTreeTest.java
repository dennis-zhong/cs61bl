package bearmaps.utils.ps;

import edu.princeton.cs.algs4.Stopwatch;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class KDTreeTest {

    @Test
    public void NaiveTest() {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        ret.getX(); // evaluates to 3.3
        ret.getY(); // evaluates to 4.4
    }

    @Test
    public void SimpleKDTest() {
        Point p1 = new Point(1.1, 2.2); // constructs a Point with x = 1.1, y = 2.2
        Point p2 = new Point(3.3, 4.4);
        Point p3 = new Point(-2.9, 4.2);
        Point p4 = new Point(-5, 5);
        Point p5 = new Point(Math.random(), Math.random());
        Point p6 = new Point(Math.random()*10, Math.random()*10);

        NaivePointSet nn = new NaivePointSet(List.of(p1, p2, p3, p4, p5, p6));
        Point ret = nn.nearest(3.0, 4.0); // returns p2
        ret.getX(); // evaluates to 3.3
        ret.getY();

        KDTree tree = new KDTree(List.of(p1, p2, p3, p4, p5, p6));
        Point treeN = tree.nearest(3.0, 4.0);
        tree.nearest(3.0, 4.0);
        assertEquals(tree.nearest(3.0, 4.0), ret);
    }

    @Test
    public void HarderKDTest() {
        ArrayList<Point> lst = new ArrayList<>();
        for(int i = 0; i<10; i++) {
            lst.add(new Point(Math.random()*100, Math.random()*100));
        }
        NaivePointSet nn = new NaivePointSet(lst);
        KDTree tree = new KDTree(lst);
        double x = Math.random()*100;
        double y = Math.random()*100;
        Point goal = nn.nearest(x, y);
        Point treeN = tree.nearest(x, y);
        tree.nearest(x, y);
        assertEquals(tree.nearest(x, y), goal);
    }

    @Test
    public void SuperHarderKDTest() {
        double totalRound1 = 0;
        double totalRound2 = 0;
        for(int a = 0; a < 10; a++) {
            ArrayList<Point> lst = new ArrayList<>();
            for(int i = 0; i < 100000; i++) {
                lst.add(new Point(Math.random() * 1000, Math.random() * 1000));
            }
            NaivePointSet nn = new NaivePointSet(lst);
            KDTree tree = new KDTree(lst);
            for(int b = 0; b<10000; b++) {
                double x = Math.random() * 1000;
                double y = Math.random() * 1000;
                Stopwatch watch1 = new Stopwatch();
                Point goal = nn.nearest(x, y);
                double round1 = watch1.elapsedTime();
                totalRound1+=round1;
                Stopwatch watch2 = new Stopwatch();
                Point treeN = tree.nearest(x, y);
                double round2 = watch2.elapsedTime();
                totalRound2+=round2;
                assertEquals(treeN, goal);
            }
        }
        System.out.println("Naive time: " +totalRound1+"\nTree time: "+totalRound2);
    }
}