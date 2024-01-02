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

/*
 * Gli oggetti modellati da questa classe si occupano della gestione delle
 * playlist all'interno del database, sia inserimento, sia rimozione
 * che ricerca.
 */
public class PlaylistManager implements PlaylistManagerInterface {

    private SQLFinder sqlfinder; // oggetto in grado di trovare dati nella tabella
    private SQLInserter sqlinserter; // oggetto in grado di inserire dati nella tabella
    private Playlist res; // contiene la playlist risultato
    private ArrayList<String> trackId; // lista che contiene i track_id delle tracce della playlist da inserire o
                                       // cercare
    private String[] searchCriteria; // criteri di ricerca

    // costruttore nel caso si voglia cercare a partire da un utente ed un titolo
    public PlaylistManager(String title, String user) {
        this.sqlfinder = new SQLFinder();
        this.res = null;
        this.trackId = new ArrayList<String>();
        this.searchCriteria = new String[2]; // settaggio dei criteri a titolo della playlist e userid
        if (user != null) {
            this.searchCriteria[0] = title;
            this.searchCriteria[1] = user;
        } else {
            this.searchCriteria[0] = title;
            this.searchCriteria[1] = "";
        }
        this.sqlinserter = null;
    }

    public PlaylistManager() { // costruttore nel caso si voglia inserire una playlist
        this.sqlfinder = new SQLFinder();
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
    public void setSearchCriteria(String title, String user) { // nel caso si vogliano risettare i criteri di ricerca
        this.searchCriteria[0] = title;
        this.searchCriteria[1] = user;
    }

    @Override
    // metodo per cercare una playlist
    public Playlist getPlaylist() {
        this.sqlfinder.renewQuery(); // rinnovamento della query
        this.sqlfinder.renewResultSet(); // rinnovamento dei risultati
        // costruzione della query
        this.sqlfinder.setQuery("track_id, image", "playlist",
                "LOWER(title) = LOWER('" + this.searchCriteria[0] + "') AND LOWER(userid) = LOWER('"
                        + this.searchCriteria[1] + "');");
        this.sqlfinder.executeQuery(); // esecuzione della query
        try {
            String img = null;
            
            while (this.sqlfinder.getRes().next()) { // ciclo finchè ci sono risultati
                String trackId = this.sqlfinder.getRes().getString("track_id");
                img = this.sqlfinder.getRes().getString("image");// ottenimento del trackId
                this.trackId.add(trackId); // aggiunta alla lista
            }
            // costruzione della playlist risultato
            this.res = new Playlist(this.searchCriteria[0], this.trackId, img, this.searchCriteria[1]);
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

    @Override
    // metodo per ottenere tutte le playlist presenti nel database
    public ArrayList<Playlist> getAllPlaylist() {
        try {
            // lista per il contenimento dei risultati
            ArrayList<Playlist> res = new ArrayList<Playlist>();
            // lista per il controllo delle playlist già ottenute
            ArrayList<String[]> counted = new ArrayList<String[]>();
            this.sqlfinder.renewQuery(); // rinnovamento della query
            this.sqlfinder.renewResultSet(); // rinnovamento dei risultati
            this.sqlfinder.setQuery("*", "playlist"); // setteggio della query
            this.sqlfinder.executeQuery(); // eseuzione della query
            while (this.sqlfinder.getRes().next()) { // cicla finchè ci sono risultati
                // ottenimento degli userid e del titolo della playlist che serviranno per la
                // ricerca successiva
                this.searchCriteria = new String[] { this.sqlfinder.getRes().getString("userid"),
                        this.sqlfinder.getRes().getString("title") };
                String img = this.sqlfinder.getRes().getString("image");
                // se la playlist non è già stata cercata
                if (!this.contains(counted, this.searchCriteria)) {
                    counted.add(this.searchCriteria); // aggiornamento delle playlist contate
                    // aggiunta della playlist ai risultati
                    res.add(new Playlist(this.searchCriteria[1],
                            this.getTrackList(this.searchCriteria[0], this.searchCriteria[1]), img,
                            this.searchCriteria[0]));
                }
            }
            // ritorno dei risultati
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // metodo di servizio che controlla se è già presente la coppia userid + title è
    // già presente nella lista passata
    private boolean contains(ArrayList<String[]> ar, String[] s) {
        for (int i = 0; i < ar.size(); i++) {
            if (ar.get(i)[0].equals(s[0]) && ar.get(i)[1].equals(s[1])) {
                return true;
            }
        }
        return false;
    }

    // metodo di servizio per l'ottenimento della tracklist legata ad una playlist
    @Override
    public ArrayList<String> getTrackList(String userid, String title) {
        try {
            ArrayList<String> res = new ArrayList<String>();
            SQLFinder f = new SQLFinder();
            f.renewResultSet();
            f.setQuery("track_id", "playlist",
                        "LOWER(title) = LOWER('" + title + "') AND LOWER(userid) = LOWER('" + userid + "');");
            f.executeQuery();
            System.out.println(f.getQuery());
            while(f.getRes().next()) {
                res.add(f.getRes().getString("track_id"));
            }
            if(res.isEmpty()) {
                return null;
            } else {
                return res;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    // metodo per l'eliminazione di una playlist
    public void deletePlayList(Playlist p) {
        this.sqlinserter.renewQuery();
        this.sqlinserter.delete("playlist", "userid = '" + p.getUser() + "' AND title = '" + p.getTitle() + "';");
        this.sqlinserter.executeQuery();
    }

    @Override
    // metodo per l'eliminazione di una singola traccia da una playlist
    public void deleteTrack(Playlist p, String trackId) {
        this.sqlinserter.renewQuery();
        this.sqlinserter.delete("playlist",
                "userid = '" + p.getUser() + "' AND title = '" + p.getTitle() + "' AND track_id = '" + trackId + "';");
        this.sqlinserter.executeQuery();
    }
}
