package dk.easv.mytunes.gui.newSongView;

import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.SongFormat;
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
        Stage newSongStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = getFileChooser().showOpenDialog(newSongStage);
        
        if (selectedFile != null) {
            try {
                SongFormat songFormat = setSongFormat(selectedFile);
                double duration =setDuration(selectedFile,songFormat);
                this.fileLocation.setText(selectedFile.getPath());
                this.songDuration.setText(String.valueOf(duration));
            } catch (MyTunesException e) {
                getAlert().setContentText(e.getMessage());
                getAlert().showAndWait();
            }
        }
    }

    private double setDuration(File file, SongFormat songFormat) throws MyTunesException {
      return editModel.getDuration(file,songFormat);        
    }
    private SongFormat setSongFormat(File file) throws MyTunesException {
    return  editModel.getFormat(file.getName());  
    }

    @FXML
    private void addNewSong(ActionEvent event) {
        Stage newSongStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String title = songTitle.getText();
        String artist = songArtist.getText();
        String genre = genreDropDown.getSelectionModel().getSelectedItem();
        String path = fileLocation.getText();
        String time = songDuration.getText();
        if (editModel.areTitleOrPathEmpty(title, path)) {
            initiateInfoAlert(newSongStage, null);
            return;
        }

        boolean checkIfDuplicate = false;
        try {
            checkIfDuplicate = editModel.checkIfDuplicate(path);
        } catch (MyTunesException e) {
            initiateErrorAlert(e, newSongStage);
            return;
        }
        if (checkIfDuplicate) {
            initiateInfoAlert(newSongStage, "Please chose another file, this song is already in the list");
            return;
        }

        if (!editModel.checkIfFileExists(path)) {
            initiateInfoAlert(newSongStage, "No file, returned by your path!" + "\n" + "Please check again");
            return;
        }

        try {
            editModel.createNewSong(path, title, artist, genre, time);
        } catch (MyTunesException e) {
            initiateErrorAlert(e, newSongStage);
            newSongStage.close();
            return;
        }
        getReloadableController().reloadSongsFromDB();
        newSongStage.close();
    }


    @FXML
    private void cancelAddNewSong(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.editModel = NewEditModel.getInstance();
        } catch (MyTunesException e) {
            getAlert().setContentText(e.getMessage());
            Platform.runLater(() -> {
                getAlert().show();
            });
        }
        setItems(genreDropDown);
    }

}
