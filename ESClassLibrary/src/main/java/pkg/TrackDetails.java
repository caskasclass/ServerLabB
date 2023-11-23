package pkg;

import java.io.Serializable;

public class TrackDetails implements Serializable {
    public Track track;
    public String albumId;

    public TrackDetails(Track track, String albumId) {
        this.track = track;
        this.albumId = albumId;
    }



}
