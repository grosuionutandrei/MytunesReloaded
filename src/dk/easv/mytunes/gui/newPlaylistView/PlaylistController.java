package dk.easv.mytunes.gui.newPlaylistView;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.listeners.Reloadable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;

public class PlaylistController implements Initializable {
 private Reloadable reloadable;
   private PlayListModel playListModel;
    @FXML
    private TextField playlistTitle;

    public void savePlaylist(ActionEvent event) {

    }

    public void cancelPlaylist(ActionEvent event) {
    }
/**
 * Initialize the model , if an error has occurred will not show the window*/
 @Override
 public void initialize(URL location, ResourceBundle resources) {
   try{
    playListModel= PlayListModel.getInstance();
   }
   catch (MyTunesException e){
    displayAlert(Alert.AlertType.ERROR,e.getMessage());
    return;
   }
     System.out.println("asd");
 }

    private void displayAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.show();
    }
}
