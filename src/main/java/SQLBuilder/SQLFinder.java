package SQLBuilder;

import java.sql.*;

import Server.ConnectionPool;

/**
 *
 * @author lorenzo
 */

/**
 * Progetto laboratorio B: "Emotional Songs", anno 2022-2023
 * 
 * @author Beatrice Bastianello, matricola 751864, VA
 * @author Lorenzo Barbieri , matricola 748695, VA
 * @author Filippo Storti , matricola 749195, VA
 * @author Nazar Viytyuk, matricola 748964, VA
 * @version 1.0
 */
/**
 * Classe che modella oggetti in grado di eseguire query per ricercare
 * informazioni all'interno del database.
 * 
 */
public class SQLFinder implements SQLFinderInterface {

    private String select; // Contiene la clausola select
    private String from; // Contiene la clausola from
    private String where; // Contiene la clausola where
    private Connection conn; // Memorizza la connessione al database
    private ResultSet res; // Memorizza i risultati della query

    /**
     * Costruttore per connessioni specifiche al database.
     * 
     * @param url      URL del database
     * @param username Nome utente per la connessione al database
     * @param psw      Password per la connessione al database
     */
    public SQLFinder(String url, String username, String psw) {
        try {
            this.conn = DriverManager.getConnection(url, username, psw);
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            return;
        }
        // Settaggio della query di base
        this.select = "SELECT ?\n";
        this.from = "FROM ?\n";
        this.where = "WHERE ?\n";
        this.res = null;
    }

    /**
     * Costruttore per la connessione standard al database.
     * Utilizza un ciclo per tentare la connessione fino a quando non riesce.
     */
    public SQLFinder() {
        // Settaggio della query di base
        this.select = "SELECT ?\n";
        this.from = "FROM ?\n";
        this.where = "WHERE ?\n";
        this.res = null;
    }

    /**
     * Imposta la query con clausole SELECT e FROM.
     * 
     * @param select Stringa contenente la clausola SELECT
     * @param from   Stringa contenente la clausola FROM
     */
    @Override
    public void setQuery(String select, String from) {
        this.select = this.select.replace("?", select);
        this.from = this.from.replace("?", from);
        this.where = ""; // Rimozione della clausola WHERE
        System.out.println(this.getQuery());
    }

    /**
     * Imposta la query con clausole SELECT, FROM e WHERE.
     * 
     * @param select Stringa contenente la clausola SELECT
     * @param from   Stringa contenente la clausola FROM
     * @param where  Stringa contenente la clausola WHERE
     */
    @Override
    public void setQuery(String select, String from, String where) {
        this.select = this.select.replace("?", select);
        this.from = this.from.replace("?", from);
        this.where = this.where.replace("?", where);
    }

    /**
     * Aggiorna la query alle clausole di base.
     */
    @Override
    public void renewQuery() {
        this.select = "SELECT ?\n";
        this.from = "FROM ?\n";
        this.where = "WHERE ?\n";
    }

    // metodo per l'esecuzione della query
    /**
     * Esegue la query e restituisce i risultati.
     * 
     * @return ResultSet contenente i risultati della query
     */
    @Override
    public ResultSet executeQuery() {
        PreparedStatement ps;

        // controllo di avvenuta connessione
        do{ // cicla finchè non si connette
            this.conn = ConnectionPool.getConnection();
        }while(this.conn==null);
    
        try {
            ps = this.conn.prepareStatement(select + from + where); // Costruzione della query
            this.res = ps.executeQuery(); // Esecuzione della query
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return res;
    }

    /**
     * Aggiorna il ResultSet a null.
     */
    @Override
    public void renewResultSet() {
        this.res = null;
    }

    /**
     * Imposta la clausola SELECT della query.
     * 
     * @param select Stringa contenente la clausola SELECT
     */
    @Override
    public void setSelect(String select) {
        this.select = this.select.replace("?", select);
    }

    /**
     * Imposta la clausola FROM della query.
     * 
     * @param from Stringa contenente la clausola FROM
     */
    @Override
    public void setFrom(String from) {
        this.from = this.from.replace("?", from);
    }

    /**
     * Imposta la clausola WHERE della query.
     * 
     * @param where Stringa contenente la clausola WHERE
     */
    @Override
    public void setWhere(String where) {
        this.where = this.where.replace("?", where);
    }

    /**
     * Restituisce i risultati della query.
     * 
     * @return ResultSet contenente i risultati della query
     */
    @Override
    public ResultSet getRes() {
        return res;
    }

    /**
     * Restituisce la query completa.
     * 
     * @return Stringa contenente la query completa
     */
    @Override
    public String getQuery() {
        return this.select + this.from + this.where;
    }

    @Override
    public void releaseConnection() {
    if(this.conn!=null)
       ConnectionPool.releaseConnection(this.conn);
    }

}
