package Finder;

import java.util.ArrayList;
import pkg.Playlist;
import pkg.Track;
import pkg.User;

/**
 *
 * @author lorenzo
 */
public interface PlaylistManagerInterface {

    void createPlaylist(Playlist p);

    ArrayList<Track> getAllTrackInformation(int begin, int end);

    Playlist getPlaylist();

    void setSearchCriteria(String title, User user);
    
}
