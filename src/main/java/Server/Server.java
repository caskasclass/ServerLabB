package Server;

import java.rmi.*;
import java.rmi.server.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.rmi.registry.*;

import jars.*;
import UserManager.*;
import Finder.*;
import SQLBuilder.*;

import java.util.ArrayList;

/**
 * Progetto laboratorio B: "Emotional Songs", anno 2022-2023
 * 
 * @author Beatrice Bastianello, matricola 751864, VA
 * @author Lorenzo Barbieri , matricola 748695, VA
 * @author Filippo Storti , matricola 749195, VA
 * @author Nazar Viytyuk, matricola 748964, VA
 * @version 1.0
 */
    /**
     * Il server RMI implementa dei metodi che a loro volta consistono in dei servizi,
     * ogni servizio è eseguito da uno specifico oggetto, costrudendo
     * l'oggetto appropriato ed eseguendo il metodo più opportuno.
     * Per maggiori informazioni su ogni servizio si veda la documentazione apposita
     * di ogni classe.
     */
public class Server extends UnicastRemoteObject implements ServerInterface {



    /**
     * Costruttore della classe Server.
     *
     * @throws RemoteException Lanciata in caso di errore nella gestione delle
     *                         operazioni remote.
     */
    public Server() throws RemoteException {
        super();
    }

    /**
     * Metodo remoto per l'accesso utente.
     *
     * @param strAccess Nome utente o email per l'accesso.
     * @param psw       Password associata all'utente.
     * @return Oggetto User rappresentante l'utente autenticato.
     */
    @Override
    public User access(String strAccess, String psw) {
        UserManager um = new UserManager(strAccess, psw);
        return um.access();
    }

    /**
     * Metodo remoto per la registrazione di un nuovo utente.
     *
     * @param u Oggetto User rappresentante l'utente da registrare.
     */
    @Override
    public void registration(User u) {
        UserManager um = new UserManager();
        um.registration(u);
    }

    /**
     * Metodo remoto per ottenere gli ID degli album dato il nome.
     *
     * @param name Nome dell'album.
     * @return ArrayList contenente gli ID degli album corrispondenti al nome
     *         specificato.
     */
    @Override
    public ArrayList<String> getAlbumId(String name) {
        AlbumFinder af = new AlbumFinder(name);
        return af.getAlbumId();
    }

    /**
     * Metodo remoto per ottenere gli ID degli album dato l'artista e l'anno di
     * pubblicazione.
     *
     * @param artist Nome dell'artista.
     * @param year   Anno di pubblicazione dell'album.
     * @return ArrayList contenente gli ID degli album corrispondenti all'artista e
     *         all'anno specificati.
     */
    @Override
    public ArrayList<String> getAlbumId(String artist, int year) {
        AlbumFinder af = new AlbumFinder(artist, year);
        return af.getAlbumId();
    }

    /**
     * Metodo remoto per ottenere tutte le informazioni sugli album dato un insieme
     * di ID delle tracce.
     *
     * @param trackId ArrayList contenente gli ID delle tracce.
     * @param begin   Indice di partenza per la selezione delle informazioni.
     * @param end     Indice di fine per la selezione delle informazioni.
     * @return ArrayList contenente le informazioni sugli album corrispondenti.
     */
    @Override
    public ArrayList<Album> getAllAlbumInformation(ArrayList<String> trackId, int begin, int end) {
        AlbumFinder af = new AlbumFinder(trackId);
        return af.getAllAlbumInformation(begin, end);
    }

    /**
     * Metodo remoto per inserire una valutazione emotiva.
     *
     * @param e Oggetto EmotionEvaluation rappresentante la valutazione emotiva da
     *          inserire.
     */
    @Override
    public void insertEmotion(EmotionEvaluation e) {
        EmotionManager em = new EmotionManager();
        em.insertEmotions(e);
    }

    /**
     * Metodo remoto per ottenere la valutazione emotiva dell'utente per una
     * specifica traccia.
     *
     * @param track   Oggetto Track rappresentante la traccia di interesse.
     * @param user_id ID dell'utente.
     * @return Oggetto EmotionEvaluation rappresentante la valutazione emotiva
     *         dell'utente.
     */
    @Override
    public EmotionEvaluation getMyEmotion(Track track, String user_id) {
        EmotionManager em = new EmotionManager(track);
        return em.getMyEmotions(user_id);
    }

    /**
     * Metodo remoto per ottenere tutte le valutazioni emotive per una specifica
     * traccia.
     *
     * @param track Oggetto Track rappresentante la traccia di interesse.
     * @return ArrayList contenente le valutazioni emotive per la traccia
     *         specificata.
     * @throws RemoteException Lanciata in caso di errore nella gestione delle
     *                         operazioni remote.
     */
    @Override
    public ArrayList<ChartData> getAllEmotion(Track track) throws RemoteException {
        EmotionManager em = new EmotionManager(track);
        return em.getAllEmotions();
    }

