package tree;

// import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.TreeSet;

public class PointSET {

    private final TreeSet<Point2D> points;

    public PointSET() {
        this.points = new TreeSet<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null argument passed!!!");
        }
        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Null argument passed!!!");
        }
        return points.contains(p);
    }

    public void draw() {
        if (isEmpty()) {
            StdOut.println("No points to draw");
            return;
        }
        for (Point2D p : points) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Null argument passed!!!");
        }
        // StdDraw.setPenColor(StdDraw.YELLOW);
        // rect.draw();
        Queue<Point2D> q = new Queue<>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                q.enqueue(p);
                /* StdDraw.setPenColor(StdDraw.RED);
                StdDraw.point(p.x(), p.y());
            } else {
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.point(p.x(), p.y()); */
            }
        }
        // StdOut.printf("COUNTER ---> %d\n", q.size());
        return q;
    }

    public Point2D nearest(Point2D p) {
        if (points.isEmpty()) {
            return null;
        }
        if (p == null) {
            throw new IllegalArgumentException("Null argument passed!!!");
        }
        /* StdDraw.setPenRadius(0.05);
        StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.point(p.x(), p.y()); */
        Point2D min = null;
        double minDistance = Integer.MAX_VALUE;
        for (Point2D po : points) {
            /* StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(po.x(), po.y()); */
            double distance = po.distanceSquaredTo(p);
            if (distance < minDistance) {
                minDistance = distance;
                min = po;
            }
        }
        /* StdDraw.setPenColor(StdDraw.RED);
        StdDraw.point(min.x(), min.y());
        StdOut.printf("DISTANCEMIN ---> %f\n", minDistance); */
        return min;
    }

    public static void main(String[] args) {

        /* In in = new In("_data/input10.txt");
        tree.PointSET ps = new tree.PointSET();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            ps.insert(p);
        }
        ps.draw();
        Point2D p = new Point2D(0.225, 0.555);
        StdOut.printf("NEAREST NEIGHBOR ---> %s\n", ps.nearest(p)); */
 /* RectHV rect = new RectHV(0.405, 0.375, 0.555, 0.555);
        Iterable<Point2D> i = ps.range(rect); */
 /* int counter = 0;
        for (Point2D p : i) {
            // StdOut.printf("%s\n", p);
            counter++;
        }
        StdOut.printf("\nCOUNTER ---> %d\n", counter); */
    }

}
