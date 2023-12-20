package dk.easv.mytunes.gui.newEditDeletePlaylist;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.bll.MyTunesPlaylistCreation;
import dk.easv.mytunes.exceptions.MyTunesException;

import java.util.List;

public class PlayListModel {

    private MyTunesPlaylistCreation playlistCreation;
    private static PlayListModel instance;

    /**
     * Stores the current selected playlist used for edit or delete operations
     */
    private PlayList currentSelectedPlayList;
    /**
     * stores the current selected song to be added to the playlist
     */
    private Song currentSelectedSong;


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

    public PlayList getCurrentSelectedPlayList() {
        return currentSelectedPlayList;
    }

    public void setCurrentSelectedPlayList(PlayList currentSelectedPlayList) {
        this.currentSelectedPlayList = currentSelectedPlayList;
    }

    public boolean updatePlayList(String newTitle) throws MyTunesException {
        boolean executed = this.playlistCreation.updatePlayList(this.currentSelectedPlayList, newTitle);
        this.currentSelectedPlayList = null;
        return executed;
    }

    public void cancelUpdatePlayList() {
        this.currentSelectedPlayList = null;
    }

    public boolean deletePlayList(PlayList playListToDelete) throws MyTunesException {
        return this.playlistCreation.deletePlayList(playListToDelete.getId());
    }

    public boolean addSongToPlayList(PlayList playListToAdd, Song songToBeAdded) throws MyTunesException {
        return this.playlistCreation.addSongToPlaylist(playListToAdd, songToBeAdded);
    }

    public boolean deleteSongFromPlayList(Song songToDelete, List<Song> playListSongs) throws MyTunesException {
        return this.playlistCreation.deleteSongFromPLayList(songToDelete, playListSongs);
    }

    public boolean saveChange(PlayList currentPlaylist) throws MyTunesException {
        return this.playlistCreation.saveChange(currentPlaylist);

    }
}