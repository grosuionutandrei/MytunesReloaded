package dk.easv.mytunes.gui.editView;
import dk.easv.mytunes.bll.MyTunesCreation;
import dk.easv.mytunes.exceptions.MyTunesException;
import dk.easv.mytunes.utility.SongFormat;

import java.io.File;


public class NewEditModel {
    private static NewEditModel instance;
    private MyTunesCreation myTunesCreation;

    private NewEditModel() throws  MyTunesException{
        myTunesCreation=MyTunesCreation.getInstance();
    }
    public static  NewEditModel getInstance()throws MyTunesException {
        if(instance==null){
            instance=new NewEditModel();
        }
        return instance;
    }

    public void createNewSong(String path, String title , String artist ,String genre,String songDuration ) throws MyTunesException {
        myTunesCreation.createNewSong(path,title,artist,genre,songDuration);
    }


    public SongFormat getFormat(String name) throws MyTunesException {
        return myTunesCreation.extractFormat(name);
    }
    public double getDuration(File file, SongFormat songFormat) throws MyTunesException {
        return myTunesCreation.getSongDuration(file,songFormat);
    }



}
