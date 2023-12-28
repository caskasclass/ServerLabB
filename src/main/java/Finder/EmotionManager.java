package Finder;

import SQLBuilder.SQLFinder;
import SQLBuilder.SQLInserter;
import java.sql.SQLException;
import java.util.ArrayList;
import jars.Emotion;
import jars.Track;

/**
 *
 * @author lorenzo
 */
public class EmotionManager implements EmotionManagerInterface {

    private SQLFinder sqlfinder; // oggetto in gradi di trovare nelle tabelle
    private SQLInserter sqlinserter; // oggetto in grado di inserire nelle tabelle
    private ArrayList<Emotion> res; // risultato di una query
    private String searchCriteria; // criterio di ricerca

    // costruttore in caso ricerca di dati
    public EmotionManager(Track track) {
        this.sqlfinder = new SQLFinder();
        this.sqlinserter = null;
        this.res = new ArrayList<Emotion>();
        this.searchCriteria = track.getTrack_id(); // criterio di ricerca settato con il track_id della traccia passata
    }

    // costruttore nel caso di inserimento
    public EmotionManager() {
        this.sqlfinder = null;
        this.sqlinserter = new SQLInserter();
        this.res = null;
        this.searchCriteria = null;
    }

    // nel caso si voglia risettare il criterio di ricerca
    @Override
    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    // metodo di inserimento di un'emozione
    @Override
    public void insertEmotion(Emotion emotion) { // viene passata un'emozione che contiene già al suo interno track_id e
                                                 // userid
        this.sqlinserter.renewQuery(); // rinnovamento della query
        ArrayList<String> column = new ArrayList<String>(); // lista che contiene i nomi delle colonne in cui inserire i
                                                            // dati
        // settaggio dei nomi
        column.add("emotion");
        column.add("userid");
        column.add("track_id");
        column.add("notes");
        column.add("points");
        ArrayList<String> values = new ArrayList<String>(); // lista che contiene i valori da inserire nelle rispettive
                                                            // colonne
        // settaggio dei valori
        values.add(emotion.getEmozione());
        values.add(emotion.getUserid());
        values.add(emotion.getTrack_id());
        values.add(emotion.getNote());
        values.add("" + emotion.getPunteggio());
        // settaggio di colonne e valori rispettivi nella query
        this.sqlinserter.setColums(column);
        this.sqlinserter.setValues(values);
        // settaggio della query
        this.sqlinserter.setQuery("emotions"); // viene passato il nome della tabella in cui inserire
        this.sqlinserter.executeQuery(); // esecuzione della query
    }

    @Override
    public ArrayList<Emotion> getEmotions() { // siccome il costruttore ha già settato i criteri di ricerca non viene
                                              // passato nulla
        this.sqlfinder.renewQuery(); // rinnovamento della query
        this.sqlfinder.renewResultSet(); // rinnovamento dei risultati
        // settaggio della query
        this.sqlfinder.setQuery("*", "emotions", "track_id = '" + this.searchCriteria + "'");
        this.sqlfinder.executeQuery(); // esecuzione della query
        try {
            while (this.sqlfinder.getRes().next()) { // ciclo finchè ci sono risultati
                String emozione = this.sqlfinder.getRes().getString("emotion");
                String userid = this.sqlfinder.getRes().getString("userid");
                String track_id = this.sqlfinder.getRes().getString("track_id");
                String note = this.sqlfinder.getRes().getString("notes");
                byte punteggio = this.sqlfinder.getRes().getByte("points");
                this.res.add(new Emotion(emozione, userid, track_id, note, punteggio)); // costruzione dell'emozione ed
                                                                                        // inserimento nella lista
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return res; // ritorno della lista
    }
}
