package Server;

import java.sql.Date;
import java.util.ArrayList;

/**
 *
 * @author lorenzo
 */
public class Album {
    private String album_id;
    private String album_name;
    private Date release_date;
    private String album_img0;
    private String album_img1;
    private String album_img2;
    private ArrayList<Track> albums_track;

    public Album(String album_id, String album_name, Date release_date, String album_img0, String album_img1, String album_img2) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.release_date = release_date;
        this.album_img0 = album_img0;
        this.album_img1 = album_img1;
        this.album_img2 = album_img2;
        this.albums_track = new ArrayList<Track>();
        
    }

    public ArrayList<Track> getAlbums_track() {
        return albums_track;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public String getAlbum_img0() {
        return album_img0;
    }

    public String getAlbum_img1() {
        return album_img1;
    }

    public String getAlbum_img2() {
        return album_img2;
    }
    
    
}
