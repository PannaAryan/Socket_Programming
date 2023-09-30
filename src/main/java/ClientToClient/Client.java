package ClientToClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("localhost", 5000);
            System.out.println("Connected to Server");

            new Thread(() -> {
                try {
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[1024];

                    while (true) {
                        int bytesRead = inputStream.read(buffer);
                        if (bytesRead == -1) {
                            break;
                        }
                        String message = new String(buffer, 0, bytesRead);
                        System.out.println("Received: " + message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            OutputStream outputStream = socket.getOutputStream();
            Scanner scanner = new Scanner(System.in);

            while (true) {
                String message = scanner.nextLine();
                outputStream.write(message.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
