package Server;

import java.rmi.server.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lorenzo
 */
public class Server {

    public static final int PORT = 8080;
    private static final byte KEY = 5;

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

    private static String encrypt(String s, int key) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char current = s.charAt(i);
            if (!Character.isLetter(current)) {
                res.append(current);
                continue;
            }
            char cChar = (char) (current + key);
            if (Character.isLowerCase(current) && cChar > 'z') {
                cChar = (char) (cChar - 26);
            } else if (Character.isUpperCase(current) && cChar > 'Z') {
                cChar = (char) (cChar - 26);
            }
            res.append(cChar);
        }
        return res.toString();
    }

    public synchronized User registration(String userid, String nome, String cognome, String cf, String indirizzo, int cap, String città, String mail, String psw, Connection conn) {
        //crittazione della password
        String cPassword = encrypt(psw, KEY);
        try {
            //invio della query
            PreparedStatement ps = conn.prepareStatement("INSERT INTO utenti_registrati (userid, nome, cognome, cf, indirizzo, cap, città, mail, password)\n"
                    + "VALUES ('" + userid + "', '" + nome + "', '" + cognome + "', '" + cf + "', '" + indirizzo + "', " + cap + ", '" + città + "', '" + mail + "', '" + cPassword + "');");
            ps.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //creazione e restituzione dello user
        return new User(userid, nome, cognome, cf, indirizzo, cap, città, mail, psw);
    }

    public User access(String stringAccess, String psw, Connection conn) {
        PreparedStatement ps = null;
        //controlla prima se il valore che gli viene passato è una mail oppure un userid, lo fa se nella stringa è presente la chiocciola
        try {
            if (stringAccess.contains("@")) {
                ps = conn.prepareStatement("SELECT * FROM utenti_registrati WHERE mail = '" + stringAccess + "';"); //query nel caso di mail
            } else {
                ps = conn.prepareStatement("SELECT * FROM utenti_registrati WHERE userid = '" + stringAccess + "';"); //query nel caso di userid
            }
            ResultSet qres = ps.executeQuery();
            while (qres.next()) {
                String cPassword = qres.getString("password"); //prima di prendere il resto dei dati prendo solo la password per controllarne la correttezza
                if (psw.equals(encrypt(cPassword, (26 - KEY)))) { //decrittazione, se la password corrisponde a quella passata come parametro vengono presi gli altri dati
                    String userid = qres.getString("userid");
                    String nome = qres.getString("nome");
                    String cognome = qres.getString("cognome");
                    String cf = qres.getString("cf");
                    String indirizzo = qres.getString("indirizzo");
                    int cap = qres.getInt("cap");
                    String città = qres.getString("città");
                    String mail = qres.getString("mail");
                    return new User(userid, nome, cognome, cf, indirizzo, cap, città, mail, psw); //costruzione dello user
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //ritorna null in tutti i casi in cui non ci sia una corrispondenza userid/mail e password
        return null;
    }

    //generatore di una password random
    public String pswGenerator(int length) {
        StringBuilder res = new StringBuilder();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        alphabet += alphabet.toUpperCase();
        alphabet += "0123456789";
        Random rn = new Random();
        for (int i = 0; i < length; i++) {
            int randomInt = rn.nextInt(alphabet.length());
            char randomChar = alphabet.charAt(randomInt);
            res.append(randomChar);
        }
        return res.toString();
    }

    public static void main(String[] args) throws SQLException {
        Server s = new Server();
        Connection conn = s.connect("jdbc:postgresql://localhost:5432/EmotionalSongs", "postgres", "5640");
        User u = s.access("_barbio", "prova1", conn);
        System.out.println(u.toString());
    }

}
