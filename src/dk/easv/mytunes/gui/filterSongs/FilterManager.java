package dk.easv.mytunes.gui.filterSongs;

import dk.easv.mytunes.gui.components.searchButton.ISearchGraphic;
import dk.easv.mytunes.gui.components.searchButton.SearchGraphic;
import dk.easv.mytunes.gui.components.searchButton.UndoGraphic;
import dk.easv.mytunes.gui.mainView.Model;
import dk.easv.mytunes.utility.GraphicIdValues;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FilterManager {
    private final Model model;
    private ISearchGraphic searchGraphic;
    private Label infoLabel;
    private Button searchButton;
    private TextField searchValue;

    public FilterManager(Model model, ISearchGraphic searchGraphic, Label infoLabel, Button searchButton, TextField searchValue) {
        this.model = model;
        this.searchGraphic = searchGraphic;
        this.infoLabel = infoLabel;
        this.searchButton = searchButton;
        this.searchValue = searchValue;
    }

    public void applyFilter(ActionEvent event) {
        String filter = searchValue.getText();

        if (searchButton.getGraphic().getId().equals(GraphicIdValues.SEARCH.getValue())) {
            if (!filter.isEmpty()) {
                applySearchFilter(filter);
            } else {
                showInfoMessage("Filter is empty");
            }
        } else {
            clearSearchFilter();
        }
    }

    private void applySearchFilter(String filter) {
        searchGraphic = new UndoGraphic();
        model.applyFilter(filter);
        infoLabel.setVisible(false);
        searchButton.setGraphic(searchGraphic.getGraphic());
        searchValue.setText("");
        searchValue.setEditable(false);
    }

    private void clearSearchFilter() {
        searchGraphic = new SearchGraphic();
        searchButton.setGraphic(searchGraphic.getGraphic());
        searchValue.setEditable(true);
        model.resetFilter();
    }

    private void showInfoMessage(String message) {
        infoLabel.setVisible(true);
    }
}
