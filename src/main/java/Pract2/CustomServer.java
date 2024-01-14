import java.io.IOException;
import java.util.concurrent.*;

public class CustomServer {
    private static final ConcurrentHashMap<String, CustomSocket> clients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        CustomServerSocket customServerSocket = new CustomServerSocket(1234);

        while (true) {
            final CustomSocket customSocket = customServerSocket.acceptConnection();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    handleCustomClient(customSocket);
                }
            }).start();
        }
    }

    private static void handleCustomClient(CustomSocket customSocket) {
        try {
            String username = customSocket.customReadLine();
            clients.put(username, customConnection);

            String line;
            while ((line = customSocket.customReadLine()) != null) {
                broadcastCustomMessage(username, line);
            }

            clients.remove(username);
            customSocket.closeCustomSocketConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void broadcastCustomMessage(String sender, String message) {
        for (CustomSocket customSocket : clients.values()) {
            customSocket.customPrintln("[" + sender + "]: " + message);
        }
    }
}
