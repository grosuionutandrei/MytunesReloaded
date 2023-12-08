package dk.easv.mytunes.dal;


import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;

import java.util.List;

public interface IPlaylistDao {
    public boolean createPlayList(PlayList playlist) throws MyTunesException;
    public boolean deletePlayList(int playListId) throws MyTunesException;
    public boolean updatePlayList(int playListId,String newTitle) throws MyTunesException;
    public PlayList getPlayList(int id) throws MyTunesException;
    public List<PlayList> getAllPlaylistsFromCache() throws  MyTunesException;
    public boolean addSongToPlaylist(PlayList playlistToAdd, Song songToBEAdded) throws MyTunesException;

    List<PlayList> reloadPlaylistsFromDb() throws MyTunesException;
    public boolean updatePlayListNullSong(int playListId,int songId) throws MyTunesException;
}
