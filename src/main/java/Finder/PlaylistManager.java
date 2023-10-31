package Finder;

import SQLBuilder.*;
import java.sql.SQLException;
import java.util.ArrayList;
import pkg.Playlist;
import pkg.Track;
import pkg.User;

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
        this.searchCriteria[0] = title;
        this.searchCriteria[1] = user.getUserid();
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
                "titolo = '" + this.searchCriteria[0] + "' AND userid = '" + this.searchCriteria[1] + "'");
        this.sqlfinder.executeQuery(); // esecuzione della query
        try {
            while (this.sqlfinder.getRes().next()) { // ciclo finchè ci sono risultati
                String trackId = this.sqlfinder.getRes().getString("track_id"); // ottenimento del trackId
                this.trackId.add(trackId); // aggiunta alla lista
            }
            this.res = new Playlist(this.searchCriteria[0], this.trackId, this.searchCriteria[1]); // costruzione della
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
        SongFinder sf = new SongFinder(this.trackId);
        return sf.getAllTrackInformation(ar, begin, end);
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
        column.add("titolo");
        column.add("track_id");
        this.sqlinserter.setColums(column);
        ArrayList<String> values = new ArrayList<String>(); // lista che conterrà i valori delle colonne
        values.add(p.getUser());
        values.add(p.getTitolo());
        for (int i = 0; i < p.getTrackList().size(); i++) { // cicla tutti i trackId presenti nella playlist
            values.add(p.getTrackList().get(i)); // prese del valore del trackId corrente ed aggiunta alla lista
            this.sqlinserter.setValues(values); // settaggio della lista dei valori
            this.sqlinserter.setQuery("playlist"); // settaggio della query con nome della tabella in cui inserire
            this.sqlinserter.executeQuery(); // esecuzione della query
            values.remove(values.size() - 1); // rimozione dell'ultimo valore, ovvero il trackId per poi reinserire il
                                              // prossimo
        }
    }

}
