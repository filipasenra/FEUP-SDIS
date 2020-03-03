package i02;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

//Call outside of folder i02
// javac i02/EchoServer.java
// java i02/EchoServer 1024 224.0.0.1 1025

public class EchoServer {

    private static int srvc_port;
    private static String mcast_addr;
    private static int mcast_port;

    private static HashMap<String, String> database = new HashMap<String, String>();

    static String NOT_REGISTER = "-1";
    static String NOT_FIND = "NOT_FOUND";

    public static void main(String[] args) throws IOException {

        if (!parseArgs(args)) {
            return;
        }

        String message = "localhost " + srvc_port;
        final ScheduledThreadPoolExecutor scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
        final ScheduledFuture<?> advertiser = scheduler.scheduleAtFixedRate(
                new SenderOfMessages(srvc_port, "localhost", mcast_addr, mcast_port, message), 2, 1, TimeUnit.SECONDS);

        service();

        advertiser.cancel(true);
        scheduler.shutdown();

    }

    public static void service() throws IOException {

        // send request
        DatagramSocket socket = new DatagramSocket(srvc_port);

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
        String DNSName = tokens[1].trim();

        if (oper.equals(TypeOper.REGISTER.name())) {

            if (!database.containsKey(DNSName)) {

                database.put(DNSName, tokens[2]);
                response = Integer.toString(database.size());

            } else {
                response = NOT_REGISTER;
            }

        } else if (oper.equals(TypeOper.LOOKUP.name())) {

            if (database.containsKey(DNSName)) {

                response = database.get(DNSName);

            } else {
                response = NOT_FIND;
            }

        } else {

            System.out.println("ERROR IN RECEIVED MESSAGE");
            System.exit(-1);
        }

        return response;

    }

    private static boolean parseArgs(String[] args) {

        if (args.length != 3) {
            System.out.println("Usage: java Server <srvc_port> <mcast_addr> <mcast_port> ");
            return false;
        }

        srvc_port = Integer.parseInt(args[0]);
        mcast_addr = args[1];
        mcast_port = Integer.parseInt(args[2]);

        return true;

    }

    static class SenderOfMessages implements Runnable {

        private int srvc_port;
        private String mcast_addr;
        private int mcast_port;
        private String message;
        private String srvc_addr;

        public SenderOfMessages(int srvc_port, String srvc_addr, String mcast_addr, int mcast_port, String message)
                throws UnknownHostException {

            this.srvc_port = srvc_port;
            this.mcast_addr = mcast_addr;
            this.mcast_port = mcast_port;
            this.message = message;
            this.srvc_addr = srvc_addr;
        }

        public void run() {

            try {

                InetAddress addr = InetAddress.getByName(this.mcast_addr);
                DatagramSocket serverSocket = new DatagramSocket();
                DatagramPacket msgPacket = new DatagramPacket(this.message.getBytes(), this.message.getBytes().length,
                        addr, this.mcast_port);

                serverSocket.send(msgPacket);

                System.out.println("multicast: " + this.mcast_addr + " " + this.mcast_port + ": " + this.srvc_addr + " "
                        + this.srvc_port);

                serverSocket.close();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }

    }
}