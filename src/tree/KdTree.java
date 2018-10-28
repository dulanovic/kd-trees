package tree;
// import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
// import edu.princeton.cs.algs4.StdOut;
import java.awt.Color;
import edu.princeton.cs.algs4.Queue;
// import edu.princeton.cs.algs4.StdRandom;

public class KdTree {

    private Node root;
    private int count;
    private Point2D pointMin;
    private double minDistance;
    private Rectangle rect;

    public KdTree() {
        this.root = null;
        this.count = 0;
        this.minDistance = Double.POSITIVE_INFINITY;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return count;
    }

    public void insert(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("Null argument passed!!!");
        }
        if (!contains(point)) {
            this.rect = new Rectangle(0.0, 0.0, 1.0, 1.0);
            this.root = insert(root, null, point);
            //this.root = insert(root, null, point, new RectHV(0.0, 0.0, 1.0, 1.0));
        }
    }

    /* private Node insert(Node node, Node parentNode, Point2D point, RectHV rect) {
        if (root == null) {
            count++;
            Node n = new Node(point, new RectHV(0.0, 0.0, 1.0, 1.0), true);
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            n.rect.draw();
            StdDraw.setPenColor(StdDraw.BLACK);
            return n;
        }
        if (node == null) {
            count++;
            Node n = new Node(point, rect, !parentNode.orientation);
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            n.rect.draw();
            StdDraw.setPenColor(StdDraw.BLACK);
            return n;
        }
        int cmp = compare(node, point);
        if (cmp < 0) {
            RectHV rectNew = calculateRectHV(node, point);
            node.lb = insert(node.lb, node, point, rectNew);
        } else {
            if (node.point == point) {
                return parentNode;
            }
            RectHV rectNew = calculateRectHV(node, point);
            node.rt = insert(node.rt, node, point, rectNew);
        }
        return node;
    } */
    private Node insert(Node node, Node parentNode, Point2D point) {
        if (root == null) {
            count++;
            Node n = new Node(point, new RectHV(0.0, 0.0, 1.0, 1.0), true);
            /* StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            n.rect.draw();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.005); */
            return n;
        }
        if (node == null) {
            count++;
            Node n = new Node(point, rect.rectangleToRectHV(), !parentNode.orientation);
            /* StdDraw.setPenRadius(0.01);
            StdDraw.setPenColor(StdDraw.BOOK_LIGHT_BLUE);
            n.rect.draw();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.005); */
            return n;
        }
        int cmp = compare(node, point);
        if (cmp < 0) {
            if (node.orientation) {
                rect.setXmax(node.point.x());
            } else {
                rect.setYmax(node.point.y());
            }
            node.lb = insert(node.lb, node, point);
        } else {
            if (node.point == point) {
                return parentNode;
            }
            if (node.orientation) {
                rect.setXmin(node.point.x());
            } else {
                rect.setYmin(node.point.y());
            }
            node.rt = insert(node.rt, node, point);
        }
        return node;
    }

    public boolean contains(Point2D point) {
        if (point == null) {
            throw new IllegalArgumentException("Null argument passed!!!");
        }
        Node node = root;
        while (node != null) {
            int cmp = compare(node, point);
            if (cmp < 0) {
                node = node.lb;
            } else if (cmp > 0) {
                node = node.rt;
            } else {
                if (node.point.equals(point)) {
                    return true;
                }
                node = node.rt;
            }
        }
        return false;
    }

    public void draw() {
        Iterable<Node> i = nodes();
        for (Node node : i) {
            Line line = calculateLines(node);
            StdDraw.setPenColor(line.color);
            StdDraw.point(node.point.x(), node.point.y());
            StdDraw.line(line.point1.x(), line.point1.y(), line.point2.x(), line.point2.y());
        }
        StdDraw.setPenColor(StdDraw.BLACK);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Null argument passed!!!");
        }
        Queue<Point2D> q = new Queue<>();
        range(root, rect, q);
        return q;
    }

    private void range(Node node, RectHV rect, Queue<Point2D> queue) {
        if (node == null) {
            return;
        }
        // StdOut.printf("NODE EXAMINED ---> %s\n", node);
        /* StdDraw.setPenColor(StdDraw.YELLOW);
        StdDraw.setPenRadius(0.01);
        StdDraw.point(node.point.x(), node.point.y()); */
        if (rect.contains(node.point)) {
            queue.enqueue(node.point);
        }
        int cmp = rectPointComparator(rect, node);
        if (cmp < 0) {
            range(node.lb, rect, queue);
        } else if (cmp > 0) {
            range(node.rt, rect, queue);
        } else {
            range(node.lb, rect, queue);
            range(node.rt, rect, queue);
        }
    }

    private static int rectPointComparator(RectHV rect, Node node) {
        Point2D point = node.point;
        if (node.orientation) {
            if (point.x() > rect.xmax()) {
                return -1;
            } else if (point.x() < rect.xmin()) {
                return +1;
            } else {
                return 0;
            }
        } else {
            if (point.y() > rect.ymax()) {
                return -1;
            } else if (point.y() < rect.ymin()) {
                return +1;
            } else {
                return 0;
            }
        }
    }

    public Point2D nearest(Point2D point) {
        if (root == null) {
            return null;
        }
        if (point == null) {
            throw new IllegalArgumentException("Null argument passed!!!");
        }
        Node node = root;
        pointMin = null;
        minDistance = Double.POSITIVE_INFINITY;
        nearest(node, point);
        return pointMin;
    }

    private void nearest(Node node, Point2D point) {
        if (node == null) {
            return;
        }
        double distance = point.distanceSquaredTo(node.point);
        if (distance < minDistance) {
            minDistance = distance;
            pointMin = node.point;
        }
        // StdOut.printf("NODE EXAMINED ---> %s\n", node);
        if (node.orientation) {
            if (point.x() < node.point.x()) {
                nearest(node.lb, point);
                if (node.rt != null) {
                    double distanceToRect = node.rt.rect.distanceSquaredTo(point);
                    // StdOut.printf("minDistance ---> %f, %s ---> distanceToRect ---> %f\n", minDistance, node, distanceToRect);
                    if (minDistance >= distanceToRect) {
                        // StdOut.println("\t\t\tMINDISTANCE >>>");
                        nearest(node.rt, point);
                    }
                }
            } else {
                nearest(node.rt, point);
                if (node.lb != null) {
                    double distanceToRect = node.lb.rect.distanceSquaredTo(point);
                    // StdOut.printf("minDistance ---> %f, %s ---> distanceToRect ---> %f\n", minDistance, node, distanceToRect);
                    if (minDistance >= distanceToRect) {
                        // StdOut.println("\t\t\tMINDISTANCE >>>");
                        nearest(node.lb, point);
                    }
                }
            }
        } else {
            if (point.y() < node.point.y()) {
                nearest(node.lb, point);
                if (node.rt != null) {
                    double distanceToRect = node.rt.rect.distanceSquaredTo(point);
                    // StdOut.printf("minDistance ---> %f, %s ---> distanceToRect ---> %f\n", minDistance, node, distanceToRect);
                    if (minDistance >= distanceToRect) {
                        // StdOut.println("\t\t\tMINDISTANCE >>>");
                        nearest(node.rt, point);
                    }
                }
            } else {
                nearest(node.rt, point);
                if (node.lb != null) {
                    double distanceToRect = node.lb.rect.distanceSquaredTo(point);
                    // StdOut.printf("minDistance ---> %f, %s ---> distanceToRect ---> %f\n", minDistance, node, distanceToRect);
                    if (minDistance >= distanceToRect) {
                        // StdOut.println("\t\t\tMINDISTANCE >>>");
                        nearest(node.lb, point);
                    }
                }
            }

        }
    }

    private static class Node {

        private final Point2D point;
        private final RectHV rect;
        private Node lb;
        private Node rt;
        private final boolean orientation;

        public Node(Point2D point, RectHV rect, boolean orientation) {
            this.point = point;
            this.rect = rect;
            this.orientation = orientation;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(point.toString() + " - ");
            if (orientation) {
                sb.append("VERTICAL");
            } else {
                sb.append("HORIZONTAL");
            }
            return sb.toString();
        }

        @Override
        public boolean equals(Object object) {
            if (object == this) {
                return true;
            }
            if (object == null) {
                return false;
            }
            if (object.getClass() != this.getClass()) {
                return false;
            }
            Node node = (Node) object;
            return node.point == this.point;
        }

    }

    private class Rectangle {

        private double xmin;
        private double ymin;
        private double xmax;
        private double ymax;

        public Rectangle(double xmin, double ymin, double xmax, double ymax) {
            this.xmin = xmin;
            this.ymin = ymin;
            this.xmax = xmax;
            this.ymax = ymax;
        }

        public void setXmin(double xmin) {
            this.xmin = xmin;
        }

        public void setYmin(double ymin) {
            this.ymin = ymin;
        }

        public void setXmax(double xmax) {
            this.xmax = xmax;
        }

        public void setYmax(double ymax) {
            this.ymax = ymax;
        }

        public RectHV rectangleToRectHV() {
            return new RectHV(this.xmin, this.ymin, this.xmax, this.ymax);
        }

    }

    private static class Line {

        private final Point2D point1;
        private final Point2D point2;
        private final Color color;

        public Line(Point2D point1, Point2D point2, Color color) {
            this.point1 = point1;
            this.point2 = point2;
            this.color = color;
        }

    }

    private static int compare(Node node, Point2D point) {
        Point2D point2 = node.point;
        if (node.orientation) {
            if (point.x() < point2.x()) {
                return -1;
            } else if (point.x() > point2.x()) {
                return +1;
            } else {
                return 0;
            }
        } else {
            if (point.y() < point2.y()) {
                return -1;
            } else if (point.y() > point2.y()) {
                return +1;
            } else {
                return 0;
            }
        }
    }

    /* private static RectHV calculateRectHV(Node parentNode, Point2D point) {
        RectHV parentRect = parentNode.rect;
        Point2D parentPoint = parentNode.point;
        boolean orientation = parentNode.orientation;
        RectHV rectNew;
        if (orientation) {
            if (point.x() < parentPoint.x()) {
                rectNew = new RectHV(parentRect.xmin(), parentRect.ymin(), parentPoint.x(), parentRect.ymax());
            } else {
                rectNew = new RectHV(parentPoint.x(), parentRect.ymin(), parentRect.xmax(), parentRect.ymax());
            }
        } else {
            if (point.y() < parentPoint.y()) {
                rectNew = new RectHV(parentRect.xmin(), parentRect.ymin(), parentRect.xmax(), parentPoint.y());
            } else {
                rectNew = new RectHV(parentRect.xmin(), parentPoint.y(), parentRect.xmax(), parentRect.ymax());
            }
        }
        return rectNew;
    } */
    private Iterable<Node> nodes() {
        Queue<Node> q = new Queue<>();
        inorder(root, q);
        return q;
    }

    private void inorder(Node node, Queue<Node> q) {
        if (node == null) {
            return;
        }
        inorder(node.lb, q);
        q.enqueue(node);
        inorder(node.rt, q);
    }

    private static Line calculateLines(Node node) {
        Point2D point1;
        Point2D point2;
        Color color;
        if (node.orientation) {
            point1 = new Point2D(node.point.x(), node.rect.ymax());
            point2 = new Point2D(node.point.x(), node.rect.ymin());
            color = StdDraw.RED;
        } else {
            point1 = new Point2D(node.rect.xmin(), node.point.y());
            point2 = new Point2D(node.rect.xmax(), node.point.y());
            color = StdDraw.BLUE;
        }
        return new Line(point1, point2, color);
    }

    public static Point2D nearestPoint(KdTree tree, Point2D point) {
        Point2D pointNearest = tree.nearest(point);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.point(point.x(), point.y());
        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.point(pointNearest.x(), pointNearest.y());
        StdDraw.setPenColor(StdDraw.BLACK);
        for (Node node : tree.nodes()) {
            if (node.point.equals(point) || node.point.equals(pointNearest)) {
                continue;
            }
            StdDraw.point(node.point.x(), node.point.y());
        }
        return pointNearest;
    }

    public static int rangeInRect(KdTree tree, double xmin, double ymin, double xmax, double ymax) {
        RectHV rect = new RectHV(xmin, xmax, ymin, ymax);
        StdDraw.setPenColor(StdDraw.BOOK_BLUE);
        rect.draw();
        Queue<Point2D> q = (Queue) tree.range(rect);
        for (Node n : tree.nodes()) {
            if (rect.contains(n.point)) {
                StdDraw.setPenColor(StdDraw.YELLOW);
            } else {
                StdDraw.setPenColor(StdDraw.BLACK);
            }
            StdDraw.point(n.point.x(), n.point.y());
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        return q.size();
    }

    public static void main(String[] args) {

        // tree.KdTree tree = new tree.KdTree();
        /* StdDraw.setPenRadius(0.01);
        In in = new In("_data/input5.txt");
        while (!in.isEmpty()) {
            // in.readString();
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            tree.insert(p);
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(p.x(), p.y());
        }
        // tree.draw(); */

 /* for (int i = 0; i < 80; i++) {
            Point2D point = new Point2D(StdRandom.uniform(), StdRandom.uniform());
            tree.insert(point);
            StdDraw.point(point.x(), point.y());
        }
        tree.draw(); */

 /*
        tree.insert(new Point2D());
        StdOut.printf("isEmpty() ---> %s\n", tree.isEmpty());
        StdOut.printf("size() ---> %d\n", tree.size());
        StdOut.printf("contains() ---> %s\n", tree.contains(new Point2D()));
        StdOut.printf("nearest() ---> %s\n", nearestPoint(tree, new Point2D()));
        StdOut.printf("range() ---> %d\n", rangeInRect(tree, ));
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point();
         */
 /* tree.insert(new Point2D(1.0, 0.375));
        StdOut.printf("size() ---> %d\n", tree.size());
        StdOut.printf("range() ---> %d\n", rangeInRect(tree, 0.0, 0.8125, 0.1875, 0.625));
        StdOut.printf("range() ---> %d\n", rangeInRect(tree, 0.0625, 0.8125, 0.0, 0.8125));
        StdOut.printf("nearest() ---> %s\n", nearestPoint(tree, new Point2D(0.0, 0.5625)));
        StdOut.printf("nearest() ---> %s\n", nearestPoint(tree, new Point2D(0.0625, 0.1875)));
        StdOut.printf("size() ---> %d\n", tree.size());
        StdOut.printf("range() ---> %d\n", rangeInRect(tree, 0.1875, 0.4375, 0.4375, 0.6875));
        StdOut.printf("contains() ---> %s\n", tree.contains(new Point2D(0.0, 0.1875)));
        StdOut.printf("range() ---> %d\n", rangeInRect(tree, 0.5, 0.875, 0.0625, 0.25));
        StdOut.printf("range() ---> %d\n", rangeInRect(tree, 0.1875, 0.875, 0.25, 0.4375));
        tree.insert(new Point2D(0.0625, 0.125));
        tree.insert(new Point2D(0.375, 0.5625));
        StdOut.printf("contains() ---> %s\n", tree.contains(new Point2D(0.75, 0.8125)));
        StdOut.printf("size() ---> %d\n", tree.size());
        StdOut.printf("nearest() ---> %s\n", nearestPoint(tree, new Point2D(0.5, 0.6875)));
        StdOut.printf("contains() ---> %s\n", tree.contains(new Point2D(0.5, 1.0)));
        StdOut.printf("size() ---> %d\n", tree.size());
        StdOut.printf("range() ---> %d\n", rangeInRect(tree, 0.3125, 0.5625, 0.0, 0.75));
        tree.insert(new Point2D(0.5625, 0.75));
        tree.insert(new Point2D(0.0, 0.625));
        for (Node node : tree.nodes()) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.point(node.point.x(), node.point.y());
        }
        StdOut.printf("nearest() ---> %s\n", nearestPoint(tree, new Point2D(0.75, 0.1875))); */
    }

}
