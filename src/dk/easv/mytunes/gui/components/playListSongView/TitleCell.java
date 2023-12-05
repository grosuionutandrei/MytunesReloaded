package dk.easv.mytunes.gui.components.playListSongView;


import dk.easv.mytunes.be.Song;
import dk.easv.mytunes.gui.listeners.SongSelectionListener;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class TitleCell<T extends Song> extends ListCell<T> {
    private final Tooltip longDescription =  new Tooltip();
    private int cellWidth ;
    private Duration deelayDuration = new Duration(0.2);
    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setGraphic(createCell(item));
        }
    }
    public TitleCell(int width, Duration duration){
        this.cellWidth=width;
        if(duration!=null){
            this.deelayDuration=duration;
        }
    }


    private Label createCell(T item){
        Label label = new Label(item.getTitle());
        label.setPrefWidth(cellWidth);
        longDescription.setText(item.getTitle());
        longDescription.setShowDelay(this.deelayDuration);
        longDescription.setText(item.getTitle());
         setOnMouseEnterAction(label);
       setOnMouseExitAction(label);
       return label;

    }

    private void setOnMouseEnterAction(Label label){
        label.setOnMouseEntered(event->
                Tooltip.install(this,longDescription));
    }

    private void setOnMouseExitAction(Label label)
    {
        label.setOnMouseExited(event -> Tooltip.uninstall(this,longDescription));
    }

    public void setOnMouseClick(SongSelectionListener listener,String tableId){
        this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (!this.isEmpty() && event.getClickCount() == 2) {
                listener.onSongSelect(this.getIndex(),tableId,true);
            }else if(!this.isEmpty()&& event.getClickCount()==1){
                System.out.println();

            }
        });

    };
    }




