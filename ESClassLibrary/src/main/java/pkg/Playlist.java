package pkg;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author lorenzo
 */
public class Playlist implements Serializable {

    private String title;
    private ArrayList<String> trackList; //contiene solo i track_id
    private String user;

    public Playlist(String title, String user) {
        this.title = title;
        this.user = user;
        this.trackList = new ArrayList<String>();
    }

    public Playlist(String title, ArrayList<String> trackList, String user) {
        this.title = title;
        this.trackList = trackList;
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        String res = this.title + "<?SEP" + this.user + "\n";
        for (int i = 0; i < this.trackList.size(); i++) {
            res += this.trackList.get(i) + "<$SEP>";
        }
        return res;
    }

}
