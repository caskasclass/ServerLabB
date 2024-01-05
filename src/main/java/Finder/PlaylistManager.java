package Finder;

import SQLBuilder.*;

import java.security.spec.ECFieldF2m;
import java.sql.SQLException;
import java.util.ArrayList;
import jars.Playlist;
import jars.Track;
import jars.User;

/**
 * Progetto laboratorio B: "Emotional Songs", anno 2022-2023
 * 
 * @author Beatrice Bastianello, matricola 751864, VA
 * @author Lorenzo Barbieri  , matricola 748695, VA
 * @author Filippo Storti , matricola 749195, VA
 * @author Nazar Viytyuk, matricola 748964, VA
 * @version 1.0
 */

/**
 * Gli oggetti modellati da questa classe si occupano della gestione delle playlist all'interno del database, sia inserimento, sia rimozione che ricerca.
 */
public class PlaylistManager implements PlaylistManagerInterface {

    /** Oggetto in grado di trovare dati nel DB. */
    private SQLFinder sqlfinder;
    /** Oggetto in grado di inserire dati nel DB. */
    private SQLInserter sqlinserter;
    /** Contiene la playlist risultato. */
    private Playlist res;
    /** Lista che contiene i risultati degli id univoci per ciascuna tabella. */
    private ArrayList<String> trackId;
    /** Criteri di ricerca. */
    private String[] searchCriteria;

    /**
     * Costruttore nel caso si voglia cercare a partire da un utente ed un titolo.
     * @param title Titolo della playlist.
     * @param user ID dell'utente.
     */
    public PlaylistManager(String title, String user) {
        this.sqlfinder = new SQLFinder();
        this.res = null;
        this.trackId = new ArrayList<String>();
        this.searchCriteria = new String[2]; // Settaggio dei criteri a titolo della playlist e userid
        if (user != null) {
            this.searchCriteria[0] = title;
            this.searchCriteria[1] = user;
        } else {
            this.searchCriteria[0] = title;
            this.searchCriteria[1] = "";
        }
        this.sqlinserter = null;
    }

    /**
     * Costruttore nel caso si voglia inserire una playlist.
     */
    public PlaylistManager() {
        this.sqlfinder = new SQLFinder();
        this.res = null;
        this.trackId = null;
        this.sqlinserter = new SQLInserter();
        this.searchCriteria = null;
    }

    /**
     * Costruttore nel caso si abbia già una playlist "attiva" e si vogliano trovare tutte le informazioni relative a quelle tracce.
     * @param trackId Lista di trackId.
     */
    public PlaylistManager(ArrayList<String> trackId) {
        this.sqlfinder = new SQLFinder();
        this.res = null;
        this.trackId = trackId;
        this.sqlinserter = null;
        this.searchCriteria = null;
    }

    /**
     * Imposta i criteri di ricerca.
     * @param title Titolo della playlist.
     * @param user ID dell'utente.
     */
    @Override
    public void setSearchCriteria(String title, String user) {
        this.searchCriteria[0] = title;
        this.searchCriteria[1] = user;
    }

