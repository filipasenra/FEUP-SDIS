package i04;

import java.io.IOException;

// Second
// 
// java Client localhost 4444 lookup ola

public class Client {

    private static ClientHandler clientHandler;

    public static void main(final String[] args) throws IOException {

        clientHandler = new ClientHandler();
        
        if (!parseArgs(args)) {
            return;
        }

        clientHandler.service();
    }

    private static boolean parseArgs(final String[] args){

        if (args.length < 3) {
            System.out.println("Usage: java Client <host_name> <port_number> <oper> <opnd>");

            return false;
        }

        return clientHandler.setAttributes(args);

    }
}
