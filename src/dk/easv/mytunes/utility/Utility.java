package dk.easv.mytunes.utility;

public class Utility {





    public static String convertSecondsToStringRepresentation(long length) {
        if (length == 0.0) {
            return String.format("%02d:%02d:%02d", 0, 0, 0);
        }
        int hours = (int) length / 3600;
        int minutes = (int) (length / 60);
        int seconds = (int) (length % 60);
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public double calculateMidPoint(double mainX,double mainW,double popW){
     return mainX+(mainW/2)-(popW/2);
    }




}
