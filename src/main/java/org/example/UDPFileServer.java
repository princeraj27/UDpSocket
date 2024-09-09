package org.example;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPFileServer {
    private static final int PORT = 9999;
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            System.out.println("File Server is running on port " + PORT + "...");

            DatagramPacket filenamePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(filenamePacket);
            String filename = new String(filenamePacket.getData(), 0, filenamePacket.getLength()).trim();
            System.out.println("Receiving file: " + filename);

            try (OutputStream fileOutput = new FileOutputStream(filename)) {
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);

                    String data = new String(packet.getData(), 0, packet.getLength()).trim();
                    if (data.equals("EOF")) {
                        System.out.println("File transfer completed.");
                        break;
                    }

                    fileOutput.write(packet.getData(), 0, packet.getLength());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
