package Finder;

import java.util.ArrayList;
import pkg.Track;

/**
 *
 * @author lorenzo
 */
public interface SongFinderInterface {

    boolean checkResult();

    ArrayList<Track> getAllTrackInformation(ArrayList<String> ar, int begin, int end);

    ArrayList<String> getTrackId();

    void setSearchCriteria(String title);

    void setSearchCriteria(String artist, int year);
    
}
