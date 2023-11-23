package Finder;

import SQLBuilder.SQLFinder;
import java.sql.*;
import java.util.ArrayList;
import pkg.Track;
import pkg.TrackDetails;

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
        this.dbmanager.setQuery("track_id", "tracks ORDER BY popolarity DESC LIMIT 50");
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
                    + "JOIN albums ON tracks.album_id = albums.album_id\n"
                    + "JOIN artist_mapping_album ON albums.album_id = artist_mapping_album.album_id\n"
                    + "JOIN artists ON artists.artist_id = artist_mapping_album.artist_id");
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
        ArrayList<Track> res = new ArrayList<Track>();
        try {
            for (int i = begin; i < end; i++) {
                Track t = new Track(null, null, 0, null, null, null, null, null);
                this.dbmanager.renewQuery();
                this.dbmanager.setQuery("*", "tracks", "track_id = '" + this.trackId + "';");
                this.dbmanager.executeQuery();
                String album_id = "";
                while (this.dbmanager.getRes().next()) {
                    String track_id = this.dbmanager.getRes().getString("track_id");
                    t.setTrack_id(track_id);
                    String name = this.dbmanager.getRes().getString("name");
                    t.setName(name);
                    int duration = this.dbmanager.getRes().getInt("duration");
                    t.setDuration(duration);
                    album_id = this.dbmanager.getRes().getString("album_id");
                }
                this.dbmanager.renewQuery();
                this.dbmanager.setQuery("*", "albums", "album_id = '" + album_id + "';");
                this.dbmanager.executeQuery();
                while (this.dbmanager.getRes().next()) {
                    String album_name = this.dbmanager.getRes().getString("album_name");
                    t.setAlbum_name(album_name);
                    String album_img0 = this.dbmanager.getRes().getString("album_img0");
                    t.setAlbum_img0(album_img0);
                    String album_img1 = this.dbmanager.getRes().getString("album_img1");
                    t.setAlbum_img1(album_img1);
                    String album_img2 = this.dbmanager.getRes().getString("album_img2");
                    t.setAlbum_img2(album_img2);
                }
                this.dbmanager.renewQuery();
                this.dbmanager.setQuery("artists.*", "albums NATURAL JOIN artist_mapping_album NATURAL JOIN artists",
                        "album_id = '" + album_id + "';");
                this.dbmanager.executeQuery();
                while (this.dbmanager.getRes().next()) {
                    String name = this.dbmanager.getRes().getString("name");
                    t.setArtist_name(name);
                }
                res.add(t);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return res; // ritorno dei risultati
    }

    public ArrayList<TrackDetails> getTopTracks() {
        ArrayList<String> trackId = this.getAllTrackId();
        ArrayList<Track> tmp = this.getAllTrackInformation(0, trackId.size());
        ArrayList<TrackDetails> topTracks = new ArrayList<TrackDetails>();
        try {
            for (int i = 0; i < tmp.size(); i++) {
                this.dbmanager.renewQuery();
                this.dbmanager.setQuery("album_id", "tracks", "track_id = '" + tmp.get(i).getTrack_id() + "';");
                this.dbmanager.executeQuery();
                String album_id = "";
                while (this.dbmanager.getRes().next()) {
                    album_id = this.dbmanager.getRes().getString("album_id");
                }
                topTracks.add(new TrackDetails(tmp.get(i), album_id));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return topTracks;
    }

}
