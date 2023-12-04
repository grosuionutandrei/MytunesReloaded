package dk.easv.mytunes.gui.newSongView;

import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.listeners.Reloadable;
import dk.easv.mytunes.gui.mainView.MainGuiController;
import dk.easv.mytunes.utility.Genre;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public abstract class NewEditController {
    private final Alert alertWindowOperation = new Alert(Alert.AlertType.NONE);
    private Alert info;
    private Reloadable reloadable;
    private final FileChooser fileChooser = new FileChooser();

    public FileChooser getFileChooser() {
        setDefaultExtensions();
        return fileChooser;
    }

    private void setDefaultExtensions() {
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Mp3 Files", "*.mp3")
                , new FileChooser.ExtensionFilter("Wav Files", "*.wav")
        );
    }

    public Alert getAlert() {
        return alertWindowOperation;
    }

    public Alert getInfo() {
        return info;
    }


    public void setItems(ComboBox<String> comboBox) {
        Genre[] items = Genre.values();
        for (Genre item : items) {
            comboBox.getItems().add(item.getGenre());
        }
    }

    public void setReloadableController(Reloadable reloadable) {
        this.reloadable = reloadable;
    }

    public Reloadable getReloadableController() {
        return reloadable;
    }

    public void initiateInfoAlert(Stage newSongStage, String message) {
        alertWindowOperation.setAlertType(Alert.AlertType.INFORMATION);
        alertWindowOperation.setX(newSongStage.getX());
        alertWindowOperation.setY(newSongStage.getY());
        if (message == null) {
            alertWindowOperation.setContentText("Title and Location can not be empty");
        } else {
            alertWindowOperation.setContentText(message);
        }

        alertWindowOperation.showAndWait();
    }

    public void initiateErrorAlert(MyTunesException e, Stage newSongStage) {
        alertWindowOperation.setAlertType(Alert.AlertType.ERROR);
        alertWindowOperation.setX(newSongStage.getX());
        alertWindowOperation.setY(newSongStage.getY());
        alertWindowOperation.setContentText(e.getMessage());
        alertWindowOperation.showAndWait();
    }
}
