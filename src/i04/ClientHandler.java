package i04;

import java.io.IOException;
import java.net.*;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStreamReader;

public class ClientHandler {

    private String host_name; // hostname
    private int port_number;

    private TypeOper oper;
    private String DNSName;
    private String ipAddress;

    public ClientHandler() {
    }

    public void service() throws IOException {


        Socket clientSocket = new Socket(this.host_name, this.port_number);
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

            if (args.length != 5) {
                System.out.println("Usage: Client <host_name> <port_number> register <DNS name> <IP address>");
                return false;
            }

            DNSName = args[3];
            ipAddress = args[4];

        } else {

            if (args.length != 4) {
                System.out.println("Usage: Client <host_name> <port_number> lookup <DNS name>");
                return false;
            }

            DNSName = args[3];

        }

        return true;

    }
}
