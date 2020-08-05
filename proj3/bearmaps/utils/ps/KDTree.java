package bearmaps.utils.ps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class KDTree implements PointSet {

    private KDNode root;
    //private HashSet<Point> copy = new HashSet<>();
    private Comparator<Point> compareX = (a, b) -> Double.compare(a.getX(), b.getX());
    private Comparator<Point> compareY = (a, b) -> Double.compare(a.getY(), b.getY());


    public KDTree(List<Point> points) {
        HashSet<Point> copy = new HashSet<>();
        /*for(Point point: points) {
            if(!copy.contains(point)) {
                copy.add(point);
            }
        }*/
        root = insert(root, points, 0);
    }

    private KDNode insert(KDNode curr, List<Point> points, int depth) {
        if(points.isEmpty()) {
            return curr;
        }
        /*
        points.sort(getComp(depth));
        if(contents.contains(points.get(points.size()/2))) {
            points.remove(points.size()/2);
        }
        int middle = points.size()/2;
        contents.add(points.get(middle));
        curr = new KDNode(points.get(middle));
        curr.left = insert(curr.left, points.subList(0, middle), depth+1);
        curr.right = insert(curr.right, points.subList(middle+1, points.size()), depth+1);*/
        points.sort(getComp(depth));
        int middle = points.size()/2;
        curr = new KDNode(points.get(middle));
        curr.left = insert(curr.left, points.subList(0, middle), depth+1);
        curr.right = insert(curr.right, points.subList(middle+1, points.size()), depth+1);
        return curr;
    }

    private Comparator<Point> getComp(int depth) {
        if(depth%2 == 0) {
            return compareX;
        } else {
            return compareY;
        }
    }

    @Override
    public Point nearest(double x, double y) {
        Point temp = new Point(x, y);
        return nearestHelper(root, root, temp, 0).item;
    }

    private KDNode nearestHelper(KDNode from, KDNode best, Point to, int depth) {
        if(from == null) {
            return best;
        }
        double dist = Point.distance(from.item, to);
        double bestDist = Point.distance(best.item, to);
        if(dist<bestDist) {
            best = from;
        }
        KDNode goodSide;
        KDNode badSide;
        if(getComp(depth).compare(to, from.item)<0) {
            goodSide = from.left;
            badSide = from.right;
        } else {
            goodSide = from.right;
            badSide = from.left;
        }
        best = nearestHelper(goodSide, best, to, depth+1);
        if(badSide != null) {
            /*if(depth%2==0) {
                prune = from.item.getX()<Point.distance(best, to)+to.getX();
            } else {
                prune = from.item.getY()<Point.distance(best, to)+to.getY();
            }
            Point leftBest = null;
            Point rightBest = null;
            if(getComp(depth).compare(to, badSide.item)<0 || prune) {
                leftBest = nearestHelper(badSide.left, Point.distance(to, badSide.item), badSide, to, depth+1);
            } else if(getComp(depth).compare(to, badSide.item)>0 || prune) {
                rightBest = nearestHelper(badSide.right, Point.distance(to, badSide.item), badSide, to, depth+1);
            }
            Point badBest = null;
            if(leftBest == null) {
                badBest = rightBest;
            } else if(rightBest == null) {
                badBest = leftBest;
            } else if(Point.distance(leftBest, to)<Point.distance(rightBest, to)) {
                badBest = leftBest;
            } else {
                badBest = rightBest;
            }
            if(Point.distance(badBest, to) < Point.distance(best, to)) {
                return badBest;
            }*/
            Point simPoint = null;
            if(depth%2 == 0) {
                simPoint = new Point(from.item.getX(), to.getY());
            } else {
                simPoint = new Point(to.getX(), from.item.getY());
            }
            if(Point.distance(simPoint, to)<Point.distance(best.item, to)) {
                best = nearestHelper(badSide, best, to, depth+1);
            }
        }
        return best;
    }

    private static class KDNode {
        Point item;
        KDNode left;
        KDNode right;

        public KDNode(Point point) {
            item = point;
        }
    }
}
