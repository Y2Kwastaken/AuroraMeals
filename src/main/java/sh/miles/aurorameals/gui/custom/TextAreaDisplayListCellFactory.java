package sh.miles.aurorameals.gui.custom;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;

public class TextAreaDisplayListCellFactory implements Callback<ListView<String>, ListCell<String>> {

    @Override
    public ListCell<String> call(ListView<String> stringListView) {
        return new TextAreaDisplayListCell();
    }

    private static class TextAreaDisplayListCell extends ListCell<String> {

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else if (item != null) {
                setGraphic(createGraphic(item));
            }
        }

        private static TextArea createGraphic(String item) {
            final TextArea area = new TextArea(item);
            area.setWrapText(true);
            area.setPrefRowCount(3);
            area.setEditable(false);
            return area;
        }
    }
}
