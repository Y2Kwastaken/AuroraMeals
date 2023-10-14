package sh.miles.aurorameals.gui.custom;

import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;

public class TextAreaListCell extends ListCell<String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else if (item != null) {
            setText(null);
            setGraphic(createGraphic(item));
        } else {
            setGraphic(null);
        }
    }

    private static TextArea createGraphic(String content) {
        final TextArea area = new TextArea();
        area.setText(content);
        area.setWrapText(true);
        return area;
    }
}
