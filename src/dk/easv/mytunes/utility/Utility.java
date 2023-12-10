package dk.easv.mytunes.utility;

import javafx.scene.control.Alert;

public class Utility {





    public static String convertSecondsToStringRepresentation(double length) {
        if (length == 0.0) {
            return String.format("%02d:%02d:%02d", 0, 0, 0);
        }
        int hours = (int) length / 3600;
        int minutes = (int) (length / 60);
        int seconds = (int) (length % 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static void displayInformation(Alert.AlertType alertType,String message){
        Alert alert = new Alert(alertType);
            alert.setContentText(message);
            alert.show();
    }

    public double calculateMidPoint(double mainX,double mainW,double popW){
     return mainX+(mainW/2)-(popW/2);
    }




}
