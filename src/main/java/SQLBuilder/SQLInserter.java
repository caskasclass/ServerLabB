package SQLBuilder;

import java.sql.*;
import java.util.ArrayList;

import pkg.Playlist;

/**
 *
 * @author lorenzo
 */
public class SQLInserter implements SQLInserterInterface {

    private Connection conn; // memorizza la connessione
    private ArrayList<String> columns; // nomi delle colonne in cui inserire i valori
    private ArrayList<String> values; // valori delle colonne
    private String query; // query da eseguire

    // costruttore nel caso di connessioni specifiche
    public SQLInserter(String url, String username, String psw) {
        try {
            this.conn = DriverManager.getConnection(url, username, psw);
        } catch (SQLException e) {
            System.err.println("Database connection failed");
        }
        // settaggio delle liste
        this.columns = new ArrayList<String>();
        this.values = new ArrayList<String>();
        // settaggio della query
        this.query = "INSERT INTO (?)\n"
                + "VALUES (!);";
    }

    // costruttore nel caso di connessione standard
    public SQLInserter() {
        try {
            this.conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/EmotionalSongs", "postgres",
                    "5640");
        } catch (SQLException e) {
            System.err.println("Database connection failed");
        }
        // settaggio delle liste
        this.columns = new ArrayList<String>();
        this.values = new ArrayList<String>();
        // settaggio della query
        this.query = "INSERT INTO %(?)\n"
                + "VALUES (!);";
    }

    // metodo per aumentare il parametro popolarità di una traccia specifica
    @Override
    public void updateTrackPopularity(String trackId) {
        // costruzione della query
        this.query = "UPDATE tracks SET popolarity = popolarity + 1 WHERE track_id = '" + trackId + "';";
        // esecuzione della query
        this.executeQuery();
    }

    public void updatePlaylistPopolarity(Playlist p) {
        //costruzione della query
        for(int i = 0; i < p.getTrackList().size(); i++) {
            this.query = "UPDATE playlist" 
            + "SET popolarity = popolarity + 1"
            + "WHERE title = '" + p.getTitle() + "' AND userid = '" + p.getUser() + "' AND trackId = '" + p.getTrackList().get(i) + "'";
            this.executeQuery();
        }
    }

    // metodo per il settaggio della query
    @Override
    public void setQuery(String tablename) { // passato il nome della tabella in cui eseguire la query
        this.query = this.query.replace("%", tablename); // settaggio del nome della tabella
        String iColumn = "", iValues = ""; // stringhe che conteranno i valori delle rispettive liste
        // i due cili for successivi servono a fare in modo che la stringa non abbia
        // errori formali come le virgole, le virgolette ecc
        for (int i = 0; i < columns.size(); i++) {
            if (i == columns.size() - 1) {
                iColumn += columns.get(i);
            } else {
                iColumn += columns.get(i) + ", ";
            }
        }
        this.query = this.query.replace("?", iColumn);
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
        this.query = this.query.replace("!", iValues);
    }

    // metodo per verificare se un valore sia intero o no
    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // metodo per l'esecuzione della query
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

    // meoto per il rinnovamento della query
    @Override
    public void renewQuery() {
        this.query = "INSERT INTO %(?)\n"
                + "VALUES (!);";
    }

    // setter delle colonne e dei valori
    @Override
    public void setColums(ArrayList<String> ar) {
        this.columns = ar;
    }

    @Override
    public void setValues(ArrayList<String> ar) {
        this.values = ar;
    }

}
