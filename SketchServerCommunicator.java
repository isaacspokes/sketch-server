import java.awt.*;
import java.io.*;
import java.net.Socket;

/**
 * Handles communication between the server and one client, for SketchServer
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate SketchServerCommunicator
 */
public class SketchServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServer server;			// handling communication for

	public SketchServerCommunicator(Socket sock, SketchServer server) {
		this.sock = sock;
		this.server = server;
	}

	/**
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg) {
		out.println(msg);
	}
	
	/**
	 * Keeps listening for and handling (your code) messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");

			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);
			// Tell the client the current state of the world
			// TODO: YOUR CODE HERE
			System.out.println("sending current state");
			Sketch sketch = server.getSketch();
			for(int id : sketch.getidShapeList()) {
				Shape shape = sketch.getShape(id);
				send("Add " +shape.toString());
			}
			// Keep getting and handling messages from the client
			// TODO: YOUR CODE HERE
			String line;
			while((line = in.readLine()) != null) {
				System.out.println(line);
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
						sketch.addShape(temp);
					}
					else {
						int x1 = Integer.parseInt(messages[2]);
						int y1 = Integer.parseInt(messages[3]);
						int x2 = Integer.parseInt(messages[4]);
						int y2 = Integer.parseInt(messages[5]);
						Color color = new Color(Integer.parseInt(messages[6]));
						if(messages[1].equals("Ellipse")) {
							sketch.addShape(new Ellipse(x1, y1, x2, y2, color));
						}
						else if(messages[1].equals("Rectangle")) {
							sketch.addShape(new Rectangle(x1, y1, x2, y2, color));
						}
						else if(messages[1].equals("Segment")) {
							sketch.addShape(new Segment(x1, y1, x2, y2, color));
						}
					}
				}
				else if(messages[0].equals("Move")) {
					int id = Integer.parseInt(messages[3]);
					int dx = Integer.parseInt(messages[1]);
					int dy = Integer.parseInt(messages[2]);
					sketch.moveShape(id,dx,dy);
					System.out.println("shape moved");
				}
				else if(messages[0].equals("Delete")) {
					int id = Integer.parseInt(messages[1]);
					sketch.deleteShape(id);
				}
				else if(messages[0].equals("Recolor")) {
					Color color = new Color(Integer.parseInt(messages[2]));
					int id = Integer.parseInt(messages[1]);
					sketch.recolorShape(id,color);
				}
				server.broadcast(line);
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
			// figure out how to send that shit back down to all editors

			// Clean up -- note that also remove self from server's list so it doesn't broadcast here
		try {
			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
		}
		catch ( IOException e) {
			e.printStackTrace();
		}
		}
	}
