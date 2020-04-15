package i05;

import java.io.IOException;
import java.util.Arrays;

// First!
//
// java i05/SSLServer 4444
// Example of cyphers: SSL_RSA_WITH_RC4_128_MD5 | SSL_RSA_WITH_RC4_128_SHA | TLS_RSA_WITH_AES_128_CBC_SHA | TLS_DHE_RSA_WITH_AES_128_CBC_SHA


public class SSLServer {

    static int srvc_port;
    static String[] cypher_suite;

    public static void main(String[] args) throws IOException {

        if (!parseArgs(args)) {
            return;
        }

        ServerHandler serverObj = new ServerHandler(srvc_port, cypher_suite);
        serverObj.service();

    }

    private static boolean parseArgs(final String[] args) {

        if (args.length < 1) {
            System.out.println("Usage: java Server <srvc_port> <cypher_suite>* ");
            return false;
        }

        srvc_port = Integer.parseInt(args[0]);
        cypher_suite = Arrays.copyOf(args, args.length-1);

        return true;

    }
}