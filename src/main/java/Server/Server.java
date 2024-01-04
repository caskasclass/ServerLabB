package Server;

import java.rmi.*;
import java.rmi.server.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.rmi.registry.*;
import java.sql.SQLException;
import java.sql.ResultSet;
import jars.*;
import UserManager.*;
import Finder.*;
import SQLBuilder.*;

import java.util.ArrayList;

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
 *
 * @author lorenzo
 */
public class Server extends UnicastRemoteObject implements ServerInterface {

    /*
     * Il server consiste in un array di oggetti di tipo Server instanziati ognuno
     * su una porta diversa a partire da 8080.
     * Sono instanziati fino a 100 oggetti a cui il client può accedere tramite
     * l'interfaccia ServerInterface.
     * Il server implementa dei metodi che a loro volta consistono in dei servizi,
     * ogni servizio è eseguito da uno specifico oggetto, costrudendo
     * l'oggetto appropriato ed eseguendo il metodo più opportuno.
     * Per maggiori informazioni su ogni servizio si veda la documentazione apposita
     * di ogni classe.
     */

    public Server() throws RemoteException {
        super();
    }

    @Override
    public User access(String strAccess, String psw) {
        UserManager um = new UserManager(strAccess, psw);
        return um.access();
    }

    @Override
    public boolean registration(User u) {
        UserManager um = new UserManager();
        return um.registration(u);
    }

    @Override
    public ArrayList<String> getAlbumId(String name) {
        AlbumFinder af = new AlbumFinder(name);
        return af.getAlbumId();
    }

    @Override
    public ArrayList<String> getAlbumId(String artist, int year) {
        AlbumFinder af = new AlbumFinder(artist, year);
        return af.getAlbumId();
    }

    @Override
    public ArrayList<Album> getAllAlbumInformation(ArrayList<String> trackId, int begin, int end) {
        AlbumFinder af = new AlbumFinder(trackId);
        return af.getAllAlbumInformation(begin, end);
    }

    @Override
    public void insertEmotion(EmotionEvaluation e) {
        EmotionManager em = new EmotionManager();
        em.insertEmotions(e);
    }

    @Override
    public EmotionEvaluation getMyEmotion(Track track, String user_id) {
        EmotionManager em = new EmotionManager(track);
        return em.getMyEmotions(user_id);
    }
    @Override
    public ArrayList<ChartData> getAllEmotion(Track track) throws RemoteException {
        EmotionManager em = new EmotionManager(track);
        return em.getAllEmotions();
    }

    @Override
    public void createPlaylist(Playlist p) {
        PlaylistManager pm = new PlaylistManager();
        pm.createPlaylist(p);
    }

    @Override
    public Playlist getPlaylist(String title, String user) {
        PlaylistManager pm = new PlaylistManager(title, user);
        return pm.getPlaylist();
    }

    @Override
    public ArrayList<Track> getAllTrackInformation(Playlist p, int begin, int end) {
        PlaylistManager pm = new PlaylistManager(p.getTrackList());
        new PopularityIncreaser(p.getTrackList());
        return pm.getAllTrackInformation(p.getTrackList(), begin, end);
    }

    @Override
    public ArrayList<String> getTrackId(String title) {
        SongFinder sf = new SongFinder(title);
        return sf.getTrackId();
    }

    @Override
    public ArrayList<String> getTrackId(String artist, int year) {
        SongFinder sf = new SongFinder(artist, year);
        return sf.getTrackId();
    }

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

    @Override
    public ArrayList<Playlist> getAllPlaylist() {
        PlaylistManager pm = new PlaylistManager("","");
        return pm.getAllPlaylist();
    }

