import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A multi-segment Shape, with straight lines connecting "joint" points -- (x1,y1) to (x2,y2) to (x3,y3) ...
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 * @author CBK, updated Fall 2016
 */
public class Polyline implements Shape {
    // TODO: YOUR CODE HERE
    List<Point> points;
    private Color color;

    /**
     * A new polyline with just the starting point
     */
    public Polyline (Point p, Color color) {
        points = new ArrayList<>();
        this.color = color;
        points.add(p);
    }

    public void addPoint(Point p) {
        points.add(p);
    }

    @Override
    public void moveBy(int dx, int dy) {
        for (Point p : points){
            p.x = p.x + dx;
            p.y = p.y + dy;
        }
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Checks if any of the segments of
     * the polyline contains the point
     */
    @Override
    public boolean contains(int x, int y) {
        for (int i = 0; i<points.size()-1; i++){
            Segment segment = new Segment(points.get(i).x, points.get(i).y, points.get(i+1).x, points.get(i+1).y, color);
            boolean check = segment.pointToSegmentDistance(x, y, points.get(i).x, points.get(i).y, points.get(i+1).x, points.get(i+1).y) <= 3;
            if (check) return true;
        }
        return false;
    }

    /**
     * Draws each individual segment
     */
    @Override
    public void draw(Graphics g) {
        for (int i = 0; i<points.size()-1; i++) {
            Segment segment = new Segment(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y, color);
            segment.draw(g);
        }
    }

    /**
     * Custom toString to give all necessary
     * information to editor communicator
     */
    @Override
    public String toString() {
        String toReturn = "Polyline "+color.getRGB()+" ";
        for (int i = 0; i<points.size(); i++) {
            toReturn += (points.get(i).x+" "+points.get(i).y+" ");
        }
        return toReturn;
    }
}