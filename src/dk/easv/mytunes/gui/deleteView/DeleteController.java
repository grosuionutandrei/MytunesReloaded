package dk.easv.mytunes.gui.deleteView;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.confirmationWindow.ConfirmationWindow;
import dk.easv.mytunes.gui.listeners.ConfirmationController;
import dk.easv.mytunes.gui.listeners.Reloadable;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DeleteController implements ConfirmationController,Initializable {

    private DeleteModel deleteModel;
    private VBox confirmationWindow;
    private Song songToDelete;
    private Reloadable reloadable;

    @Override
    public void confirmationEventHandler(boolean confirmation) {
        boolean deleted = false;
        if (confirmation) {
            try {
                deleteModel.deleteSong(songToDelete.getSongId(), songToDelete.getSongPath());
                deleted = true;
            } catch (MyTunesException e) {
                displayInfoMessage(e.getMessage(), Alert.AlertType.ERROR);
            }
            if (deleted) {
                String message = songToDelete.getTitle() + " " + "Deleted with success";
                Platform.runLater(() -> {
                    displayInfoMessage(message, Alert.AlertType.INFORMATION);

                });
                reloadable.reloadSongsFromDB();
                reloadable.reloadSongsFromDB();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            deleteModel = DeleteModel.getInstance();
        } catch (MyTunesException e) {
            displayInfoMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
        if (deleteModel != null) {
           ConfirmationWindow confirmationView = new ConfirmationWindow();
           if(confirmationView.getConfirmationWindow()==null){
               return;
           }
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

    public void setSongToDelete(Song songToDelete) {
        this.songToDelete = songToDelete;
    }

    private void initializeConfirmationWindow(ConfirmationWindow confirmationWindow, ConfirmationController confirmationController) {
        confirmationWindow.setConfirmationController(confirmationController);
        confirmationWindow.setOperationTitle("Delete operation");
        String message = "Are you sure that you want to delete this song" + "\n";
        String songName = "\"" + songToDelete.getTitle() + "\"?";
        confirmationWindow.setOperationInformation(message + songName);
    }

    public void setReloadable(Reloadable reloadable) {
        this.reloadable = reloadable;
    }

    public VBox getConfirmationWindow() {
        return confirmationWindow;
    }

}
