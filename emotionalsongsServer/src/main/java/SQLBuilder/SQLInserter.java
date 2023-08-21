package SQLBuilder;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author lorenzo
 */
public class SQLInserter implements SQLInserterInterface {

    private Connection conn;
    private ArrayList<String> column;
    private ArrayList<String> values;
    private String query;

    public SQLInserter(String url, String username, String psw) {
        try {
            this.conn = DriverManager.getConnection(url, username, psw);
        } catch (SQLException e) {
            System.err.println("Database connection failed");
        }
        this.column = new ArrayList<String>();
        this.values = new ArrayList<String>();
        this.query = "INSERT INTO (?)\n"
                + "VALUES (!);";
    }

    public SQLInserter() {
        try {
            this.conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/EmotionalSongs", "postgres", "5640");
        } catch (SQLException e) {
            System.err.println("Database connection failed");
        }
        this.column = new ArrayList<String>();
        this.values = new ArrayList<String>();
        this.query = "INSERT INTO %(?)\n"
                + "VALUES (!);";
    }

    @Override
    public void setQuery(String tablename, ArrayList<String> column, ArrayList<String> values) {
        this.query.replace("%", tablename);
        String iColumn = "", iValues = "";
        for (int i = 0; i < column.size(); i++) {
            if (i == column.size() - 1) {
                iColumn += column.get(i);
            } else {
                iColumn += column.get(i) + ", ";
            }
        }
        this.query.replace("?", iColumn);
        for (int i = 0; i < values.size(); i++) {
            if (i == values.size() - 1) {
                if (isInteger(values.get(i))) {
                    iValues += values.get(i);
                } else {
                    iValues += "'" + values.get(i) + "'";
                }
            } else if (isInteger(values.get(i))) {
                iValues += values.get(i) + ", ";
            } else {
                iValues += "'" + values.get(i) + "', ";
            }
        }
        this.query.replace("!", iValues);
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void executeQuery() {
        PreparedStatement ps;
        try {
            ps = this.conn.prepareStatement(this.query);
            ps.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void renewQuery() {
        this.query = "INSERT INTO %(?)\n"
                + "VALUES (!);";
    }

}
