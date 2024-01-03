package jars;

import java.io.Serializable;

/**
 *
 * @author lorenzo
 */
public class AlbumPreview implements Serializable  {

    String albumId;
    String albumName;
    String albumImg0;
    String albumImg1;
    String albumImg2;
    String AlbumArtists;

    public AlbumPreview(String albumId, String albumName, String albumImg0, String albumImg1, String albumImg2, String AlbumArtists) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.albumImg0 = albumImg0;
        this.albumImg1 = albumImg1;
        this.albumImg2 = albumImg2;
        this.AlbumArtists = AlbumArtists;
    }

    public String  getAlbumId() {
        return albumId;
    }

    public String  getAlbumName() {
        return albumName;
    }

    public String  getAlbumImg0() {
        return albumImg0;
    }

    public String  getAlbumImg1() {
        return albumImg1;
    }

    public String  getAlbumImg2() {
        return albumImg2;
    }

    public String  getAlbumArtists() {
        return AlbumArtists;
    }


}
