package Finder;

import SQLBuilder.SQLFinder;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import jars.Album;
import jars.Artist;

/**
 *
 * @author lorenzo
 */

/*
 * Gli oggetti modellati da questa classe si occupano della ricerca nel database degli album e solo dell'ottenimento delle informazioni relative ad essi.
 */
public class AlbumFinder implements AlbumFinderInterface {

    private SQLFinder dbmanager; // essendo che gli album non devono essere toccati non è presente un inserter
    private ArrayList<Album> res; // risultato della query che contiene tutte le info degli album
    private ArrayList<String> albumId; // risultato della query che contiene solo gli albumId
    private String[] searchCriteria; // criteri di ricerca

    public AlbumFinder(String name) { // costruttore nel caso di ricerca cono solo il nome dell'album
        this.dbmanager = new SQLFinder();
        this.res = null;
        this.albumId = new ArrayList<String>();
        this.searchCriteria = new String[1]; // l'array è inizializzato ad uno e viene inserito solo il nome dell'album
        this.searchCriteria[0] = name;
    }

    public AlbumFinder(String artist, int year) { // costruttore nel caso di ricerca cono solo il nome dell'album
        this.dbmanager = new SQLFinder();
        this.res = null;
        this.albumId = new ArrayList<String>();
        this.searchCriteria = new String[2]; // l'array è inizializzato a due con artista e anno di pubblicazione
        this.searchCriteria[0] = artist;
        this.searchCriteria[1] = "" + year;
    }

    public AlbumFinder(ArrayList<String> albumId) { // costruttore nel caso si voglia passare un array di trackId già
                                                    // creato
        this.dbmanager = new SQLFinder();
        this.res = new ArrayList<Album>();
        this.albumId = albumId;
        this.searchCriteria = null; // criteri di ricerca settati a null
    }

    @Override
    public void setSearchCriteria(String title) { // nel caso si vogliano modificare i criteri di ricerca con solo il
                                                  // titolo
        this.searchCriteria = new String[1];
        this.searchCriteria[0] = title;
    }

    @Override
    public void setSearchCriteria(String artist, int year) { // nel caso si vogliano modificare i criteri di ricerca con
                                                             // solo l'album
        this.searchCriteria = new String[2];
        this.searchCriteria[0] = artist;
        this.searchCriteria[1] = "" + year;
    }

    @Override
    public ArrayList<String> getAlbumId() { // metodo per l'ottenimento dei trackId
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
        //esecuzione della query
        this.dbmanager.executeQuery();
        try {
            while (this.dbmanager.getRes().next()) { // ciclo finchè ci sono risultati nella query
                String album_id = this.dbmanager.getRes().getString("album_id"); // ottenimento del valore dell'album_id
                this.albumId.add(album_id); // aggiunta alla lista
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return this.albumId; // ritorno della lista
    }

    @Override
    public boolean checkResult() {
        return this.albumId.isEmpty(); // controllo se la lista con gli album_id di quella ricerca sono vuoti
    }

    // il metodo prende la lista degli albumId, non serve passarla perchè esiste un
    // costruttore apposito
    @Override
    public ArrayList<Album> getAllAlbumInformation(int begin, int end) { // si sceglie da che albumId iniziare e a quale
                                                                         // finire
        this.dbmanager.renewResultSet(); // rinnovo dei risultati
        if (this.checkResult()) { // se la lista degli albumId è vuota restituisce null
            return null;
        }
        for (int i = begin; i < end; i++) { // scorrimento di tutti i trackId da begin a end
            this.dbmanager.renewQuery(); // rinnovo della query
            // costruzione della query con SELECT, FROM, WHERE separati
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
