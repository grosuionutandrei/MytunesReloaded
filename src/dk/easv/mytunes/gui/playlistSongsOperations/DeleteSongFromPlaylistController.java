package dk.easv.mytunes.gui.playlistSongsOperations;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.confirmationWindow.ConfirmationWindow;
import dk.easv.mytunes.gui.listeners.ConfirmationController;
import dk.easv.mytunes.gui.listeners.PlaylistReloadable;
import dk.easv.mytunes.gui.newEditDeletePlaylist.PlayListModel;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteSongFromPlaylistController implements ConfirmationController, Initializable {
    private VBox confirmationWindow;
    private Song songToDelete;
    private PlayListModel playlistModel;
    private PlaylistReloadable playlistReloadable;

    /**
     * Handles the confirmation result of deleting a song from a playlist.
     *
     * @param confirmation True if the user confirmed the deletion, false otherwise.
     */
    @Override
    public void confirmationEventHandler(boolean confirmation) {
        if (confirmation) {
            try {
                boolean deleted = playlistModel.deleteSongFromPlayList(this.songToDelete);
                if (deleted) {
                    String message = "Deleted with success";
                    displayInfoMessage(message, Alert.AlertType.INFORMATION);
                    playlistReloadable.reloadSongs();
                }
            } catch (MyTunesException e) {
                displayInfoMessage(e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            playlistModel = PlayListModel.getInstance();
        } catch (MyTunesException e) {
            displayInfoMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
        if (playlistModel != null) {
            ConfirmationWindow confirmationView = new ConfirmationWindow();
            confirmationWindow = confirmationView.getConfirmationWindow();
            initializeConfirmationWindow(confirmationView, this);
        }
    }

    private void displayInfoMessage(String message, Alert.AlertType type) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setAlertType(type);
        alert.setContentText(message);
        alert.show();
    }

    public void getSongToDelete(Song song) {
        this.songToDelete = song;
    }

    private void initializeConfirmationWindow(ConfirmationWindow confirmationWindow, ConfirmationController confirmationController) {
        confirmationWindow.setConfirmationController(confirmationController);
        confirmationWindow.setOperationTitle("Delete operation");
        String message = "Are you sure that you want to delete this song from the playlist " + "\n";
        String songName = "\"" + this.songToDelete.getTitle() + "\" ?";
        confirmationWindow.setOperationInformation(message + songName);
    }

    public void setReloadable(PlaylistReloadable playlistReloadable) {
        this.playlistReloadable = playlistReloadable;
    }

    public VBox getConfirmationWindow() {
        return confirmationWindow;
    }
}
