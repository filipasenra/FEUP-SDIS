import java.io.IOException;
import java.net.*;

public class EchoClient {
    public static void main(String[] args) throws IOException {
         if (args.length != 3) {
            System.out.println("Usage: java Echo <hostname> <port> <string to echo>");
            return;
        }

        // send request
        DatagramSocket socket = new DatagramSocket();
         int port = Integer.parseInt(args[1]);
        byte[] sbuf = args[2].getBytes();
        InetAddress address = InetAddress.getByName(args[0]);
        DatagramPacket packet = new DatagramPacket(sbuf, sbuf.length,
                address, port);
        socket.send(packet);
    }
}