    /**
     * Metodo remoto per la creazione di una playlist.
     *
     * @param p Oggetto Playlist rappresentante la playlist da creare.
     */
    @Override
    public void createPlaylist(Playlist p) {
        PlaylistManager pm = new PlaylistManager();
        pm.createPlaylist(p);
    }

    /**
     * Metodo remoto per ottenere una playlist data il titolo e l'utente associato.
     *
     * @param title Titolo della playlist.
     * @param user  ID dell'utente proprietario della playlist.
     * @return Oggetto Playlist rappresentante la playlist richiesta.
     */
    @Override
    public Playlist getPlaylist(String title, String user) {
        PlaylistManager pm = new PlaylistManager(title, user);
        return pm.getPlaylist();
    }

    /**
     * Metodo remoto per ottenere tutte le informazioni sulle tracce in una
     * playlist.
     *
     * @param p     Oggetto Playlist rappresentante la playlist di interesse.
     * @param begin Indice di partenza per la selezione delle informazioni.
     * @param end   Indice di fine per la selezione delle informazioni.
     * @return ArrayList contenente le informazioni sulle tracce nella playlist
     *         specificata.
     */
    @Override
    public ArrayList<Track> getAllTrackInformation(Playlist p, int begin, int end) {
        PlaylistManager pm = new PlaylistManager(p.getTrackList());
        new PopularityIncreaser(p.getTrackList());
        return pm.getAllTrackInformation(p.getTrackList(), begin, end);
    }

    /**
     * Metodo remoto per ottenere gli ID delle tracce dato il titolo.
     *
     * @param title Titolo della traccia.
     * @return ArrayList contenente gli ID delle tracce corrispondenti al titolo
     *         specificato.
     */
    @Override
    public ArrayList<String> getTrackId(String title) {
        SongFinder sf = new SongFinder(title);
        return sf.getTrackId();
    }

    /**
     * Metodo remoto per ottenere gli ID delle tracce dato l'artista e l'anno di
     * pubblicazione.
     *
     * @param artist Nome dell'artista.
     * @param year   Anno di pubblicazione della traccia.
     * @return ArrayList contenente gli ID delle tracce corrispondenti all'artista e
     *         all'anno specificati.
     */
    @Override
    public ArrayList<String> getTrackId(String artist, int year) {
        SongFinder sf = new SongFinder(artist, year);
        return sf.getTrackId();
    }

