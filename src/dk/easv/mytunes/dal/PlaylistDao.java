package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistDao implements IPlaylistDao {
    private final ConnectionManager CONNECTION_MANAGER = new ConnectionManager();
    private List<PlayList> allPlaylists;
    private static PlaylistDao instance;

    private PlaylistDao() throws MyTunesException {
        loadPlayListsFromDB();
    }

    public static PlaylistDao getPlaylistDao() throws MyTunesException {
        if (instance == null) {
            instance = new PlaylistDao();
        }
        return instance;
    }

    @Override
    public boolean createPlayList(PlayList playlist) throws MyTunesException {
        String sql = "INSERT INTO PLAYLISTS VALUES (?) ";
        try (Connection conn = CONNECTION_MANAGER.getConnection()) {
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1,playlist.getName());
            int rowsAffected = psmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
         throw  new MyTunesException("Error when connecting to database",e);
        }
    }

    @Override
    public boolean deletePlayList(int playListId) throws MyTunesException {
        return false;
    }

    @Override
    public boolean updatePlayList(PlayList playlist) {
        return false;
    }

    @Override
    public PlayList getPlayList(int id) throws MyTunesException {
        return null;
    }

    @Override
    public List<PlayList> getAllPlaylistsFromCache() throws MyTunesException {
        return this.allPlaylists;
    }

    public void loadPlayListsFromDB() throws MyTunesException {
        Map<Integer, PlayList> playlistMap = new HashMap<>();
        List<PlayList> playLists = new ArrayList<>();
        PlayList playlist;
        Song song;
        try (Connection conn = CONNECTION_MANAGER.getConnection()) {
            String sql = "SELECT p.PlaylistId,p.PlaylistName, s.SongId, s.SongPath, s.Title, s.Artist, s.Genre, s.Length" +
                    " FROM PlaylistSongs ps" +
                    " JOIN Playlists p on p.playlistId = ps.playlistid" +
                    " JOIN Songs s on s.songId = ps.songId";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int pId = rs.getInt(1);
                String pName = rs.getString(2);
                int sId = rs.getInt(3);
                String sPath = rs.getString(4);
                String sTitle = rs.getString(5);
                String sArtist = rs.getString(6);
                String sGenre = rs.getString(7);
                Long sLength = rs.getLong(8);
                playlist = new PlayList(pId, pName, null);
                song = new Song(sId, sPath, sTitle, sArtist, sGenre, sLength);

                if (playlistMap.containsKey(playlist.getId())) {
                    playlistMap.get(playlist.getId()).getPlayListSongs().add(song);
                } else {
                    playlist.getPlayListSongs().add(song);
                    playlistMap.put(playlist.getId(), playlist);
                }
            }
        } catch (SQLException e) {
            throw new MyTunesException("Error when trying to read from database");
        }
        playlistMap.keySet().forEach(elem -> playLists.add(playlistMap.get(elem)));
        this.allPlaylists = playLists;
    }

}
