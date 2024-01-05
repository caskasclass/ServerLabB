package SQLBuilder;

import java.sql.*;
import java.util.ArrayList;

import jars.Playlist;

/**
 * Progetto laboratorio B: "Emotional Songs", anno 2022-2023
 *
 * Classe che modella oggetti in grado di inserire nel database delle
 * informazioni oppure di aggiornarne altre.
 *
 * @author Beatrice Bastianello, matricola 751864, VA
 * @author Lorenzo Barbieri , matricola 748695, VA
 * @author Filippo Storti , matricola 749195, VA
 * @author Nazar Viytyuk, matricola 748964, VA
 * @version 1.0
 */
public class SQLInserter implements SQLInserterInterface {

    private Connection conn; // Memorizza la connessione al database
    private ArrayList<String> columns; // Nomi delle colonne in cui inserire i valori
    private ArrayList<String> values; // Valori delle colonne
    private String query; // Query da eseguire

    /**
     * Costruttore per connessioni specifiche al database.
     *
     * @param url URL del database
     * @param username Nome utente per la connessione al database
     * @param psw Password per la connessione al database
     */
    public SQLInserter(String url, String username, String psw) {
        try {
            this.conn = DriverManager.getConnection(url, username, psw);
        } catch (SQLException e) {
            System.err.println("Database connection failed");
        }
        // Settaggio delle liste
        this.columns = new ArrayList<>();
        this.values = new ArrayList<>();
        // Settaggio della query
        this.query = "INSERT INTO (?)\n"
                + "VALUES (!);";
    }

    /**
     * Costruttore per la connessione standard al database. Utilizza un ciclo
     * per tentare la connessione fino a quando non riesce.
     */
    public SQLInserter() {
        boolean connected = false;

        while (!connected) {
            try {
                this.conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/LabDB", "postgres", "postgres");
                connected = true; // Connessione riuscita, usciamo dal ciclo
            } catch (SQLException e) {
                System.err.println("Database connection failed, trying to reconnect");
            }
        }

        // Settaggio delle liste
        this.columns = new ArrayList<>();
        this.values = new ArrayList<>();

        // Settaggio della query
        this.query = "INSERT INTO %(?)\n" + "VALUES (!);";
    }

    /**
     * Metodo per aumentare il parametro popolarità di una traccia specifica.
     *
     * @param trackId Identificatore della traccia
     */
    @Override
    public void updateTrackPopularity(String trackId) {
        // Costruzione della query
        this.query = "UPDATE tracks SET popolarity = popolarity + 1 WHERE track_id = '" + trackId + "';";
        // Esecuzione della query
        this.executeQuery();
    }

    /**
     * Metodo per l'aumento della popolarità della playlist.
     *
     * @param p Oggetto Playlist
     */
    public void updatePlaylistPopolarity(Playlist p) {
        // Costruzione della query
        for (int i = 0; i < p.getTrackList().size(); i++) {
            this.query = "UPDATE playlist"
                    + "SET popolarity = popolarity + 1"
                    + "WHERE title = '" + p.getTitle() + "' AND userid = '" + p.getUser() + "' AND trackId = '"
                    + p.getTrackList().get(i) + "'";
            this.executeQuery();
        }
    }

    /**
     * Metodo per la cancellazione di elementi generici.
     *
     * @param from Nome della tabella da cui eliminare
     * @param where Condizione per la cancellazione
     */
    public void delete(String from, String where) {
        this.query = "DELETE FROM " + from + " WHERE " + where;
    }

    /**
     * Metodo per il settaggio della query.
     *
     * @param tablename Nome della tabella in cui eseguire la query
     */
    @Override
    public void setQuery(String tablename) {
        this.query = this.query.replace("%", tablename); // Settaggio del nome della tabella
        String iColumn = "", iValues = ""; // Stringhe che conterranno i valori delle rispettive liste
        // I due cicli for successivi servono a evitare errori formali come virgole e virgolette
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

    /**
     * Metodo per l'ottenimento della query.
     *
     * @return Stringa contenente la query
     */
    public String getQuery() {
        return this.query;
    }

    /**
     * Metodo di servizio per verificare se un valore è intero o no.
     *
     * @param s Stringa da verificare
     * @return True se la stringa rappresenta un intero, altrimenti False
     */
    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Metodo per l'esecuzione della query.
     */
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

    /**
     * Metodo per il rinnovamento della query.
     */
    @Override
    public void renewQuery() {
        this.query = "INSERT INTO %(?)\n"
                + "VALUES (!);";
    }

    /**
     * Setter delle colonne.
     *
     * @param ar ArrayList contenente i nomi delle colonne
     */
    @Override
    public void setColums(ArrayList<String> ar) {
        this.columns = ar;
    }

    /**
     * Setter dei valori.
     *
     * @param ar ArrayList contenente i valori delle colonne
     */
    @Override
    public void setValues(ArrayList<String> ar) {
        this.values = ar;
    }
}
