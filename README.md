# Traffic Lights Simulator 

Simulation of a crossing of avenues with 17 traffic lights in total (5 for cars and 12 for pedestrian),
and with 12 animated cars being generated every second. A multithreading project where every object extends
the Java Thread class and are synchronized through a custom Event class.

### Compiling
To build the project, compile it and create a jar file.
The class with the main function will compile everything else.
```
javac BuildTrafficLight.java
```

Then, run the below command:
```
jar cfe FileName.jar BuildTrafficLight *.class
```

### Running Example

![Traffic Lights Simulator](/Images/running.PNG "Traffic Lights Simulator")
