package Server;

import java.rmi.*;
import java.rmi.server.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.rmi.registry.*;
import pkg.*;
import UserManager.*;
import Finder.*;
import SQLBuilder.SQLFinder;

import java.util.ArrayList;

/**
 *
 * @author lorenzo
 */
public class Server extends UnicastRemoteObject implements ServerInterface {

    public Server() throws RemoteException {
        super();
    }

    @Override
    public User access(String strAccess, String psw) {
        UserManager um = new UserManager(strAccess, psw);
        return um.access();
    }

    @Override
    public void registration(User u) {
        UserManager um = new UserManager();
        um.registration(u);
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
    public void insertEmotion(Emotion e) {
        EmotionManager em = new EmotionManager();
        em.insertEmotion(e);
    }

    @Override
    public ArrayList<Emotion> getEmotion(Track track) {
        EmotionManager em = new EmotionManager(track);
        return em.getEmotions();
    }

    @Override
    public void createPlaylist(Playlist p) {
        PlaylistManager pm = new PlaylistManager();
        pm.createPlaylist(p);
    }

    @Override
    public Playlist getPlaylist(String title, User user) {
        PlaylistManager pm = new PlaylistManager(title, user);
        return pm.getPlaylist();
    }

    @Override
    public ArrayList<Track> getAllTrackInformation(Playlist p, int begin, int end) {
        PlaylistManager pm = new PlaylistManager(p.getTrackList());
        new PopolarityIncreaser(p.getTrackList());
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
            System.setProperty("java.awt.headless", "true");
            return sf.getAllTrackInformation(begin, end);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @Override
    public void ciao() {
        System.out.println("ciao");
    }

    @Override
    public ArrayList<String> getAllTrackId() {
        SongFinder sf = new SongFinder();
        return sf.getAllTrackId();
    }

    @Override
    public ArrayList<TrackDetails> getTopTracks() {
        SongFinder sf = new SongFinder();
        return sf.getTopTracks();
    }

    public static void main(String[] args) throws RemoteException {

        try {
            Server s = new Server();
            Registry r = LocateRegistry.createRegistry(PORT);
            r.rebind("SERVER", s);
            System.out.println("Server start correct");
            for (;;) {
            }
        } catch (Exception e) {
            System.out.println("Server start failed");
            System.out.println(e.getMessage());
        }

        /*
         * Server s = new Server();
         * try {
         * ArrayList<TrackDetails> ar1 = s.getTopTracks();
         * System.out.println(ar1.size());
         * } catch (Exception e) {
         * System.out.println(e.getMessage());
         * }
         * System.exit(0);
         */

    }

}
