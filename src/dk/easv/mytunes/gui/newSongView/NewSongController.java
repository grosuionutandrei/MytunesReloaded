package dk.easv.mytunes.gui.newSongView;

import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.ExceptionHandler;
import dk.easv.mytunes.utility.InformationalMessages;
import dk.easv.mytunes.utility.SongFormat;
import dk.easv.mytunes.utility.Utility;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;

public class NewSongController extends NewEditController implements Initializable {
    private NewEditModel editModel;
    @FXML
    private ComboBox<String> genreDropDown;
    @FXML
    private TextField songTitle;
    @FXML
    private TextField songArtist;
    @FXML
    private TextField fileLocation;
    @FXML
    private TextField songDuration;

    public void openFileChoser(ActionEvent event) {
        Stage newSongStage = Utility.getCurrentStage(event);
        File selectedFile = getFileChooser().showOpenDialog(newSongStage);
        if (selectedFile != null) {
            try {
                SongFormat songFormat = setSongFormat(selectedFile);
                double duration = setDuration(selectedFile, songFormat);
                this.fileLocation.setText(selectedFile.getPath());
                this.songDuration.setText(String.valueOf(duration));
            } catch (MyTunesException e) {
                getAlert().setContentText(e.getMessage());
                getAlert().showAndWait();
            }
        }
    }

    private double setDuration(File file, SongFormat songFormat) throws MyTunesException {
        return editModel.getDuration(file, songFormat);
    }

    private SongFormat setSongFormat(File file) throws MyTunesException {
        return editModel.getFormat(file.getName());
    }

    @FXML
    private void addNewSong(ActionEvent event) {
        Stage newSongStage = Utility.getCurrentStage(event);
        String title = songTitle.getText();
        String artist = songArtist.getText();
        String genre = genreDropDown.getSelectionModel().getSelectedItem();
        String path = fileLocation.getText();
        String time = songDuration.getText();
        if (!validateInputs(title, path, newSongStage, this.editModel)) {
            return;
        }
        if (checkIfDuplicate(path, newSongStage)) {
            return;
        }
        if (!editModel.checkIfFileExists(path)) {
            initiateInfoAlert(newSongStage, InformationalMessages.NO_FILE.getValue());
            return;
        }
        saveSong(path,title,genre,artist,time,newSongStage);
    }

    private  void saveSong(String path,String title,String artist,String genre,String time,Stage currentStage ){
        try {
            editModel.createNewSong(path, title, artist, genre, time);
            getReloadableController().reloadSongsFromDB();
            currentStage.close();
        } catch (MyTunesException e) {
            initiateErrorAlert(e, currentStage);
            currentStage.close();
        }
    }

    private boolean checkIfDuplicate(String path, Stage currentStage) {
        try {
            if (editModel.checkIfDuplicate(path)) {
                initiateInfoAlert(currentStage, InformationalMessages.NO_DUPLICATE.getValue());
                return true;
            }
        } catch (MyTunesException e) {
            initiateErrorAlert(e, currentStage);
            return true;
        }
        return false;
    }


    @FXML
    private void cancelAddNewSong(ActionEvent event) {
        Stage stage = Utility.getCurrentStage(event);
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.editModel = NewEditModel.getInstance();
        } catch (MyTunesException e) {
            Platform.runLater(() -> {
                ExceptionHandler.displayErrorAlert(e.getMessage());
            });
        }
        setItems(genreDropDown);
    }

}
