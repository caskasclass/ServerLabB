package Finder;

import java.util.ArrayList;
import pkg.Track;

/**
 *
 * @author lorenzo
 */
public interface SongFinderInterface {

    boolean checkResult();

    ArrayList<Track> getAllTrackInformation(int begin, int end);

    ArrayList<String> getAllTrackId();

    ArrayList<String> getTrackId();

    void setSearchCriteria(String title);

    void setSearchCriteria(String artist, int year);
    
}
