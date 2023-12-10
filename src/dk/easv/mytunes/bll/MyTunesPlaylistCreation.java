package dk.easv.mytunes.bll;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.dal.PlaylistDao;
import dk.easv.mytunes.exceptions.MyTunesException;

import java.util.List;

public class MyTunesPlaylistCreation {
    private PlaylistDao playlistDao;
    private static MyTunesPlaylistCreation instance;

    private MyTunesPlaylistCreation() throws MyTunesException {
        playlistDao = PlaylistDao.getPlaylistDao();
    }

    public static MyTunesPlaylistCreation getInstance() throws MyTunesException {
        if (instance == null) {
            instance = new MyTunesPlaylistCreation();
        }
        return instance;
    }

    public static boolean checkTitle(String title) {
        return title.isEmpty();
    }

    public boolean createPlayList(PlayList playList) throws MyTunesException {
        return this.playlistDao.createPlayList(playList);
    }

    public boolean updatePlayList(PlayList playlistToUpdate,String newTitle) throws MyTunesException {
        if(playlistToUpdate.getName().equals(newTitle)){
            return false;
        }
        return this.playlistDao.updatePlayList(playlistToUpdate.getId(),newTitle);
    }

    public boolean deletePlayList(int playListId) throws MyTunesException {
        return this.playlistDao.deletePlayList(playListId);
    }

    public boolean addSongToPlaylist(PlayList playListToAdd, Song songToBeAdded) throws MyTunesException {
        Song firstSong = playListToAdd.getPlayListSongs().getFirst();
        if(firstSong.getSongId()==0){
            return this.playlistDao.updatePlayListNullSong(playListToAdd.getId(),songToBeAdded.getSongId());
        }
        return this.playlistDao.addSongToPlaylist(playListToAdd,songToBeAdded);
    }

    public boolean deleteSongFromPLayList(Song songToDelete,List<Song> songsToDeleteFrom) throws MyTunesException {
        if(songsToDeleteFrom.size()==1){
            throw new MyTunesException("List can not be empty");
        }
        return this.playlistDao.deleteSongFromPLayList(songToDelete.getSongId());
    }

    public boolean saveChange(PlayList currentPlayList) throws MyTunesException {
        return this.playlistDao.saveChange(currentPlayList);
    }
}
