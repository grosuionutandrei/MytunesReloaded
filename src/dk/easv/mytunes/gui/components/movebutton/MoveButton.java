package dk.easv.mytunes.gui.components.movebutton;

import javafx.scene.Group;
import javafx.scene.shape.SVGPath;

public interface MoveButton {
    public Group getGraphic();
    public void setGraphic(SVGPath svgPath);
}
