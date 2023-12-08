package dk.easv.mytunes.gui.newEditDeletePlaylist;
import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.confirmationWindow.ConfirmationWindow;
import dk.easv.mytunes.gui.listeners.ConfirmationController;
import dk.easv.mytunes.gui.listeners.PlaylistReloadable;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import java.net.URL;
import java.util.ResourceBundle;

public class AddToPlayListController implements ConfirmationController, Initializable {
    private PlayListModel playListModel;
    private ConfirmationWindow confirmationWindow;
    private PlaylistReloadable playlistReloadable;
    private PlayList playListToAdd;
    private Song songToBeAdded;

    @Override
    public void confirmationEventHandler(boolean confirmation) {
       boolean executed =false;
        if(confirmation){
            try{
               executed= this.playListModel.addSongToPlayList(this.playListToAdd,this.songToBeAdded);
            }catch (MyTunesException e){
                displayInfoMessage(e.getMessage(), Alert.AlertType.ERROR);
                return;
            }
            if(executed){
                playlistReloadable.reloadPlaylistsFromDb();
                playlistReloadable.reloadSongs();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.playListModel = PlayListModel.getInstance();
        } catch (MyTunesException e) {
            displayInfoMessage(e.getMessage(), Alert.AlertType.ERROR);
        }
        if (playListModel != null) {
            this.confirmationWindow = new ConfirmationWindow();
            initializeConfirmationWindow(this.confirmationWindow, this);
        }
    }

    public void setPlayListToAdd(PlayList playListToAdd) {
        this.playListToAdd = playListToAdd;
    }

    public void setSongToAdd(Song song) {
        this.songToBeAdded = song;
    }

    private void displayInfoMessage(String message, Alert.AlertType type) {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setAlertType(type);
        alert.setContentText(message);
        alert.show();
    }

    private void initializeConfirmationWindow(ConfirmationWindow confirmationWindow, ConfirmationController confirmationController) {
        confirmationWindow.setConfirmationController(confirmationController);
        setConfirmationMessage(confirmationWindow);
    }

    private void setConfirmationMessage(ConfirmationWindow confirmationWindow) {
        confirmationWindow.setOperationTitle("Add song to  playlist operation");
        String message1 = "Are you sure that you want to add ";
        String songName = "\"" + this.songToBeAdded.getTitle() + "\"";
        String message2 = "to the ";
        String playlistName = "\"" + this.playListToAdd.getName() + "\"";
        String end = " playlist?";
        String fullMessage = message1 + songName + "\n" + message2 + playlistName + end;
        confirmationWindow.setOperationInformation(fullMessage);
    }

    public Parent getConfirmationWindow() {
        return this.confirmationWindow.getConfirmationWindow();
    }
    public void setPlaylistReloadable(PlaylistReloadable playlistReloadable) {
        this.playlistReloadable = playlistReloadable;
    }
}
