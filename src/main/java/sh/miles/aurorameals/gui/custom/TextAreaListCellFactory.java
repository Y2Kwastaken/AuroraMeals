package sh.miles.aurorameals.gui.custom;

import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;
import sh.miles.aurorameals.util.MutableContainer;

import java.util.function.UnaryOperator;

public class TextAreaListCellFactory implements Callback<ListView<MutableContainer<String>>, ListCell<MutableContainer<String>>> {

    @Override
    public ListCell<MutableContainer<String>> call(ListView<MutableContainer<String>> ingredientListView) {
        return new TextAreaListCell();
    }

    private static class TextAreaListCell extends ListCell<MutableContainer<String>> {

        @Override
        protected void updateItem(MutableContainer<String> item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null) {
                setText(null);
                setGraphic(null);
            } else {
                setGraphic(createGraphic(item));
            }
        }

        private TextArea createGraphic(MutableContainer<String> content) {
            final TextArea area = new TextArea();
            area.setText(content.get());
            area.setOnKeyReleased((event) -> {
                content.set(area.getText());
            });
            area.setWrapText(true);
            area.setPrefRowCount(3);
            area.setPromptText("Enter an Instruction");
            return area;
        }
    }
}
