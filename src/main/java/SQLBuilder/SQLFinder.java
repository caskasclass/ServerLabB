package SQLBuilder;

import java.sql.*;

/**
 *
 * @author lorenzo
 */
public class SQLFinder implements SQLFinderInterface {

    private String select; // contiene la clausola select
    private String from; // contiene la clausola from
    private String where; // contiene la clausola where
    private Connection conn; // memorizza la connessione
    private ResultSet res; // risultati della query

    // costruttore in caso di connessioni specifiche
    public SQLFinder(String url, String username, String psw) {
        try {
            this.conn = DriverManager.getConnection(url, username, psw);
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            return;
        }
        // settaggio della query
        this.select = "SELECT ?\n";
        this.from = "FROM ?\n";
        this.where = "WHERE ?\n";
        this.res = null;
    }

    // costruttore in caso della connessione standard
    public SQLFinder() {
        try {
            this.conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/EmotionalSongs", "postgres",
                    "5640");
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            return;
        }
        // settaggio della query
        this.select = "SELECT ?\n";
        this.from = "FROM ?\n";
        this.where = "WHERE ?\n";
        this.res = null;
    }

    // settaggio dell'a query con solamente select e from
    @Override
    public void setQuery(String select, String from) {
        this.select = this.select.replace("?", select);
        this.from = this.from.replace("?", from);
        this.where = "";
        System.out.println(this.getQuery());
    }

    // settaggio dell'a query con tutti i parametri insieme
    @Override
    public void setQuery(String select, String from, String where) {
        this.select = this.select.replace("?", select);
        this.from = this.from.replace("?", from);
        this.where = this.where.replace("?", where);
    }

    // metodo per il rinnovamento della query
    @Override
    public void renewQuery() {
        this.select = "SELECT ?\n";
        this.from = "FROM ?\n";
        this.where = "WHERE ?\n";
    }

    // metodo per l'esecuzione della query
    @Override
    public ResultSet executeQuery() {
        PreparedStatement ps;
        try {
            ps = this.conn.prepareStatement(select + from + where); // costruzione della query assemblando le clausole
            this.res = ps.executeQuery(); // esecuzione della query
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return res;
    }

    // metodo per rinnovare i risultati della query
    @Override
    public void renewResultSet() {
        this.res = null;
    }

    // settaggio delle singole clausole
    @Override
    public void setSelect(String select) {
        this.select = this.select.replace("?", select);
    }

    @Override
    public void setFrom(String from) {
        this.from = this.from.replace("?", from);
    }

    @Override
    public void setWhere(String where) {
        this.where = this.where.replace("?", where);
    }

    // getter
    @Override
    public ResultSet getRes() {
        return res;
    }

    public String getQuery() {
        return this.select + this.from + this.where;
    }

}
