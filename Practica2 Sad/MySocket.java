import java.io.*;
import java.net.*;

public class MySocket {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public MySocket(String address, int port) throws IOException {
        this.socket = new Socket(address, port);
        setupStreams();
    }

    public MySocket(Socket socket) throws IOException {
        this.socket = socket;
        setupStreams();
    }

    private void setupStreams() throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public String readLine() throws IOException {
        return reader.readLine();
    }

    public void println(String line) {
        writer.println(line);
    }

    public void close() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }
}
