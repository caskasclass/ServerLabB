package Server;

/**
 *
 * @author lorenzo
 */
public class Emotion {
    private String emozione;
    private String userid;
    private String track_id;
    private String note;
    private byte punteggio;

    public Emotion(String emozione, String userid, String track_id, String note, byte punteggio) {
        this.emozione = emozione;
        this.userid = userid;
        this.track_id = track_id;
        this.note = note;
        this.punteggio = punteggio;
    }

    public String getEmozione() {
        return emozione;
    }

    public String getUserid() {
        return userid;
    }

    public String getTrack_id() {
        return track_id;
    }

    public String getNote() {
        return note;
    }

    public byte getPunteggio() {
        return punteggio;
    }
    
    @Override
    public String toString() {
        String res = 
    }
}
