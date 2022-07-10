import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {

        try(ServerSocket server = new ServerSocket(2000)){


            while(true){
                System.out.println("Server is listening on port 2000...");
                Socket client = server.accept();

                System.out.println("Connection established with" + client);

                new ServerThread(client).start();
            }

        }catch (IOException e){
            System.out.println("Client disconnected...");
        }
    }
}
