package SQLBuilder;

import java.sql.*;

/**
 *
 * @author lorenzo
 */
public class SQLFinder implements SQLFinderInterface {

    private String select;
    private String from;
    private String where;
    private Connection conn;
    private ResultSet res;

    public SQLFinder(String url, String username, String psw) {
        try {
            this.conn = DriverManager.getConnection(url, username, psw);
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            return;
        }
        this.select = "SELECT ?\n";
        this.from = "FROM ?\n";
        this.where = "WHERE ?\n";
        this.res = null;
        System.out.println("Database connection successfull");
    }

    public SQLFinder() {
        try {
            this.conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/EmotionalSongs", "postgres", "5640");
        } catch (SQLException e) {
            System.err.println("Database connection failed");
        }
        this.select = "SELECT ?\n";
        this.from = "FROM ?\n";
        this.where = "WHERE ?\n";
        this.res = null;
    }

    @Override
    public void setQuery(String select, String from, String where) {
        this.select.replace("?", select);
        this.from.replace("?", from);
        this.where.replace("?", where);
    }

    @Override
    public void renewQuery() {
        this.select = "SELECT ?\n";
        this.from = "FROM ?\n";
        this.where = "WHERE ?\n";
    }

    @Override
    public ResultSet executeQuery() {
        PreparedStatement ps;
        try {
            ps = this.conn.prepareStatement(select + from + where);
            this.res = ps.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return res;
    }
    
    @Override
    public void renewResultSet() {
        this.res = null;
    }

    @Override
    public void setSelect(String select) {
        this.select.replace("?", select);
    }

    @Override
    public void setFrom(String from) {
        this.from.replace("?", from);
    }

    @Override
    public void setWhere(String where) {
        this.where.replace("?", where);
    }

    @Override
    public ResultSet getRes() {
        return res;
    }

}
