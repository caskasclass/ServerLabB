package Server;

import java.rmi.Remote;
import java.sql.*;
import java.util.ArrayList;
import pkg.*;

/**
 *
 * @author lorenzo
 */
public interface ServerInterface extends Remote {
    public Connection connect(String dbUrl, String username, String psw);
    public ArrayList<String> getTrack(String titolo, Connection conn);
    public ArrayList<String> getTrack(String autore, int anno, Connection conn);
    public ArrayList<Track> getAllTrackInformation(ArrayList<String> trackId, Connection conn);
    public void disconnect(Connection conn);
    public User registration(String userid, String nome, String cognome, String cf, String indirizzo, int cap, String città, String mail, String psw, Connection conn);
    public User access(String stringAccess, String psw, Connection conn);
    public String pswGenerator(int length);
    public ArrayList<String> getAlbum(String name, Connection conn);
    public Playlist getPlaylist(User user, String title, Connection conn);
    public void createPlaylist(Playlist p, Connection conn);
    public ArrayList<Album> getAllAlbumInformation(ArrayList<String> albumId, Connection conn);
}
