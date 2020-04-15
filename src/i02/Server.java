package i02;

import java.io.IOException;
import java.net.*;
import java.util.HashMap;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

//Call outside of folder i02
// javac i02/EchoServer.java
// java i02/EchoServer 1024 224.0.0.1 1025

public class Server {

    public static ServerHandler serverHandler;

    public static void main(String[] args) throws IOException {

        serverHandler = new ServerHandler();

        if (!parseArgs(args)) {
            return;
        }

        serverHandler.setUpScheduledMessages();

        serverHandler.service();

        serverHandler.endScheduledMessages();


    }

    private static boolean parseArgs(final String[] args){

        
        if (args.length != 3) {
            System.out.println("Usage: java Server <srvc_port> <mcast_addr> <mcast_port> ");
            return false;
        }

        serverHandler.setSrvcPort(Integer.parseInt(args[0]));
        serverHandler.setMcastAddr(args[1]);
        serverHandler.setMcastPort(Integer.parseInt(args[2]));

        return true;

    }
}