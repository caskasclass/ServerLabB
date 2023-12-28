package jars;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents an Album.
 * 
 * @author lorenzo
 */
public class Album implements Serializable {

    private String album_id;
    private String album_name;
    private Date release_date;
    private String album_img0;
    private String album_img1;
    private String album_img2;
    private Artist artist;
    private ArrayList<Track> albums_track;

    public Album(String album_id, String album_name, Date release_date, String album_img0, String album_img1, String album_img2, Artist artist, ArrayList<Track> albums_track) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.release_date = release_date;
        this.album_img0 = album_img0;
        this.album_img1 = album_img1;
        this.album_img2 = album_img2;
        this.artist = artist;
        this.albums_track = albums_track;
    }

    public Album(String album_id, String album_name, Date release_date, String album_img0, String album_img1, String album_img2, Artist artist) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.release_date = release_date;
        this.album_img0 = album_img0;
        this.album_img1 = album_img1;
        this.album_img2 = album_img2;
        this.artist = artist;
        this.albums_track = new ArrayList<>();
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

    public ImageView getAlbum_img0() {
        return createImageView(album_img0);
    }

    public ImageView getAlbum_img1() {
        return createImageView(album_img1);
    }

    public ImageView getAlbum_img2() {
        return createImageView(album_img2);
    }

    public String getAlbum_img0S() {
        return album_img0;
    }

    public String getAlbum_img1S() {
        return album_img1;
    }

    public String getAlbum_img2S() {
        return album_img2;
    }

    public Artist getArtist() {
        return artist;
    }

    public ImageView createImageView(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl);
            return new ImageView(image);
        } else {
            return null;
        }
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public void setAlbum_img0(String album_img0) {
        this.album_img0 = album_img0;
    }

    public void setAlbum_img1(String album_img1) {
        this.album_img1 = album_img1;
    }

    public void setAlbum_img2(String album_img2) {
        this.album_img2 = album_img2;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setAlbums_track(ArrayList<Track> albums_track) {
        this.albums_track = albums_track;
    }
    

    @Override
    public String toString() {
        String res = album_id + "<£SEP>" + album_name + "<£SEP>" + release_date + "<£SEP>"
                + (album_img0 != null ? album_img0 : "") + "<£SEP>"
                + (album_img1 != null ? album_img1 : "") + "<£SEP>"
                + (album_img2 != null ? album_img2 : "") + "<£SEP>\n";

        for (int i = 0; i < this.albums_track.size(); i++) {
            res += this.albums_track.get(i).toString() + "<+SEP>";
        }

        return res;
    }
    

    public boolean equals(Album a) {
        return this.album_name.equals(a.album_name) && this.artist.equals(a.artist);
    }
}
