import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {
    public static void main(String[] args) {

        try(Socket socket = new Socket("localhost", 2000)){

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            Scanner scanner = new Scanner(System.in);
            String userInput = null;

            while (!"quit".equalsIgnoreCase(userInput)){
                try{
                    TimeUnit.MILLISECONDS.sleep(250);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                while(in.ready()) {
                    System.out.println(in.readLine());
                }
                // reading from user
                userInput = scanner.nextLine();

                //sending userInput to the server
                out.println(userInput);
            }
            out.println(userInput);
            scanner.close();
        }
        catch (IOException e){
            System.out.println("Error: Server is not running...");
        }

    }


}
