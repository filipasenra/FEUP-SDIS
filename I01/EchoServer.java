import java.io.IOException;
import java.net.*;
import java.util.HashMap;

public class EchoServer {

    private static int port;
    private static HashMap<String, String> database = new HashMap<String, String>();

    public static void main(String[] args) throws IOException {

        if (!parseArgs(args)) {
            return;
        }

        // send request
        DatagramSocket socket = new DatagramSocket(port);

        while (true) {
            // get request
            byte[] rbuf = new byte[256];
            DatagramPacket packet = new DatagramPacket(rbuf, rbuf.length);
            socket.receive(packet);

            // display resquest
            String received = new String(packet.getData());
            System.out.println("Server: " + received);

            // parse response
            String response = parseRequest(received);

            // sending response
            byte[] sbuf = response.getBytes();
            InetAddress address = packet.getAddress();
            int clientPort = packet.getPort();
            DatagramPacket responsePacket = new DatagramPacket(sbuf, sbuf.length, address, clientPort);
            socket.send(responsePacket);

        }

        // socket.close();
    }

    private static String parseRequest(String received) {

        String response = "";

        String[] tokens = received.split(" ", 3);
        
        if (tokens.length != 2 && tokens.length != 3) {

            System.out.println("ERROR IN RECEIVED MESSAGE");
            System.exit(-1);
        }

        String oper = tokens[0];
        String DNSName = tokens[1];

        if (oper.equals("REGISTER")) {

            if (!database.containsKey(DNSName)) {

                database.put(DNSName, tokens[2]);
                response = Integer.toString(database.size());

            } else {
                response = "-1"; // make this a MACRO
            }

        } else if (oper.equals("LOOKUP")) {

            System.out.println("|" + DNSName + "|");

            if (database.containsKey(DNSName)){

                response = database.get(DNSName);
                
            } else {
                response = "NOT_FOUND"; // make this a MACRO
            }

        } else {


            System.out.println("ERROR IN RECEIVED MESSAGE");
            System.exit(-1);
        }

        return response;

    }

    private static boolean parseArgs(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java Echo <port>");
            return false;
        }

        port = Integer.parseInt(args[0]);
        return true;

    }

}