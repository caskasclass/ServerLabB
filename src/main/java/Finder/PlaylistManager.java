package Finder;

import SQLBuilder.*;

import java.security.spec.ECFieldF2m;
import java.sql.SQLException;
import java.util.ArrayList;
import jars.Playlist;
import jars.Track;
import jars.User;

/**
 *
 * @author lorenzo
 */
public class PlaylistManager implements PlaylistManagerInterface {

    private SQLFinder sqlfinder; // oggetto in grado di trovare dati nella tabella
    private SQLInserter sqlinserter; // oggetto in grado di inserire dati nella tabella
    private Playlist res; // contiene la playlist risultato
    private ArrayList<String> trackId; // lista che contiene i track_id delle tracce della playlist da inserire o
                                       // cercare
    private String[] searchCriteria; // criteri di ricerca

    // costruttore nel caso si voglia cercare a partire da un utente ed un titolo
    public PlaylistManager(String title, User user) {
        this.sqlfinder = new SQLFinder();
        this.res = null;
        this.trackId = new ArrayList<String>();
        this.searchCriteria = new String[2]; // settaggio dei criteri a titolo della playlist e userid
        if(user != null) {
            this.searchCriteria[0] = title;
            this.searchCriteria[1] = user.getUserid();
        } else {
            this.searchCriteria[0] = title;
            this.searchCriteria[1] = "";
        }
        this.sqlinserter = null;
    }

    public PlaylistManager() { // costruttore nel caso si voglia inserire una playlist
        this.sqlfinder = null;
        this.res = null;
        this.trackId = null;
        this.sqlinserter = new SQLInserter();
        this.searchCriteria = null;
    }

    public PlaylistManager(ArrayList<String> trackId) { // costruttore nel caso si abbia già una playlist "attiva" e si
                                                        // vogliano trovare tutte le informazioni relative a quelle
                                                        // tracce
        this.sqlfinder = new SQLFinder();
        this.res = null;
        this.trackId = trackId;
        this.sqlinserter = null;
        this.searchCriteria = null;
    }

    @Override
    public void setSearchCriteria(String title, User user) { // nel caso si vogliano risettare i criteri di ricerca
        this.searchCriteria[0] = title;
        this.searchCriteria[1] = user.getUserid();
    }

    @Override
    // metodo per cercare una playlist
    public Playlist getPlaylist() {
        this.sqlfinder.renewQuery(); // rinnovamento della query
        this.sqlfinder.renewResultSet(); // rinnovamento dei risultati
        // costruzione della query
        this.sqlfinder.setQuery("track_id", "playlist",
                "title = '" + this.searchCriteria[0] + "' AND userid = '" + this.searchCriteria[1] + "'");
        this.sqlfinder.executeQuery(); // esecuzione della query
        try {
            String img = null;
            while (this.sqlfinder.getRes().next()) { // ciclo finchè ci sono risultati
                String trackId = this.sqlfinder.getRes().getString("track_id"); // ottenimento del trackId
                img = this.sqlfinder.getRes().getString("image");
                this.trackId.add(trackId); // aggiunta alla lista
            }
            this.res = new Playlist(this.searchCriteria[0], this.trackId, img, this.searchCriteria[1]); // costruzione
                                                                                                        // della
            // playlist risultato
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return this.res; // ritorno della playlist
    }

    @Override
    // ricerca di tutte le informazioni di una playlist (vedere
    // SongFinder.getAllTracInformation per i dettagli)
    public ArrayList<Track> getAllTrackInformation(ArrayList<String> ar, int begin, int end) {
        SongFinder sf = new SongFinder(ar);
        return sf.getAllTrackInformation(begin, end);
    }

    @Override
    // metodo per creare una playlist
    public void createPlaylist(Playlist p) { // prende una playlist
        // controlla se la playlist è vuota, se è così interrompe e stampa l'errore
        if (p.getTrackList().isEmpty()) {
            System.err.println("Playlist is empty");
            return;
        }
        ArrayList<String> column = new ArrayList<String>(); // lista che conterrà i nomi delle colonne
        // settaggio dei nomi delle colonne
        column.add("userid");
        column.add("title");
        column.add("track_id");
        column.add("image");
        this.sqlinserter.setColums(column);
        ArrayList<String> values = null;
        for (int i = 0; i < p.getTrackList().size(); i++) { // cicla tutti i trackId presenti nella playlist
            values = new ArrayList<String>();
            values.add(p.getUser());
            values.add(p.getTitle());
            values.add(p.getTrackList().get(i)); // prese del valore del trackId corrente ed aggiunta alla lista
            values.add(p.getImage());
            this.sqlinserter.renewQuery();
            this.sqlinserter.setValues(values); // settaggio della lista dei valori
            this.sqlinserter.setQuery("playlist"); // settaggio della query con nome della tabella in cui inserire
            this.sqlinserter.executeQuery(); // esecuzione della query
        }
    }

    public ArrayList<Playlist> getAllPlaylist() {
        try {
            ArrayList<Playlist> res = new ArrayList<Playlist>();
            this.sqlfinder.renewQuery();
            this.sqlfinder.setQuery("*", "playlist");
            this.sqlfinder.executeQuery();
            while (this.sqlfinder.getRes().next()) {
                this.searchCriteria[0] = this.sqlfinder.getRes().getString("title");
                this.searchCriteria[1] = this.sqlfinder.getRes().getString("userid");
                String img = this.sqlfinder.getRes().getString("image");
                this.sqlfinder.renewQuery();
                this.sqlfinder.setQuery("track_id", "playlist",
                        "title = '" + this.searchCriteria[0] + "' AND userid = '" + this.searchCriteria[1] + "';");
                this.sqlfinder.executeQuery();
                ArrayList<String> tmp = new ArrayList<String>();
                while (this.sqlfinder.getRes().next()) {
                    tmp.add(this.sqlfinder.getRes().getString("track_id"));
                }
                res.add(new Playlist(searchCriteria[0], tmp, img, searchCriteria[1]));
            }
            return res;
        } catch (Exception e) {
            return null;
        }
    }
}
