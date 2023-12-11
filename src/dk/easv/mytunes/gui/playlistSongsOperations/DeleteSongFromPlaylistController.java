package dk.easv.mytunes.gui.playlistSongsOperations;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.confirmationWindow.ConfirmationWindow;
import dk.easv.mytunes.gui.listeners.ConfirmationController;
import dk.easv.mytunes.gui.listeners.PlaylistReloadable;
import dk.easv.mytunes.gui.newEditDeletePlaylist.PlayListModel;
import dk.easv.mytunes.utility.InformationalMessages;
import dk.easv.mytunes.utility.Utility;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import java.util.List;

public class DeleteSongFromPlaylistController implements ConfirmationController {
    private ConfirmationWindow confirmationWindow;
    private Song songToDelete;
    private List<Song> listToDeleteFrom;
    private PlayListModel playlistModel;
    private PlaylistReloadable playlistReloadable;

    /**
     * Handles the confirmation result of deleting a song from a playlist.
     * if the list contains only one song the operation will not be allowed
     *
     * @param confirmation True if the user confirmed the deletion, false otherwise.
     */
    @Override
    public void confirmationEventHandler(boolean confirmation) {
        if (confirmation) {
            try {
                boolean deleted = playlistModel.deleteSongFromPlayList(this.songToDelete, listToDeleteFrom);
                if (deleted) {
                    Utility.displayInformation(Alert.AlertType.INFORMATION, InformationalMessages.DELETE_SUCCEEDED.getValue());
                    playlistReloadable.reloadSongs();
                }
            } catch (MyTunesException e) {
                Utility.displayInformation(Alert.AlertType.ERROR, e.getMessage());
            }
        }
    }

    public DeleteSongFromPlaylistController(Song songToDelete, List<Song> listToDeleteFrom, PlayListModel playListModel, PlaylistReloadable playlistReloadable) {
        this.songToDelete = songToDelete;
        this.listToDeleteFrom = listToDeleteFrom;
        this.playlistModel = playListModel;
        this.playlistReloadable = playlistReloadable;
        this.confirmationWindow = new ConfirmationWindow();
        if (confirmationWindow.getConfirmationWindow() != null) {
            initializeConfirmationWindow(confirmationWindow, this);
        }
    }

    private void initializeConfirmationWindow(ConfirmationWindow confirmationWindow, ConfirmationController confirmationController) {
        confirmationWindow.setConfirmationController(confirmationController);
        confirmationWindow.setOperationTitle("Delete operation");
        String message = "Are you sure that you want to delete this song from the playlist " + "\n";
        String songName = "\"" + this.songToDelete.getTitle() + "\" ?";
        confirmationWindow.setOperationInformation(message + songName);
    }

    public VBox getConfirmationWindow() {
        return confirmationWindow.getConfirmationWindow();
    }
}
