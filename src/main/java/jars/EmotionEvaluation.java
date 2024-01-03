package jars;

import java.io.Serializable;
import java.util.HashMap;


/**
 *
 * @author lorenzo
 */
public class EmotionEvaluation implements Serializable {
    private HashMap<String,Integer> emotion;
    private String userid;
    private String track_id;
    private String notes;

    public EmotionEvaluation(HashMap<String,Integer> emotion, String userid, String track_id, String notes) {
        this.emotion = emotion;
        this.userid = userid;
        this.track_id = track_id;
        this.notes = notes;
    }

    public HashMap<String,Integer> getEmozione() {
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


    public void setEmotion(HashMap<String,Integer> emotion) {
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

    
    
    @Override
    public String toString() {
        String res = emotion + "<!SEP>" + userid + "<!SEP>" + track_id + "<!SEP>" + notes + "<!SEP>";
        return res;
    }
}
