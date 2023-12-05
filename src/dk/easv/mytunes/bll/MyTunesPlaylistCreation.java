package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.dal.PlaylistDao;
import dk.easv.mytunes.exceptions.MyTunesException;

public class MyTunesPlaylistCreation {
    private PlaylistDao playlistDao;
    private static MyTunesPlaylistCreation instance;

    private MyTunesPlaylistCreation() throws MyTunesException {
        playlistDao = PlaylistDao.getPlaylistDao();
    }

    public static MyTunesPlaylistCreation getInstance() throws MyTunesException {
        if (instance == null) {
            instance = new MyTunesPlaylistCreation();
        }
        return instance;
    }

    public boolean createPlayList(PlayList playList) throws MyTunesException {
        return this.playlistDao.createPlayList(playList);
    }

}
