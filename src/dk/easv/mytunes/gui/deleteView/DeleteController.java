package dk.easv.mytunes.gui.deleteView;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.confirmationWindow.ConfirmationWindow;
import dk.easv.mytunes.gui.listeners.ConfirmationController;
import dk.easv.mytunes.gui.listeners.Reloadable;
import dk.easv.mytunes.utility.ExceptionHandler;
import dk.easv.mytunes.utility.InformationalMessages;
import dk.easv.mytunes.utility.Titles;
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
                ExceptionHandler.displayErrorAlert(e.getMessage());
            }
            if (deleted) {
                String message = songToDelete.getTitle() + " " + InformationalMessages.DELETE_SUCCEEDED.getValue();
                Platform.runLater(() -> {
                    ExceptionHandler.displayInformationAlert(message);
                });
                reloadable.reloadSongsFromDB();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            deleteModel = DeleteModel.getInstance();
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
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

    public void setSongToDelete(Song songToDelete) {
        this.songToDelete = songToDelete;
    }

    private void initializeConfirmationWindow(ConfirmationWindow confirmationWindow, ConfirmationController confirmationController) {
        confirmationWindow.setConfirmationController(confirmationController);
        confirmationWindow.setOperationTitle(Titles.DELETE_SONG.getValue());
        String message = InformationalMessages.DELETE_QUESTION.getValue() + "\n";
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
