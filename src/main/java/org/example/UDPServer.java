package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class UDPServer {
    private static final int PORT = 9999;
    private static final int BUFFER_SIZE = 4096; // Increased buffer size

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT);
             Scanner scanner = new Scanner(System.in)) {

            byte[] buffer = new byte[BUFFER_SIZE];
            System.out.println("Server is running on port " + PORT + "...");

            while (true) {
                // Receive message from client
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Client: " + received);

                // Read user input and send response
                System.out.print("Server: ");
                String response = scanner.nextLine();
                DatagramPacket responsePacket = new DatagramPacket(response.getBytes(), response.length(), packet.getAddress(), packet.getPort());
                socket.send(responsePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
