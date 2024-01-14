import java.io.IOException;
import java.net.*;

public class CustomServerSocket {
    private ServerSocket customServerSocket;

    public CustomServerSocket(int customPort) throws IOException {
        customServerSocket = new ServerSocket(customPort);
    }

    public CustomSocket acceptConnection() throws IOException {
        return new CustomSocket(customServerSocket.accept());
    }

    public void shutdown() throws IOException {
        customServerSocket.close();
    }
}

