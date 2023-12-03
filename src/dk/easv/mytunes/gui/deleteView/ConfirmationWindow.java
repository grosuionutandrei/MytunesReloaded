package dk.easv.mytunes.gui.deleteView;
import dk.easv.mytunes.gui.listeners.ConfirmationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.IOException;

public class ConfirmationWindow {

    private ConfirmationController confirmationController;
    @FXML
    private VBox container;
    @FXML
    private Label operationTitle;

    @FXML private Label operationInformation;
    @FXML
    private Button confirmButton;
    @FXML
    private Button cancelButton;


    public ConfirmationWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ConfirmationWindow.fxml"));
        loader.setController(this);
        try {
            container = loader.load();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Something went wrong with the application");
            alert.show();
        }
        if (container != null) {
         addConfirmHandler();
         addCancelHandler();
        }
    }

    public VBox getConfirmationWindow() {
        return container;
    }


    public void setOperationTitle(String title) {
        this.operationTitle.setText(title);
    }

    public void setOperationInformation(String title) {
        this.operationInformation.setText(title);
    }


    private void addConfirmHandler() {
        this.confirmButton.setOnMouseClicked(event ->
        {confirmationController.confirmationEventHandler(true);
         getCurrentStage(event).close();
        }
        );
    }

    private void addCancelHandler() {
        this.cancelButton.setOnMouseClicked(event ->{
                confirmationController.confirmationEventHandler(false);
                getCurrentStage(event).close();
        });
    }
    public void setConfirmationController(ConfirmationController confirmationController) {
        this.confirmationController = confirmationController;
    }
    private Stage getCurrentStage(MouseEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }
}
