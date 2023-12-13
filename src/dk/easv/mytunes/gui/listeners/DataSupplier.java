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
    public Media getMedia(Operations operation);

    public boolean isPlaying();

    public DoubleProperty getVolumeObservable();

    public BooleanProperty isMute();

    public void bindMediaTimeToScreen(StringProperty binder);
}
