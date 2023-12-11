package dk.easv.mytunes.gui.newEditDeletePlaylist;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.ExceptionHandler;
import dk.easv.mytunes.utility.InformationalMessages;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class EditPlaylistController extends NewEditController {
    @Override
    public void cancelPlaylist(ActionEvent event) {
        getPlayListModel().cancelUpdatePlayList();
        getCurrentStage(event).close();
    }

    public void savePlaylist(ActionEvent event) {
        if (!isValidTitle()) {
            showTitleError();
            return;
        }

        if (updatePlaylist()) {
            getReloadable().reloadPlaylistsFromDb();
        }
        closeCurrentStage(event);
    }

    private boolean isValidTitle() {
        String title = getPlaylistTitle().getText();
        return !getPlayListModel().checkTitle(title);
    }

    private boolean updatePlaylist() {
        try {
            String title = getPlaylistTitle().getText();
            return getPlayListModel().updatePlayList(title);
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
            return false;
        }
    }

    private void closeCurrentStage(ActionEvent event) {
        getCurrentStage(event).close();
    }


    public void setPlaylistToEdit(PlayList playList) {
        getPlayListModel().setCurrentSelectedPlayList(playList);
        getPlaylistTitle().setText(playList.getName());
    }

}
