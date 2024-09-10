package org.example;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.StringTokenizer;

public class UDPServer {
    private static final int PORT = 9999;
    private static final int BUFFER_SIZE = 4096;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {

            byte[] buffer = new byte[BUFFER_SIZE];
            System.out.println("Server is running on port " + PORT + "...");

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Client: " + received);

                StringTokenizer st = new StringTokenizer(received);
                String response;

                if (st.countTokens() != 3) {
                    response = "Invalid format. Expected format: <number1> <operator> <number2>";
                } else {
                    try {
                        int firstOp = Integer.parseInt(st.nextToken());
                        String operation = st.nextToken();
                        int secondOp = Integer.parseInt(st.nextToken());

                        int result = 0;
                        switch (operation) {
                            case "+":
                                result = firstOp + secondOp;
                                break;
                            case "-":
                                result = firstOp - secondOp;
                                break;
                            case "*":
                                result = firstOp * secondOp;
                                break;
                            case "/":
                                if (secondOp == 0) {
                                    response = "Error: Division by zero";
                                    break;
                                }
                                result = firstOp / secondOp;
                                break;
                            default:
                                response = "Invalid operator. Supported operators: +, -, *, /";
                                break;
                        }
                        response = "Result: " + result;
                    } catch (NumberFormatException e) {
                        response = "Error: Invalid number format.";
                    } catch (Exception e) {
                        response = "Error: An unexpected error occurred.";
                    }
                }

                DatagramPacket responsePacket = new DatagramPacket(response.getBytes(), response.length(), packet.getAddress(), packet.getPort());
                socket.send(responsePacket);

                System.out.println("Server: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
