package BiDirectionalMessageShareDemo.Server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        ExecutorService threadPool = Executors.newFixedThreadPool(10); // Create a thread pool with 10 threads (adjust as needed)

        try {
            serverSocket = new ServerSocket(5000);
            System.out.println("Server Started");
            
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("Client Added");
                
                // Submit tasks to the thread pool to handle communication with clients
                threadPool.submit(new ReadHandler(client));
                threadPool.submit(new WriteHandler(client));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (serverSocket != null)
                    serverSocket.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
