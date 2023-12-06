package dk.easv.mytunes.gui.newEditDeletePlaylist;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.bll.MyTunesPlaylistCreation;
import dk.easv.mytunes.exceptions.MyTunesException;

public class PlayListModel {

    private MyTunesPlaylistCreation playlistCreation;
    private static PlayListModel instance;
    private PlayList playListToEdit;


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

    public boolean checkTitle(String title) {
        return MyTunesPlaylistCreation.checkTitle(title);
    }

    public PlayList getPlayListToEdit() {
        return playListToEdit;
    }

    public void setPlayListToEdit(PlayList playListToEdit) {
        this.playListToEdit = playListToEdit;
    }
    public boolean updatePlayList(String newTitle) throws MyTunesException {
        return this.playlistCreation.updatePlayList(this.playListToEdit,newTitle);
    }
}
