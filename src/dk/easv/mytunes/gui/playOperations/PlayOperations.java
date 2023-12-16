package dk.easv.mytunes.gui.playOperations;

import dk.easv.mytunes.gui.mainView.Model;
import dk.easv.mytunes.utility.Operations;
import javafx.scene.media.Media;

public interface PlayOperations {
    Media getMedia(Operations operation);
}
