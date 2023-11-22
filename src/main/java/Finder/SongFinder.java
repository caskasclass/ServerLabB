package Finder;

import SQLBuilder.SQLFinder;
import java.sql.*;
import java.util.ArrayList;
import pkg.Track;

/**
 *
 * @author lorenzo
 */
public class SongFinder implements SongFinderInterface {

    private SQLFinder dbmanager; // oggetto in grado di ricercare dati
    private ArrayList<Track> res; // lista che contiene i risultati
    private ArrayList<String> trackId; // lista che contiene solo i trackId
    private String[] searchCriteria; // criteri di ricerca

    // costruttore in caso di ricerca pr titolo
    public SongFinder(String title) {
        this.dbmanager = new SQLFinder();
        this.res = new ArrayList<Track>();
        this.trackId = new ArrayList<String>();
        this.searchCriteria = new String[1]; // l'array ha un elemento ed è solo il titolo della canzone
        this.searchCriteria[0] = title;
    }

    // costruttore in caso di ricerca per artista e anno
    public SongFinder(String artist, int year) {
        this.dbmanager = new SQLFinder();
        this.res = new ArrayList<Track>();
        this.trackId = new ArrayList<String>();
        this.searchCriteria = new String[2]; // l'array ha due elementi e sono inizializzati ad autore ed anno
        this.searchCriteria[0] = artist;
        this.searchCriteria[1] = "" + year;
    }

    // costruttore in caso mi servano tutte le tracce del db
    public SongFinder() {
        this.dbmanager = new SQLFinder();
        this.trackId = new ArrayList<String>();
        this.res = new ArrayList<Track>();
        this.searchCriteria = null; // i criteri di ricerca non sono necessari
    }

    // costruttore nel caso si voglia ricercare tutte le informazioni di una lista
    // di tracce
    public SongFinder(ArrayList<String> trackId) {
        this.dbmanager = new SQLFinder();
        this.res = new ArrayList<Track>();
        this.trackId = trackId;
        this.searchCriteria = null;
    }

    @Override
    public void setSearchCriteria(String title) { // nel caso si voglia cambiare il criterio di ricerca
        this.searchCriteria = new String[1];
        this.searchCriteria[0] = title;
    }

    @Override
    public void setSearchCriteria(String artist, int year) { // nel caso si voglia cambiare il cirterio di ricerca
        this.searchCriteria = new String[2];
        this.searchCriteria[0] = artist;
        this.searchCriteria[1] = "" + year;
    }

    @Override
    public ArrayList<String> getAllTrackId() {
        this.dbmanager.renewQuery();
        this.dbmanager.renewResultSet();
        this.dbmanager.setQuery("track_id", "tracks ORDER BY popolarity DESC LIMIT 100");
        this.dbmanager.executeQuery();
        try {
            while (this.dbmanager.getRes().next()) { // cicla finchè ci sono risultati
                this.trackId.add(this.dbmanager.getRes().getString("track_id")); // ottenimento del trackId
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return trackId;
    }

    @Override
    // ricerca della lista dei trackId
    public ArrayList<String> getTrackId() { // ricerca della lista dei trackId
        this.dbmanager.renewQuery(); // rinnovamento della query
        this.dbmanager.renewResultSet(); // rinnovamento dei risultati
        // se si ha inserito solo il titolo si cerca per titolo, altrimenti si cerca per
        // autore e anno
        if (this.searchCriteria.length == 1) {
            // costruzione della query
            this.dbmanager.setQuery("track_id", "tracks", "name = '" + this.searchCriteria[0] + "';");
        } else {
            // costruzione della query con SELECT, FROM, WHERE separati
            this.dbmanager.setSelect("track_id");
            this.dbmanager.setFrom("tracks\n"
                    + "join albums on tracks.album_id = albums.album_id\n"
                    + "join artist_mapping_album on albums.album_id = artist_mapping_album.album_id\n"
                    + "join artists on artists.artist_id = artist_mapping_album.artist_id");
            this.dbmanager.setWhere("artists.name = '" + this.searchCriteria[0] + "' and albums.release_date between '"
                    + this.searchCriteria[1] + "-01-01' AND '" + this.searchCriteria[1] + "-12-31';");
        }
        this.dbmanager.executeQuery(); // esecuzione della query
        try {
            while (this.dbmanager.getRes().next()) { // cicla finchè ci sono risultati
                this.trackId.add(this.dbmanager.getRes().getString("track_id")); // ottenimento del trackId
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return this.trackId; // restituzione della lista
    }

    @Override
    // controlla se la lista con i trackid ha dei risultati
    public boolean checkResult() {
        return this.trackId.isEmpty();
    }

    @Override
    // ricerca di tutte le informazioni relative ad ogni trackId
    public ArrayList<Track> getAllTrackInformation(int begin, int end) { // vengono passati il
                                                                         // punto da dove
                                                                         // iniziare, il punto da
                                                                         // dove finire e la lista
                                                                         // di trackId
        this.dbmanager.renewResultSet();
        if (this.checkResult()) { // se la lista è vuota viene interrotto il metodo
            return null;
        }
        for (int i = begin; i < end; i++) { // scorrimento di tutti i trackId da begin a end
            this.dbmanager.renewQuery(); // rinnovamento della query
            // costruzione della query con SELECT, FROM, WHERE separati
            this.dbmanager.setSelect("DISTINCT artists.name AS artist_name, tracks.*, albums.*");
            this.dbmanager.setFrom("tracks\n"
                    + "JOIN artist_mapping_track ON tracks.track_id = artist_mapping_track.track_id \n"
                    + "JOIN artists ON artist_mapping_track.artist_id = artists.artist_id\n"
                    + "JOIN artist_mapping_album ON artists.artist_id = artist_mapping_album.artist_id\n"
                    + "JOIN albums ON artist_mapping_album.album_id = albums.album_id");
            this.dbmanager.setWhere("tracks.track_id = '" + this.trackId.get(i) + "' ORDER BY tracks.popolarity DESC;");
            this.dbmanager.executeQuery(); // esecuzione della query
            try {
                // ottenimento dei valori risultanti
                while (this.dbmanager.getRes().next()) { // ciclo fino a che ci sono risultati
                    String track_id = this.dbmanager.getRes().getString("track_id");
                    String name = this.dbmanager.getRes().getString("name");
                    String artist_name = this.dbmanager.getRes().getString("artist_name");
                    int duration_ms = this.dbmanager.getRes().getInt("duration_ms");
                    String album_name = this.dbmanager.getRes().getString("album_name");
                    String album_img0 = this.dbmanager.getRes().getString("album_img0");
                    String album_img1 = this.dbmanager.getRes().getString("album_img1");
                    String album_img2 = this.dbmanager.getRes().getString("album_img2");
                    res.add(new Track(track_id, name, duration_ms, artist_name, album_name, album_img0, album_img1,
                            album_img2));
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            } catch (ArrayIndexOutOfBoundsException ei) {
                System.err.println(ei.getMessage());
            } catch (NullPointerException en) {
                System.err.println(en.getMessage());
            }
        }
        return this.res; // ritorno dei risultati
    }
}
