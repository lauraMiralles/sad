package src;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

import javax.swing.SwingWorker;

class CustomChatClient {
    private SocketChannelWrapper socketChannel;
    private CustomChatWindow chatWindow;

    public CustomChatClient(String host, int port, CustomChatWindow chatWindow) throws IOException {
        socketChannel = new SocketChannelWrapper(host, port);
        this.chatWindow = chatWindow;
    }

    public void sendMessage(String message) throws IOException {
        ByteBufferWrapper buffer = ByteBufferWrapper.wrap(message.getBytes());
        socketChannel.write(buffer);
    }

    public void startReading() {
        new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() throws Exception {
                ByteBufferWrapper buffer = ByteBufferWrapper.allocate(1024);

                while (true) {
                    buffer.clear();
                    int bytesRead = socketChannel.read(buffer);

                    if (bytesRead > 0) {
                        buffer.flip();
                        byte[] bytes = new byte[bytesRead];
                        buffer.get(bytes);
                        String message = new String(bytes);
                        publish(message);
                    }
                }
            }

            @Override
            protected void process(List<String> messages) {
                for (String message : messages) {
                    chatWindow.addMessage(message);
                }
            }
        }.execute();
    }
}
