package dk.easv.mytunes.gui.newEditDeletePlaylist;

import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.listeners.PlaylistReloadable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class NewEditController implements Initializable {
    private PlaylistReloadable reloadable;
    private PlayListModel playListModel;


    @FXML
    private TextField playlistTitle;
    @FXML
    private Label information;
    @FXML
    private Button saveUpdateButton;

    /**
     * Initialize the model , if an error has occurred will not show the window
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            setPlayListModel(PlayListModel.getInstance());
        } catch (MyTunesException e) {
            displayAlert(Alert.AlertType.ERROR, (e.getMessage()));
            return;
        }
        setOnChangeListener(getPlaylistTitle(), getInformation());
    }

    /**
     * Abstract method to cancel playlist creation/editing.
     *
     * @param event The ActionEvent that triggered the cancellation.
     */
    public abstract void cancelPlaylist(ActionEvent event);

    /**
     * Abstract method to save/update a playlist.
     *
     * @param event The ActionEvent that triggered the save/update.
     */
    public abstract void savePlaylist(ActionEvent event);

    /**
     * Display an alert with the specified type and message.
     *
     * @param alertType The type of alert (e.g., INFORMATION, WARNING).
     * @param message   The message to display in the alert.
     */

    public void displayAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public PlaylistReloadable getReloadable() {
        return reloadable;
    }

    public void setReloadable(PlaylistReloadable playlistReloadable) {
        this.reloadable = playlistReloadable;
    }

    /**
     * Hide the information message when the user writes into the text field.
     *
     * @param textField The TextField to monitor for changes.
     * @param label     The Label to hide when the text field content changes.
     */
    public void setOnChangeListener(TextField textField, Label label) {
        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (label.isVisible()) {
                label.setVisible(false);
            }
        });
    }

    public Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public PlayListModel getPlayListModel() {
        return playListModel;
    }

    public void setPlayListModel(PlayListModel playListModel) {
        this.playListModel = playListModel;
    }

    public TextField getPlaylistTitle() {
        return playlistTitle;
    }

    public Label getInformation() {
        return information;
    }

    public Button getSaveUpdateButton() {
        return saveUpdateButton;
    }


}
