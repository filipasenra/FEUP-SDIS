package i05;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;

public class ServerHandler {

    private HashMap<String, String> database = new HashMap<>();

    static String NOT_REGISTER = "-1";
    static String NOT_FOUND = "NOT_FOUND";

    SSLServerSocket serverSocket;
    String[] cypher_suite;

    public ServerHandler(int srvc_port, String[] cypher_suite){


        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        System.setProperty("javax.net.ssl.trustStore", "i05/truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");
        System.setProperty("javax.net.ssl.keyStore", "i05/server.keys");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");


        this.cypher_suite = cypher_suite;

        try
        {
            SSLServerSocketFactory ssf = null;
            ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

            if(ssf == null)
                System.out.println("NULL");

            serverSocket = (SSLServerSocket) ssf.createServerSocket(srvc_port);

            System.out.println("OLA");

            if(cypher_suite.length==0){
                serverSocket.setEnabledCipherSuites(ssf.getDefaultCipherSuites());
            }
            else{
                serverSocket.setEnabledCipherSuites(cypher_suite);
            }
        }
        catch( IOException e)
        {
            System.out.println("Server - Failed to create SSLServerSocket");
            e.getMessage();
        }
    }

    public void service() throws IOException {

        while (true) {
            //Receving Request from Client
            SSLSocket clientSocket = (SSLSocket) serverSocket.accept();

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