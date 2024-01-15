import java.nio.channels.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.*;

public abstract class GenericNIOServer {
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private ByteBuffer buffer;

    public GenericNIOServer(int port, int bufferSize) throws IOException {
        serverChannel = ServerSocketChannel.open();
        serverChannel.socket().bind(new InetSocketAddress(port));
        serverChannel.configureBlocking(false);
        selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        buffer = ByteBuffer.allocate(bufferSize);
    }

    public void startServer() throws IOException {
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectedKeys.iterator();

            while (iter.hasNext()) {
                SelectionKey key = iter.next();

                if (key.isAcceptable()) {
                    handleAccept(serverChannel, key);
                }

                if (key.isReadable()) {
                    handleRead(key);
                }

                iter.remove();
            }
        }
    }

    private void handleAccept(ServerSocketChannel serverChannel, SelectionKey key) throws IOException {
        SocketChannel client = serverChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        onClientConnected(client);
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        buffer.clear();
        int numRead = client.read(buffer);

        if (numRead == -1) {
            onClientDisconnected(client);
            client.close();
            key.cancel();
            return;
        }

        onClientDataReceived(client, buffer.array(), numRead);
    }

    protected abstract void onClientConnected(SocketChannel client);
    protected abstract void onClientDisconnected(SocketChannel client);
    protected abstract void onClientDataReceived(SocketChannel client, byte[] data, int numRead);

}
