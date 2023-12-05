package dk.easv.mytunes.gui.newPlaylistView;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.bll.MyTunesPlaylistCreation;
import dk.easv.mytunes.exceptions.MyTunesException;

public class PlayListModel {

    private MyTunesPlaylistCreation playlistCreation;
    private static PlayListModel instance;

    private PlayListModel() throws MyTunesException {
        playlistCreation = MyTunesPlaylistCreation.getInstance();
    }

    public static PlayListModel getInstance() throws MyTunesException {
        if (instance == null) {
            instance = new PlayListModel();
        }
        return instance;
    }

    public boolean createNewPlayList(String playListTitle) throws MyTunesException {
        PlayList playListCreated = new PlayList(playListTitle);
        return playlistCreation.createPlayList(playListCreated);
    }
    public boolean checkTitle(String title){
        return MyTunesPlaylistCreation.checkTitle(title);
    }

}
