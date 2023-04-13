import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

class Server {

    ServerSocket server;
    Socket socket;
    BufferedReader bufRead;
    PrintWriter write;


    public Server() { // Creating Constructor
        try {
            server = new ServerSocket(7860);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting...");
            socket = server.accept();

            bufRead = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            write = new PrintWriter(socket.getOutputStream());

            startReading();
            startWriting();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*We have to simultaneously read and write the data. Here we will take the help of multithreading.
      Multithreading is a concept that will help us to create threads and run the actions together
     */
    public void startReading() {
        //thread -  will read the data consistently

        Runnable threadRead = () -> { //thread for reading code
            System.out.println("Reading Started ... ");
            try {
                while (true) {
                    String incomingMessage = null;

                    incomingMessage = bufRead.readLine();

                    if (incomingMessage.toLowerCase().equals("bye")) {
                        System.out.println("Client Left the chat");
                        socket.close();
                        break;
                    }

                    System.out.println("Client : " + incomingMessage);
                }

            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("Connection Terminated");
            }


        };

        new Thread(threadRead).start();
    }

    public void startWriting() {
        //thread - will take data from user and send it to client
        Runnable threadWrite = () -> { //thread for writing code
            System.out.println("Writer Started ...");
            try {
                while (!socket.isClosed()) {
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
        System.out.println("Hey from Server ... ");
        new Server();
    }
}