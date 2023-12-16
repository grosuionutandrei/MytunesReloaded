package dk.easv.mytunes.gui.components.player;

import dk.easv.mytunes.gui.listeners.DataSupplier;
import dk.easv.mytunes.gui.mainView.Model;
import dk.easv.mytunes.gui.playOperations.PlayOperations;
import dk.easv.mytunes.utility.Operations;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.scene.media.Media;

public class DataHandler implements DataSupplier
{
    private Model model;
    private static DataHandler instance;
    private PlayOperations playOperations;
    public static DataHandler getInstance(Model model,PlayOperations playOperationsHandler){
        if(instance==null){
            instance=new DataHandler(model,playOperationsHandler);
        }
        return instance;
    }

    private  DataHandler (Model model,PlayOperations playOperationsHandler){
        this.model=model;
        this.playOperations=playOperationsHandler;
    }


    /**
     * provides the media player with the necessary data
     *
     * @param operation the operation that needs to be performed, play next, play previous , play initial song
     */
    @Override
    public Media getMedia(Operations operation) {
        return playOperations.getMedia(operation);
    }


    /**
     * controls iff the song can be played or not
     */
    @Override
    public boolean isPlaying() {
        return this.model.isPlayMusic();
    }



    /**
     * binds the volume off the player with the volume level stored in the model
     */
    @Override
    public DoubleProperty getVolumeObservable() {
        return this.model.volumeLevelProperty();
    }


        /**
     * supplies data to the player when the volume is mute
     */
    @Override
    public BooleanProperty isMute() {
        return this.model.isMuteProperty();
    }
}
