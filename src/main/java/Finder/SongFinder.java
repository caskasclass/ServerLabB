package Finder;

import SQLBuilder.SQLFinder;
import java.sql.*;
import java.util.ArrayList;

import jars.Track;
import jars.TrackDetails;

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
 * Classe che modella gli oggetti per la ricerca delle tracce all'interno del database.
 */
public class SongFinder implements SongFinderInterface {

    /** Oggetto in grado di ricercare dati. */
    private SQLFinder dbmanager;
    /** Lista che contiene i risultati. */
    private ArrayList<Track> res;
    /** Lista che contiene solo i trackId. */
    private ArrayList<String> trackId;
    /** Criteri di ricerca. */
    private String[] searchCriteria;

    /**
     * Costruttore in caso di ricerca per titolo.
     * @param title Titolo della canzone.
     */
    public SongFinder(String title) {
        this.dbmanager = new SQLFinder();
        this.res = new ArrayList<Track>();
        this.trackId = new ArrayList<String>();
        this.searchCriteria = new String[1]; // L'array ha un elemento ed è solo il titolo della canzone
        this.searchCriteria[0] = title;
    }

    /**
     * Costruttore in caso di ricerca per artista e anno.
     * @param artist Nome dell'artista.
     * @param year Anno di pubblicazione.
     */
    public SongFinder(String artist, int year) {
        this.dbmanager = new SQLFinder();
        this.res = new ArrayList<Track>();
        this.trackId = new ArrayList<String>();
        this.searchCriteria = new String[2]; // L'array ha due elementi e sono inizializzati ad autore ed anno
        this.searchCriteria[0] = artist;
        this.searchCriteria[1] = "" + year;
    }

    /**
     * Costruttore nel caso mi servano tutte le tracce del database.
     */
    public SongFinder() {
        this.dbmanager = new SQLFinder();
        this.trackId = new ArrayList<String>();
        this.res = new ArrayList<Track>();
        this.searchCriteria = null; // I criteri di ricerca non sono necessari
    }

    /**
     * Costruttore nel caso si voglia ricercare tutte le informazioni di una lista di tracce.
     * @param trackId Lista di trackId.
     */
    public SongFinder(ArrayList<String> trackId) {
        this.dbmanager = new SQLFinder();
        this.res = new ArrayList<Track>();
        this.trackId = trackId;
        this.searchCriteria = null;
    }

    /**
     * Imposta i criteri di ricerca.
     * @param title Titolo della canzone.
     */
    @Override
    public void setSearchCriteria(String title) {
        this.searchCriteria = new String[1];
        this.searchCriteria[0] = title;
    }

    /**
     * Imposta i criteri di ricerca.
     * @param artist Nome dell'artista.
     * @param year Anno di pubblicazione.
     */
    @Override
    public void setSearchCriteria(String artist, int year) {
        this.searchCriteria = new String[2];
        this.searchCriteria[0] = artist;
        this.searchCriteria[1] = "" + year;
    }

