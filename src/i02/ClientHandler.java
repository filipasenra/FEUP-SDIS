package i02;

import java.io.IOException;
import java.net.*;

public class ClientHandler {

    private String mcast_addr; //hostname
    private int port;
    private TypeOper oper;
    private String DNSName;
    private String ipAddress;
    private int srvc_port;
    private String srvc_address;

    public ClientHandler(){

    }

    public void connectToServer() throws IOException{

        //Receiving the info host
        final MulticastSocket multiSocket = new MulticastSocket(this.port);
        final InetAddress group = InetAddress.getByName(this.mcast_addr);
        multiSocket.joinGroup(group);

        final byte[] infoPacketArray = new byte[256];
        final DatagramPacket infoPacket = new DatagramPacket(infoPacketArray, infoPacketArray.length);
        multiSocket.receive(infoPacket);

        String infoFromHost = new String(infoPacketArray);
        parseInfoHost(infoFromHost);

        multiSocket.leaveGroup(group);
        multiSocket.close();

    }

    public void service() throws IOException{

        DatagramSocket socket = new DatagramSocket();

        // send request
        final String request = buildRequest();

        final byte[] sbuf = request.getBytes();
        final InetAddress address = InetAddress.getByName(this.srvc_address);
        final DatagramPacket packet = new DatagramPacket(sbuf, sbuf.length, address, this.srvc_port);

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

    private String buildRequest() {

        String request = this.oper + " " + this.DNSName;

        if (this.oper == TypeOper.REGISTER) {

            request += " " + this.ipAddress;

        }

        return request;

    }

    private void parseInfoHost(String infoHost){

        String[] tokens = infoHost.split(" ", 2);

        if (tokens.length != 2) {

            System.out.println("ERROR IN RECEIVED MESSAGE");
            System.exit(-1);
        }

        this.srvc_address = tokens[0].trim();
        this.srvc_port = Integer.parseInt(tokens[1].trim());


    }

    public boolean setAttributes(String[] args) {

        this.mcast_addr = args[0];
        this.port = Integer.parseInt(args[1]);

        if (args[2].equals("register"))
            this.oper = TypeOper.REGISTER;
        else if (args[2].equals("lookup"))
            this.oper = TypeOper.LOOKUP;
        else {
            System.out.println("Usage: <oper> must be 'register' or 'lookup'");
            return false;
        }

        if (this.oper == TypeOper.REGISTER) {

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
