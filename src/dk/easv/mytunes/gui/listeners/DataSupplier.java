package dk.easv.mytunes.gui.listeners;

import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.Operations;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.media.Media;

/**
 * supplies Media data to the player
 */
public interface DataSupplier {
    Media getMedia(Operations operation);

    boolean isPlaying();

    DoubleProperty getVolumeObservable();

    BooleanProperty isMute();

}
