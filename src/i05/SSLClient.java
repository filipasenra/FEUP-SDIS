package i05;

import java.io.IOException;
import java.net.*;

// Second
// 
// java i05/SSLClient localhost 4444 lookup ola
// Example of cyphers: SSL_RSA_WITH_RC4_128_MD5 | SSL_RSA_WITH_RC4_128_SHA | TLS_RSA_WITH_AES_128_CBC_SHA | TLS_DHE_RSA_WITH_AES_128_CBC_SHA

public class SSLClient {

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
            System.out.println("Usage: java Client <host_name> <port_number> <oper> <opnd> <cypher_suite>*");

            return false;
        }

        return clientHandler.setAttributes(args);

    }
}
