package Finder;

import SQLBuilder.SQLBuilder;
import java.sql.*;
import java.util.ArrayList;
import pkg.Track;

/**
 *
 * @author lorenzo
 */
public class SongFinder implements SongFinderInterface {

    private SQLBuilder dbmanager;
    private ArrayList<Track> res;
    private ArrayList<String> trackId;
    private String[] searchCriteria;

    public SongFinder(String title) {
        this.dbmanager = new SQLBuilder();
        this.res = new ArrayList<Track>();
        this.trackId = new ArrayList<String>();
        this.searchCriteria = new String[1];
        this.searchCriteria[0] = title;
    }

    public SongFinder(String artist, int year) {
        this.dbmanager = new SQLBuilder();
        this.res = new ArrayList<Track>();
        this.trackId = new ArrayList<String>();
        this.searchCriteria = new String[2];
        this.searchCriteria[0] = artist;
        this.searchCriteria[1] = "" + year;
    }

    @Override
    public void setSearchCriteria(String title) {
        this.searchCriteria = new String[1];
        this.searchCriteria[0] = title;
    }

    @Override
    public void setSearchCriteria(String artist, int year) {
        this.searchCriteria = new String[2];
        this.searchCriteria[0] = artist;
        this.searchCriteria[1] = "" + year;
    }

    @Override
    public ArrayList<String> getTrackId() {
        this.dbmanager.renewQuery();
        this.dbmanager.renewResultSet();
        if (this.searchCriteria.length == 1) {
            this.dbmanager.setQuery("track_id", "tracks", "name = '" + this.searchCriteria[0] + "';");
        } else {
            this.dbmanager.setSelect("track_id");
            this.dbmanager.setFrom("tracks\n"
                    + "join albums on tracks.album_id = albums.album_id\n"
                    + "join artist_mapping_album on albums.album_id = artist_mapping_album.album_id\n"
                    + "join artisti on artisti.artist_id = artist_mapping_album.artist_id");
            this.dbmanager.setWhere("artisti.name = '" + this.searchCriteria[0] + "' and albums.release_date between '" + this.searchCriteria[1] + "'-01-01' AND " + this.searchCriteria[1] + "-12-31';");
        }
        try {
            while (this.dbmanager.getRes().next()) {
                this.trackId.add(this.dbmanager.getRes().getString("track_id"));
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return this.trackId;
    }

    @Override
    public boolean checkResult() {
        return this.trackId.isEmpty();
    }

    @Override
    public ArrayList<Track> getAllTrackInformation(int begin, int end) {
        this.dbmanager.renewResultSet();
        if (this.checkResult()) {
            return null;
        }
        for (int i = begin; i < this.trackId.size() || i < end; i++) {
            this.dbmanager.renewQuery();
            this.dbmanager.setSelect("DISTINCT artisti.name AS artist_name, tracks.*, albums.*");
            this.dbmanager.setFrom("tracks\n"
                    + "JOIN artist_mapping_track ON tracks.track_id = artist_mapping_track.track_id \n"
                    + "JOIN artisti ON artist_mapping_track.artist_id = artisti.artist_id\n"
                    + "JOIN artist_mapping_album ON artisti.artist_id = artist_mapping_album.artist_id\n"
                    + "JOIN albums ON artist_mapping_album.album_id = albums.album_id");
            this.dbmanager.setWhere("tracks.track_id = '" + this.trackId.get(i) + "';");
            this.dbmanager.executeQuery();
            try {
                while (this.dbmanager.getRes().next()) {
                    String track_id = this.dbmanager.getRes().getString("track_id");
                    String name = this.dbmanager.getRes().getString("name");
                    String artist_name = this.dbmanager.getRes().getString("artist_name");
                    int duration_ms = this.dbmanager.getRes().getInt("duration_ms");
                    String album_name = this.dbmanager.getRes().getString("album_name");
                    String album_img0 = this.dbmanager.getRes().getString("album_img0");
                    String album_img1 = this.dbmanager.getRes().getString("album_img1");
                    String album_img2 = this.dbmanager.getRes().getString("album_img2");
                    res.add(new Track(track_id, name, duration_ms, artist_name, album_name, album_img0, album_img1, album_img2));
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            } catch (NullPointerException e) {
                System.err.println(e.getMessage());
            }
        }
        return this.res;
    }
}
