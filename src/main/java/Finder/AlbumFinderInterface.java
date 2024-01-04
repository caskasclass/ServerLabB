package Finder;

import java.util.ArrayList;
import jars.Album;

/**
 *
 * @author lorenzo
 */
public interface AlbumFinderInterface {

    boolean checkResult();

    ArrayList<String> getAlbumId();

    ArrayList<Album> getAllAlbumInformation(int begin, int end);

    void setSearchCriteria(String title);

    void setSearchCriteria(String artist, int year);
    
}
