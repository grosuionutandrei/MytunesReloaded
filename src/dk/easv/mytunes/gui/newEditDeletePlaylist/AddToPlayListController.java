package dk.easv.mytunes.gui.newEditDeletePlaylist;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.components.confirmationWindow.ConfirmationWindow;
import dk.easv.mytunes.gui.listeners.ConfirmationController;
import dk.easv.mytunes.gui.listeners.PlaylistReloadable;
import dk.easv.mytunes.utility.ExceptionHandler;
import dk.easv.mytunes.utility.InformationalMessages;
import dk.easv.mytunes.utility.Titles;
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
                ExceptionHandler.displayErrorAlert(e.getExceptionsMessages());
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
            ExceptionHandler.displayErrorAlert(InformationalMessages.FXML_MISSING);
        }
    }


    private void initializeConfirmationWindow(ConfirmationWindow confirmationWindow, ConfirmationController confirmationController) {
        confirmationWindow.setConfirmationController(confirmationController);
        setConfirmationMessage(confirmationWindow);
    }

    private void setConfirmationMessage(ConfirmationWindow confirmationWindow) {
        confirmationWindow.setOperationTitle(Titles.ADD_SONG_PLAYLIST.getValue());

        String songName = "\"" + this.songToBeAdded.getTitle() + "\"";
        String message2 = "to the ";
        String playlistName = "\"" + this.playListToAdd.getName() + "\"";
        String end = " playlist?";
        String fullMessage = InformationalMessages.ADD_QUESTION.getValue() + songName + "\n" + message2 + playlistName + end;
        confirmationWindow.setOperationInformation(fullMessage);
    }

    public Parent getConfirmationWindow() {
        return this.confirmationWindow.getConfirmationWindow();
    }
}
