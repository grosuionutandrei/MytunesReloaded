package dk.easv.mytunes.gui.components.addToPlaylist;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;



public class AddToPlaylist {


    private Button button;
    private SVGPath svgPath;

    public AddToPlaylist(Alert alert) {
        initializeButton();
    }

    private void initializeButton() {
        this.button = new Button();
        this.svgPath = new SVGPath();
        this.svgPath.setContent("M20 11.25C20.4142 11.25 20.75 11.5858 20.75 12C20.75 12.4142 20.4142 12.75 20 12.75H10.75L10.75 18C10.75 18.3034 10.5673 18.5768 10.287 18.6929C10.0068 18.809 9.68417 18.7449 9.46967 18.5304L3.46967 12.5304C3.32902 12.3897 3.25 12.1989 3.25 12C3.25 11.8011 3.32902 11.6103 3.46967 11.4697L9.46967 5.46969C9.68417 5.25519 10.0068 5.19103 10.287 5.30711C10.5673 5.4232 10.75 5.69668 10.75 6.00002L10.75 11.25H20Z");
        this.svgPath.setFill(Color.web("#1C274C"));
        this.button.setGraphic(this.svgPath);
        this.button.getStyleClass().add("leftButton");
    }

    private void addClickListener(){
    this.button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {

    });
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public SVGPath getSvgPath() {
        return svgPath;
    }

    public void setSvgPath(SVGPath svgPath) {
        this.svgPath = svgPath;
    }


}
