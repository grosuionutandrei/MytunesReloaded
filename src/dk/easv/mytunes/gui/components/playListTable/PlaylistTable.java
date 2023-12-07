package dk.easv.mytunes.gui.components.playListTable;

import dk.easv.mytunes.be.PlayList;
import dk.easv.mytunes.gui.listeners.PlayListSelectionListener;
import dk.easv.mytunes.utility.PlayingLocation;
import dk.easv.mytunes.utility.Utility;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class PlaylistTable extends TableView<PlayList> {
    private PlayListSelectionListener playListSelectionListener;

    private int selectedPlaylistId;

    public PlaylistTable(PlayListSelectionListener playListSelectionListener) {
        super();
        this.playListSelectionListener = playListSelectionListener;
        setupColumns();
        setRowFactory();
    }

    private void setupColumns() {

        TableColumn<PlayList, String> titleColumn = new TableColumn<>("Name");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        titleColumn.setPrefWidth(65);
        titleColumn.setCellFactory(column -> new TableCell<>(65, null));


        TableColumn<PlayList, String> totalSongs = new TableColumn<>("Songs");

        totalSongs.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(String.valueOf(cellData.getValue().totalSongs())));
        totalSongs.setPrefWidth(38);
        totalSongs.setCellFactory(column ->
                new TableCell<>(38, null));


        TableColumn<PlayList, String> totalTime = new TableColumn<>("Time");
        totalTime.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(new Utility().convertSecondsToStringRepresentation(cellData.getValue().getTotalTime())));
        totalTime.setPrefWidth(65);
        totalTime.setCellFactory(column -> new TableCell<>(65, null));

        this.getColumns().addAll(titleColumn, totalSongs, totalTime);
        this.setPlaceholder(new Label("No songs to display"));
        this.setId(PlayingLocation.PLAYLIST_SONGS.getValue());
    }

    public void setSongs(ObservableList<PlayList> observableList) {
        this.setItems(observableList);
    }

    private void setRowFactory() {
        this.setRowFactory(tv -> {
            TableRow<PlayList> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    playListSelectionListener.onPlayListSelect(row.getIndex());

                } else if (!row.isEmpty() && event.getClickCount() == 1) {
                    System.out.println(this.getSelectionModel().getSelectedItem());

                }
            });
            return row;
        });
    }


    /**
     * controls the current index and the playList from where song will be played
     */
//    public void bindModelToPlayList(Model model ) {
//        this.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            model.setCurrentPlayListId(newSelection.getId());
//            model.setCurrentPlayListSongs();
//            model.setCurrentListSelected(this.getId());
//            model.setCurrentPlayingList();
//        });
//
//    }
    public void setPlayListSelectionListener(PlayListSelectionListener playListSelectionListener) {
        this.playListSelectionListener = playListSelectionListener;
    }

}
