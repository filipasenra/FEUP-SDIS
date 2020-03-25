package i07;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

// First!
//
// java Server 4444

public class Server {

    static int srvc_port;

    public static void main(String[] args) throws IOException {

        if (!parseArgs(args)) {
            return;
        }

        ServerHandler serverObj = new ServerHandler(srvc_port);
        serverObj.service();

    }

    private static boolean parseArgs(final String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java Server <srvc_port> ");
            return false;
        }

        srvc_port = Integer.parseInt(args[0]);

        return true;

    }
}