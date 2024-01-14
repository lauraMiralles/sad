import java.io.*;
import java.net.*;

public class CustomSocket {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public CustomSocketConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        initializeStreams();
    }

    public CustomSocketConnection(Socket socket) throws IOException {
        this.socket = socket;
        initializeStreams();
    }

    private void initializeStreams() throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public String customReadLine() throws IOException {
        return reader.readLine();
    }

    public void customPrintln(String message) {
        writer.println(message);
    }

    public void closeCustomSocketConnection() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }
}

