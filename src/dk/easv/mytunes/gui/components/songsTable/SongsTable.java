package dk.easv.mytunes.gui.components.songsTable;
import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.gui.listeners.SongSelectionListener;
import dk.easv.mytunes.utility.PlayingLocation;
import dk.easv.mytunes.utility.Utility;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class SongsTable extends TableView<Song> {
    private SongSelectionListener songSelectionListener;
    public SongsTable(SongSelectionListener listener) {
         this.songSelectionListener= listener;
        setupColumns();
        setRowFactory();
    }

    private void setupColumns() {

        TableColumn<Song, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleColumn.setPrefWidth(95);
        titleColumn.setCellFactory(column -> new TableCell<>(95, null));

        TableColumn<Song, String> artistColumn = new TableColumn<>("Artist");
        artistColumn.setCellValueFactory(new PropertyValueFactory<>("artist"));
        artistColumn.setPrefWidth(95);
        artistColumn.setCellFactory(column ->
                new TableCell<>(95, null));


        TableColumn<Song, String> genreColumn = new TableColumn<>("Genre");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        genreColumn.setPrefWidth(95);
        genreColumn.setCellFactory(column -> new TableCell<>(95, null));


        TableColumn<Song, String> durationColumn = new TableColumn<>("Duration");
        durationColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(Utility.convertSecondsToStringRepresentation(cellData.getValue().getLength())));
        durationColumn.setPrefWidth(48);
        durationColumn.setCellFactory(column -> new TableCell<>(48, null));

        this.getColumns().addAll(titleColumn, artistColumn, genreColumn, durationColumn);
        this.setPlaceholder(new Label("No songs to display"));
        this.setId(PlayingLocation.ALL_SONGS.getValue());


    }

    private void setRowFactory(){
        this.setRowFactory(tv -> {
            TableRow<Song> row = new TableRow<>();
            row.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                 songSelectionListener.playSelectedSong(row.getIndex(),this.getId(),true);
                }else if(!row.isEmpty()&& event.getClickCount()==1){
                }
            });
            return row;
        });
    }

    public void setSongs(ObservableList<Song> songs) {
        this.setItems(songs);
    }

    public void setSongSelectionListener(SongSelectionListener songSelectionListener) {
        this.songSelectionListener = songSelectionListener;
    }


}


