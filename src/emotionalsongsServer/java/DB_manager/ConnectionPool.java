package DB_manager;
import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariDataSource;

import classes.RequestElaboration;


public class ConnectionPool {
    private static HikariDataSource dataSource;
    private static final int POOLSIZE = 5;

    public static void initialize() {
        // Inizializza il pool di connessioni
        dataSource = new HikariDataSource();
        dataSource.setPoolName(RequestElaboration.DEAFAULT_NAME);
        dataSource.setMaximumPoolSize(POOLSIZE);
        dataSource.setConnectionTimeout(5000);
        
        // Configura il pool di connessioni
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/emotionalsongs");
        dataSource.setUsername("caska");
        dataSource.setPassword("");
    }

    public synchronized static Connection getConnection(){
        //ottengo la connessione dal poll 
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL Exception  :"+e.getSQLState());
        }
        return null;
    }

    
    public synchronized static void releaseConnection(Connection con){
        //rialscio la connessione una volta che l'ho utilizzata 
        if(con != null){
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("SQL Exception  :"+e.getSQLState()); 
            }
        }
    }

    public static void shutdown(){
        //chiudo il poll, alla chiusura dell'applicazione 
        if(dataSource != null){
            dataSource.close();
        }
    }

    
}
