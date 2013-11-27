## Dependencies:
    $ sudo apt-get install librxtx-java

## Compile and run with:
    $ javac -cp /usr/share/java/RXTXcomm.jar:. TwoWaySerialComm.java
    $ java -Djava.library.path=/usr/lib/jni -cp /usr/share/java/RXTXcomm.jar:. TwoWaySerialComm

or with optional main arguemnts

    $ java -Djava.library.path=/usr/lib/jni -cp /usr/share/java/RXTXcomm.jar:. TwoWaySerialComm "/dev/tty/USB0" baudRate bufferSize # (baudRate and bufferSize musts be ints)
