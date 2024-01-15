import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChatServer extends GenericNIOServer {
    private Set<SocketChannel> clients = Collections.synchronizedSet(new HashSet<>());

    public ChatServer(int port, int bufferSize) throws IOException {
        super(port, bufferSize);
    }

    @Override
    protected void onClientConnected(SocketChannel client) {
        clients.add(client);
        System.out.println("Cliente conectado: " + client);
    }

    @Override
    protected void onClientDisconnected(SocketChannel client) {
        clients.remove(client);
        System.out.println("Cliente desconectado: " + client);
    }

    @Override
    protected void onClientDataReceived(SocketChannel client, byte[] data, int numRead) {
        String message = new String(data, 0, numRead, StandardCharsets.UTF_8);
        System.out.println("Mensaje recibido: " + message);
        broadcastMessage(client, message);
    }

    private void broadcastMessage(SocketChannel sender, String message) {
        for (SocketChannel client : clients) {
            if (client != sender) {
                try {
                    client.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            ChatServer server = new ChatServer(12345, 1024);
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
