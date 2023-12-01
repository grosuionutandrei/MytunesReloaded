package dk.easv.mytunes.gui.editView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Popup;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ResourceBundle;

public class NewSongController implements Initializable {
   @FXML
    private Popup popup ;

    public void createNewGenre(ActionEvent event) {
    }

    public void openFileChoser(ActionEvent event) {
    }

    public void addNewSong(ActionEvent event) {
    }

    public void cancelAddNewSong(ActionEvent event) {
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }


}
