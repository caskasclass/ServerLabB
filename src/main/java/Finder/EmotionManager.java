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
 * Progetto laboratorio B: "Emotional Songs", anno 2022-2023
 * 
 * @author Beatrice Bastianello, matricola 751864, VA
 * @author Lorenzo Barbieri  , matricola 748695, VA
 * @author Filippo Storti , matricola 749195, VA
 * @author Nazar Viytyuk, matricola 748964, VA
 * @version 1.0
 */

/**
 * Gli oggetti modellati da questa classe si occupano dell'inserimento e della lettura delle informazioni relative alle emozioni legate dei brani.
 */
public class EmotionManager implements EmotionManagerInterface {

    /** Oggetto in grado di trovare nelle tabelle. */
    private SQLFinder sqlfinder;
    /** Oggetto in grado di inserire nelle tabelle. */
    private SQLInserter sqlinserter;
    /** Risultato di una query. */
    private ArrayList<EmotionEvaluation> res;
    /** Criterio di ricerca. */
    private String searchCriteria;

    /**
     * Costruttore nel caso di ricerca di dati.
     * @param track Traccia per la quale si effettua la ricerca.
     */
    public EmotionManager(Track track) {
        this.sqlfinder = new SQLFinder();
        this.sqlinserter = null;
        this.res = new ArrayList<EmotionEvaluation>();
        this.searchCriteria = track.getTrack_id(); // Criterio di ricerca settato con il track_id della traccia passata
    }

    /**
     * Costruttore nel caso di inserimento.
     */
    public EmotionManager() {
        this.sqlfinder = null;
        this.sqlinserter = new SQLInserter();
        this.res = null;
        this.searchCriteria = null;
    }

    /**
     * Imposta il criterio di ricerca.
     * @param searchCriteria Criterio di ricerca.
     */
    @Override
    public void setSearchCriteria(String searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    /**
     * Metodo di inserimento di un'emozione.
     * @param emotion Emozione da inserire.
     */
    @Override
    public void insertEmotions(EmotionEvaluation emotion) {
        this.sqlinserter.renewQuery(); // Rinnovamento della query
        ArrayList<String> column = new ArrayList<String>(); // Lista che contiene i nomi delle colonne in cui inserire i dati
        // Settaggio dei nomi
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
            // Settaggio della query
            this.sqlinserter.setQuery("emotions"); // Viene passato il nome della tabella in cui inserire
            this.sqlinserter.executeQuery();
            this.sqlinserter.renewQuery(); // Rinnovamento della query
            values.clear();
        }
    }

    /**
     * Ottiene le emozioni di un utente per una traccia specifica.
     * @param userId ID dell'utente.
     * @return Emozioni dell'utente per la traccia specifica.
     */
    @Override
    public EmotionEvaluation getMyEmotions(String userId) {
        EmotionEvaluation emotion = null; // Passato nulla
        HashMap<String, Integer> emotions = new HashMap<>(); // Passato nulla
        this.sqlfinder.renewQuery(); // Rinnovamento della query
        this.sqlfinder.renewResultSet(); // Rinnovamento dei risultati
        // Settaggio della query
        this.sqlfinder.setQuery("*", "emotions",
                "track_id = '" + this.searchCriteria + "' and userid = '" + userId + "'");
        System.out.println("Questa è la query : " + this.sqlfinder.getQuery());
        this.sqlfinder.executeQuery(); // Esecuzione della query
        ResultSet res = this.sqlfinder.getRes(); // Risultati della query
        try {
            if (res.next()) {
                emotions.put(res.getString("emotion"), (int) res.getByte("points"));
                while (this.sqlfinder.getRes().next()) { // Ciclo finché ci sono risultati
                    String emozione = this.sqlfinder.getRes().getString("emotion");
                    byte points = this.sqlfinder.getRes().getByte("points");
                    emotions.put(emozione, (int) points); // Inserimento nella lista
                }
            } else {
                // Do nothing per ora
            }
            emotion = new EmotionEvaluation(emotions, userId, this.searchCriteria, "Silence is golden"); // Creazione
                                                                                                         // dell'oggetto
                                                                                                         // emozione

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return emotion; // Ritorno della lista
    }

    /**
     * Ottiene tutte le emozioni associate a una traccia.
     * @return Lista di dati per il grafico delle emozioni.
     */
    @Override
    public ArrayList<ChartData> getAllEmotions() {
        ArrayList<ChartData> listOfData = new ArrayList<ChartData>(); // Lista di oggetti che contengono i dati per il grafico
        ChartData chartData; // Grafico
        this.sqlfinder.renewQuery(); // Rinnovamento della query
        this.sqlfinder.renewResultSet(); // Rinnovamento dei risultati
        // Settaggio della query
        this.sqlfinder.setQuery("emotion, AVG(points) AS average_rating, COUNT(*) AS total_ratings", "emotions",
                "track_id = '" + this.searchCriteria + "' GROUP BY emotion");
        System.out.println("Questa è la query : " + this.sqlfinder.getQuery());
        this.sqlfinder.executeQuery();
        ResultSet res = sqlfinder.getRes();
        try {
            while (res.next()) { // Ciclo finché ci sono risultati
                chartData = new ChartData(res.getString("emotion"), res.getDouble("average_rating"),
                        res.getInt("total_ratings"));
                listOfData.add(chartData); // Inserimento nella lista
            }
        } catch (SQLException e) {
            System.err.println("Qualcosa è andato storto...\n" + e.getMessage());
        }
        return listOfData;
    }
}
