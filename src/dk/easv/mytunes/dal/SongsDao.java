package dk.easv.mytunes.dal;

import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.ExceptionsMessages;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SongsDao implements ISongsDao {
    private final ConnectionManager CONNECTION_MANAGER = new ConnectionManager();
    private List<Song> objectSongs = null;
    private static SongsDao instance;


    private SongsDao() throws MyTunesException {
        loadAllSongsFromDB();
    }


    public static SongsDao getSongsDao() throws MyTunesException {
        if (instance == null) {
            instance = new SongsDao();
        }
        return instance;
    }

    @Override
    public boolean createSong(Song s) throws MyTunesException {
        boolean executed = false;
        try (Connection conn = CONNECTION_MANAGER.getConnection()) {
            String sql = "INSERT INTO SONGS values (?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, s.getSongPath());
            pstmt.setString(2, s.getTitle());
            pstmt.setString(3, s.getArtist());
            pstmt.setString(4, s.getGenre());
            pstmt.setDouble(5, s.getLength());
            pstmt.execute();
            executed = true;
        } catch (SQLServerException e) {
            throw new MyTunesException(ExceptionsMessages.SONG_CREATION_FAILED, e.getCause());
        } catch (SQLException es) {
            throw new MyTunesException(ExceptionsMessages.DELETE_SONG_FAILED, es.getCause());
        }
        return executed;
    }


    public boolean deleteSong(int songId, String path) throws MyTunesException {
        String sqlDeleteSong = "DELETE FROM Songs WHERE SongId=?";
        String sqlDeleteFromJoin = "DELETE FROM PlaylistSongs WHERE SongId=?";
        try (Connection connection = CONNECTION_MANAGER.getConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement psmtDelete = connection.prepareStatement(sqlDeleteSong);
                 PreparedStatement psmtFromJoin = connection.prepareStatement(sqlDeleteFromJoin)) {
                psmtDelete.setInt(1, songId);
                psmtFromJoin.setInt(1, songId);

                psmtFromJoin.execute();
                psmtDelete.execute();
                FileHandler fileHandler = FileHandler.getInstance();
                if (!fileHandler.deleteSongLocal(path)) {
                    connection.rollback();
                    return false;
                }
                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                System.out.println(e.getMessage());
                throw new MyTunesException(ExceptionsMessages.TRANSACTION_FAILED, e);
            }
        } catch (SQLException e) {
            throw new MyTunesException(ExceptionsMessages.NO_DATABASE_CONNECTION, e);
        }
    }

    @Override
    public boolean updateSong(Song song) throws MyTunesException {
        System.out.println(song);
        boolean executed = false;
        String sql = "UPDATE Songs SET SongPath=?, Title=?, Artist=?,Genre=?,Length=? WHERE SongId=?";
        try (Connection conn = CONNECTION_MANAGER.getConnection()) {
            PreparedStatement psmt = conn.prepareStatement(sql);
            psmt.setString(1, song.getSongPath());
            psmt.setString(2, song.getTitle());
            psmt.setString(3, song.getArtist());
            psmt.setString(4, song.getGenre());
            psmt.setDouble(5, song.getLength());
            psmt.setInt(6, song.getSongId());
            psmt.execute();
            executed = true;
        } catch (SQLException | MyTunesException e) {
            throw new MyTunesException(ExceptionsMessages.UPDATE_SONG_FAILED, e.getCause());
        }
        return executed;
    }

    @Override
    public List<Song> getAllSongsFromCache() throws MyTunesException {
        return this.objectSongs;
    }

    private void loadAllSongsFromDB() throws MyTunesException {

        List<Song> songs = new ArrayList<>();
        try (Connection conn = CONNECTION_MANAGER.getConnection()) {
            Song song = null;
            String sql = "SELECT * FROM SONGS";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int songId = rs.getInt(1);
                String path = rs.getString(2);
                String title = rs.getString(3);
                String artist = rs.getString(4);
                String genre = rs.getString(5);
                long length = rs.getLong(6);
                song = new Song(songId, path, title, artist, genre, length);
                songs.add(song);
            }

        } catch (SQLException e) {
            throw new MyTunesException(ExceptionsMessages.READING_FROMDB_FAILED, e.getCause());
        }
        this.objectSongs = songs;
    }

    @Override
    public List<Song> getPlayListSongs(int playListId) {
        return null;
    }

    @Override
    public void reloadSongsFromDB() throws MyTunesException {
        loadAllSongsFromDB();
    }
}
