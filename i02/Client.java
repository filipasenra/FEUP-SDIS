package i02;

import java.io.IOException;
import java.net.*;

//Call outside of folder i02
//javac i02/Client.java 
//java i02/Client 224.0.0.1 1025 lookup ola 
// or 
//java i02/Client 224.0.0.1 1025 register ola 123

public class Client {

    private static ClientHandler clientHandler;

    public static void main(final String[] args) throws IOException {

        clientHandler = new ClientHandler();
        
        if (!parseArgs(args)) {
            return;
        }

        clientHandler.connectToServer();

        clientHandler.service();
    }

    private static boolean parseArgs(final String[] args){

        if (args.length < 3) {

            System.out.println("Usage: java client <mcast_addr> <mcast_port> <oper> <opnd>");

            return false;
        }

        return clientHandler.setAttributes(args);

    }
}
