package Server;

import java.rmi.Remote;
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

    User access(String strAccess, String psw);

    void createPlaylist(Playlist p);

    ArrayList<String> getAlbumId(String name);

    ArrayList<String> getAlbumId(String artist, int year);

    ArrayList<Album> getAllAlbumInformation(ArrayList<String> trackId, int begin, int end);

    ArrayList<Track> getAllTrackInformation(Playlist p, int begin, int end);

    ArrayList<Emotion> getEmotion(Track track);

    Playlist getPlaylist(String title, User user);

    ArrayList<String> getTrackId(String title);

    ArrayList<String> getTrackId(String artist, int year);

    ArrayList<Track> gtAllTrackInformation(ArrayList<String> trackId, int begin, int end);

    void insertEmotion(Emotion e);

    void registration(User u);
    
}
