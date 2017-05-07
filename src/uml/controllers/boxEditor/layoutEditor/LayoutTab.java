package uml.controllers.boxEditor.layoutEditor;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import uml.controllers.boxEditor.BECompanion;
import uml.controllers.colorUtilities.ColorConverter;
import uml.views.PaneView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * "Companion" for the 'layout'-tab
 */
public class LayoutTab {

    private static final StringConverter<Color> CONVERTER = new ColorConverter();
    private List<ComboBox<Color>> colorCombos;
    private PaneView paneView;
    private BECompanion companion;
    private HashMap<ComboBox<Color>, Runnable> boxToRun = new HashMap<>();

    public LayoutTab(PaneView pane, BECompanion beCompanion) {
        paneView = pane;
        companion = beCompanion;

        // Initialize the current values of the ComboBoxes according to the user's preferences/previous layout
        HashMap<String, String> layout = companion.getModel().getCustomLayout().get(companion.getBox().getName());
        if (layout != null) {
            companion.bBackgroundColor.setValue(CONVERTER.fromString(layout.get("backgroundC")));
            companion.bBorderColor.setValue(CONVERTER.fromString(layout.get("borderC")));
            companion.bTextColor.setValue(CONVERTER.fromString(layout.get("textC")));
            companion.bFontFamily.setValue(layout.get("fontF"));
        }
        else {
            companion.bBackgroundColor.setValue(CONVERTER.fromString(paneView.getModel().getLayoutPrefs().getPreference(
                    "boxBackgroundColor", "antiquewhite")));
            companion.bBorderColor.setValue(CONVERTER.fromString(paneView.getModel().getLayoutPrefs().getPreference(
                    "boxBorderColor", "black")));
            companion.bTextColor.setValue(CONVERTER.fromString(paneView.getModel().getLayoutPrefs().getPreference(
                    "boxTextColor", "black")));
            companion.bFontFamily.setValue(paneView.getModel().getLayoutPrefs().getPreference(
                    "boxTextFontFamily", "Arial"));
        }

        // Set the color ComboBoxes values
        colorCombos = new ArrayList<>();
        colorCombos.add(companion.bBackgroundColor);
        colorCombos.add(companion.bBorderColor);
        colorCombos.add(companion.bTextColor);

        for (ComboBox<Color> comboBox : colorCombos) {
            comboBox.getItems().addAll(Arrays.asList(Color.RED, Color.GREEN, Color.WHITE, Color.BLACK,
                    Color.ANTIQUEWHITE, Color.LIGHTGRAY, Color.MAGENTA, Color.CYAN, Color.YELLOW));
            comboBox.setConverter(CONVERTER);
            comboBox.setCellFactory(ComboCell::new);
        }

        // Set the font ComboBox' values
        companion.bFontFamily.getItems().addAll(Font.getFontNames());

        boxToRun.put(companion.bBackgroundColor, () -> changeBoxBackgroundC());
        boxToRun.put(companion.bBorderColor, () -> changeBoxBorderC());
        boxToRun.put(companion.bTextColor, () -> changeBoxTextC());
    }

    public static class ComboCell extends ListCell<Color> {

        private Rectangle rectangle;

        public ComboCell(ListView<Color> list) {
            this.rectangle = new Rectangle(10, 10);
            setContentDisplay(ContentDisplay.LEFT);
        }

        protected void updateItem(Color item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
                setText(null);
            } else {
                rectangle.setFill(item);
                setGraphic(rectangle);
                String itemString = CONVERTER.toString(item);
                setText(itemString);
            }
        }
    }

    /**
     * Save the currently shown information:
     *      if the checkbox is checked --> change preferences + fire invalidation
     *      if the checkbox is unchecked --> change the selected VBox' layout
     */
    public void saveChanges() {
        if (companion.checkBox.isSelected()) {
            paneView.getModel().getLayoutPrefs().setPreference("boxBackgroundColor",
                                                            CONVERTER.toString(companion.bBackgroundColor.getValue()));
            paneView.getModel().getLayoutPrefs().setPreference("boxBorderColor",
                                                            CONVERTER.toString(companion.bBorderColor.getValue()));
            paneView.getModel().getLayoutPrefs().setPreference("boxTextColor",
                                                            CONVERTER.toString(companion.bTextColor.getValue()));
            paneView.getModel().getLayoutPrefs().setPreference("boxTextFontFamily",
                                                            companion.bFontFamily.getValue());
            companion.getModel().getCustomLayout().clear();
            companion.getModel().fireInvalidationEvent();
        }
        else {
            for (ComboBox<Color> comboBox : colorCombos) {
                boxToRun.get(comboBox).run();
            }
            if (companion.bFontFamily.getValue() != null) {
                changeBoxFontFamily();
            }
            // Necessary to save this specific VBox' layout, so it doesn't change after invalidation event
            if (!companion.getBox().getName().equals("")) {
                HashMap<String, String> layout = new HashMap<>();
                layout.put("backgroundC", CONVERTER.toString(companion.bBackgroundColor.getValue()));
                layout.put("borderC", CONVERTER.toString(companion.bBorderColor.getValue()));
                layout.put("textC", CONVERTER.toString(companion.bTextColor.getValue()));
                layout.put("fontF", companion.bFontFamily.getValue());
                companion.getModel().getCustomLayout().put(companion.getBox().getName(), layout);
            }
        }
    }

    /**
     * Change the VBox' font by chaning the font of every text it has.
     */
    private void changeBoxFontFamily() {
        if(companion.getTexts() != null) {
            for (Text text : companion.getTexts()) {
                text.setFont(Font.font(companion.bFontFamily.getValue()));
            }
        }
    }

    /**
     * Changes the VBox' backgroundcolor
     */
    private void changeBoxBackgroundC() {
        replaceCStyle(companion.getVbox(), "-fx-background-color: ", companion.bBackgroundColor);
    }

    /**
     * Changes the VBox' bordercolor.
     */
    private void changeBoxBorderC() {
        replaceCStyle(companion.getVbox(), "-fx-border-color: ", companion.bBorderColor);
    }

    /**
     * Changes the VBox' textcolor.
     */
    private void changeBoxTextC() {
        if(companion.getTexts() != null) {
            for (Text text : companion.getTexts()) {
                text.setFill(companion.bTextColor.getValue());
            }
        }
    }

    /**
     * Replace the value of a style property.
     * @param node
     * @param property
     * @param comboBox
     */
    private void replaceCStyle(Node node, String property, ComboBox<Color> comboBox) {
        node.setStyle(node.getStyle().replaceAll(property + " [^;]*;", "") +
                property + CONVERTER.toString(comboBox.getValue()) + ";");
    }
}