package Server;

import java.util.ArrayList;

import java.sql.*;

/**
 *
 * @author lorenzo
 */
public class Track {

    private String track_id;
    private String name;
    private int duration;
    private String artist_name;
    private String album_name;
    private String album_img0;
    private String album_img1;
    private String album_img2;
    private ArrayList<Emotion> assotiated_emotions;

    //questo è il costruttore a cui viene già mandato anche l'array delle emozioni più gli altri dati, non so se potà servire
    public Track(String track_id, String name, int duration, String artist_name, String album_name, String album_img0, String album_img1, String album_img2, ArrayList<Emotion> assotiated_emotions) {
        this.track_id = track_id;
        this.name = name;
        this.duration = duration / (1000 * 60);
        this.artist_name = artist_name;
        this.album_name = album_name;
        this.album_img0 = album_img0;
        this.album_img1 = album_img1;
        this.album_img2 = album_img2;
        this.assotiated_emotions = assotiated_emotions;
    }

    //il costruttore a cui vengono mandati tutti i dati della canzone, ma le emozioni le prende dal db
    public Track(String track_id, String name, int duration, String artist_name, String album_name, String album_img0, String album_img1, String album_img2) throws SQLException {
        this.track_id = track_id;
        this.name = name;
        this.duration = duration / (1000 * 60); //trasforma la durata da millisecondi a minuti
        this.artist_name = artist_name;
        this.album_name = album_name;
        this.album_img0 = album_img0;
        this.album_img1 = album_img1;
        this.album_img2 = album_img2;
        this.assotiated_emotions = new ArrayList<Emotion>();
        Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/EmotionalSongs", "postgres", "5640");
        //statement con la query che cerca una canzone per titolo
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM emozioni WHERE track_id = '" + this.track_id + "'");
        //esecuzione della query
        ResultSet qres = ps.executeQuery();
        //elaborazione dei risultati
        while (qres.next()) {
            String emozione = qres.getString("emozione");
            String userid = qres.getString("userid");
            String note = qres.getString("note");
            byte punteggio = qres.getByte("punteggio");
            this.assotiated_emotions.add(new Emotion(emozione, userid, this.track_id, note, punteggio));
        }
        ps.close();
        conn.close();
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

    public String getAlbum_img0() {
        return album_img0;
    }

    public String getAlbum_img1() {
        return album_img1;
    }

    public String getAlbum_img2() {
        return album_img2;
    }

    public ArrayList<Emotion> getAssotiated_emotions() {
        return assotiated_emotions;
    }

    //inserisco comunque il metodo toString anche se non so se potrà servire
    @Override
    public String toString() {
        String res = this.track_id + "<SEP>" + this.name + "<SEP>" + this.getDuration() + "<SEP>" + this.artist_name + "<SEP>"
                + this.album_img0 + "<SEP>" + this.album_img1 + "<SEP>" + this.album_img2 + "<SEP>";
        for (int i = 0; i < this.assotiated_emotions.size(); i++) {
            res += "\n";
            res += "<SEP>" + this.assotiated_emotions.get(i).toString() + "<SEP>";
        }
        return res;
    }
}
