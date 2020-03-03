package i02;

import java.io.IOException;
import java.net.*;

//Call outside of folder i02
//javac i02/EchoClient.java 
//java i02/EchoClient 224.0.0.1 1025 lookup ola 
// or 
//java i02/EchoClient 224.0.0.1 1025 register ola 123

public class EchoClient {

    private static String mcast_addr; //hostname
    private static int port;
    private static TypeOper oper;
    private static String DNSName;
    private static String ipAddress;
    private static int srvc_port;
    private static String srvc_address;

    public static void main(final String[] args) throws IOException {
        if (!parseArgs(args)) {
            return;
        }

        //Receiving the info host
        final MulticastSocket multiSocket = new MulticastSocket(port);
        final InetAddress group = InetAddress.getByName(mcast_addr);
        multiSocket.joinGroup(group);

        final byte[] infoPacketArray = new byte[256];
        final DatagramPacket infoPacket = new DatagramPacket(infoPacketArray, infoPacketArray.length);
        multiSocket.receive(infoPacket);

        String infoFromHost = new String(infoPacketArray);
        parseInfoHost(infoFromHost);

        multiSocket.leaveGroup(group);
        multiSocket.close();

        DatagramSocket socket = new DatagramSocket();

        // send request
        final String request = buildRequest();

        final byte[] sbuf = request.getBytes();
        final InetAddress address = InetAddress.getByName(srvc_address);
        final DatagramPacket packet = new DatagramPacket(sbuf, sbuf.length, address, srvc_port);

        socket.send(packet);


        //receiving response
        final byte[] rbuf = new byte[256];
        final DatagramPacket responsePacket = new DatagramPacket(rbuf, rbuf.length);
        socket.receive(responsePacket);

        // display response
        final String received = new String(responsePacket.getData());
        System.out.println("Client: " + received);

        socket.close();
    }

    private static String buildRequest() {

        String request = oper + " " + DNSName;

        if (oper == TypeOper.REGISTER) {

            request += " " + ipAddress;

        }

        return request;

    }

    private static void parseInfoHost(String infoHost){

        String[] tokens = infoHost.split(" ", 2);

        if (tokens.length != 2) {

            System.out.println("ERROR IN RECEIVED MESSAGE");
            System.exit(-1);
        }

        srvc_address = tokens[0].trim();
        srvc_port = Integer.parseInt(tokens[1].trim());


    }

    private static boolean parseArgs(final String[] args) {

        if (args.length < 3) {

            System.out.println("Usage: java client <mcast_addr> <mcast_port> <oper> <opnd>");

            return false;
        }

        mcast_addr = args[0];
        port = Integer.parseInt(args[1]);

        if (args[2].equals("register"))
            oper = TypeOper.REGISTER;
        else if (args[2].equals("lookup"))
            oper = TypeOper.LOOKUP;
        else {
            System.out.println("Usage: <oper> must be 'register' or 'lookup'");
            return false;
        }

        if (oper == TypeOper.REGISTER) {

            if (args.length != 5) {
                System.out.println("Usage: Client <hostname> <port> register <DNS name> <IP address>");
                return false;
            }

            DNSName = args[3];
            ipAddress = args[4];

        } else {

            if (args.length != 4) {
                System.out.println("Usage: Client <hostname> <port> lookup <DNS name>");
                return false;
            }

            DNSName = args[3];

        }

        return true;

    }
}
