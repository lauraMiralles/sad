

package src;

import java.io.IOException;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        new Thread(() -> {
            try {
                new CustomChatServer(1234).runServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        SwingUtilities.invokeLater(() -> {
            try {
                CustomChatWindow chatWindow = new CustomChatWindow();
                CustomChatClient chatClient = new CustomChatClient("localhost", 1234, chatWindow);
                chatClient.startReading();

                chatWindow.getInputField().addActionListener(e -> {
                    String message = chatWindow.getInputField().getText();
                    chatWindow.getInputField().setText("");
                    try {
                        chatClient.sendMessage(message);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}



