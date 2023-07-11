import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import DB_manager.ConnectionPool;
import classes.ServerSkeleton;

public class Server {

    ServerSocket server;
    Socket client;
    boolean end = false;

    public Server(){
        ConnectionPool.initialize();
        try {
            server = new ServerSocket(8080);
        } catch (IOException e) {
            System.err.println("Server failure.");
            e.printStackTrace();
        }//inserire la port in interfaccia 
        //inizializzare un pool di 15 con.

    }

    public static void main(String[] args) {
        new Server().exec();
    }

    public void exec() {
        try{
            while (true) {
                    System.out.println("Server in ascolto sulla porta 8080");
                    client = server.accept();
                    System.out.println("Connessione stabilita con il client"+client);
                    new Thread(new ServerSkeleton(client)).start();
                    if(end){
                        break;
                    }
            }
        }catch(IOException e){
            System.err.println("Err 1");
            e.printStackTrace();
        }

    }
}
