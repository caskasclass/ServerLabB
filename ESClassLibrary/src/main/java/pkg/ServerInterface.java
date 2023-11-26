package pkg;

import java.rmi.*;
import java.util.ArrayList;
import pkg.Album;
import pkg.Emotion;
import pkg.Playlist;
import pkg.Track;
import pkg.User;

/**
 *
 * @author lorenzo
 */
public interface ServerInterface extends Remote {

    int PORT = 8080;

    User access(String strAccess, String psw) throws RemoteException;

    void createPlaylist(Playlist p) throws RemoteException;

    ArrayList<String> getAlbumId(String name) throws RemoteException;

    ArrayList<String> getAlbumId(String artist, int year) throws RemoteException;

    ArrayList<Album> getAllAlbumInformation(ArrayList<String> trackId, int begin, int end) throws RemoteException;

    ArrayList<Track> getAllTrackInformation(Playlist p, int begin, int end) throws RemoteException;

    ArrayList<Emotion> getEmotion(Track track) throws RemoteException;

    Playlist getPlaylist(String title, User user) throws RemoteException;

    ArrayList<String> getTrackId(String title) throws RemoteException;

    ArrayList<String> getTrackId(String artist, int year) throws RemoteException;

    ArrayList<Track> getAllTrackInformation(ArrayList<String> trackId, int begin, int end) throws RemoteException;

    ArrayList<String> getAllTrackId() throws RemoteException;

    void insertEmotion(Emotion e) throws RemoteException;

    void registration(User u) throws RemoteException;

    void ciao() throws RemoteException;

    ArrayList<TrackDetails> getTopTracks() throws RemoteException;
    
}
