package src;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;


public abstract class MyCustomNIOServer {
    private Selector customSelector;
    private ServerSocketChannelWrapper customServerSocketChannel;

    public MyCustomNIOServer(int customPort) throws IOException {
        customSelector = Selector.open();
        customServerSocketChannel = ServerSocketChannelWrapper.open();
        customServerSocketChannel.configureNonBlocking();
        customServerSocketChannel.socket().bind(new InetSocketAddress(customPort));
        customServerSocketChannel.register(customSelector, SelectionKey.OP_ACCEPT);
    }

    public void runCustomServer() {
        while (true) {
            try {
                customSelector.select();
                Iterator<SelectionKeyWrapper> customKeys = customSelector.selectedKeys().iterator();

                while (customKeys.hasNext()) {
                    SelectionKeyWrapper customKey = customKeys.next();
                    customKeys.remove();

                    if (!customKey.isValid()) {
                        continue;
                    }

                    if (customKey.isAcceptable()) {
                        handleAccept(customKey);
                    } else if (customKey.isReadable()) {
                        handleRead(customKey);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void handleAccept(SelectionKeyWrapper customKey) throws IOException;
    protected abstract void handleRead(SelectionKeyWrapper customKey) throws IOException;
}

