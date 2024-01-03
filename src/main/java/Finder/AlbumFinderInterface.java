package Finder;

import java.util.ArrayList;
import jars.Album;

/**
 * Progetto laboratorio B: "Emotional Songs", anno 2022-2023
 * 
 * @author Beatrice Bastianello, matricola 751864, VA
 * @author Lorenzo Barbieri  , matricola 748695, VA
 * @author Filippo Storti , matricola 749195, VA
 * @author Nazar Viytyuk, matricola 748964, VA
 * @version 1.0
 */
public interface AlbumFinderInterface {

    boolean checkResult();

    ArrayList<String> getAlbumId();

    ArrayList<Album> getAllAlbumInformation(int begin, int end);

    void setSearchCriteria(String title);

    void setSearchCriteria(String artist, int year);
    
}
