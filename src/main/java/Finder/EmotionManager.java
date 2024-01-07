package Finder;

import SQLBuilder.SQLFinder;
import SQLBuilder.SQLInserter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import jars.Track;
import java.util.HashMap;
import jars.ChartData;
import jars.CommentSection;
import jars.EmotionEvaluation;

/**
 * Progetto laboratorio B: "Emotional Songs", anno 2022-2023
 * 
 * @author Beatrice Bastianello, matricola 751864, VA
 * @author Lorenzo Barbieri , matricola 748695, VA
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
            values.add(s);
            values.add(emotion.getUserid());
            values.add(emotion.getTrack_id());
            values.add(emotion.getNote().get(s));
            values.add("" + emotion.getEmozione().get(s));
            this.sqlinserter.setColums(column);
            this.sqlinserter.setValues(values);
            // Settaggio della query
            this.sqlinserter.setQuery("emotions"); // Viene passato il nome della tabella in cui inserire
            this.sqlinserter.executeQuery();
            this.sqlinserter.releaseConnection();
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
    public EmotionEvaluation getMyEmotions(String userId) { // siccome il costruttore ha già settato i criteri di
                                                            // ricerca non viene
        EmotionEvaluation emotion = null; // passato nulla
        HashMap<String, Integer> emotions = new HashMap<>();
        HashMap<String, String> emotionComments = new HashMap<>(); // passato nulla
        this.sqlfinder.renewQuery(); // rinnovamento della query
        this.sqlfinder.renewResultSet(); // rinnovamento dei risultati
        // settaggio della query
        this.sqlfinder.setQuery("*", "emotions",
                "track_id = '" + this.searchCriteria + "' and userid = '" + userId + "'");
        System.out.println("Questa è la query : " + this.sqlfinder.getQuery());
        this.sqlfinder.executeQuery(); // Esecuzione della query
        try {
            while (this.sqlfinder.getRes().next()) { // ciclo finchè ci sono risultati
                String emozione = this.sqlfinder.getRes().getString("emotion");
                byte points = this.sqlfinder.getRes().getByte("points");
                emotions.put(emozione, (int) points); // inserimento nella lista
                emotionComments.put(emozione, this.sqlfinder.getRes().getString("notes"));
            }
            sqlfinder.releaseConnection();
            emotion = new EmotionEvaluation(emotions, userId, this.searchCriteria, emotionComments); // creazione
                                                                                                     // dell'oggetto
                                                                                                     // emozione

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return emotion; // ritorno della lista
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
            sqlfinder.releaseConnection();
        } catch (SQLException e) {
            System.err.println("Qualcosa è andato storto...\n" + e.getMessage());
        }
        return listOfData;
    }

    // CascasCreationAbomination 2.0
    @Override
    public ArrayList<CommentSection> getAllComments() {
        ArrayList<CommentSection> listOfComments = new ArrayList<CommentSection>(); // lista di oggetti che contengono
                                                                                    // i commenti
        CommentSection commentSection; // oggetto che contiene i commenti
        this.sqlfinder.renewQuery(); // rinnovamento della query
        this.sqlfinder.renewResultSet(); // rinnovamento dei risultati
        // settaggio della query
        this.sqlfinder.setQuery("emotion,userid, notes", "emotions",
                "track_id = '" + this.searchCriteria+"'");
        System.out.println("Questa è la query : " + this.sqlfinder.getQuery());
        this.sqlfinder.executeQuery();
        ResultSet res = sqlfinder.getRes();
        try {
            while (res.next()) { // ciclo finchè ci sono risultati
                commentSection = new CommentSection(res.getString("emotion"), res.getString("userid"),
                        res.getString("notes"));
                listOfComments.add(commentSection); // inserimento nella lista
            }
            sqlfinder.releaseConnection();
        } catch (SQLException e) {
            System.err.println("Qualcosa è andato storto...\n" + e.getMessage());
        }
        return listOfComments;
    }

}
