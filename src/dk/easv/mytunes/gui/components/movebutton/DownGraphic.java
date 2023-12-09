package dk.easv.mytunes.gui.components.movebutton;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class DownGraphic implements MoveButton {

    private SVGPath svgPath;
    private Group svgGroup;


    public DownGraphic() {
        svgPath = new SVGPath();
        svgGroup = new Group(svgPath);
        svgPath.setContent("M505.387,32.779c-3.947-1.707-8.533-0.747-11.627,2.347L256.32,272.779L18.24,35.232c-4.16-4.16-10.88-4.16-15.04,0 C1.067,37.259,0,39.925,0,42.805v182.613c0,2.88,1.173,5.547,3.2,7.573l245.653,243.947c4.16,4.16,10.88,4.16,15.04,0 l245.013-244.16c2.027-2.027,3.093-4.693,3.093-7.573V42.592C512,38.325,509.44,34.379,505.387,32.779z M490.667,220.725 L256.32,454.219L21.333,220.939V68.512l227.52,226.88c4.16,4.16,10.88,4.16,15.04,0L490.667,68.405V220.725z");
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
