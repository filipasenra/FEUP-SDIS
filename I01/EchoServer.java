import java.io.IOException;
import java.net.*;

public class EchoServer {
    public static void main(String[] args) throws IOException {
         if (args.length != 1) {
            System.out.println("Usage: java Echo <port>");
            return;
        }

        int port = Integer.parseInt(args[0]);

        // send request
        DatagramSocket socket = new DatagramSocket(port);
       // byte[] sbuf = args[1].getBytes();
        InetAddress address = InetAddress.getByName(args[0]);
        //DatagramPacket packet = new DatagramPacket(sbuf, sbuf.length, address, 4445);
          
        while (true) {
        // get response
        byte[] rbuf = new byte[256];
        DatagramPacket packet = new DatagramPacket(rbuf, rbuf.length);
        socket.receive(packet);
        
        // display response
        String received = new String(packet.getData());
        System.out.println("Echoed Message: " + received);


       
        }
        
        

    }
}