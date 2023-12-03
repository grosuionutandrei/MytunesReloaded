package dk.easv.mytunes.gui.editSongView;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.newSongView.NewEditController;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.editModel = EditSongModel.getInstance();
        } catch (MyTunesException e) {
            getAlert().setAlertType(Alert.AlertType.ERROR);
            getAlert().setContentText(e.getMessage());
            Platform.runLater(() -> {
                getAlert().show();
            });

        }
        setItems(genreDropDown);
    }

    private void initializeView(Song songToUpdate) {
        songTitle.setText(songToUpdate.getTitle());
        songArtist.setText(songToUpdate.getArtist());
        songDuration.setText(String.valueOf(songToUpdate.getLength()));
        genreDropDown.setValue(songToUpdate.getGenre());
        fileLocation.setText(songToUpdate.getSongPath());
    }

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
     @FXML
    private void updateSong(ActionEvent event) {
        Stage editSongStage = getCurrentStage(event);
        if (!validateInputs(editSongStage)) {
            return;
        }
        try {
            performSongUpdate();
        } catch (MyTunesException e) {
            handleUpdateError(e, editSongStage);
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
            initiateInfoAlert(stage, "No file, returned by your path!\nPlease check again");
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
        editModel.updateSong(updatedSong);
        getParentController().reloadSongsFromDB();
    }

    private void handleUpdateError(MyTunesException e, Stage stage) {
        initiateErrorAlert(e, stage);
    }

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
            displayError(e);
        }
    }

    private void displayError(MyTunesException e) {
        getAlert().setContentText(e.getMessage());
        getAlert().showAndWait();
    }

}
