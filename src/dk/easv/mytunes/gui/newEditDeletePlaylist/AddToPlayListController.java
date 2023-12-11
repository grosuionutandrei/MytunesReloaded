package dk.easv.mytunes.gui.newEditDeletePlaylist;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.confirmationWindow.ConfirmationWindow;
import dk.easv.mytunes.gui.listeners.ConfirmationController;
import dk.easv.mytunes.gui.listeners.PlaylistReloadable;
import dk.easv.mytunes.utility.InformationalMessages;
import dk.easv.mytunes.utility.Utility;
import javafx.scene.Parent;
import javafx.scene.control.Alert;


public class AddToPlayListController implements ConfirmationController {
    private PlayListModel playListModel;
    private ConfirmationWindow confirmationWindow;
    private PlaylistReloadable playlistReloadable;
    private PlayList playListToAdd;
    private Song songToBeAdded;


    @Override
    public void confirmationEventHandler(boolean confirmation) {
        boolean executed = false;
        if (confirmation) {
            try {
                executed = this.playListModel.addSongToPlayList(this.playListToAdd, this.songToBeAdded);
            } catch (MyTunesException e) {
                Utility.displayInformation(Alert.AlertType.ERROR, e.getMessage());
                return;
            }
            if (executed) {
                playlistReloadable.reloadPlaylistsFromDb();
                playlistReloadable.reloadSongs();
            }
        }
    }

    public AddToPlayListController(PlayListModel playListModel, PlayList playListToAdd, Song songToBeAdded, PlaylistReloadable playlistReloadable) {
        this.playListModel = playListModel;
        this.playListToAdd = playListToAdd;
        this.songToBeAdded = songToBeAdded;
        this.playlistReloadable = playlistReloadable;
        this.confirmationWindow = new ConfirmationWindow();
        if (confirmationWindow.getConfirmationWindow() != null) {
            initializeConfirmationWindow(this.confirmationWindow, this);
        } else {
            Utility.displayInformation(Alert.AlertType.ERROR, InformationalMessages.FXML_MISSING.getValue());
        }
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
}
