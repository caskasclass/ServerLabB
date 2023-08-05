package Server;

import java.util.ArrayList;

/**
 *
 * @author lorenzo
 */
public class Playlist {

    private String titolo;
    private ArrayList<String> trackList; //contiene solo i track_id
    private String user;

    public Playlist(String titolo, User user) {
        this.titolo = titolo;
        this.user = user.getUserid();
        this.trackList = new ArrayList<String>();
    }

    public Playlist(String titolo, ArrayList<String> trackList, User user) {
        this.titolo = titolo;
        this.trackList = trackList;
        this.user = user.getUserid();
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public ArrayList<String> getTrackList() {
        return trackList;
    }

    public void setTrackList(ArrayList<String> trackList) {
        this.trackList = trackList;
    }

    public String getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user.getUserid();
    }

    @Override
    public String toString() {
        String res = this.titolo + "<?SEP" + this.user + "\n";
        for (int i = 0; i < this.trackList.size(); i++) {
            res += this.trackList.get(i) + "<$SEP>";
        }
        return res;
    }

}
