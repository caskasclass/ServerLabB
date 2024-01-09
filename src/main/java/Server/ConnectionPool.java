package Server;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * La classe ConnectionPool gestisce un pool di connessioni al database
 * utilizzando HikariCP.
 * Il pool viene inizializzato con le impostazioni specificate nel metodo
 * {@link #initialize()}.
 * Fornisce metodi per ottenere connessioni dal pool ({@link #getConnection()}),
 * rilasciare connessioni
 * ({@link #releaseConnection(Connection)}), e chiudere il pool alla chiusura
 * dell'applicazione ({@link #shutdown()}).
 *
 * Utilizza la libreria HikariCP per configurare e gestire il pool di
 * connessioni.
 * La configurazione prevede la specifica del JDBC URL, username, password,
 * dimensione massima del pool, timeout
 * di connessione, query di test di connessione, nome del pool, e la
 * registrazione di MBeans per il monitoraggio.
 */

public class ConnectionPool {
    private static HikariDataSource dataSource;
    private static final int POOLSIZE = 100;

    /**
     * Inizializza il pool di connessioni con le impostazioni specificate.
     */

    public static void initialize() {
        // Inizializza il pool di connessioni
        // Configura il pool di connessioni
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/EmotionalSongs");
        config.setUsername("postgres");
        config.setPassword("5640");
        config.setMaximumPoolSize(POOLSIZE);
        config.setConnectionTimeout(5000);
        config.setConnectionTestQuery("SELECT 1");
        config.setPoolName("EmotionalSongsPool");
        config.setRegisterMbeans(true);

        dataSource = new HikariDataSource(config);
    }

    /**
     * Ottiene una connessione dal pool.
     *
     * @return Connessione al database.
     */

    public synchronized static Connection getConnection() {
        // ottengo la connessione dal poll
        try {
            // stampo il numero di connessioni attive
            System.out.println("Active Connections: " + (dataSource.getHikariPoolMXBean().getActiveConnections() + 1));
            return dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("SQL Exception  :" + e.getSQLState());
        }
        return null;
    }

    /**
     * Rilascia una connessione precedentemente ottenuta dal pool.
     *
     * @param con Connessione da rilasciare.
     */

    public synchronized static void releaseConnection(Connection con) {
        // rialscio la connessione una volta che l'ho utilizzata
        if (con != null) {
            try {
                con.close();
                System.out.println("Connection released");
                System.out.println("Active Connections: " + dataSource.getHikariPoolMXBean().getActiveConnections());
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("SQL Exception  :" + e.getSQLState());
            }
        }
    }

    /**
     * Chiude il pool di connessioni alla chiusura dell'applicazione.
     */

    public static void shutdown() {
        // chiudo il poll, alla chiusura dell'applicazione
        if (dataSource != null) {
            dataSource.close();
        }
    }

}