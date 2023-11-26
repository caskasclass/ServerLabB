package pkg;

import java.io.Serializable;

/**
 *
 * @author lorenzo
 */
public class Emotion implements Serializable {
    private String emotion;
    private String userid;
    private String track_id;
    private String notes;
    private byte points;

    public Emotion(String emotion, String userid, String track_id, String notes, byte points) {
        this.emotion = emotion;
        this.userid = userid;
        this.track_id = track_id;
        this.notes = notes;
        this.points = points;
    }

    public String getEmozione() {
        return emotion;
    }

    public String getUserid() {
        return userid;
    }

    public String getTrack_id() {
        return track_id;
    }

    public String getNote() {
        return notes;
    }

    public byte getPunteggio() {
        return points;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setTrack_id(String track_id) {
        this.track_id = track_id;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPoints(byte points) {
        this.points = points;
    }
    
    
    @Override
    public String toString() {
        String res = emotion + "<!SEP>" + userid + "<!SEP>" + track_id + "<!SEP>" + notes + "<!SEP>" + points + "<!SEP>";
        return res;
    }
}
