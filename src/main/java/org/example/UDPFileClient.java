package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.UUID;

public class UDPFileClient {
    private static final int PORT = 9999;
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the path of the file to send: ");
        String originalFilePath = scanner.nextLine();

        String fileExtension = getFileExtension(originalFilePath);
        String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

        System.out.println("Generated unique filename: " + uniqueFilename);

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName("localhost");

            byte[] filenameBytes = uniqueFilename.getBytes();
            DatagramPacket filenamePacket = new DatagramPacket(filenameBytes, filenameBytes.length, address, PORT);
            socket.send(filenamePacket);

            try (InputStream fileInput = new FileInputStream(originalFilePath)) {
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead;
                while ((bytesRead = fileInput.read(buffer)) != -1) {
                    DatagramPacket packet = new DatagramPacket(buffer, bytesRead, address, PORT);
                    socket.send(packet);
                }

                byte[] eofSignal = "EOF".getBytes();
                DatagramPacket eofPacket = new DatagramPacket(eofSignal, eofSignal.length, address, PORT);
                socket.send(eofPacket);

                System.out.println("File transfer completed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    private static String getFileExtension(String filePath) {
        Path path = Paths.get(filePath);
        String fileName = path.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex);
        }
        return "";
    }
}
