package ClientToClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private List<ClientHandler> clients;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ClientHandler(Socket clientSocket, List<ClientHandler> clients) {
        this.clientSocket = clientSocket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
            broadcast("New user connected");

            while (true) {
                byte[] buffer = new byte[1024];
                int bytesRead = inputStream.read(buffer);
                if (bytesRead == -1) {
                    break;
                }
                String message = new String(buffer, 0, bytesRead);
                broadcast(message);
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        } finally {
            closeConnection();
        }
    }

    private void broadcast(String message) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != this) {
                    try {
                        client.outputStream.write(message.getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void closeConnection() {
        try {
            clients.remove(this);
            clientSocket.close();
            broadcast("User disconnected");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
