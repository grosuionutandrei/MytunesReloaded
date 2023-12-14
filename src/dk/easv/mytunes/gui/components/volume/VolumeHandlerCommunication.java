package dk.easv.mytunes.gui.components.volume;

import dk.easv.mytunes.gui.listeners.VolumeBinder;
import dk.easv.mytunes.gui.mainView.Model;

public class VolumeHandlerCommunication implements VolumeBinder {
    private Model model;
    private static VolumeHandlerCommunication instance;

    public static VolumeHandlerCommunication getInstance(Model model){
        if(instance==null){
            instance=new VolumeHandlerCommunication(model);
        }
        return instance;
    }
    private VolumeHandlerCommunication(Model model){
        this.model=model;
    }



    /**
     * sets the  model volume property according to the current slider value
     */
    @Override
    public void setVolumeLevel(Double volumeLevel) {
        this.model.volumeLevelProperty().set(volumeLevel / 100);
    }

    /**
     * sets the value off the slider back to the previous value
     */
    @Override
    public Double getVolumeLevel() {
        return this.model.volumeLevelProperty().getValue();
    }

    /**
     * sets the model isMute boolean propriety when the mute/unmute button is pressed
     */
    @Override
    public void setIsMuteValue(boolean value) {
        this.model.isMuteProperty().setValue(value);
    }
}
