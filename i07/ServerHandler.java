package i07;

import java.io.IOException;
import java.net.*;
import java.rmi.Remote;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.net.ServerSocket;

public class ServerHandler {

    private HashMap<String, String> database = new HashMap<String, String>();

    static String NOT_REGISTER = "-1";
    static String NOT_FOUND = "NOT_FOUND";

    ServerSocket serverSocket;

    public ServerHandler(int srvc_port) throws IOException{

        serverSocket = new ServerSocket(srvc_port);
    }

    public void service() throws IOException {

        while (true) {
            //Receving Request from Client
            Socket clientSocket = serverSocket.accept();

            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));

            // display resquest
            String received = in.readLine();
            System.out.println("Server: " + received);

            // parse response
            String response = parseRequest(received);

            // sending response
            out.println(response);

        }

        // socket.close();

    }

    private String parseRequest(String received) {

        String response = "";

        String[] tokens = received.split(" ", 3);

        if (tokens.length != 2 && tokens.length != 3) {

            System.out.println("ERROR IN RECEIVED MESSAGE");
            System.exit(-1);
        }

        String oper = tokens[0];
        String DNSName = tokens[1].trim();

        if (oper.equals(TypeOper.REGISTER.name())) {

            return this.register(DNSName, oper);

        } else if (oper.equals(TypeOper.LOOKUP.name())) {

            return this.lookup(DNSName);

        } else {

            System.out.println("ERROR IN RECEIVED MESSAGE");
            System.exit(-1);
        }

        return response;

    }

    public String register(String DNSName, String IPAddress) {

        System.out.print("REGISTER : " + DNSName + " " + IPAddress + " : ");

        if (!database.containsKey(DNSName)) {

            database.put(DNSName, IPAddress);

            System.out.println(database.size());

            return Integer.toString(database.size());

        }

        System.out.println(NOT_REGISTER);
        return NOT_REGISTER;

    }

    public String lookup(String DNSName) {


        System.out.print("LOOKUP : " + DNSName + " : ");

        if (database.containsKey(DNSName)) {

            System.out.println(database.get(DNSName));
            return database.get(DNSName);

        }

        System.out.println(NOT_FOUND);
        return NOT_FOUND;

    }

}