package dk.easv.mytunes.gui.components.movebutton;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class UpGraphic implements MoveButton {
    private SVGPath svgPath;
    private Group svgGroup;

    public UpGraphic() {
        svgPath = new SVGPath();
        svgGroup = new Group(svgPath);
        svgPath.setContent("M508.8,278.947L263.467,35.107c-4.16-4.16-10.88-4.16-15.04,0L3.2,279.053c-2.027,2.027-3.093,4.693-3.2,7.573v182.72 c0,5.867,4.8,10.667,10.667,10.667c2.88,0,5.547-1.173,7.573-3.093L256,238.947l237.867,237.76 c4.16,4.16,10.987,4.053,15.04-0.107c1.92-1.92,2.987-4.587,3.093-7.36V286.52C512,283.64,510.827,280.867,508.8,278.947z M490.667,443.427l-227.2-227.093c-4.267-4.053-10.88-4.053-15.04,0L21.333,443.64V291.107L256,57.72l234.667,233.173V443.427z");
        svgPath.setStrokeWidth(2);
        svgPath.setStroke(Color.BLACK);
        svgPath.setFill(Color.BLACK);
        double desiredWidth = 20;
        double currentWidth = svgPath.getBoundsInLocal().getWidth();
        double scale = desiredWidth / currentWidth;
        svgPath.setScaleX(scale);
        svgPath.setScaleY(scale);
    }


    @Override
    public Group getGraphic() {
        return this.svgGroup;
    }

    @Override
    public void setGraphic(SVGPath svgPath) {
        this.svgPath = svgPath;
    }

}
