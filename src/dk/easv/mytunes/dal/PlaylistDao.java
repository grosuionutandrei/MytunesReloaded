package dk.easv.mytunes.dal;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.ExceptionsMessages;

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
            throw new MyTunesException(ExceptionsMessages.DB_UNSUCCESFULL,e.getCause());
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
            throw new MyTunesException(ExceptionsMessages.DB_UNSUCCESFULL, e.getCause());
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
            throw new MyTunesException(ExceptionsMessages.DB_UNSUCCESFULL, e.getCause());
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
            throw new MyTunesException(ExceptionsMessages.DB_UNSUCCESFULL, e.getCause());
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
            throw new MyTunesException(ExceptionsMessages.DB_UNSUCCESFULL,e.getCause());
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
            throw new MyTunesException(ExceptionsMessages.READING_FROMDB_FAILED);
        }
        playlistMap.keySet().forEach(elem -> playLists.add(playlistMap.get(elem)));
        this.allPlaylists = playLists;
    }

    public List<PlayList> reloadPlaylistsFromDb() throws MyTunesException {
        this.loadPlayListsFromDB();
        return this.allPlaylists;
    }

    @Override
    public boolean addSongToPlaylist(PlayList playListToAdd, Song songToBeAdded) throws MyTunesException {
        String sql = "INSERT INTO PlaylistSongs VALUES (?,?)";
        try (Connection conn = CONNECTION_MANAGER.getConnection()) {
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setInt(1, playListToAdd.getId());
            psmt.setInt(2, songToBeAdded.getSongId());
            int rowAffected = psmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            throw new MyTunesException(ExceptionsMessages.NO_DATABASE_CONNECTION, e);
        }
    }

    /**
     * Updates the null song id from the join table
     */
    @Override
    public boolean updatePlayListNullSong(int playListId, int songId) throws MyTunesException {
        String sql = "UPDATE PlaylistSongs SET SongId=? WHERE PlaylistId=?";
        try (Connection connection = CONNECTION_MANAGER.getConnection()) {
            PreparedStatement psmt = connection.prepareStatement(sql);
            psmt.setInt(1, songId);
            psmt.setInt(2, playListId);
            int rowAffected = psmt.executeUpdate();
            return rowAffected > 0;
        } catch (SQLException e) {
            throw new MyTunesException(ExceptionsMessages.DB_UNSUCCESFULL ,e.getCause());
        }
    }

    @Override
    public boolean deleteSongFromPLayList(int songId) throws MyTunesException {
        String sql = "DELETE FROM PlaylistSongs WHERE SongId=?";
        try (Connection conn=CONNECTION_MANAGER.getConnection()){
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setInt(1,songId);
            int affectedRows= psmt.executeUpdate();
            return affectedRows>0;
        } catch (SQLException |MyTunesException e) {
            throw new MyTunesException(ExceptionsMessages.DELETE_SONG_FAILED,e.getCause());
        }
    }

    @Override
    public boolean saveChange(PlayList currentPlayList) throws MyTunesException {
        String deleteSql = "DELETE FROM PlaylistSongs WHERE PlaylistId = ?";
        String insertSql = "INSERT INTO PlaylistSongs (PlaylistId, SongId) VALUES (?, ?)";

        try (Connection conn = CONNECTION_MANAGER.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement deletePsmt = conn.prepareStatement(deleteSql);
                 PreparedStatement insertPsmt = conn.prepareStatement(insertSql)) {

                // Delete all songs associated with the specified playlist ID
                deletePsmt.setInt(1, currentPlayList.getId());
                deletePsmt.executeUpdate();

                // Insert the updated list of songs for the playlist
                for (Song song : currentPlayList.getPlayListSongs()) {
                    insertPsmt.setInt(1, currentPlayList.getId());
                    insertPsmt.setInt(2, song.getSongId());
                    insertPsmt.executeUpdate();
                }

                conn.commit(); // Commit the transaction if everything is successful
                return true; // Return true to indicate success
            } catch (SQLException e) {
                conn.rollback(); // Rollback the transaction if an error occurs
                throw new MyTunesException(ExceptionsMessages.TRANSACTION_FAILED, e.getCause());
            }
        } catch (SQLException e) {
            throw new MyTunesException(ExceptionsMessages.NO_DATABASE_CONNECTION, e.getCause());
        }
    }
}



