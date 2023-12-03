package dk.easv.mytunes.dal;



import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;

import java.util.List;

public interface ISongsDao {
    public boolean createSong(Song s) throws MyTunesException;
    public boolean deleteSong(int songId);
    public boolean updateSong(Song song) throws MyTunesException;
    public List<Song> getAllSongsFromCache() throws MyTunesException;
    public List<Song> getPlayListSongs(int playListId);
    public void reloadSongsFromDB() throws MyTunesException;
}
