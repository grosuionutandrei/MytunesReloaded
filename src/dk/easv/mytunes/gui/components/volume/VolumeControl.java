package dk.easv.mytunes.gui.components.volume;
import dk.easv.mytunes.gui.listeners.VolumeBinder;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class VolumeControl {
    private final Slider volumeLevel = new Slider(0, 100, 100);
    private VolumeGraphic volumeGraphic;
    private Button button = initializeButton();
    private final String VOLUME_ON = "volumeOn";
    private final String VOLUME_OFF = "volumeOff";
    private VolumeBinder volumeBinder;

    public VolumeControl(VolumeBinder volumeBinder) {
        this.volumeBinder=volumeBinder;
    }


    public Button initializeButton() {
        this.volumeGraphic = new VolumeOnGraphic();
        Button btn = new Button();
        btn.setGraphic(volumeGraphic.getPane());
        setOnMouseevent(btn);
        setOActionVolumeLevel();
        return btn;
    }

    private void setOnMouseevent(Button btn) {
        btn.setOnMouseClicked(event -> {
            if (button.getGraphic().getId().equals(VOLUME_ON)) {
                this.volumeGraphic = new VolumeOffGraphic();
                button.setGraphic(volumeGraphic.getPane());
                volumeBinder.setVolumeLevel(volumeLevel.getValue());
                System.out.println(volumeBinder.getVolumeLevel() + "from button is on ");
                volumeLevel.setValue(0.0);
                volumeBinder.setIsMuteValue(true);
            } else {
                this.volumeGraphic = new VolumeOnGraphic();
                button.setGraphic(volumeGraphic.getPane());
                volumeLevel.setValue(volumeBinder.getVolumeLevel()*100);
                volumeBinder.setIsMuteValue(false);
                System.out.println(volumeBinder.getVolumeLevel() +" form buttton is off");
            }
        });
    }

    /**
     * Add another instance field that can save the slider value before the mute was pressed
     **/
    private void setOActionVolumeLevel() {
        this.volumeLevel.valueProperty().addListener((observable, oldValue, newValue) -> {
            if ((Double) newValue == 0) {
                System.out.println(oldValue);
                volumeBinder.setVolumeLevel((Double) oldValue);
                System.out.println(volumeBinder.getVolumeLevel()+" from slider is zero");
               // model.setVolume((Double) oldValue);
            } else {
                //model.setVolume((Double) newValue);
                volumeBinder.setVolumeLevel((Double)newValue);
                System.out.println(volumeBinder.getVolumeLevel() + " from slider is moving ");
            }
          //  player.getMediaPlayer().setVolume((Double) newValue / 100);
            if ((Double) newValue > 0) {
                volumeGraphic = new VolumeOnGraphic();
                button.setGraphic(volumeGraphic.getPane());
            } else {
                volumeGraphic = new VolumeOffGraphic();
                button.setGraphic(volumeGraphic.getPane());
            }
        });
    }

    public Pane getGraphic() {
        return (Pane) this.button.getGraphic();
    }

    public Button getButton() {
        return this.button;
    }

    public Slider getVolumeValue() {
        return volumeLevel;
    }
}
