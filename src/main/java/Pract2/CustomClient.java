import java.io.*;

public class CustomClient {
    public static void main(String[] args) throws IOException {
        CustomSocket customSocket = new CustomSocket("localhost", 1234);
        BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Enter your name: ");
        String username = userInput.readLine();
        customSocket.sendMessage(username);

        Thread inputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String line;
                try {
                    while ((line = userInput.readLine()) != null) {
                        customSocket.sendMessage(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread outputThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String line;
                try {
                    while ((line = customSocket.receiveMessage()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        inputThread.start();
        outputThread.start();
    }
}

