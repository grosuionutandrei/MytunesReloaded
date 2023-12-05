package dk.easv.mytunes.gui.newPlaylistView;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.listeners.PlaylistReloadable;
import dk.easv.mytunes.gui.listeners.Reloadable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PlaylistController implements Initializable {

    private PlaylistReloadable reloadable;


    private PlayListModel playListModel;
    @FXML
    private TextField playlistTitle;
    @FXML
    private Label information;


    public void savePlaylist(ActionEvent event) {
        String title = playlistTitle.getText();
        boolean isTitleEmpty = playListModel.checkTitle(title);
        if (isTitleEmpty) {
            String infoMessage = "Title can not be empty!";
            information.setText(infoMessage);
            information.setVisible(true);
            return;
        }

        try {
            playListModel.createNewPlayList(title);
        } catch (MyTunesException e) {
            displayAlert(Alert.AlertType.ERROR, e.getMessage());
            return;
        }
        reloadable.reloadPlaylistsFromDb();
        getCurrentStage(event).close();

    }

    @FXML
    private void cancelPlaylist(ActionEvent event) {
    }

    /**
     * Initialize the model , if an error has occurred will not show the window
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            playListModel = PlayListModel.getInstance();
        } catch (MyTunesException e) {
            displayAlert(Alert.AlertType.ERROR, (e.getMessage()));
            return;
        }
        setOnChangeListener(this.playlistTitle, this.information);


    }

    private void displayAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public void setReloadable(PlaylistReloadable playlistReloadable) {
        this.reloadable = playlistReloadable;
    }

    private void setOnChangeListener(TextField textField, Label label) {
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (label.isVisible()) {
                label.setVisible(false);
            }
        });
    }
    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

}
