package dk.easv.mytunes.gui.newEditDeletePlaylist;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.exceptions.MyTunesException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class EditPlaylistController extends NewEditController {
    @Override
    public void cancelPlaylist(ActionEvent event) {
        getPlayListModel().cancelUpdatePlayList();
        getCurrentStage(event).close();
    }

    @Override
    public void savePlaylist(ActionEvent event) {
        String title = getPlaylistTitle().getText();
        boolean isTitleEmpty = getPlayListModel().checkTitle(title);
        boolean updated = false;
        if (isTitleEmpty) {
            String infoMessage = "Title can not be empty!";
            getInformation().setText(infoMessage);
            getInformation().setVisible(true);
            return;
        }

        try {
            updated = getPlayListModel().updatePlayList(title);
        } catch (MyTunesException e) {
            displayAlert(Alert.AlertType.ERROR, e.getMessage());
            return;
        }
        if (updated) {
            getReloadable().reloadPlaylistsFromDb();
        }

        getCurrentStage(event).close();
    }

    public void setPlaylistToEdit(PlayList playList) {
        getPlayListModel().setCurrentSelectedPlayList(playList);
        getPlaylistTitle().setText(playList.getName());
    }

}
