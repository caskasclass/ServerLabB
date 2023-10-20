package Finder;

import SQLBuilder.SQLFinder;
import SQLBuilder.SQLInserter;
import java.sql.SQLException;
import java.util.ArrayList;
import pkg.Emotion;
import pkg.Track;

/**
 *
 * @author lorenzo
 */
public class EmotionManager implements EmotionManagerInterface {
    
    private SQLFinder sqlfinder;
    private SQLInserter sqlinserter;
    private ArrayList<Emotion> res;
    private String searchCriteria;
    
    public EmotionManager(Track track) {
        this.sqlfinder = new SQLFinder();
        this.sqlinserter = null;
        this.res = new ArrayList<Emotion>();
        this.searchCriteria = track.getTrack_id();
    }
    
    public EmotionManager() {
        this.sqlfinder = null;
        //this.sqlinserter = new SQLInserter();
        this.res = null;
        this.searchCriteria = null;
    }

    @Override
    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }
    
    @Override
    public void insertEmotion(Emotion emotion) {
        this.sqlinserter.renewQuery();
        ArrayList<String> column = new ArrayList<String>();
        column.add("emozione");
        column.add("userid");
        column.add("track_id");
        column.add("note");
        column.add("punteggio");
        ArrayList<String> values = new ArrayList<String>();
        values.add(emotion.getEmozione());
        values.add(emotion.getUserid());
        values.add(emotion.getTrack_id());
        values.add(emotion.getNote());
        values.add("" + emotion.getPunteggio());
        //this.sqlinserter.setQuery("emozioni", column, values);
        this.sqlinserter.executeQuery();
    }
    
    @Override
    public ArrayList<Emotion> getEmotions() {
        this.sqlfinder.renewQuery();
        this.sqlfinder.renewResultSet();
        this.sqlfinder.setQuery("*", "emozioni", "track_id = '" + this.searchCriteria + "'");
        this.sqlfinder.executeQuery();
        try {
            while(this.sqlfinder.getRes().next()) {
                String emozione = this.sqlfinder.getRes().getString("emozione");
                String userid = this.sqlfinder.getRes().getString("userid");
                String track_id = this.sqlfinder.getRes().getString("track_id");
                String note = this.sqlfinder.getRes().getString("note");
                byte punteggio = this.sqlfinder.getRes().getByte("punteggio");
                this.res.add(new Emotion(emozione, userid, track_id, note, punteggio));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return res;
    }
}
