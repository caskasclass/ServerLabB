package Finder;

import java.util.ArrayList;
import jars.Playlist;
import jars.Track;
import jars.User;

/**
 * Progetto laboratorio B: "Emotional Songs", anno 2022-2023
 * 
 * @author Beatrice Bastianello, matricola 751864, VA
 * @author Lorenzo Barbieri  , matricola 748695, VA
 * @author Filippo Storti , matricola 749195, VA
 * @author Nazar Viytyuk, matricola 748964, VA
 * @version 1.0
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
