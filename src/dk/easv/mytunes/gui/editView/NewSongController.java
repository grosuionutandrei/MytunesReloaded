package dk.easv.mytunes.gui.editView;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.Genre;
import dk.easv.mytunes.utility.SongFormat;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class NewSongController implements Initializable {


    private Alert error;
    private Alert info;
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
    public TextField songDuration;


    public void openFileChoser(ActionEvent event) {
        Stage newSongStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Mp3 Files", "*.mp3")
                , new FileChooser.ExtensionFilter("Wav Files", "*.wav")
        );
        File selectedFile = fileChooser.showOpenDialog(newSongStage);
        SongFormat songFormat = null;
        double duration = 0.0;
        if (selectedFile != null) {
            try {
                songFormat = editModel.getFormat(selectedFile.getName());
                duration = editModel.getDuration(selectedFile, songFormat);
                this.fileLocation.setText(selectedFile.getPath());
                this.songDuration.setText(String.valueOf(duration));
            } catch (MyTunesException e) {
                error.setContentText(e.getMessage());
                error.showAndWait();
            }
        }
    }
    @FXML
    private void addNewSong(ActionEvent event) {
        Stage newSongStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String title = songTitle.getText();
        String artist = songArtist.getText();
        String genre = genreDropDown.getSelectionModel().getSelectedItem();
        System.out.println(genre);
        String path = fileLocation.getText();
        String time = songDuration.getText();
        if (title.isEmpty() || path.isEmpty()) {
            initiateInfoAlert(newSongStage);
        } else {
            try {
                editModel.createNewSong(path, title, artist, genre,time);
            } catch (MyTunesException e) {
                setErrorLocation(e, newSongStage);
                error.showAndWait();
            }
        }
        newSongStage.close();
    }

    @FXML
    private void cancelAddNewSong(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        error = new Alert(Alert.AlertType.ERROR);
        try {
            this.editModel = NewEditModel.getInstance();
        } catch (MyTunesException e) {
            error.setContentText(e.getMessage());
            Platform.runLater(() -> {
                error.show();
            });
        }
        setItems(genreDropDown);
    }

    private void setItems(ComboBox<String> comboBox) {
        Genre[] items = Genre.values();
        for (Genre item : items) {
            comboBox.getItems().add(item.getGenre());
        }
    }

    private void setErrorLocation(MyTunesException e, Stage newSongStage) {
        error.setX(newSongStage.getX());
        error.setY(newSongStage.getY());
        error.setContentText(e.getMessage());
    }

    private void initiateInfoAlert(Stage newSongStage) {
        info = new Alert(Alert.AlertType.INFORMATION);
        info.setX(newSongStage.getX());
        info.setY(newSongStage.getY());
        info.setContentText("Title and Location can not be empty");
        info.showAndWait();
    }

}
