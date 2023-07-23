package Server;

import java.rmi.server.*;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author lorenzo
 */
public class Server {

    public static final int PORT = 8080;

    public synchronized Connection connect(String dbUrl, String username, String psw) {
        Connection conn = null; //connessione inizializzata a null
        try {
            conn = DriverManager.getConnection(dbUrl, username, psw); //connessione al db generica
        } catch (SQLException e) {
            System.out.println(e.getMessage()); //stampa il messaggio di errore in caso di eccezione
        }
        if (conn != null) {
            System.out.println("Database connection successfull"); //stampa se la connessione è avvenuta con successo
        }
        return conn;
    }

    //ricerca tramite titolo della canzone
    public synchronized ArrayList<String> getTrack(Connection conn, String titolo) {
        ArrayList<String> res = new ArrayList<String>();
        try {
            //statement con la query che cerca una canzone per titolo
            PreparedStatement ps = conn.prepareStatement("SELECT track_id FROM tracks WHERE name = '" + titolo + "';");
            //esecuzione della query
            ResultSet qres = ps.executeQuery();
            while (qres.next()) {
                //aggiunge alla lista l'id delle trace che risultano
                res.add(qres.getString("track_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    public synchronized ArrayList<String> getTrack(Connection conn, String autore, int anno) {
        ArrayList<String> res = new ArrayList<String>();
        try {
            //statement con la query che cerca una canzone per titolo
            PreparedStatement ps = conn.prepareStatement("select track_id from tracks\n"
                    + "join albums on tracks.album_id = albums.album_id\n"
                    + "join artist_mapping_album on albums.album_id = artist_mapping_album.album_id\n"
                    + "join artisti on artisti.artist_id = artist_mapping_album.artist_id\n"
                    + "where artisti.name = '" + autore + "' and albums.release_date between '" + anno + "'-01-01' AND " + 1984 + "-12-31';");
            //esecuzione della query
            ResultSet qres = ps.executeQuery();
            while (qres.next()) {
                //aggiunge alla lista l'id delle trace che risultano
                res.add(qres.getString("track_id"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    /*
        Essendo che potrebbe essere che laq stessa canzone è stata inserita in album diversi, ho sperimentato
        che possono esistere più istanze a fronte di uno stesso track_id, vengono trattate come canzoni separate
     */
    //il metodo cerca tutto
    public synchronized ArrayList<Track> getAllTrackInformation(Connection conn, ArrayList<String> trackId) {
        ArrayList<Track> res = new ArrayList<Track>();
        int index = 0;
        try {
            //prende una trackId alla volta
            while (index < trackId.size()) {
                String query = "SELECT DISTINCT artisti.name AS artist_name, tracks.*, albums.*\n"
                        + "FROM tracks \n"
                        + "JOIN artist_mapping_track ON tracks.track_id = artist_mapping_track.track_id \n"
                        + "JOIN artisti ON artist_mapping_track.artist_id = artisti.artist_id\n"
                        + "JOIN artist_mapping_album ON artisti.artist_id = artist_mapping_album.artist_id\n"
                        + "JOIN albums ON artist_mapping_album.album_id = albums.album_id\n"
                        + "WHERE tracks.track_id = '" + trackId.get(index) + "';";
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet qres = ps.executeQuery();
                while (qres.next()) {
                    String track_id = qres.getString("track_id");
                    String name = qres.getString("name");
                    String artist_name = qres.getString("artist_name");
                    int duration_ms = qres.getInt("duration_ms");
                    String album_name = qres.getString("album_name");
                    String album_img0 = qres.getString("album_img0");
                    String album_img1 = qres.getString("album_img1");
                    String album_img2 = qres.getString("album_img2");
                    res.add(new Track(track_id, name, duration_ms, artist_name, album_name, album_img0, album_img1, album_img2));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

    public synchronized void disconnect(Connection conn) {
        try {
            conn.close(); //disconnessione dal db
            System.out.println("Database connection closed"); //stampa se la connessione è stat chiusa con successo
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws SQLException {
        
    }

}
