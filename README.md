# Traffic Lights Simulator 

Simulation of a crossing of avenues with 17 traffic lights in total (5 for cars and 12 for pedestrian),
and with 12 animated cars being generated every second. A multithreading project where every object extends
the Java Thread class and are synchronized through a custom Event class.

To build, compile the java files with
javac BuildTrafficLight.java
since its the class with the main function it compiles everything.

Then, run the command
jar cfe myjar.jar BuildTrafficLight *.class

![Traffic Lights Simulator](/Images/running.PNG "Traffic Lights Simulator")
