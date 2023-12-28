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

    void setSearchCriteria(String title, User user);
    
}
