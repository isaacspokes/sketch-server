import java.util.*;
import java.awt.*;

public class Sketch {
    public TreeMap<Integer, Shape> idShapeList;
    private int id;

    public Sketch(){
        idShapeList = new TreeMap<>();
        id = 0;
    }

    /**
     * Takes in a shape, gives it an ID
     * and places that shape in the map
     */
    public synchronized int addShape(Shape shape){
        //TODO our code here
        id++;
        idShapeList.put(id, shape);
        return id;
    }

    /**
     * Takes in the shape id and moves
     * it by the given dx and dy values
     */
    public synchronized void moveShape (int id, int dx, int dy){
        //TODO our code here
        if (idShapeList.containsKey(id)) idShapeList.get(id).moveBy(dx,dy);
    }

    /**
     * Takes in the shape id and
     * recolors it to the new color
     */
    public synchronized void recolorShape (int id, Color color){
        //TODO our code here
        if (idShapeList.containsKey(id)) idShapeList.get(id).setColor(color);
    }

    /**
     * Takes in the shape id and
     * deletes it from the sketch
     */
    public synchronized void deleteShape (int id){
        //TODO our code here
        if (idShapeList.containsKey(id)) idShapeList.remove(id);
    }

    /**
     * returns a navigable keyset of the shapes
     */
    public synchronized Set<Integer> getidShapeList() {
        return idShapeList.descendingKeySet();
    }

    /**
     * returns the shape given an ID
     */
    public synchronized Shape getShape (int id) {
        return idShapeList.get(id);
    }

    /**
     * returns the id of the shape containing the point
     */
    public synchronized int contains(Point p){
        for(int id : idShapeList.descendingKeySet()){
            if(idShapeList.get(id).contains(p.x,p.y)) {
                return id;
            }
        }
        return -1;
    }


}
