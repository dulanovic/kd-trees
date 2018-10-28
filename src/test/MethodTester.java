package test;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;
import tree.KdTree;
import java.awt.Font;

public class MethodTester {

    private final KdTree tree;

    public MethodTester(KdTree tree) {
        this.tree = tree;
    }

    interface MethodTest {

        void test(double... coordinates);
    }

    private static final double PROBABILITY_INSERT = 0.3;
    private static final double PROBABILITY_ISEMPTY = 0.05;
    private static final double PROBABILITY_SIZE = 0.05;
    private static final double PROBABILITY_CONTAINS = 0.2;
    private static final double PROBABILITY_RANGE = 0.2;
    private static final double PROBABILITY_NEAREST = 0.2;

    public void testInsert(double... coordinates) {
        StdDraw.clear();
        Point2D point = new Point2D(coordinates[0], coordinates[1]);
        tree.insert(point);
        // StdOut.printf("NODE_INSERTED ---> %s\n", point);
        StdDraw.text(0.5, 0.55, "insert()");
        StdDraw.text(0.5, 0.5, point.toString());
        tree.draw();
        while (!StdDraw.isMousePressed()) {
        }
        StdDraw.pause(600);
    }

    public void testIsEmpty(double... coordinates) {
        StdDraw.clear();
        // StdOut.printf("isEmpty() ---> %s\n", tree.isEmpty());
        StdDraw.text(0.5, 0.55, "isEmpty()");
        StdDraw.text(0.5, 0.5, tree.isEmpty() + "");
        while (!StdDraw.isMousePressed()) {
        }
        StdDraw.pause(600);
    }

    public void testSize(double... coordinates) {
        StdDraw.clear();
        // StdOut.printf("size() ---> %s\n", tree.size());
        StdDraw.text(0.5, 0.55, "size()");
        StdDraw.text(0.5, 0.5, tree.size() + "");
        while (!StdDraw.isMousePressed()) {
        }
        StdDraw.pause(600);
    }

    public void testContains(double... coordinates) {
        StdDraw.clear();
        Point2D point = new Point2D(coordinates[0], coordinates[1]);
        // StdOut.printf("contains() ---> %s\n", tree.contains(point));
        StdDraw.text(0.5, 0.55, "contains()");
        StdDraw.text(0.5, 0.5, tree.contains(point) + "");
        while (!StdDraw.isMousePressed()) {
        }
        StdDraw.pause(600);
    }

    public void testRange(double... coordinates) {
        StdDraw.clear();
        KdTree.rangeInRect(tree, coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
        // StdOut.printf("range() ---> %d\n", rangeCount);
        while (!StdDraw.isMousePressed()) {
        }
        StdDraw.pause(600);
    }

    public void testNearest(double... coordinates) {
        StdDraw.clear();
        Point2D point = new Point2D(coordinates[0], coordinates[1]);
        Point2D pointNearest = KdTree.nearestPoint(tree, point);
        // StdOut.printf("nearest() ---> %s\n", tree.KdTree.nearestPoint(tree, point));
        StdDraw.text(0.5, 0.6, "nearest()");
        StdDraw.text(0.5, 0.55, point + "");
        StdDraw.text(0.5, 0.5, pointNearest + "");
        while (!StdDraw.isMousePressed()) {
        }
        StdDraw.pause(600);
    }

    private MethodTest[] methods = new MethodTest[]{
            new MethodTest() {
                @Override
                public void test(double... coordinates) {
                    testInsert(coordinates);
                }
            }, new MethodTest() {
        @Override
        public void test(double... coordinates) {
            testIsEmpty(coordinates);
        }
    }, new MethodTest() {
        @Override
        public void test(double... coordinates) {
            testSize(coordinates);
        }
    }, new MethodTest() {
        @Override
        public void test(double... coordinates) {
            testContains(coordinates);
        }
    }, new MethodTest() {
        @Override
        public void test(double... coordinates) {
            testRange(coordinates);
        }
    }, new MethodTest() {
        @Override
        public void test(double... coordinates) {
            testNearest(coordinates);
        }
    }
    };

    public void test(int index, double... coordinates) {
        methods[index].test(coordinates);
    }

    public void testMethod(int index, double... coordinates) {
        methods[index].test(coordinates);
    }

    public static void main(String[] args) {

        KdTree tree = new KdTree();
        MethodTester mt = new MethodTester(tree);
        double[] probabilities = new double[]{PROBABILITY_INSERT, PROBABILITY_ISEMPTY, PROBABILITY_SIZE, PROBABILITY_CONTAINS, PROBABILITY_RANGE, PROBABILITY_NEAREST};
        StdDraw.setPenRadius(0.005);
        StdDraw.setFont(new Font("Courier New", Font.PLAIN, 24));
        /* for (int i = 0; i < 100; i++) {
            mt.testMethod(0, StdRandom.uniform(), StdRandom.uniform());
        } */
        for (int i = 0; i < 100; i++) {
            int random = StdRandom.discrete(probabilities);
            switch (random) {
                case 1:
                case 2:
                    mt.methods[random].test(null);
                    break;
                case 4:
                    double xmin = StdRandom.uniform();
                    double ymin = StdRandom.uniform();
                    double xmax = xmin;
                    double ymax = ymin;
                    do {
                        xmax = StdRandom.uniform();
                    } while (xmax <= xmin);
                    do {
                        ymax = StdRandom.uniform();
                    } while (ymax <= ymin);
                    mt.methods[random].test(xmin, xmax, ymin, ymax);
                    break;
                case 5:
                    if (tree.isEmpty()) {
                        continue;
                    }
                default:
                    mt.methods[random].test(StdRandom.uniform(), StdRandom.uniform());
                    break;
            }
        }

    }

}