    @Override
    public ArrayList<String> getAllTrackId() {
        SongFinder sf = new SongFinder();
        return sf.getAllTrackId();
    }

<<<<<<< HEAD
    public  ArrayList<TrackDetails> getTopTracks(){
=======
    @Override
    public ArrayList<TrackDetails> getTopTracks() {
>>>>>>> d14d03d7c979dcf79ea316c3c9470a121f3de32f
        ArrayList<TrackDetails> topTracks = new ArrayList<TrackDetails>();
        SQLFinder dbmanager = new SQLFinder();
        dbmanager.renewQuery();
        dbmanager.renewResultSet();
<<<<<<< HEAD
        dbmanager.setQuery("*"," tracks join albums using (album_id) order by popularity DESC limit 50");
        dbmanager.executeQuery();
        try {
            while (dbmanager.getRes().next()) { // cicla finchè ci sono risultati
                ResultSet res = dbmanager.getRes();
                System.out.println("Result set size "+res.getFetchSize());

                // create  TrackDetails and add it to the list
                Track track = new Track(res.getString("track_id"), res.getString("name"), res.getInt("duration_ms"), "Silence is golden", res.getString("album_name"), res.getString("album_img0"),res.getString("album_img1"),res.getString("album_img2"));
                topTracks.add(new TrackDetails(track,res.getString("album_id")));
            }
            for (TrackDetails trackDetails : topTracks) {
                System.out.println(trackDetails.track.getName()+" album id : "+trackDetails.albumId);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("topTracks size: "+topTracks.size());
        return topTracks;

        
    }
    public static void main(String[] args) throws RemoteException {
=======
        dbmanager.setQuery("*",
                " tracks join albums using (album_id) where track_id in (SELECT track_id FROM tracks ORDER BY popolarity DESC LIMIT 50);");
        dbmanager.executeQuery();
>>>>>>> d14d03d7c979dcf79ea316c3c9470a121f3de32f
        try {
            while (dbmanager.getRes().next()) { // cicla finchè ci sono risultati
                ResultSet res = dbmanager.getRes();
                System.out.println("Result set size " + res.getFetchSize());

                // create TrackDetails and add it to the list
                Track track = new Track(res.getString("track_id"), res.getString("name"), res.getInt("duration_ms"),
                        "Silence is golden", res.getString("album_name"), res.getString("album_img0"),
                        res.getString("album_img1"), res.getString("album_img2"));
                topTracks.add(new TrackDetails(track, res.getString("album_id")));
            }
            for (TrackDetails trackDetails : topTracks) {
                System.out.println(trackDetails.track.getName() + " album id : " + trackDetails.albumId);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("topTracks size: " + topTracks.size());
        return topTracks;
    }

    @Override
    public boolean checkIfRated(String trackid, String user_id) throws RemoteException {

        SQLFinder dbmanager = new SQLFinder();
        dbmanager.renewQuery();
        dbmanager.renewResultSet();
        dbmanager.setQuery("count(*)","emotions where track_id = '" + trackid + "' and userid = '" + user_id + "'");
        dbmanager.executeQuery();
        ResultSet res = dbmanager.getRes();
        // get the number of rows from the result set
        try {
            res.next();
            System.out.println("Result set size " + res.toString());
            int count = res.getInt(1);
            System.out.println("count: " + count);
            if (count > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            
        }
        return false;
    
    }

    @Override
    public void deletePlayList(Playlist p) {
        new PlaylistManager().deletePlayList(p);
    }

    @Override
    public void deleteTrack(Playlist p, String trackid) {
        new PlaylistManager().deleteTrack(p, trackid);
    }

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
                System.out.println("Result set size " + res.getFetchSize());

                // create TrackDetails and add it to the list

                AlbumPreview album = new AlbumPreview(res.getString("album_id"), res.getString("album_name"),
                        res.getString("album_img0"), res.getString("album_img1"), res.getString("album_img2"),
                        "Silence is golden");
                topAlbums.add(album);
            }
            for (AlbumPreview albumDetails : topAlbums) {
                System.out.println(albumDetails.getAlbumName());
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        System.out.println("topAlbums size: " + topAlbums.size());
        return topAlbums;
    }


    @Override
    public ArrayList<String> findexExistingUsers() throws RemoteException {
        UserManager u = new UserManager();
        return u.findexExistingUsers();
    }


    public static void main(String[] args) throws RemoteException {

        try {
            //ciclo per l'instanziazione dei vari oggetti su ogni porta che si trova nell'array PORT descritto nell'interfaccia ServerInterface
            for (int i = 0; i < PORT.length; i++) { 
                Registry r = LocateRegistry.createRegistry(ServerInterface.PORT[i]);
                r.rebind("SERVER" + i, new Server());
            }
            //comunicazione dell'avvenuta creazione degli oggetti
            System.out.println("Server start correct");
            //ciclo infinito per l'utilizzo di ogni oggetto
            for (;;) {
            }
        } catch (Exception e) {
            //gestione dell'eccezione nel caso si verifichi
            System.out.println("Server start failed");
            System.out.println(e.getMessage());
            System.exit(0);
        }

    }

}
