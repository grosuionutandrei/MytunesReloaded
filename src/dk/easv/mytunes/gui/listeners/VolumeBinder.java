package dk.easv.mytunes.gui.listeners;

import javafx.beans.property.DoubleProperty;

public interface VolumeBinder {
    public void setVolumeLevel(Double volumeLevel);
    public Double getVolumeLevel();
    public void setIsMuteValue(boolean value);
}
