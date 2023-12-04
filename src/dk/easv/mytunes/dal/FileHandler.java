package dk.easv.mytunes.dal;

import dk.easv.mytunes.exceptions.MyTunesException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler {

    private static FileHandler instance;

    private FileHandler(){

    }
    public static FileHandler getInstance(){
        if(instance==null){
            instance= new FileHandler();
        }
        return instance;
    }


    private boolean deleteSongFromLocal(String path) throws MyTunesException {
        boolean deleted;
        try{
            deleted =Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            throw new MyTunesException("Deleting song from local gone wrong ");
        }
        return deleted;
    }
    public boolean deleteSongLocal(String path) throws MyTunesException {
        return deleteSongFromLocal(path);
    }




}
