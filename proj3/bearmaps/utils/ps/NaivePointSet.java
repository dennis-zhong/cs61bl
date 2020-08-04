package bearmaps.utils.ps;

import java.util.ArrayList;
import java.util.List;

public class NaivePointSet implements PointSet {

    private List<Point> points;

    public NaivePointSet(List<Point> points) {
        this.points = points;
    }

    @Override
    public Point nearest(double x, double y) {
        double shortestDist = Double.MAX_VALUE;
        Point shortestPoint = null;
        for(int i = 0; i < points.size(); i++) {
            Point curr = points.get(i);
            double xx = curr.getX() - x;
            double yy = curr.getY() - y;
            double dist = xx*xx+yy*yy;
            if(dist<shortestDist) {
                shortestDist = dist;
                shortestPoint = curr;
            }
        }
        return shortestPoint;
    }
}
