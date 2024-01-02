package Finder;

import java.util.ArrayList;
import jars.Playlist;
import jars.Track;
import jars.User;

/**
 *
 * @author lorenzo
 */
public interface PlaylistManagerInterface {

    void createPlaylist(Playlist p);

    ArrayList<Track> getAllTrackInformation(ArrayList<String> ar, int begin, int end);

    Playlist getPlaylist();

    void setSearchCriteria(String title, String user);

    ArrayList<Playlist> getAllPlaylist();

    ArrayList<String> getTrackList(String userid, String title);

    void deleteTrack(Playlist p, String trackId);

    void deletePlayList(Playlist p);
    
}
