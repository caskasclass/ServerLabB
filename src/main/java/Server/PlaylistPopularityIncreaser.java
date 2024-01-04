package Server;

import SQLBuilder.SQLInserter;
import jars.Playlist;

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
 * Oggetto che si occupa dell'aumento nel database del campo popolarity nella tabella playlist
 */

public class PlaylistPopularityIncreaser extends Thread {
    private Playlist p;
    private SQLInserter sqlinserter;

    public PlaylistPopularityIncreaser(Playlist p) {
        this.p = p;
        this.sqlinserter = new SQLInserter();
        this.run();
    }

    @Override
    public void run() {
        System.out.println("Popolarity increaser start"); // annuncio dell'avvio corretto
        this.sqlinserter.updatePlaylistPopolarity(p); // esecuzione dell'update
    }

}
