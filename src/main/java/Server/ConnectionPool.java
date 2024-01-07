package Server;
import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;



public class ConnectionPool {
    private static HikariDataSource dataSource;
    private static final int POOLSIZE = 100;

    public static void initialize() {
        // Inizializza il pool di connessioni
        // Configura il pool di connessioni
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/LabDB");
        config.setUsername("postgres");
        config.setPassword("postgres");
        config.setMaximumPoolSize(POOLSIZE);
        config.setConnectionTimeout(5000);
        config.setConnectionTestQuery("SELECT 1");
        config.setPoolName("EmotionalSongsPool");
        config.setRegisterMbeans(true);

        dataSource = new HikariDataSource(config);
    }

    public synchronized static Connection getConnection(){
        //ottengo la connessione dal poll 
        try {
            //stampo il numero di connessioni attive
            System.out.println("Active Connections: "+(dataSource.getHikariPoolMXBean().getActiveConnections()+1));
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
                System.out.println("Connection released");
                System.out.println("Active Connections: "+dataSource.getHikariPoolMXBean().getActiveConnections());
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