import java.io.IOException;
import java.net.*;

enum TypeOper {
    REGISTER, LOOKUP
}

public class EchoClient {

    private static String host;
    private static String port;
    private static TypeOper oper;
    private static String DNSName;
    private static String ipAddress;

    public static void main(String[] args) throws IOException {
        if (!parseArgs(args)) {
            return;
        }

        // send request
        DatagramSocket socket = new DatagramSocket();
        int port = Integer.parseInt(args[1]);

        String request = buildRequest();

        byte[] sbuf = request.getBytes();
        InetAddress address = InetAddress.getByName(args[0]);
        DatagramPacket packet = new DatagramPacket(sbuf, sbuf.length, address, port);

        socket.send(packet);


        //receiving response
        byte[] rbuf = new byte[256];
        DatagramPacket responsePacket = new DatagramPacket(rbuf, rbuf.length);
        socket.receive(responsePacket);

        // display response
        String received = new String(responsePacket.getData());
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

    private static boolean parseArgs(String[] args) {

        if (args.length < 3) {

            System.out.println("Usage: java Client <host> <port> <oper> <opnd>");

            return false;
        }

        host = args[0];
        port = args[1];

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
