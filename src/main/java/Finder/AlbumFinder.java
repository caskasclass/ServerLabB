package Finder;

import SQLBuilder.SQLFinder;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import jars.Album;
import jars.Artist;

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
 * Gli oggetti modellati da questa classe si occupano della ricerca nel database degli album e solo dell'ottenimento delle informazioni relative ad essi.
 */
public class AlbumFinder implements AlbumFinderInterface {

    /** Essendo che gli album non devono essere toccati, non è presente un inserter. */
    private SQLFinder dbmanager; // oggetto per la gestione delle query SQL
    private ArrayList<Album> res; // risultato della query che contiene tutte le informazioni degli album
    private ArrayList<String> albumId; // risultato della query che contiene solo gli albumId
    private String[] searchCriteria; // criteri di ricerca

    /**
     * Costruttore nel caso di ricerca con solo il nome dell'album.
     * @param name Nome dell'album per la ricerca.
     */
    public AlbumFinder(String name) {
        this.dbmanager = new SQLFinder();
        this.res = null;
        this.albumId = new ArrayList<String>();
        this.searchCriteria = new String[1]; // l'array è inizializzato ad uno e viene inserito solo il nome dell'album
        this.searchCriteria[0] = name;
    }

    /**
     * Costruttore nel caso di ricerca con nome dell'artista e anno di pubblicazione dell'album.
     * @param artist Nome dell'artista.
     * @param year Anno di pubblicazione dell'album.
     */
    public AlbumFinder(String artist, int year) {
        this.dbmanager = new SQLFinder();
        this.res = null;
        this.albumId = new ArrayList<String>();
        this.searchCriteria = new String[2]; // l'array è inizializzato a due con artista e anno di pubblicazione
        this.searchCriteria[0] = artist;
        this.searchCriteria[1] = "" + year;
    }

    /**
     * Costruttore nel caso si voglia passare un array di trackId già creato.
     * @param albumId Lista degli albumId.
     */
    public AlbumFinder(ArrayList<String> albumId) {
        this.dbmanager = new SQLFinder();
        this.res = new ArrayList<Album>();
        this.albumId = albumId;
        this.searchCriteria = null; // criteri di ricerca settati a null
    }

    /**
     * Imposta il criterio di ricerca con solo il titolo dell'album.
     * @param title Titolo dell'album per la ricerca.
     */
    @Override
    public void setSearchCriteria(String title) {
        this.searchCriteria = new String[1];
        this.searchCriteria[0] = title;
    }

    /**
     * Imposta il criterio di ricerca con nome dell'artista e anno di pubblicazione dell'album.
     * @param artist Nome dell'artista.
     * @param year Anno di pubblicazione dell'album.
     */
    @Override
    public void setSearchCriteria(String artist, int year) {
        this.searchCriteria = new String[2];
        this.searchCriteria[0] = artist;
        this.searchCriteria[1] = "" + year;
    }

    /**
     * Metodo per ottenere la lista degli albumId.
     * @return Lista degli albumId.
     */
    @Override
    public ArrayList<String> getAlbumId() {
        this.dbmanager.renewQuery(); // rinnovamento della query
        this.dbmanager.renewResultSet(); // rinnovamento del resultset
        // se il criterio di ricerca è uno si ricerca per titolo, altrimenti per autore
        // e data
        if (this.searchCriteria.length == 1) {
            // costruzione della query
            this.dbmanager.setQuery("album_id", "albums", "album_name = '" + this.searchCriteria[0] + "';");
        } else {
            // costruzione della query con SELECT, FROM, WHERE separati
            this.dbmanager.setSelect("albums.album_id");
            this.dbmanager.setFrom("albums\n"
                    + "JOIN artist_mapping_album ON albums.album_id = artist_mapping_album.album_id\n"
                    + "JOIN artists ON artist_mapping_album.artist_id = artists.artist_id");
            this.dbmanager.setWhere("LOWER(artists.name) LIKE LOWER('" + this.searchCriteria[0] + "%') AND albums.release_date between '"
                    + this.searchCriteria[1] + "-01-01' AND '" + this.searchCriteria[1] + "-12-31' LIMIT 20;");
        }
        // Esecuzione della query
        this.dbmanager.executeQuery();
        try {
            while (this.dbmanager.getRes().next()) { // Ciclo finché ci sono risultati nella query
                String album_id = this.dbmanager.getRes().getString("album_id"); // Ottenimento del valore dell'album_id
                this.albumId.add(album_id); // Aggiunta alla lista
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return this.albumId; // Ritorno della lista
    }

    /**
     * Verifica se ci sono risultati nella ricerca.
     * @return True se la lista degli albumId è vuota, altrimenti false.
     */
    @Override
    public boolean checkResult() {
        return this.albumId.isEmpty(); // Controllo se la lista con gli album_id di quella ricerca è vuota
    }

    /**
     * Metodo per ottenere tutte le informazioni degli album in un intervallo specificato.
     * @param begin Indice di partenza nella lista degli albumId.
     * @param end Indice di fine nella lista degli albumId.
     * @return Lista degli album con le relative informazioni.
     */
    @Override
    public ArrayList<Album> getAllAlbumInformation(int begin, int end) {
        this.dbmanager.renewResultSet(); // Rinnovo dei risultati
        if (this.checkResult()) { // Se la lista degli albumId è vuota, restituisce null
            return null;
        }
        for (int i = begin; i < end; i++) { // Scorrimento di tutti gli albumId da begin a end
            this.dbmanager.renewQuery(); // Rinnovo della query
            // Costruzione della query con SELECT, FROM, WHERE separati
            this.dbmanager.setSelect("albums.*, artists.*");
            this.dbmanager.setFrom("albums\n"
                    + "JOIN artist_mapping_album ON albums.album_id = artist_mapping_album.album_id\n"
                    + "JOIN artists ON artist_mapping_album.artist_id = artists.artist_id");
            this.dbmanager.setWhere("albums.album_id = '" + this.albumId.get(i) + "';");
            this.dbmanager.executeQuery();
            try {
                while (this.dbmanager.getRes().next()) { // ciclo finchè ci sono risultati nella query
                    String album_name = this.dbmanager.getRes().getString("album_name");
                    Date release_date = this.dbmanager.getRes().getDate("release_date");
                    String album_img0 = this.dbmanager.getRes().getString("album_img0");
                    String album_img1 = this.dbmanager.getRes().getString("album_img1");
                    String album_img2 = this.dbmanager.getRes().getString("album_img2");
                    String artist_id = this.dbmanager.getRes().getString("artist_id");
                    String artist_name = this.dbmanager.getRes().getString("name");
                    this.res.add(new Album(albumId.get(i), album_name, release_date, album_img0, album_img1, album_img2,
                            new Artist(artist_id, artist_name))); // aggiunta dell'album con annessa costruzione
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return this.res; // ritorno della lista di album
    }
}

