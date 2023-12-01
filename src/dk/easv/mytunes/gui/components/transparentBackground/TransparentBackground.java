package dk.easv.mytunes.gui.components.transparentBackground;

import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;


public class TransparentBackground {
    public Rectangle getBackground() {
        return background;
    }

    private Rectangle background;

    public TransparentBackground(){
      background = new Rectangle();
      background.getStyleClass().add("backgroundTransparent");
    }
}
