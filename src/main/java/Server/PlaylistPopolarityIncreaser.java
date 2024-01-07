package Server;

import SQLBuilder.SQLInserter;
import jars.*;

public class PlaylistPopolarityIncreaser extends Thread {
   private Playlist p;
    private SQLInserter sqlinserter;

    public PlaylistPopolarityIncreaser(Playlist p) {
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
