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
        SongFormat songFormat = null;
        double duration = 0.0;
        if (selectedFile != null) {
            try {
                songFormat = editModel.getFormat(selectedFile.getName());
                duration = editModel.getDuration(selectedFile, songFormat);
                this.fileLocation.setText(selectedFile.getPath());
                this.songDuration.setText(String.valueOf(duration));
            } catch (MyTunesException e) {
                getAlert().setContentText(e.getMessage());
                getAlert().showAndWait();
            }
        }
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
            initiateInfoAlert(newSongStage,null);
            return;
        }
        if(!editModel.checkIfFileExists(path)){
            initiateInfoAlert(newSongStage,"No file, returned by your path!" +"\n"+"Please check again");
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
