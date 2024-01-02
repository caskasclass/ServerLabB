package jars;

import java.rmi.*;
import java.util.ArrayList;

/**
 *
 * @author lorenzo
 */
public interface ServerInterface extends Remote {

    int[] PORT = new int[] {
        8081, 8082, 8083, 8084, 8085, 8086, 8087, 8088, 8089, 8090,
        8091, 8092, 8093, 8094, 8095, 8096, 8097, 8098, 8099, 8100,
        8101, 8102, 8103, 8104, 8105, 8106, 8107, 8108, 8109, 8110,
        8111, 8112, 8113, 8114, 8115, 8116, 8117, 8118, 8119, 8120,
        8121, 8122, 8123, 8124, 8125, 8126, 8127, 8128, 8129, 8130,
        8131, 8132, 8133, 8134, 8135, 8136, 8137, 8138, 8139, 8140,
        8141, 8142, 8143, 8144, 8145, 8146, 8147, 8148, 8149, 8150,
        8151, 8152, 8153, 8154, 8155, 8156, 8157, 8158, 8159, 8160,
        8161, 8162, 8163, 8164, 8165, 8166, 8167, 8168, 8169, 8170,
        8171, 8172, 8173, 8174, 8175, 8176, 8177, 8178, 8179, 8180,
        8181, 8182, 8183, 8184, 8185, 8186, 8187, 8188, 8189, 8190,
    };
    
    User access(String strAccess, String psw) throws RemoteException;

    void createPlaylist(Playlist p) throws RemoteException;

    ArrayList<String> getAlbumId(String name) throws RemoteException;

    ArrayList<String> getAlbumId(String artist, int year) throws RemoteException;

    ArrayList<Album> getAllAlbumInformation(ArrayList<String> trackId, int begin, int end) throws RemoteException;

    ArrayList<Track> getAllTrackInformation(Playlist p, int begin, int end) throws RemoteException;

    EmotionEvaluation getMyEmotion(Track track,String user_id) throws RemoteException;

    ArrayList<ChartData> getAllEmotion(Track track) throws RemoteException;

    Playlist getPlaylist(String title, String user) throws RemoteException;

    ArrayList<String> getTrackId(String title) throws RemoteException;

    ArrayList<String> getTrackId(String artist, int year) throws RemoteException;

    ArrayList<Track> getAllTrackInformation(ArrayList<String> trackId, int begin, int end) throws RemoteException;

    ArrayList<String> getAllTrackId() throws RemoteException;

    void insertEmotion(EmotionEvaluation e) throws RemoteException;

    boolean checkIfRated(String track_id, String user_id) throws RemoteException;

    void registration(User u) throws RemoteException;

    ArrayList<TrackDetails> getTopTracks() throws RemoteException;

    ArrayList<AlbumPreview> getTopAlbums() throws RemoteException;

    ArrayList<Playlist> getAllPlaylist() throws RemoteException;

    void deletePlayList(Playlist p) throws RemoteException;

    void deleteTrack(Playlist p, String trackid) throws RemoteException;
    
}
