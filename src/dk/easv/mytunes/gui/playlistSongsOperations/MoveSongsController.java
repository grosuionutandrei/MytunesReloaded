package dk.easv.mytunes.gui.playlistSongsOperations;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.listeners.PlaylistReloadable;
import dk.easv.mytunes.gui.newEditDeletePlaylist.PlayListModel;
import dk.easv.mytunes.utility.GraphicIdValues;
import dk.easv.mytunes.utility.Utility;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.List;

public class MoveSongsController {
    private PlayListModel playListModel;
    private PlaylistReloadable playlistReloadable;
    private final int MOVEUP = -1;
    private final int MOVEDOWN = 1;

    public MoveSongsController(PlaylistReloadable playlistReloadable) {
        try {
            this.playListModel = PlayListModel.getInstance();
        } catch (MyTunesException e) {
            Utility.displayInformation(Alert.AlertType.ERROR, e.getMessage());
        }
        this.playlistReloadable = playlistReloadable;
    }


    /**
     * It moves songs from the playList up and down, tries to save the changes to the database if it fails will
     * display an error message to inform the user, and allows the user to still perform the movement operation on the playlistSongs
     * updates the current index off the playing song, to play the next song in the list according to the new location
     *
     * @param currentPlayList the current playlist playing
     * @param selectedIndex   the selected index from the ListView<Song>
     * @param operation       the operation that will be executed move up or move down
     * @param observableList  the current list off songs that is displayed
     */
    public int moveSong(PlayList currentPlayList, int selectedIndex, String operation, ObservableList<Song> observableList) {
        System.out.println(selectedIndex);
        if (selectedIndex < 0) {
            playlistReloadable.resetButtons();
            return selectedIndex;
        }
        List<Song> temporaryList = currentPlayList.getPlayListSongs();
        Song songToMove = temporaryList.get(selectedIndex);
        int newIndex = selectedIndex;
        try {
            if (isMoveUpOperation(operation)) {
                newIndex = moveSongByOffset(temporaryList, selectedIndex, MOVEUP, songToMove);
            } else {
                newIndex = moveSongByOffset(temporaryList, selectedIndex, MOVEDOWN, songToMove);
            }
            currentPlayList.setPlayListSongs(temporaryList);
            playListModel.saveChange(currentPlayList);
            playlistReloadable.reloadSongs();
            playlistReloadable.changePlayingIndex(newIndex);
            playlistReloadable.resetButtons();
        } catch (MyTunesException e) {
            Utility.displayInformation(Alert.AlertType.ERROR,e.getMessage() + " changes will not be saved");
            playlistReloadable.resetButtons();
            resetObservableList(observableList, temporaryList);
        }
        return newIndex;
    }

    private int moveSongByOffset(List<Song> songs, int selectedIndex, int offset, Song songToMove) {
        int newIndex = selectedIndex + offset;
        if (isIndexValid(selectedIndex, newIndex, songs.size())) {
            songs.remove(selectedIndex);
            songs.add(newIndex, songToMove);
        }
        return newIndex;
    }

    private boolean isIndexValid(int currentIndex, int newIndex, int size) {
        return newIndex >= 0 && newIndex < size && currentIndex >= 0 && currentIndex < size;
    }

    public PlayListModel getPlayListModel() {
        return playListModel;
    }

    public void setPlayListModel(PlayListModel playListModel) {
        this.playListModel = playListModel;
    }

    public PlaylistReloadable getPlaylistReloadable() {
        return playlistReloadable;
    }

    public void setPlaylistReloadable(PlaylistReloadable playlistReloadable) {
        this.playlistReloadable = playlistReloadable;
    }

    private boolean isMoveUpOperation(String operation) {
        return GraphicIdValues.UP.getValue().equals(operation);
    }

    private void resetObservableList(ObservableList<Song> observableList, List<Song> temporaryList) {
        observableList.setAll(temporaryList);
    }

}