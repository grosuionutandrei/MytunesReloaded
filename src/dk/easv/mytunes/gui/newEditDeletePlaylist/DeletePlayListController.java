package dk.easv.mytunes.gui.newEditDeletePlaylist;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.confirmationWindow.ConfirmationWindow;
import dk.easv.mytunes.gui.listeners.ConfirmationController;
import dk.easv.mytunes.gui.listeners.PlaylistReloadable;
import dk.easv.mytunes.gui.listeners.Reloadable;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DeletePlayListController implements ConfirmationController, Initializable {
    private PlayListModel playlistModel;
    private VBox confirmationWindow;
    private PlaylistReloadable playlistReloadable;

    @Override
    public void confirmationEventHandler(boolean confirmation) {
        boolean deleted = false;
        if (confirmation) {
            try {
                deleted = playlistModel.deletePlayList();
            } catch (MyTunesException e) {
                displayInfoMessage(e.getMessage(), Alert.AlertType.ERROR);
            }
            if (deleted) {
                String message = "Deleted with success";
                Platform.runLater(() -> {
                    displayInfoMessage(message, Alert.AlertType.INFORMATION);
                });
                playlistReloadable.reloadPlaylistsFromDb();

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

    public void setPlaylistToDelete(PlayList playListToDelete) {
        this.playlistModel.setCurrentSelectedPlayList(playListToDelete);
    }

    private void initializeConfirmationWindow(ConfirmationWindow confirmationWindow, ConfirmationController confirmationController) {
        confirmationWindow.setConfirmationController(confirmationController);
        confirmationWindow.setOperationTitle("Delete operation");
        confirmationWindow.setOperationInformation("Are you sure that you want to delete this file?");
    }

    public void setReloadable(PlaylistReloadable playlistReloadable) {
        this.playlistReloadable = playlistReloadable;
    }

    public VBox getConfirmationWindow() {
        return confirmationWindow;
    }

}
