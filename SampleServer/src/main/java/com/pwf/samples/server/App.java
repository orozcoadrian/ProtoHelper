package com.pwf.samples.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println("Hello World!");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4444);
        } catch (IOException e) {
            System.out.println("Could not listen on port: 4444");
            System.exit(-1);
        }
        
        while (true) {
            Socket clientSocket = null;
            try {
                System.out.println("listening for connections...");
                clientSocket = serverSocket.accept();
                System.out.println("accepted a connection.");
            } catch (IOException e) {
                System.out.println("Accept failed: 4444");
                System.exit(-1);
            }

        }
    }
}
