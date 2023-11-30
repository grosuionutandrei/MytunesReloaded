package dk.easv.mytunes.dal;

import dk.easv.mytunes.exceptions.MyTunesException;
import javafx.scene.media.Media;

public interface ISongReader {
    public Media getMedia(String path) throws MyTunesException;
}
