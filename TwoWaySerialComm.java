import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

// Dependencies:
// $ sudo apt-get install librxtx-java

// Compile and run with:
// $ javac -cp /usr/share/java/RXTXcomm.jar:. TwoWaySerialComm.java
// $ java -Djava.library.path=/usr/lib/jni -cp /usr/share/java/RXTXcomm.jar:. TwoWaySerialComm
// or with optional main arguemnts
// $ java -Djava.library.path=/usr/lib/jni -cp /usr/share/java/RXTXcomm.jar:. TwoWaySerialComm "/dev/ttyUSB0" baudRate bufferSize # (baudRate and bufferSize musts be ints)
public class TwoWaySerialComm {

  void connect( String portName, int baudRate, int bufferSize ) throws Exception {
    CommPortIdentifier portIdentifier = CommPortIdentifier
        .getPortIdentifier( portName );
    if( portIdentifier.isCurrentlyOwned() ) {
      System.out.println( "[ERROR] Port '" + portName + "' is currently in use" );
    } else {
      int timeout = 2000;
      CommPort commPort = portIdentifier.open( this.getClass().getName(), timeout );

      if( commPort instanceof SerialPort ) {
        SerialPort serialPort = ( SerialPort ) commPort;
        System.out.println( "[SET] Serial port params" );
        serialPort.setSerialPortParams( baudRate,
                                        SerialPort.DATABITS_8,
                                        SerialPort.STOPBITS_1,
                                        SerialPort.PARITY_NONE );

        System.out.println( "[GET] Input stream" );
        System.out.println( "[GET] Output stream" );
        InputStream in = serialPort.getInputStream();
        OutputStream out = serialPort.getOutputStream();

        System.out.println( "[START] Serial reader" );
        System.out.println( "[START] Serial writer" );
        ( new Thread( new SerialReader( in, bufferSize ) ) ).start();
        ( new Thread( new SerialWriter( out ) ) ).start();

      } else {
        System.out.println( "[ERROR] Only serial ports are implemented" );
      }
    }
  }

  public static class SerialReader implements Runnable {

    InputStream in;
    int bufferSize;

    public SerialReader( InputStream in, int bufferSize ) {
      this.in = in;
      this.bufferSize = bufferSize;
    }

    public void run() {
      byte[] buffer = new byte[ this.bufferSize ];
      int len = -1;
      try {
        while( ( len = this.in.read( buffer ) ) > -1 ) {
          // TODO: Implement
          /* TODO: Outputs everything that is printed to terminal
                   i.e if you write "HEJ" and press ENTER output will be:
                    [OUTPUT]='H'
                    [OUTPUT]='EJ
                    '
          */
          System.out.println("[OUTPUT]='" + new String( buffer, 0, len ) + "'");
        }
      } catch( IOException e ) {
        e.printStackTrace();
      }
    }
  }

  public static class SerialWriter implements Runnable {

    OutputStream out;

    public SerialWriter( OutputStream out ) {
      this.out = out;
    }

    public void run() {
      try {
        int c = 0;
        while( ( c = System.in.read() ) > -1 ) {
          // TODO: Implement
          this.out.write( c );
        }
      } catch( IOException e ) {
        e.printStackTrace();
      }
    }
  }

  public static void main( String[] args ) {
    // Defaults
    String serialPort = "/dev/ttyUSB1";
    int baudRate = 38400;
    int bufferSize = 1024; // TODO: Find an appropriate buffer size

    for (int i = 0; i < args.length; i++) {
        switch (i) {
          case 0:
            try {
              baudRate = Integer.parseInt(args[1]);
              System.out.println("[ARGUMENT] baud rate: " + baudRate);
            } catch (NumberFormatException e) {
              System.out.println("[ERROR] Baud rate must be an integer. Was: '" + args[1] + "'");
              System.out.println("[SYSTEM EXIT]");
              System.exit(0);
            }
            break;
          case 1:
            try {
              bufferSize = Integer.parseInt(args[2]);
              System.out.println("[ARGUMENT] buffer size: " + bufferSize);
            } catch (NumberFormatException e) {
              System.out.println("[ERROR] Buffer size must be an integer. Was: '" + args[2] + "'");
              System.out.println("[SYSTEM EXIT]");
              System.exit(0);
            }
            break;
          case 2:
            serialPort = args[0];
            System.out.println("[ARGUMENT] port: " + serialPort);
            break;

        }
    }

    System.out.println("[USING] port:        " + serialPort);
    System.out.println("[USING] baud rate:   " + baudRate);
    System.out.println("[USING] buffer size: " + bufferSize);


    // TODO: Potentially not necessary to set it explicitly with System.setProperty
    // Set SerialPorts property for gnu.io.rxtx
    System.setProperty("gnu.io.rxtx.SerialPorts", serialPort);
    try {
      System.out.println("\n\n[CONNECT]: serial=" + serialPort + ", baudRate=" + baudRate + ", bufferSize=" + bufferSize);
      ( new TwoWaySerialComm() ).connect( serialPort, baudRate, bufferSize);
    } catch( Exception e ) {
      e.printStackTrace();
    }
  }
}
