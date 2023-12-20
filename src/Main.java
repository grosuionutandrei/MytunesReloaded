
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.gui.mainView.MainGuiController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        if (!mainGuiController.isError()) {
            Scene scene = new Scene(root);
            primaryStage.setX(100);
            primaryStage.setTitle("MyTunes");
            primaryStage.setScene(scene);
            primaryStage.show();
        } else {
            primaryStage.close();
        }

    }
}