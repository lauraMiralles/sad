package src;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

class CustomChatServer extends NIOServer {
    private Map<SocketChannelWrapper, ByteBufferWrapper> clients = new HashMap<>();

    public CustomChatServer(int port) throws IOException {
        super(port);
    }

    @Override
    protected void onAccept(SelectionKey key) throws IOException {
        SocketChannelWrapper clientChannel = ((ServerSocketChannelWrapper) key.channel()).accept();
        clientChannel.configureBlocking(false);
        clientChannel.register(key.selector(), SelectionKey.OP_READ);
        clients.put(clientChannel, ByteBufferWrapper.allocate(1024));
    }

    @Override
    protected void onRead(SelectionKey key) throws IOException {
        SocketChannelWrapper clientChannel = (SocketChannelWrapper) key.channel();
        ByteBufferWrapper buffer = clients.get(clientChannel);
        int bytesRead = clientChannel.read(buffer);

        if (bytesRead == -1) {
            clients.remove(clientChannel);
            clientChannel.close();
            return;
        }

        String message = new String(buffer.array(), 0, bytesRead).trim();

        buffer.clear();

        for (SocketChannelWrapper channel : clients.keySet()) {
            if (channel != clientChannel) {
                buffer = ByteBufferWrapper.wrap(message.getBytes());
                while (buffer.hasRemaining()) {
                    channel.write(buffer);
                }
            }
        }
    }
}