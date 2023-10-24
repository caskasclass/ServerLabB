package Finder;

import SQLBuilder.*;
import java.sql.SQLException;
import java.util.ArrayList;
import pkg.Playlist;
import pkg.Track;
import pkg.User;

/**
 *
 * @author lorenzo
 */
public class PlaylistManager implements PlaylistManagerInterface {

    private SQLFinder sqlfinder;
    private SQLInserter sqlinserter;
    private Playlist res;
    private ArrayList<String> trackId;
    private String[] searchCriteria;

    public PlaylistManager(String title, User user) {
        this.sqlfinder = new SQLFinder();
        this.res = null;
        this.trackId = new ArrayList<String>();
        this.searchCriteria = new String[2];
        this.searchCriteria[0] = title;
        this.searchCriteria[1] = user.getUserid();
        this.sqlinserter = null; 
    }
    
    public PlaylistManager() {
        this.sqlfinder = null;
        this.res = null;
        this.trackId = null;
        this.sqlinserter = new SQLInserter();
        this.searchCriteria = null;
    }
    
    public PlaylistManager(ArrayList<String> trackId) {
        this.sqlfinder = new SQLFinder();
        this.res = null;
        this.trackId = trackId;
        this.sqlinserter = null;
        this.searchCriteria = null;
    }

    @Override
    public void setSearchCriteria(String title, User user) {
        this.searchCriteria[0] = title;
        this.searchCriteria[1] = user.getUserid();
    }

    @Override
    public Playlist getPlaylist() {
        this.sqlfinder.renewQuery();
        this.sqlfinder.renewResultSet();
        this.sqlfinder.setQuery("track_id", "playlist", "titolo = '" + this.searchCriteria[0] + "' AND userid = '" + this.searchCriteria[1] + "'");
        this.sqlfinder.executeQuery();
        try {
            while (this.sqlfinder.getRes().next()) {
                String trackId = this.sqlfinder.getRes().getString("track_id");
                this.trackId.add(trackId);
            }
            this.res = new Playlist(this.searchCriteria[0], this.trackId, this.searchCriteria[1]);
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return this.res;
    }

    @Override
    public ArrayList<Track> getAllTrackInformation(int begin, int end) {
        SongFinder sf = new SongFinder(this.trackId);
        return sf.getAllTrackInformation(begin, end);
    }

    @Override
    public void createPlaylist(Playlist p) {
        if (p.getTrackList().isEmpty()) {
            System.err.println("Playlist is empty");
            return;
        }
        ArrayList<String> column = new ArrayList<String>();
        column.add("userid");
        column.add("titolo");
        column.add("track_id");
        this.sqlinserter.setColums(column);
        ArrayList<String> values = new ArrayList<String>();
        values.add(p.getUser());
        values.add(p.getTitolo());
        for (int i = 0; i < p.getTrackList().size(); i++) {
            values.add(p.getTrackList().get(i));
            this.sqlinserter.setValues(values);
            this.sqlinserter.setQuery("playlist");
            this.sqlinserter.executeQuery();
            values.remove(values.size() - 1);
        }
    }
    
}
