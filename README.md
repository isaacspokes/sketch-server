# sketch-server
program where multiple people can draw on a shared UI from different machines.
There are certain object class files for each type of shape (Ellipse, Polyline - multiple connected segmemnts, Rectangle, Segment) 
  all of which extend the Shape class
There is a sketch server file which runs as the host
  Echo server is the file which this was based on
And a sketch server communicator file to run as the client
Editor and Editor communicator are also used for this purpose.
