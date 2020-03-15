package i03;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;

import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

// First!
//
// compile files into folder destDir
// javac *.java -d destDir
// Call inside folder destDir
// rmiregistry &
// Followed by 
// java -Djava.rmi.server.codebase=file:./destDir/ i03.Server RemoteServer &

public class Server {

    static String remote_object_name;

    public static void main(String[] args) throws IOException {

        if (!parseArgs(args)) {
            return;
        }

        ServerHandler serverObj = new ServerHandler();
        try {

            RemoteInterface remoteObject = (RemoteInterface) UnicastRemoteObject.exportObject(serverObj, 0);
            Registry rmiReg = LocateRegistry.getRegistry();
            rmiReg.bind(remote_object_name, remoteObject);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static boolean parseArgs(final String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: java Server <remote_object_name> ");
            return false;
        }

        remote_object_name = args[0];

        return true;

    }
}