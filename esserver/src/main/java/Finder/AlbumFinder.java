package Finder;

import SQLBuilder.SQLFinder;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import pkg.Album;
import pkg.Artist;

/**
 *
 * @author lorenzo
 */
public class AlbumFinder implements AlbumFinderInterface {

    private SQLFinder dbmanager;
    private ArrayList<Album> res;
    private ArrayList<String> albumId;
    private String[] searchCriteria;

    public AlbumFinder(String name) {
        this.dbmanager = new SQLFinder();
        this.res = null;
        this.albumId = new ArrayList<String>();
        this.searchCriteria = new String[1];
        this.searchCriteria[0] = name;
    }

    public AlbumFinder(String artist, int year) {
        this.dbmanager = new SQLFinder();
        this.res = null;
        this.albumId = new ArrayList<String>();
        this.searchCriteria = new String[2];
        this.searchCriteria[0] = artist;
        this.searchCriteria[1] = "" + year;
    }
    
    public AlbumFinder(ArrayList<String> albumId) {
        this.dbmanager = new SQLFinder();
        this.res = new ArrayList<Album>();
        this.albumId = albumId;
        this.searchCriteria = null;
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
    public ArrayList<String> getAlbumId() {
        this.dbmanager.renewQuery();
        this.dbmanager.renewResultSet();
        if (this.searchCriteria.length == 1) {
            this.dbmanager.setQuery("album_id", "albums", "album_name = '" + this.searchCriteria[0] + "';");
        } else {
            this.dbmanager.setSelect("album_id");
            this.dbmanager.setFrom("albums\n"
                    + "join artist_mapping_album on albums.album_id = artist_mapping_album.album_id"
                    + "join artisti on artist_mapping_album.artist_id = artisti.artist_id");
            this.dbmanager.setWhere("artisti.name = '" + this.searchCriteria[0] + "' and albums.release_date between '" + this.searchCriteria[1] + "'-01-01' AND " + this.searchCriteria[1] + "-12-31';");
        }
        this.dbmanager.executeQuery();
        return this.albumId;
    }

    @Override
    public boolean checkResult() {
        return this.albumId.isEmpty();
    }

    @Override
    public ArrayList<Album> getAllAlbumInformation(int begin, int end) {
        this.dbmanager.renewResultSet();
        if (this.checkResult()) {
            return null;
        }
        for (int i = begin; i < this.albumId.size() || i < end; i++) {
            this.dbmanager.renewQuery();
            this.dbmanager.setSelect("albums.*, artist.*");
            this.dbmanager.setFrom("albums\n"
                    + "JOIN artist_mapping_album ON albums.album_id = artist_mapping_album.album_id\n"
                    + "JOIN artisti ON artist_mapping_album.artist_id = artisti.artist_id");
            this.dbmanager.setWhere("albums.album_id = '" + this.albumId.get(i) + "';");
            this.dbmanager.executeQuery();
            try {
                while (this.dbmanager.getRes().next()) {
                    String album_name = this.dbmanager.getRes().getString("album_name");
                    Date release_date = this.dbmanager.getRes().getDate("release_date");
                    String album_img0 = this.dbmanager.getRes().getString("album_img0");
                    String album_img1 = this.dbmanager.getRes().getString("album_img1");
                    String album_img2 = this.dbmanager.getRes().getString("album_img2");
                    String artist_id = this.dbmanager.getRes().getString("artist_id");
                    String artist_name = this.dbmanager.getRes().getString("name");
                    this.res.add(new Album(albumId.get(i), album_name, release_date, album_img0, album_img1, album_img2, new Artist(artist_id, artist_name)));
                }
            } catch (SQLException e) {
                System.err.println(e.getMessage());
            }
        }
        return this.res;
    }
}
