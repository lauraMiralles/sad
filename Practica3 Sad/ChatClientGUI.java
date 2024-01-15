import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ChatClientGUI {
    private JFrame frame;
    private JTextArea messageArea;
    private JTextField inputField;
    private JList<String> userList;
    private DefaultListModel<String> userModel;

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public ChatClientGUI() {
        // Configuración de la ventana
        frame = new JFrame("Chat Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        // Área de mensajes
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        frame.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        // Campo de entrada
        inputField = new JTextField();
        frame.add(inputField, BorderLayout.SOUTH);
        inputField.addActionListener(e -> {
            sendMessage(inputField.getText());
            inputField.setText("");
        });

        // Lista de usuarios
        userModel = new DefaultListModel<>();
        userList = new JList<>(userModel);
        frame.add(new JScrollPane(userList), BorderLayout.EAST);

        // Configuración de la red
        setupNetworking();

        // Inicio del hilo que escucha los mensajes del servidor
        Thread listenerThread = new Thread(new Listener());
        listenerThread.start();

        // Mostrar la ventana
        frame.setVisible(true);
    }

    private void setupNetworking() {
        try {
            
            socket = new Socket("localhost", 12345);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();   
        }
    }

    private void sendMessage(String message) {
        out.println(message);
        messageArea.append("Yo: " + message + "\n");
    }

    private class Listener implements Runnable {
        public void run() {
            try {
                String message;
                while ((message = in.readLine()) != null) {
                    String finalMessage = message; 
                    SwingUtilities.invokeLater(() -> {
                        messageArea.append(finalMessage + "\n");
                    });
                }
            } catch (IOException e) {

            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ChatClientGUI::new);
    }
}
