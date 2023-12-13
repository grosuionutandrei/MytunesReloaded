import dk.easv.mytunes.dal.SongReader;
import dk.easv.mytunes.dal.SongsDao;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.mainView.MainGuiController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main extends Application {

    public static void main(String[] args) throws MyTunesException {
        launch();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/dk/easv/mytunes/gui/mainView/AppGui.fxml"));
        Parent root = loader.load();
        MainGuiController mainGuiController = loader.getController();
        mainGuiController.setCurrentStage(primaryStage);
        if(!mainGuiController.isError()){
            Scene scene = new Scene(root);
            primaryStage.setX(100);
            primaryStage.setScene(scene);
            primaryStage.show();
        }else{
            primaryStage.close();
        }

    }
}