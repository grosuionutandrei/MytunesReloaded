package dk.easv.mytunes.dal;


import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.exceptions.MyTunesException;

import java.util.List;

public interface IPlaylistDao {
    public boolean createPlayList(PlayList playlist) throws MyTunesException;
    public boolean deletePlayList(int playListId) throws MyTunesException;
    public boolean updatePlayList(PlayList playlist);
    public PlayList getPlayList(int id) throws MyTunesException;
    public List<PlayList> getAllPlaylistsFromCache() throws  MyTunesException;

}
