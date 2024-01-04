package Server;

import java.util.ArrayList;

import SQLBuilder.SQLInserter;

/**
 * Progetto laboratorio B: "Emotional Songs", anno 2022-2023
 * 
 * @author Beatrice Bastianello, matricola 751864, VA
 * @author Lorenzo Barbieri  , matricola 748695, VA
 * @author Filippo Storti , matricola 749195, VA
 * @author Nazar Viytyuk, matricola 748964, VA
 * @version 1.0
 */

/*
 * Oggetto che si occupa dell'update del campo popolarity nel database della tabella tracks
 */

public class PopularityIncreaser extends Thread {
    private SQLInserter sqlinserter; // oggetto in grado di inserire i risultati
    private ArrayList<String> trackId; // lista dei trackId di cui aumentare la popolarità

    // costruttore a cui viene solo passata la lista
    public PopularityIncreaser(ArrayList<String> trackId) {
        this.sqlinserter = new SQLInserter();
        this.trackId = trackId;
        this.run();
    }

    @Override
    public void run() {
        System.out.println("Popolarity increaser start"); // annuncio dell'avvio corretto
        for (int i = 0; i < trackId.size(); i++) { // per ogni trackId aumenta la popolarità della traccia che ha quel
                                                   // trackId
            this.sqlinserter.updateTrackPopularity(this.trackId.get(i)); // esecuzione del metodo apposito
        }
    }
}
