import java.io.*;

public class Client {
    public static void main(String[] args) {
        try {
            MySocket socket = new MySocket("localhost", 12345);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

            
            System.out.print("Your Name: ");
            String userName = keyboard.readLine();
            socket.println(userName); // Enviar nombre al servidor

            Thread sender = new Thread(() -> {
                try {
                    String line;
                    while ((line = keyboard.readLine()) != null) {
                        socket.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Thread receiver = new Thread(() -> {
                try {
                    String line;
                    while ((line = socket.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            sender.start();
            receiver.start();

            sender.join();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
