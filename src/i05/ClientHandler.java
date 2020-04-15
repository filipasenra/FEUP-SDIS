package i05;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ClientHandler {

    private String host_name; // hostname
    private int port_number;
    private String[] cypher_suite;

    private TypeOper oper;
    private String DNSName;
    private String ipAddress;

    public ClientHandler() {


        System.setProperty("javax.net.ssl.trustStoreType", "JKS");
        System.setProperty("javax.net.ssl.trustStore", "i05/truststore");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");
        System.setProperty("javax.net.ssl.keyStore", "i05/client.keys");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");

    }

    public void service() throws IOException {


        SSLSocket clientSocket = (SSLSocket) SSLSocketFactory.getDefault().createSocket(this.host_name, this.port_number);

        if(cypher_suite.length==0){
            SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            clientSocket.setEnabledCipherSuites(ssf.getDefaultCipherSuites());
        }
        else{
            clientSocket.setEnabledCipherSuites(cypher_suite);
        }

        SSLServerSocketFactory ssf = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        clientSocket.setEnabledCipherSuites(ssf.getDefaultCipherSuites());

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader( new InputStreamReader(clientSocket.getInputStream()));

        // send request
        final String request = buildRequest();
        out.println(request);


        final String received =  in.readLine();
        System.out.println("Client: " + received);

    }

    private String buildRequest() {

        String request = this.oper + " " + this.DNSName;

        if (this.oper == TypeOper.REGISTER) {

            request += " " + this.ipAddress;

        }

        return request;

    }

    public boolean setAttributes(String[] args) {

        this.host_name = args[0];
        this.port_number = Integer.parseInt(args[1]);

        if (args[2].equals("register"))
            this.oper = TypeOper.REGISTER;
        else if (args[2].equals("lookup"))
            this.oper = TypeOper.LOOKUP;
        else {
            System.out.println("Usage: <oper> must be 'register' or 'lookup'");
            return false;
        }

        if (this.oper == TypeOper.REGISTER) {

            if (args.length < 5) {
                System.out.println("Usage: Client <host_name> <port_number> register <DNS name> <IP address> <cypher_suite>*");
                return false;
            }

            DNSName = args[3];
            ipAddress = args[4];
            cypher_suite = Arrays.copyOf(args, args.length-5);

        } else {

            if (args.length < 4) {
                System.out.println("Usage: Client <host_name> <port_number> lookup <DNS name> <cypher_suite>*");
                return false;
            }

            DNSName = args[3];
            cypher_suite = Arrays.copyOf(args, args.length-4);

        }

        return true;

    }
}
