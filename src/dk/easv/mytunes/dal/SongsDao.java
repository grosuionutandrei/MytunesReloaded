package dk.easv.mytunes.dal;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
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
        try (Connection conn = CONNECTION_MANAGER.getConnection()) {
            String sql = "INSERT INTO SONGS values (?,?,?,?,?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, s.getSongPath());
            pstmt.setString(2, s.getTitle());
            pstmt.setString(3, s.getArtist());
            pstmt.setString(4, s.getGenre());
            pstmt.setDouble(5, s.getLength());
            pstmt.execute();
        } catch (SQLServerException e) {
            throw new MyTunesException("Database error when tried to create the song", e.getCause());
        } catch (SQLException es) {
            throw new MyTunesException("Database error when tried to create the song", es.getCause());
        }
        return false;
    }

    @Override
    public boolean deleteSong(int songId) {
        return false;
    }

    @Override
    public boolean updateSong(Song song) {
        return false;
    }

    @Override
    public List<Song> getAllSongsFromCache() throws MyTunesException {
        return this.objectSongs;
    }


    public void loadAllSongsFromDB() throws MyTunesException {

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
            throw new MyTunesException("Reading songs from the data base gone wrong", e.getCause());
        }
        this.objectSongs = songs;
    }




    @Override
    public List<Song> getPlayListSongs(int playListId) {
        return null;
    }
}
