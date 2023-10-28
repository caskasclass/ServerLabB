package Server;

import java.util.ArrayList;

import SQLBuilder.SQLInserter;

public class PopolarityIncreaser extends Thread {
    private SQLInserter sqlinserter;
    private ArrayList<String> trackId;
    
    public PopolarityIncreaser(ArrayList<String> trackId) {
        this.sqlinserter = new SQLInserter();
        this.trackId = trackId;
        this.run();
    }

    @Override
    public void run() {
        System.out.println("Popolarity increaser start");
        for(int i = 0; i < trackId.size(); i++) {
            this.sqlinserter.updateTrackPopularity(this.trackId.get(i));
        }
    }
}
