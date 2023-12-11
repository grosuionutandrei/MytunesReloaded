package dk.easv.mytunes.gui.newEditDeletePlaylist;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.ExceptionHandler;
import javafx.event.ActionEvent;


public class NewPlaylistController extends NewEditController {

    /**
     * Create a new playlist
     */
    public void savePlaylist(ActionEvent event) {
        String title = getPlaylistTitle().getText();
        if (isTitleEmpty(title)) {
            showTitleError();
            return;
        }
        if (createPlayList(title)) {
            getReloadable().reloadPlaylistsFromDb();
        }
        getCurrentStage(event).close();
    }

    private boolean isTitleEmpty(String title) {
        return getPlayListModel().checkTitle(title);
    }

    private boolean createPlayList(String title) {
        try {
            getPlayListModel().createNewPlayList(title);
            return true;
        } catch (MyTunesException e) {
            ExceptionHandler.displayErrorAlert(e.getMessage());
            return false;
        }
    }

    /**
     * Cancel new playlist creation
     */
    @Override
    public void cancelPlaylist(ActionEvent event) {
        getCurrentStage(event).close();
    }
}
