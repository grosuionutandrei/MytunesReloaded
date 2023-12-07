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
    public boolean createPlayList(PlayList playList) throws MyTunesException {
        boolean playListCreated = false;
        long playListId = insertPlayList(playList);
        if (playListId > 0) {
            playListCreated = addPlayListToJoinTable(playListId);
        }
        return playListCreated;
    }

    private long insertPlayList(PlayList playlist) throws MyTunesException {
        long playListId = -1;
        String sql = "INSERT INTO PLAYLISTS VALUES (?) ";
        try (Connection conn = CONNECTION_MANAGER.getConnection()) {
            PreparedStatement psmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            psmt.setString(1, playlist.getName());
            int rowsAffected = psmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = psmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long insertedId = generatedKeys.getLong(1);
                        playListId = insertedId;
                    }
                }
            }
        } catch (SQLException | MyTunesException e) {
            throw new MyTunesException(e.getMessage());
        }
        return playListId;
    }

    private boolean addPlayListToJoinTable(long playListId) throws MyTunesException {

        String sql = "INSERT INTO PlaylistSongs (PlayListId) values(?)";
        try (Connection conn = CONNECTION_MANAGER.getConnection()) {
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setLong(1, playListId);
            int rowsAffected = psmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | MyTunesException e) {
            throw new MyTunesException(e.getMessage(), e);
        }
    }

    /**
     * Delete the playlist from the database
     */
    @Override
    public boolean deletePlayList(int playListId) throws MyTunesException {
        String sql = "DELETE FROM Playlists WHERE PlaylistId=?";
        try (Connection conn = CONNECTION_MANAGER.getConnection()) {
            PreparedStatement psmt = null;
            int rowsAffected = 0;
            boolean deletedFromJoin = deletePlaylistFromJoinTable(playListId, conn, psmt);
            if (deletedFromJoin) {
                psmt = conn.prepareStatement(sql);
                psmt.setInt(1, playListId);
                rowsAffected = psmt.executeUpdate();
            }
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new MyTunesException("Error occurred in a database operation", e);
        }
    }

    /**
     * Delete the playlist from the join table
     */
    private boolean deletePlaylistFromJoinTable(int playListId, Connection conn, PreparedStatement psmt) throws MyTunesException {
        String sql = "DELETE FROM PlaylistSongs WHERE PlaylistId=?";
        try {
            psmt = conn.prepareStatement(sql);
            psmt.setInt(1, playListId);
            int rows = psmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            throw new MyTunesException("Error occurred in a database operation ", e);
        }
    }

    @Override
    public boolean updatePlayList(int playListId, String newTitle) throws MyTunesException {
        String sql = "UPDATE Playlists SET PlaylistName=? WHERE PlaylistId=?";
        try (Connection connection = CONNECTION_MANAGER.getConnection()) {
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setString(1, newTitle);
            psmt.setInt(2, playListId);
            int rowsAffected = psmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new MyTunesException(e.getMessage());
        }


    }

    @Override
    public PlayList getPlayList(int id) throws MyTunesException {
        return null;
    }

    @Override
    public List<PlayList> getAllPlaylistsFromCache() throws MyTunesException {
        return this.allPlaylists;
    }

    private void loadPlayListsFromDB() throws MyTunesException {
        Map<Integer, PlayList> playlistMap = new HashMap<>();
        List<PlayList> playLists = new ArrayList<>();
        PlayList playlist;
        Song song;
        try (Connection conn = CONNECTION_MANAGER.getConnection()) {
            String sql = "SELECT p.PlaylistId,p.PlaylistName, s.SongId, s.SongPath, s.Title, s.Artist, s.Genre, s.Length" +
                    " FROM PlaylistSongs ps" +
                    " JOIN Playlists p on p.playlistId = ps.playlistid" +
                    " LEFT JOIN Songs s on s.songId = ps.songId";

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

    public List<PlayList> reloadPlaylistsFromDb() throws MyTunesException {
        this.loadPlayListsFromDB();
        return this.allPlaylists;
    }

}
