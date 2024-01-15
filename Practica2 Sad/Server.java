import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static final int PORT = 12345;
    private static Map<String, MySocket> clientSockets = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        try {
            MyServerSocket serverSocket = new MyServerSocket(PORT);
            System.out.println("Server started on port " + PORT);

            while (true) {
                MySocket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(MySocket clientSocket) {
        try {
            String userName = clientSocket.readLine(); 
            clientSockets.put(userName, clientSocket);
            broadcastMessage("Server", userName + " has joined the chat.");

            String inputLine;
            while ((inputLine = clientSocket.readLine()) != null) {
                broadcastMessage(userName, inputLine);
            }

            // Cliente se desconecta
            clientSockets.remove(userName);
            broadcastMessage("Server", userName + " has left the chat.");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void broadcastMessage(String sender, String message) {
        for (MySocket socket : clientSockets.values()) {
            socket.println(sender + ": " + message);
        }
    }
}
