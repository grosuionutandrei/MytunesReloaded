package dk.easv.mytunes.gui.newEditDeletePlaylist;

import dk.easv.mytunes.exceptions.MyTunesException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

public class NewPlaylistController extends NewEditController {

/** Create a new playlist*/
    public void savePlaylist(ActionEvent event) {
        String title = getPlaylistTitle().getText();
        boolean isTitleEmpty = getPlayListModel().checkTitle(title);
        if (isTitleEmpty) {
            String infoMessage = "Title can not be empty!";
            getInformation().setText(infoMessage);
            getInformation().setVisible(true);
            return;
        }

        try {
            getPlayListModel().createNewPlayList(title);
        } catch (MyTunesException e) {
            displayAlert(Alert.AlertType.ERROR, e.getMessage());
            return;
        }
        getReloadable().reloadPlaylistsFromDb();
        getCurrentStage(event).close();
    }

    /** Cancel new playlist creation */
    @Override
    public void cancelPlaylist(ActionEvent event) {
        getCurrentStage(event).close();
    }
}
