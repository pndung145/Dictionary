package com.example.settings;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class InternetConnect {
    public static boolean isInternetAvailable() {
        try {
            int timeoutMs = 1500; // Timeout in milliseconds
            Socket socket = new Socket();
            InetSocketAddress socketAddress = new InetSocketAddress("www.google.com", 80);
            socket.connect(socketAddress, timeoutMs);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        if (isInternetAvailable()) {
            System.out.println("Internet is connected!");
        } else {
            System.out.println("No internet connection.");
        }
    }
}




