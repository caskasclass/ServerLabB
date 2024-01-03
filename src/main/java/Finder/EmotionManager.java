package Finder;

import SQLBuilder.SQLFinder;
import SQLBuilder.SQLInserter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import jars.ChartData;
import jars.EmotionEvaluation;
import jars.Track;
/**
 *
 * @author lorenzo
 */

/*
 * Gli oggetti modellati da questa classe si occupano dell'inserimento e della
 * lettura delle informazioni relative alle emozioni
 * legate dei brani.
 */
public class EmotionManager implements EmotionManagerInterface {

    private SQLFinder sqlfinder; // oggetto in gradi di trovare nelle tabelle
    private SQLInserter sqlinserter; // oggetto in grado di inserire nelle tabelle
    private ArrayList<EmotionEvaluation> res; // risultato di una query
    private String searchCriteria; // criterio di ricerca

    // costruttore in caso ricerca di dati
    public EmotionManager(Track track) {
        this.sqlfinder = new SQLFinder();
        this.sqlinserter = null;
        this.res = new ArrayList<EmotionEvaluation>();
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
    public void insertEmotions(EmotionEvaluation emotion) { // viene passata un'emozione che contiene già al suo interno
                                                            // track_id e
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
        ArrayList<String> values = new ArrayList<String>();
        for (String s : emotion.getEmozione().keySet()) {
            System.out.println(s);
            values.add(s);
            values.add(emotion.getUserid());
            values.add(emotion.getTrack_id());
            values.add("Silence is golden");
            values.add("" + emotion.getEmozione().get(s));
            this.sqlinserter.setColums(column);
            this.sqlinserter.setValues(values);
            // settaggio della query
            this.sqlinserter.setQuery("emotions"); // viene passato il nome della tabella in cui inserire
            this.sqlinserter.executeQuery();
            this.sqlinserter.renewQuery(); // rinnovamento della query
            values.clear();
        }

    }

    @Override
    public EmotionEvaluation getMyEmotions(String userId) { // siccome il costruttore ha già settato i criteri di
                                                            // ricerca non viene
        EmotionEvaluation emotion = null; // passato nulla
        HashMap<String, Integer> emotions = new HashMap<>(); // passato nulla
        this.sqlfinder.renewQuery(); // rinnovamento della query
        this.sqlfinder.renewResultSet(); // rinnovamento dei risultati
        // settaggio della query
        this.sqlfinder.setQuery("*", "emotions",
                "track_id = '" + this.searchCriteria + "' and userid = '" + userId + "'");
        System.out.println("Questa è la query : " + this.sqlfinder.getQuery());
        this.sqlfinder.executeQuery(); // esecuzione della query
        ResultSet res = this.sqlfinder.getRes(); // risultati della query
        try {
            if (res.next()) {
                emotions.put(res.getString("emotion"), (int) res.getByte("points"));
                while (this.sqlfinder.getRes().next()) { // ciclo finchè ci sono risultati
                    String emozione = this.sqlfinder.getRes().getString("emotion");
                    byte points = this.sqlfinder.getRes().getByte("points");
                    emotions.put(emozione, (int) points); // inserimento nella lista
                }
            } else {
                // do nothing per ora
            }
            emotion = new EmotionEvaluation(emotions, userId, this.searchCriteria, "Silence is golden"); // creazione
                                                                                                         // dell'oggetto
                                                                                                         // emozione

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return emotion; // ritorno della lista
    }

    @Override
    public ArrayList<ChartData> getAllEmotions() {
        ArrayList<ChartData> listOfData = new ArrayList<ChartData>(); // lista di oggetti che contengono i dati per il
        ChartData chartData; // grafico
        this.sqlfinder.renewQuery(); // rinnovamento della query
        this.sqlfinder.renewResultSet(); // rinnovamento dei risultati
        // settaggio della query
        this.sqlfinder.setQuery("emotion, AVG(points) AS average_rating, COUNT(*) AS total_ratings", "emotions",
                "track_id = '" + this.searchCriteria + "' GROUP BY emotion");
        System.out.println("Questa è la query : " + this.sqlfinder.getQuery());
        this.sqlfinder.executeQuery();
        ResultSet res = sqlfinder.getRes();
        try {
            while (res.next()) { // ciclo finchè ci sono risultati
                chartData = new ChartData(res.getString("emotion"), res.getDouble("average_rating"),
                        res.getInt("total_ratings"));
                listOfData.add(chartData); // inserimento nella lista
            }
        } catch (SQLException e) {
          System.err.println("Qualcosa è andato storto...\n"+e.getMessage());
        } 

        return listOfData;
    }
}
