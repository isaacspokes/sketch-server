import java.io.*;
import java.net.Socket;
import java.awt.*;
import java.util.ArrayList;

/**
 * Handles communication to/from the server for the editor
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Chris Bailey-Kellogg; overall structure substantially revised Winter 2014
 * @author Travis Peters, Dartmouth CS 10, Winter 2015; remove EditorCommunicatorStandalone (use echo server for testing)
 */
public class EditorCommunicator extends Thread {
	private PrintWriter out;		// to server
	private BufferedReader in;		// from server
	protected Editor editor;		// handling communication for

	/**
	 * Establishes connection and in/out pair
	 */
	public EditorCommunicator(String serverIP, Editor editor) {
		this.editor = editor;
		System.out.println("connecting to " + serverIP + "...");
		try {
			Socket sock = new Socket(serverIP, 4242);
			out = new PrintWriter(sock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println("...connected");
		}
		catch (IOException e) {
			System.err.println("couldn't connect");
			System.exit(-1);
		}
	}

	/**
	 * Sends message to the server
	 */
	public void send(String msg) {
		out.println(msg);
		System.out.println(msg);
	}

	/**
	 * Keeps listening for and handling (your code) messages from the server
	 */
	public void run() {
		try {
			// Handle messages
			// TODO: YOUR CODE HERE
            String line;
            while ((line = in.readLine()) != null) {
            	String [] messages = line.split(" ");
                if(messages[0].equals("Add")) {
                	if(messages[1].equals("Polyline")) {
                		//while loop that reads in all of the points
						Color color = new Color(Integer.parseInt(messages[2]));
						Point toAdd = new Point(Integer.parseInt(messages[3]), Integer.parseInt(messages[4]));
						Polyline temp = new Polyline(toAdd, color);
						for (int i = 5; i< messages.length-1; i += 2){
							Point p = new Point(Integer.parseInt(messages[i]), Integer.parseInt(messages[i+1]));
							temp.addPoint(p);
						}
						editor.sketch.addShape(temp);
                	}
                	else {
                		int x1 = Integer.parseInt(messages[2]);
						int y1 = Integer.parseInt(messages[3]);
						int x2 = Integer.parseInt(messages[4]);
						int y2 = Integer.parseInt(messages[5]);
						Color color = new Color(Integer.parseInt(messages[6]));
						if(messages[1].equals("Ellipse")) {
							editor.sketch.addShape(new Ellipse(x1, y1, x2, y2, color));
						}
						else if(messages[1].equals("Rectangle")) {
							editor.sketch.addShape(new Rectangle(x1, y1, x2, y2, color));
						}
						else if(messages[1].equals("Segment")) {
							editor.sketch.addShape(new Segment(x1, y1, x2, y2, color));
						}
					}
				}
				else if(messages[0].equals("Move")) {
					int id = Integer.parseInt(messages[3]);
					int dx = Integer.parseInt(messages[1]);
					int dy = Integer.parseInt(messages[2]);
					editor.sketch.moveShape(id,dx,dy);
				}
				else if(messages[0].equals("Delete")) {
					int id = Integer.parseInt(messages[1]);
					editor.sketch.deleteShape(id);
				}
				else if(messages[0].equals("Recolor")) {
					Color color = new Color(Integer.parseInt(messages[2]));
					int id = Integer.parseInt(messages[1]);
					editor.sketch.recolorShape(id,color);

				}
				editor.repaint();
            }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("server hung up");
		}
	}

	// Send editor requests to the server
	// TODO: YOUR CODE HERE

	/**
	 * Handles editor's request to add
	 * a shape to the sketch
	 */
	public void addRequest(Shape shape) {
		send("Add " +shape.toString());
	}

	/**
	 * Enables editor to move
	 * a shape in the sketch
	 */
	public void moveRequest(int id, int dx, int dy) {
		send("Move " +dx+" "+dy+" "+id);
	}
	/**
	 * Enables editor to delete
	 * shapes from the sketch
	 */
	public void deleteRequest(int id) {
		send("Delete " +id);
	}

	/**
	 * Enables editor to change
	 * the color of a shape
	 */
	public void recolorRequest(int id, Color color) {
		send("Recolor " +id+" "+color.getRGB());
	}

}
