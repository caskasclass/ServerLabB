package jars;

import java.io.Serializable;

/**
 *
 * @author lorenzo
 */
public class Artist implements Serializable {
    private String artist_id;
    private String name;

    public Artist(String artist_id, String name) {
        this.artist_id = artist_id;
        this.name = name;
    }

    public String getArtist_id() {
        return artist_id;
    }

    public String getName() {
        return name;
    }

    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
    @Override
    public String toString() {
        String res = artist_id + "<SEP>" + name;
        return res;
    }
    
}
