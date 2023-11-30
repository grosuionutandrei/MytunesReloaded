package dk.easv.mytunes.gui.listeners;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.media.Media;
/** supplies Media data to the player*/
public interface DataSupplier {
    public Media getMedia();
    public boolean isPlaying();
    public DoubleProperty getVolumeObservable();
    public BooleanProperty isMute();
}