    /**
     * Metodo per cercare una playlist.
     * @return Playlist risultato.
     */
    @Override
    public Playlist getPlaylist() {
        this.sqlfinder.renewQuery(); // Rinnovamento della query
        this.sqlfinder.renewResultSet(); // Rinnovamento dei risultati
        // Costruzione della query
        this.sqlfinder.setQuery("track_id, image", "playlist",
                "LOWER(title) = LOWER('" + this.searchCriteria[0] + "') AND LOWER(userid) = LOWER('"
                        + this.searchCriteria[1] + "');");
        this.sqlfinder.executeQuery(); // Esecuzione della query
        try {
            String img = null;
            
            while (this.sqlfinder.getRes().next()) { // Ciclo finché ci sono risultati
                String trackId = this.sqlfinder.getRes().getString("track_id");
                img = this.sqlfinder.getRes().getString("image");// Ottenimento del trackId
                this.trackId.add(trackId); // Aggiunta alla lista
            }
            // Costruzione della playlist risultato
            this.res = new Playlist(this.searchCriteria[0], this.trackId, img, this.searchCriteria[1]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return this.res; // Ritorno della playlist
    }

    /**
     * Ricerca di tutte le informazioni di una playlist.
     * @param ar Lista di trackId.
     * @param begin Indice di inizio.
     * @param end Indice di fine.
     * @return Lista di tracce.
     */
    @Override
    public ArrayList<Track> getAllTrackInformation(ArrayList<String> ar, int begin, int end) {
        SongFinder sf = new SongFinder(ar);
        return sf.getAllTrackInformation(begin, end);
    }

    /**
     * Metodo per creare una playlist.
     * @param p Playlist da creare.
     */
    @Override
    public void createPlaylist(Playlist p) {
        // Controlla se la playlist è vuota, se è così interrompe e stampa l'errore
        if (p.getTrackList().isEmpty()) {
            System.err.println("Playlist is empty");
            return;
        }
        ArrayList<String> column = new ArrayList<String>(); // Lista che conterrà i nomi delle colonne
        // Settaggio dei nomi delle colonne
        column.add("userid");
        column.add("title");
        column.add("track_id");
        column.add("image");
        this.sqlinserter.setColums(column);
        ArrayList<String> values = null;
        for (int i = 0; i < p.getTrackList().size(); i++) { // Cicla tutti i trackId presenti nella playlist
            values = new ArrayList<String>();
            values.add(p.getUser());
            values.add(p.getTitle());
            values.add(p.getTrackList().get(i)); // Prese del valore del trackId corrente ed aggiunta alla lista
            values.add(p.getImage());
            this.sqlinserter.renewQuery();
            this.sqlinserter.setValues(values); // Settaggio della lista dei valori
            this.sqlinserter.setQuery("playlist"); // Settaggio della query con nome della tabella in cui inserire
            this.sqlinserter.executeQuery(); // Esecuzione della query
        }
    }

    /**
     * Metodo per ottenere tutte le playlist presenti nel database.
     * @return Lista di playlist.
     */
    @Override
    public ArrayList<Playlist> getAllPlaylist() {
        try {
            // Lista per il contenimento dei risultati
            ArrayList<Playlist> res = new ArrayList<Playlist>();
            // Lista per il controllo delle playlist già ottenute
            ArrayList<String[]> counted = new ArrayList<String[]>();
            this.sqlfinder.renewQuery(); // Rinnovamento della query
            this.sqlfinder.renewResultSet(); // Rinnovamento dei risultati
            this.sqlfinder.setQuery("*", "playlist"); // Setteggio della query
            this.sqlfinder.executeQuery(); // Esecuzione della query
            while (this.sqlfinder.getRes().next()) { // Cicla finché ci sono risultati
                // Ottenimento degli userid e del titolo della playlist che serviranno per la ricerca successiva
                this.searchCriteria = new String[] { this.sqlfinder.getRes().getString("userid"),
                        this.sqlfinder.getRes().getString("title") };
                String img = this.sqlfinder.getRes().getString("image");
                // Se la playlist non è già stata cercata
                if (!this.contains(counted, this.searchCriteria)) {
                    counted.add(this.searchCriteria); // Aggiornamento delle playlist contate
                    // Aggiunta della playlist ai risultati
                    res.add(new Playlist(this.searchCriteria[1],
                            this.getTrackList(this.searchCriteria[0], this.searchCriteria[1]), img,
                            this.searchCriteria[0]));
                }
            }
            // Ritorno dei risultati
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Metodo di servizio che controlla se è già presente la coppia userid + title è già presente nella lista passata
    private boolean contains(ArrayList<String[]> ar, String[] s) {
        for (int i = 0; i < ar.size(); i++) {
            if (ar.get(i)[0].equals(s[0]) && ar.get(i)[1].equals(s[1])) {
                return true;
            }
        }
        return false;
    }

    // Metodo di servizio per l'ottenimento della tracklist legata ad una playlist
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

    /**
     * Metodo per l'eliminazione di una playlist.
     * @param p Playlist da eliminare.
     */
    @Override
    public void deletePlayList(Playlist p) {
        this.sqlinserter.renewQuery();
        this.sqlinserter.delete("playlist", "userid = '" + p.getUser() + "' AND title = '" + p.getTitle() + "';");
        this.sqlinserter.executeQuery();
    }

    /**
     * Metodo per l'eliminazione di una singola traccia da una playlist.
     * @param p Playlist.
     * @param trackId Track ID.
     */
    @Override
    public void deleteTrack(Playlist p, String trackId) {
        this.sqlinserter.renewQuery();
        this.sqlinserter.delete("playlist",
                "userid = '" + p.getUser() + "' AND title = '" + p.getTitle() + "' AND track_id = '" + trackId + "';");
        this.sqlinserter.executeQuery();
    }
}
