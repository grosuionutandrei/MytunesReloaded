package dk.easv.mytunes.gui.newEditDeletePlaylist;
import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.confirmationWindow.ConfirmationWindow;
import dk.easv.mytunes.gui.listeners.ConfirmationController;
import dk.easv.mytunes.gui.listeners.PlaylistReloadable;
import dk.easv.mytunes.utility.ExceptionHandler;
import dk.easv.mytunes.utility.InformationalMessages;
import dk.easv.mytunes.utility.Titles;
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
        boolean deleted;
        if (confirmation) {
            try {
                deleted = playlistModel.deletePlayList(this.playlistToDelete);
                if (deleted) {
                    Platform.runLater(() -> {
                        ExceptionHandler.displayInformationAlert(InformationalMessages.DELETE_SUCCEEDED);
                    });
                    playlistReloadable.reloadPlaylistsFromDb();
                }
            } catch (MyTunesException e) {
                ExceptionHandler.displayErrorAlert(e.getMessage());
            }
         }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            playlistModel = PlayListModel.getInstance();
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
        }
        if (playlistModel != null) {
            ConfirmationWindow confirmationView = new ConfirmationWindow();
            if(confirmationView.getConfirmationWindow()==null){
                return;
            }
            confirmationWindow = confirmationView.getConfirmationWindow();
            initializeConfirmationWindow(confirmationView, this);
        }
    }

    public void getPlayListToDelete(PlayList playList) {
        this.playlistToDelete = playList;
    }

    private void initializeConfirmationWindow(ConfirmationWindow confirmationWindow, ConfirmationController confirmationController) {
        confirmationWindow.setConfirmationController(confirmationController);
        confirmationWindow.setOperationTitle(Titles.DELETE_PLAYLIST.getValue());
        String playList = "\"" + this.playlistToDelete.getName() + "\" ?";
        confirmationWindow.setOperationInformation(InformationalMessages.DELETE_PLAYLIST_QUESTION.getValue() + playList);
    }

    public void setReloadable(PlaylistReloadable playlistReloadable) {
        this.playlistReloadable = playlistReloadable;
    }

    public VBox getConfirmationWindow() {
        return confirmationWindow;
    }

}
