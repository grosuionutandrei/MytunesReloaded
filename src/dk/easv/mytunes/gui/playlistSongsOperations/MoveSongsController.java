package dk.easv.mytunes.gui.playlistSongsOperations;
import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.listeners.MoveFocusAndSelect;
import dk.easv.mytunes.gui.listeners.PlaylistReloadable;
import dk.easv.mytunes.gui.newEditDeletePlaylist.PlayListModel;
import dk.easv.mytunes.utility.ExceptionHandler;
import dk.easv.mytunes.utility.GraphicIdValues;
import dk.easv.mytunes.utility.InformationalMessages;
import dk.easv.mytunes.utility.Utility;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.util.List;

public class MoveSongsController implements MoveFocusAndSelect {
    private PlayListModel playListModel;
    private PlaylistReloadable playlistReloadable;
    private final int MOVE_DIRECTION_UP = -1;
    private final int MOVE_DIRECTION_DOWN = 1;
    private PlayList currentPlayList;
    private int selectedIndex;
    private String operation;
    private ObservableList<Song> observableList;


    public MoveSongsController(PlaylistReloadable playlistReloadable, PlayList currentPlayList, int selectedIndex, String operation, ObservableList<Song> observableList) {
        try {
            this.playListModel = PlayListModel.getInstance();
        } catch (MyTunesException e) {
            Utility.displayInformation(Alert.AlertType.ERROR, e.getMessage());
        }
        this.playlistReloadable = playlistReloadable;
        this.currentPlayList = currentPlayList;
        this.selectedIndex = selectedIndex;
        this.operation = operation;
        this.observableList = observableList;
    }

    /**
     * It moves songs from the playList up and down, tries to save the changes to the database if it fails will
     * display an error message to inform the user, and allows the user to still perform the movement operation on the playlistSongs
     * updates the current index off the playing song, to play the next song in the list according to the new location
     */

    @Override
    public int moveFocusAndSelect() {
        if (selectedIndex < 0) {
            playlistReloadable.resetButtons();
            return selectedIndex;
        }
        List<Song> temporaryList = currentPlayList.getPlayListSongs();
        Song songToMove = temporaryList.get(selectedIndex);
        int newIndex = selectedIndex;
        try {
            if (isMoveUpOperation(operation)) {
                newIndex = moveSongByOffset(temporaryList, selectedIndex, MOVE_DIRECTION_UP, songToMove);
            } else {
                newIndex = moveSongByOffset(temporaryList, selectedIndex, MOVE_DIRECTION_DOWN, songToMove);
            }
            currentPlayList.setPlayListSongs(temporaryList);
            playListModel.saveChange(currentPlayList);
            updateUiData(newIndex);
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage() +" "+ InformationalMessages.ORDERING_NOT_PERSISTED.getValue());
            rollBackUiAndData(temporaryList);
        }
        return newIndex;
    }

    /**
     * updates the ui with the newest movement operation performed by the user
     * updates the playing index so the music will play with the updated list
     * enables the movement buttons
     */
    private void updateUiData(int newIndex) {
        playlistReloadable.reloadSongs();
        playlistReloadable.changePlayingIndex(newIndex);
        playlistReloadable.resetButtons();
    }


    /**
     * enables the move up and move down buttons,
     * an updates the listview with the user modifications without saving to the database
     */
    private void rollBackUiAndData(List<Song> temporaryList) {
        playlistReloadable.resetButtons();
        resetObservableList(observableList, temporaryList);
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