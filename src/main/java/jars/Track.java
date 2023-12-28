package jars;

import java.io.Serializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Represents a Track.
 * 
 * @author lorenzo
 */
public class Track implements Serializable {

    private String track_id;
    private String name;
    private int duration;
    private String artist_name;
    private String album_name;
    private String album_img0;
    private String album_img1;
    private String album_img2;

    // Costruttore che riceve l'array delle emozioni e altri dati
    public Track(String track_id, String name, int duration, String artist_name, String album_name, String album_img0, String album_img1, String album_img2) {
        this.track_id = track_id;
        this.name = name;
        this.duration = duration / (1000 * 60);
        this.artist_name = artist_name;
        this.album_name = album_name;
        this.album_img0 = album_img0;
        this.album_img1 = album_img1;
        this.album_img2 = album_img2;
    }

    public String getTrack_id() {
        return track_id;
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public String getAlbum_name() {
        return album_name;
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

    public void setTrack_id(String track_id) {
        this.track_id = track_id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
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
    

    // Metodo toString
    @Override
    public String toString() {
        String res = this.track_id + "<SEP>" + this.name + "<SEP>" + this.getDuration() + "<SEP>" + this.artist_name + "<SEP>"
                + (album_img0 != null ? album_img0 : "") + "<SEP>"
                + (album_img1 != null ? album_img1 : "") + "<SEP>"
                + (album_img2 != null ? album_img2 : "") + "<SEP>\n";
        return res;
    }

    // Metodo equals
    public boolean equals(Track t) {
        return this.name.equals(t.name) && this.artist_name.equals(t.artist_name) && this.album_name.equals(t.album_name);
    }

    // Metodo privato per creare un ImageView da un URL
    public ImageView createImageView(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Image image = new Image(imageUrl);
            return new ImageView(image);
        } else {
            return null;
        }
    }
}
