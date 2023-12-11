package dk.easv.mytunes.utility;

import javafx.scene.control.Alert;

public class ExceptionHandler {
    private static final Alert error = new Alert(Alert.AlertType.ERROR);
    private static final Alert warning = new Alert(Alert.AlertType.WARNING);
    private static final Alert info = new Alert(Alert.AlertType.INFORMATION);

    public static void displayErrorAlert(Messages informationalMessages) {
        error.setContentText(informationalMessages.getValue());
        error.show();
    }

    public static  void displayErrorAlert(String message){
     error.setContentText(message);
     error.show();
    }


    public static void displayWarningAlert(Messages informationalMessages) {
        warning.setContentText(informationalMessages.getValue());
        warning.show();
    }

    public static void displayInformationAlert(Messages informationalMessages) {
        info.setContentText(informationalMessages.getValue());
        info.show();
    }
    public static void displayInformationAlert(String message) {
        info.setContentText(message);
        info.show();
    }
}