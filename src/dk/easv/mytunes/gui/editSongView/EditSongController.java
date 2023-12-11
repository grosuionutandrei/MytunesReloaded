package dk.easv.mytunes.gui.editSongView;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.newSongView.NewEditController;
import dk.easv.mytunes.utility.ExceptionHandler;
import dk.easv.mytunes.utility.InformationalMessages;
import dk.easv.mytunes.utility.SongFormat;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EditSongController extends NewEditController implements Initializable {
    public TextField songTitle;
    public TextField songArtist;
    public TextField songDuration;
    public ComboBox<String> genreDropDown;
    public TextField fileLocation;
    private EditSongModel editModel;


    /**
     * Initializes the controller class. This method is automatically called
     * after the FXML file has been loaded. It initializes the edit model
     * and sets up the items in the genre dropdown.
     *
     * @param location The location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.editModel = EditSongModel.getInstance();
        } catch (MyTunesException e) {
            Platform.runLater(() -> {
                ExceptionHandler.displayErrorAlert(e.getMessage());
            });

        }
        setItems(genreDropDown);
    }
    /**
     * Initializes the view with the details of the song to be updated.
     * Sets the text fields and dropdown with the song's current information.
     *
     * @param songToUpdate The song object whose details are to be loaded into the view for editing.
     */
    private void initializeView(Song songToUpdate) {
        songTitle.setText(songToUpdate.getTitle());
        songArtist.setText(songToUpdate.getArtist());
        songDuration.setText(String.valueOf(songToUpdate.getLength()));
        genreDropDown.setValue(songToUpdate.getGenre());
        fileLocation.setText(songToUpdate.getSongPath());
    }

    /**
     * Handles the action of opening a file chooser to select a new file for the song.
     * Updates the song details if a different file is selected.
     *
     * @param event The event that triggered this method.
     */

    @FXML
    private void openFileChoser(ActionEvent event) {
        Stage newSongStage = getCurrentStage(event);
        File selectedFile = getFileChooser().showOpenDialog(newSongStage);
        if (selectedFile != null) {
            if (isDifferentFile(selectedFile)) {
                updateSongDetails(selectedFile);
            }
        }
    }
    /**
     * Updates the song information with the new details entered by the user.
     * Validates the inputs and updates the song in the model. Closes the edit stage upon completion.
     *
     * @param event The event that triggered this method.
     */
     @FXML
    private void updateSong(ActionEvent event) {
        Stage editSongStage = getCurrentStage(event);
        if (!validateInputs(editSongStage)) {
            return;
        }
        try {
            performSongUpdate();
        } catch (MyTunesException e) {
         handleUpdateError(e,editSongStage);
        } finally {
            editSongStage.close();
        }
    }



    private Stage getCurrentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    private boolean validateInputs(Stage stage) {
        String title = songTitle.getText();
        String path = fileLocation.getText();

        if (editModel.areTitleOrPathEmpty(title, path)) {
            initiateInfoAlert(stage, null);
            return false;
        }

        if (!editModel.checkIfFileExists(path)) {
            initiateInfoAlert(stage, InformationalMessages.NO_FILE.getValue());
            return false;
        }

        return true;
    }

    private void performSongUpdate() throws MyTunesException {
        String title = songTitle.getText();
        String artist = songArtist.getText();
        String duration = songDuration.getText();
        String genre = genreDropDown.getSelectionModel().getSelectedItem();
        String path = fileLocation.getText();
        int initialId = editModel.getInitialSong().getSongId();
        Song updatedSong = new Song(initialId, path, title, artist, genre, Double.parseDouble(duration));
        boolean updated = editModel.updateSong(updatedSong);
        if(updated){
            getReloadableController().reloadSongsFromDB();
        }

    }

    private void handleUpdateError(MyTunesException e, Stage stage) {
         initiateErrorAlert(e, stage);
    }

    /**
     * Closes the edit song stage without saving changes.
     *
     * @param event The event that triggered this method.
     */
    public void cancelEditSong(ActionEvent event) {
        Stage editStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        editStage.close();
    }

    public void initializeEditView(Song song) {
        initializeView(song);
        editModel.setInitialSong(song);
    }

    private boolean isDifferentFile(File selectedFile) {
        return !selectedFile.getPath().equalsIgnoreCase(fileLocation.getText());
    }

    private void updateSongDetails(File selectedFile) {
        try {
            SongFormat songFormat = editModel.getFormat(selectedFile.getName());
            double duration = editModel.getDuration(selectedFile, songFormat);
            fileLocation.setText(selectedFile.getPath());
            songDuration.setText(String.valueOf(duration));
        } catch (MyTunesException e) {
           ExceptionHandler.displayErrorAlert(e.getMessage());
        }
    }

}