    /**
     * Metodo remoto per ottenere tutte le informazioni sulle tracce dato un insieme
     * di ID delle tracce.
     *
     * @param trackId ArrayList contenente gli ID delle tracce.
     * @param begin   Indice di partenza per la selezione delle informazioni.
     * @param end     Indice di fine per la selezione delle informazioni.
     * @return ArrayList contenente le informazioni sulle tracce corrispondenti agli
     *         ID specificati.
     */
    @Override
    public ArrayList<Track> getAllTrackInformation(ArrayList<String> trackId, int begin, int end) {
        SongFinder sf = new SongFinder(trackId);
        try {
            new PopularityIncreaser(trackId);
            return sf.getAllTrackInformation(begin, end);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Metodo remoto per ottenere tutte le playlist presenti nel sistema.
     *
     * @return ArrayList contenente tutte le playlist presenti nel sistema.
     */
    @Override
    public ArrayList<Playlist> getAllPlaylist() {
        PlaylistManager pm = new PlaylistManager("", "");
        return pm.getAllPlaylist();
    }

    /**
     * Metodo remoto per ottenere gli ID di tutte le tracce presenti nel sistema.
     *
     * @return ArrayList contenente gli ID di tutte le tracce presenti nel sistema.
     */
    @Override
    public ArrayList<String> getAllTrackId() {
        SongFinder sf = new SongFinder();
        return sf.getAllTrackId();
    }

    /**
     * Metodo remoto per ottenere le informazioni sulle tracce più popolari.
     *
     * @return ArrayList contenente le informazioni sulle tracce più popolari.
     */
    @Override
    public ArrayList<TrackDetails> getTopTracks() {
        ArrayList<TrackDetails> topTracks = new ArrayList<TrackDetails>();
        SQLFinder dbmanager = new SQLFinder();
        dbmanager.renewQuery();
        dbmanager.renewResultSet();
        dbmanager.setQuery("*",
                " tracks join albums using (album_id) where track_id in (SELECT track_id FROM tracks ORDER BY popolarity DESC LIMIT 50);");
        dbmanager.executeQuery();
        try {
            while (dbmanager.getRes().next()) { // cicla finchè ci sono risultati
                ResultSet res = dbmanager.getRes();
                // create TrackDetails and add it to the list
                Track track = new Track(res.getString("track_id"), res.getString("name"), res.getInt("duration_ms"),
                        "Silence is golden", res.getString("album_name"), res.getString("album_img0"),
                        res.getString("album_img1"), res.getString("album_img2"));
                topTracks.add(new TrackDetails(track, res.getString("album_id")));
            }
            dbmanager.releaseConnection();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return topTracks;
    }

    /**
     * Metodo remoto per verificare se una traccia è stata valutata da un utente.
     *
     * @param arg0 traccia commentata
     * @return true se l'utente ha valutato la traccia, false altrimenti.
     * @throws RemoteException Lanciata in caso di errore nella gestione delle
     *                         operazioni remote.
     */
    @Override
    public ArrayList<CommentSection> getAllComments(Track arg0) throws RemoteException {
        EmotionManager em = new EmotionManager(arg0);
        return em.getAllComments();
    }

    /**
     * Verifica se un determinato utente ha già valutato una traccia specifica.
     *
     * @param trackid Identificativo univoco della traccia da verificare.
     * @param user_id Identificativo univoco dell'utente per il quale si vuole
     *                verificare la valutazione.
     * @return true se l'utente ha già valutato la traccia, false altrimenti.
     * @throws RemoteException Lanciata in caso di errore di comunicazione remota.
     */

    @Override
    public boolean checkIfRated(String trackid, String user_id) throws RemoteException {

        SQLFinder dbmanager = new SQLFinder();
        dbmanager.renewQuery();
        dbmanager.renewResultSet();
        dbmanager.setQuery("count(*)", "emotions where track_id = '" + trackid + "' and userid = '" + user_id + "'");
        dbmanager.executeQuery();
        ResultSet res = dbmanager.getRes();
        // get the number of rows from the result set
        try {
            res.next();
            int count = res.getInt(1);
            dbmanager.releaseConnection();

            if (count > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return false;

    }

    /**
     * Metodo remoto per eliminare una playlist.
     *
     * @param p Oggetto Playlist rappresentante la playlist da eliminare.
     */
    @Override
    public void deletePlayList(Playlist p) {
        new PlaylistManager().deletePlayList(p);
    }

    /**
     * Metodo remoto per eliminare una traccia da una playlist.
     *
     * @param p       Oggetto Playlist rappresentante la playlist di riferimento.
     * @param trackid ID della traccia da eliminare.
     */
    @Override
    public void deleteTrack(Playlist p, String trackid) {
        new PlaylistManager().deleteTrack(p, trackid);
    }

    /**
     * Metodo remoto per ottenere le anteprime degli album più popolari.
     *
     * @return ArrayList contenente le anteprime degli album più popolari.
     * @throws RemoteException Lanciata in caso di errore nella gestione delle
     *                         operazioni remote.
     */

    @Override
    public ArrayList<AlbumPreview> getTopAlbums() throws RemoteException {

        ArrayList<AlbumPreview> topAlbums = new ArrayList<AlbumPreview>();
        SQLFinder dbmanager = new SQLFinder();
        dbmanager.renewQuery();
        dbmanager.renewResultSet();
        dbmanager.setQuery("*", " albums  ORDER BY release_date DESC LIMIT 6");
        dbmanager.executeQuery();
        try {
            while (dbmanager.getRes().next()) { // cicla finchè ci sono risultati
                ResultSet res = dbmanager.getRes();

                // create TrackDetails and add it to the list

                AlbumPreview album = new AlbumPreview(res.getString("album_id"), res.getString("album_name"),
                        res.getString("album_img0"), res.getString("album_img1"), res.getString("album_img2"),
                        "Silence is golden");
                topAlbums.add(album);
            }
            dbmanager.releaseConnection();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return topAlbums;
    }

    /**
     *
     * @return ArrayList di String - Lista degli utenti esistenti.
     * @throws RemoteException Se si verifica un problema legato alla comunicazione
     *                         durante l'esecuzione del metodo.
     */
    @Override
    public ArrayList<String> findexExistingUsers() throws RemoteException {
        UserManager u = new UserManager();
        return u.findexExistingUsers();
    }

    public static void main(String[] args) throws RemoteException {

        ConnectionPool.initialize();
        try {
            Registry r = LocateRegistry.createRegistry(1099);
            r.rebind("SERVER", new Server());
            // comunicazione dell'avvenuta creazione degli oggetti
            System.out.println("Server avviato correttamente");
            // ciclo infinito per l'utilizzo di ogni oggetto
            for (;;) {
            }
        } catch (Exception e) {
            // gestione dell'eccezione nel caso si verifichi
            System.out.println("Server start failed");
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

}
