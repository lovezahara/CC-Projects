// Porting Note: This the the Point.java from the last assignment with minor
//                  changes: point_init method name has been shortened
//                           point_add method name has been shortened
//                           main method has been removed

/**
 * Representation of a 2D point
 */
// Porting Note: a struct is the data part of a class
public class Point {
    // Porting Note: instance variables -> struct fields
    public int x;
    public int y;

    // The private constructor forces the used of the static method
    //   to get new point instances
    private Point() { }

    /**
     * Generate a new point
     * @param x the x coordinate
     * @param y the y coordinate
     * @return a point with correctly set x and y
     */
    public static Point init(int x, int y) {
        // Porting Note: JAVA forces non-native types into the heap. In your
        //                 port, the point data should be in the stack.
        Point p = new Point();
        p.x = x;
        p.y = y;
        return p;
    }

    /**
     * Computes euclidean distance between two points
     * @param p1 point a
     * @param p2 point b
     * @return the euclidean distance as a float
     */
    public static float point_dist(Point p1, Point p2) {
        float dx = p1.x-p2.x;
        float dy = p1.y-p2.y;
        // Porting Note: sqrtf() is available from math.h
        return (float)Math.sqrt(dx*dx + dy*dy);
    }

    /**
     * Multiplies the components by a scalar
     * @param p1 the point
     * @param s  the scalar
     * @return the point computed by multipling x and y by s
     */
    public static Point point_smult(Point p1, int s) {
        // Porting Note: JAVA forces non-native types into the heap. In your
        //                 port, the point data should be in the stack.
        Point p = new Point();
        p.x = p1.x * s;
        p.y = p1.y * s;
        return p;
    }

    /**
     * Adds two point together
     * @param p1 point a
     * @param p2 point b
     * @return a new point computed by adding the xs and ys
     */
    public static Point add(Point p1, Point p2) {
        // Porting Note: JAVA forces non-native types into the heap. In your
        //                 port, the point data should be in the stack.
        Point p = new Point();
        p.x = p1.x + p2.x;
        p.y = p1.y + p2.y;
        return p;
    }
}
