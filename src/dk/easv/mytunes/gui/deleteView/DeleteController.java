package dk.easv.mytunes.gui.deleteView;

import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.listeners.ConfirmationController;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteController implements ConfirmationController, Initializable {

    private DeleteModel deleteModel;
    private VBox confirmationWindow;
    private Alert alert;
    private Song songToDelete;

    @Override
    public void confirmationEventHandler(boolean confirmation) {
    if(confirmation) {
        try {
            deleteModel.deleteSong(songToDelete.getSongId(), songToDelete.getSongPath());
        } catch (MyTunesException e) {
          displayError(e, Alert.AlertType.ERROR);
        }
    }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
  try{
      deleteModel=DeleteModel.getInstance();
  } catch (MyTunesException e) {
      displayError(e,Alert.AlertType.ERROR);
  }
  if(deleteModel!=null){
      ConfirmationWindow confirmationView =new ConfirmationWindow();
      confirmationWindow= confirmationView.getConfirmationWindow();
      initializeConfirmationWindow(confirmationView,this);
      System.out.println(confirmationView.getConfirmationWindow().getChildren());
  }
    }

    private void displayError(MyTunesException e, Alert.AlertType type) {
        alert= new Alert(type);
        alert.setContentText(e.getMessage());
        alert.show();
    }

    public Song getSongToDelete() {
        return songToDelete;
    }

    public void setSongToDelete(Song songToDelete) {
        this.songToDelete = songToDelete;
    }
    private void initializeConfirmationWindow(ConfirmationWindow confirmationWindow,ConfirmationController confirmationController) {
        confirmationWindow.setConfirmationController(confirmationController);
        confirmationWindow.setOperationTitle("Delete operation");
        confirmationWindow.setOperationInformation("Are you sure that you want to delete this file?");
    }

    public VBox getConfirmationWindow() {
        return confirmationWindow;
    }
}
