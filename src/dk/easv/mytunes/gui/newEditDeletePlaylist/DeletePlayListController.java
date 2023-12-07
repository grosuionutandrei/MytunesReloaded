package dk.easv.mytunes.gui.newEditDeletePlaylist;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.confirmationWindow.ConfirmationWindow;
import dk.easv.mytunes.gui.listeners.ConfirmationController;
import dk.easv.mytunes.gui.listeners.PlaylistReloadable;
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
    private PlayList playlistToDelete;

    @Override
    public void confirmationEventHandler(boolean confirmation) {
        boolean deleted = false;
        if (confirmation) {
            try {
                deleted = playlistModel.deletePlayList(this.playlistToDelete);
            } catch (MyTunesException e) {
                displayInfoMessage(e.getMessage(), Alert.AlertType.ERROR);
                return;
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

    public void getPlayListToDelete(PlayList playList) {
        this.playlistToDelete = playList;
    }

    private void initializeConfirmationWindow(ConfirmationWindow confirmationWindow, ConfirmationController confirmationController) {
        confirmationWindow.setConfirmationController(confirmationController);
        confirmationWindow.setOperationTitle("Delete operation");
        String message = "Are you sure that you want to delete this playlist " + "\n";
        String playList = "\"" + this.playlistToDelete.getName() + "\" ?";
        confirmationWindow.setOperationInformation(message + playList);
    }

    public void setReloadable(PlaylistReloadable playlistReloadable) {
        this.playlistReloadable = playlistReloadable;
    }

    public VBox getConfirmationWindow() {
        return confirmationWindow;
    }

}
