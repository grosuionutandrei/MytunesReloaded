package dk.easv.mytunes.gui.components.player;


import dk.easv.mytunes.utility.Operations;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.util.Duration;

public interface PlayerControl {
    void play();
    void pause();
    void playNext(Media media, boolean play);
    void playPrevious(Media media,boolean play);
    Duration getCurrentTime();
    void playCurrent(Media media,boolean play);

    void bindMediaTimeToScreen(Label label);


}
