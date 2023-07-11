package classes;

import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;

import DB_manager.ConnectionPool;

public class ServerSkeleton extends Thread implements RequestElaboration {
    Socket client;

    public ServerSkeleton(Socket client) {
        this.client = client;
    }

    public void run() {
        System.out.println("Prova connessione al db da pool;");
        Connection con = ConnectionPool.getConnection();
        System.out.println("Connessione al db stabilita : "+con);
        System.out.println("Rilascio la connessione...");
        ConnectionPool.releaseConnection(con);
        System.out.println("Connessione rilasciata con successo.");
        try {
            client.close();
        } catch (IOException e) {
            System.err.println("Err 2");
            e.printStackTrace();
        }
        
    }

}
