package Server;

/**
 *
 * @author lorenzo
 */
public class Artist {
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
    
    @Override
    public String toString() {
        String res = artist_id + "<SEP>" + name;
        return res;
    }
    
}