    /**
     * Metodo per l'ottenimento di tutti i primi 20 trackId del database in ordine di popolarità.
     * @return Lista di trackId.
     */
    @Override
    public ArrayList<String> getAllTrackId() {
        this.dbmanager.renewQuery(); // Rinnovamento della query
        this.dbmanager.renewResultSet(); // Rinnovamento dei risultati
        // Settaggio della query
        this.dbmanager.setQuery("track_id", "tracks ORDER BY popolarity DESC LIMIT 20");
        this.dbmanager.executeQuery(); // Esecuzione della query
        try {
            while (this.dbmanager.getRes().next()) { // Cicla finché ci sono risultati
                this.trackId.add(this.dbmanager.getRes().getString("track_id")); // Ottenimento del trackId
            }
            this.dbmanager.releaseConnection();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        // Restituzione dei trackId
        return trackId;
    }

    /**
     * Metodo per l'ottenimento dei trackId tramite ricerca.
     * @return Lista di trackId.
     */
    @Override
    public ArrayList<String> getTrackId() {
        this.dbmanager.renewQuery(); // Rinnovamento della query
        this.dbmanager.renewResultSet(); // Rinnovamento del resultset
        // Se i parametri di ricerca sono due si cerca per autore e data, altrimenti per titolo
        if (this.searchCriteria.length == 1) {
            this.dbmanager.setQuery("track_id", "tracks", "LOWER(name) LIKE LOWER('" + this.searchCriteria[0] + "%') LIMIT 20;");
            this.dbmanager.executeQuery();
            try {
                while (this.dbmanager.getRes().next()) { // Cicla finché ci sono risultati
                    this.trackId.add(this.dbmanager.getRes().getString("track_id")); // Ottenimento dei trackid
                }
                this.dbmanager.releaseConnection();
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        } else {
            try {
                this.dbmanager.setQuery("artist_id", "artists", "LOWER(name) LIKE LOWER('" + this.searchCriteria[0] + "%') LIMIT 20;");
                this.dbmanager.executeQuery();

                String artist_id = "";
                while (this.dbmanager.getRes().next()) {
                    artist_id = this.dbmanager.getRes().getString("artist_id");
                }
                this.dbmanager.releaseConnection();

                this.dbmanager.renewQuery();
                this.dbmanager.setQuery("album_id", "artist_mapping_album", "artist_id = '" + artist_id + "'");
                this.dbmanager.executeQuery();

                ArrayList<String> tmp = new ArrayList<String>();
                while (this.dbmanager.getRes().next()) {
                    tmp.add(this.dbmanager.getRes().getString("album_id"));
                }
                this.dbmanager.releaseConnection();

                SQLFinder dbTmp2 = new SQLFinder();
                for (int i = 0; i < tmp.size(); i++) {
                    this.dbmanager.renewQuery();
                    this.dbmanager.setQuery("release_date", "albums", "album_id = '" + tmp.get(i) + "'");
                    this.dbmanager.executeQuery();

                    while (this.dbmanager.getRes().next()) {
                        String sdate = this.dbmanager.getRes().getString("release_date");
                        String[] tmp2 = sdate.split("-");
                       
                        if (tmp2[0].equals(this.searchCriteria[1])) {
                            dbTmp2.renewQuery();
                            dbTmp2.setQuery("track_id", "tracks", "album_id = '" + tmp.get(i) + "'");
                            dbTmp2.executeQuery();

                            while (dbTmp2.getRes().next()) {
                                this.trackId.add(dbTmp2.getRes().getString("track_id"));
                            }
                            dbTmp2.releaseConnection();
                        }
                    }
                    this.dbmanager.releaseConnection();
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        // Ritorno dei risultati
        return this.trackId;
    }

    /**
     * Controlla se la lista con i trackid ha dei risultati.
     * @return True se la lista è vuota, altrimenti false.
     */
    @Override
    public boolean checkResult() {
        return this.trackId.isEmpty();
    }

    /**
     * Ricerca di tutte le informazioni relative ad ogni trackId.
     * @param begin Punto da dove iniziare.
     * @param end Punto da dove finire.
     * @return Lista di tracce.
     */
    @Override
    public ArrayList<Track> getAllTrackInformation(int begin, int end) {
        System.out.println("\n*************** getAllTrackInformation ************\n");
        this.dbmanager.renewResultSet();
        if (this.checkResult()) { // Se la lista è vuota viene interrotto il metodo
            return null;
        }
        ArrayList<Track> res = new ArrayList<Track>();
        try {
            // Ottenimento di tutte le infrmazioni relative alla traccia
            for (int i = begin; i < end; i++) {
                Track t = new Track(null, null, 0, null, null, null, null, null);
                this.dbmanager.renewQuery();
                this.dbmanager.setQuery("*", "tracks", "track_id = '" + this.trackId.get(i) + "';");
                this.dbmanager.executeQuery();
                String album_id = "";
                while (this.dbmanager.getRes().next()) {
                    String track_id = this.dbmanager.getRes().getString("track_id");
                    t.setTrack_id(track_id);
                    String name = this.dbmanager.getRes().getString("name");
                    t.setName(name);
                    int duration = this.dbmanager.getRes().getInt("duration_ms");
                    t.setDuration(duration);
                    album_id = this.dbmanager.getRes().getString("album_id");
                }
                this.dbmanager.releaseConnection();

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
                this.dbmanager.releaseConnection();


                this.dbmanager.renewQuery();
                this.dbmanager.setQuery("artists.*", "albums NATURAL JOIN artist_mapping_album NATURAL JOIN artists",
                        "album_id = '" + album_id + "';");
                this.dbmanager.executeQuery();
                while (this.dbmanager.getRes().next()) {
                    String name = this.dbmanager.getRes().getString("name");
                    t.setArtist_name(name);
                }
                this.dbmanager.releaseConnection();

                res.add(t); // Aggiunta della traccia ai risultati
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
         System.out.println("\n*************** Fine getAllTrackInformation ************\n");
        return res; // Ritorno dei risultati
    }

    /**
     * Metodo per l'ottenimento delle top 20 tracce presenti nel database.
     * @return Lista di dettagli delle tracce.
     */
    @Override
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
                this.dbmanager.releaseConnection();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return topTracks;
    }

}
