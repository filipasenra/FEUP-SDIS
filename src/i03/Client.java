package i03;

import java.io.IOException;
import java.net.*;

// Second
// 
// compile files into folder destDir
// javac *.java -d destDir
// Call inside folder destDir
// java i03.Client localhost RemoteServer register ola 123
// or
// java i03.Client localhost RemoteServer lookup ola

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

            System.out.println("Usage: java Client <host_name> <remote_object_name> <oper> <opnd>");

            return false;
        }

        return clientHandler.setAttributes(args);

    }
}
