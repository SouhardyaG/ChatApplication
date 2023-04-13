import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;

public class Client {

    Socket socket;
    BufferedReader read;
    PrintWriter write;


    //Constructor
    public Client() {
        try {
            System.out.println("Sending Request to Server...");
            socket = new Socket("127.0.0.1", 7860);
            System.out.println("Connection Done.");

            read = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            write = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //start Reading
    public void startReading() {
        //thread -  will read the data consistently

        Runnable threadRead = () -> { //thread for reading code
            System.out.println("Reading Started ... ");
            try {
                while (true) {

                    String incomingMessage = null;
                    incomingMessage = read.readLine();

                    if (incomingMessage.toLowerCase().equals("bye")) {
                        System.out.println("Server Left the chat");
                        socket.close();
                        break;
                    }

                    System.out.println("Server : " + incomingMessage);
                }

            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Connection Terminated");
            }

        };
        new Thread(threadRead).start();
    }


    //start Writing
    public void startWriting() {
        //thread - will take data from user and send it to client
        Runnable threadWrite = () -> { //thread for writing code
            System.out.println("Writer Started ...");
            try {
                while (true) {
                    //System.out.print("Me : ");
                    BufferedReader consoleMessage = new BufferedReader(new InputStreamReader(System.in));
                    String outgoingMessage = consoleMessage.readLine();
                    write.println(outgoingMessage);
                    write.flush();

                    if (outgoingMessage.toLowerCase().equals("bye")) {
                        socket.close();
                        break;
                    }
                }
                //System.out.println("Connection Terminated");
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Connection Terminated");
            }
        };
        new Thread(threadWrite).start();
    }


    public static void main(String[] args) {
        System.out.println("Hello from Client ...");
        new Client();
    }
}