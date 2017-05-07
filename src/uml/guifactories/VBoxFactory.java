package uml.guifactories;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import uml.controllers.boxEditor.BoxEditor;
import uml.controllers.colorUtilities.ColorConverter;
import uml.model.BoxModel;
import uml.model.xml.Box;
import uml.model.xml.subelements.Attribute;
import uml.model.xml.subelements.Operation;
import uml.views.PaneView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static javafx.geometry.Pos.CENTER;
import static javafx.geometry.Pos.CENTER_LEFT;

/**
 * @author Dries Marzougui
 * Visualizes a box-element by converting it into a VBox.
 */
public class VBoxFactory {

    private static final StringConverter<Color> CONVERTER = new ColorConverter();
    private static final Map<String, String> VISIBILITY = new HashMap<>();
    private ArrayList<Text> texts = new ArrayList<>();
    private Box box;
    private VBox classVBox;
    private BoxEditor boxEditor;
    private double mouseX;
    private double mouseY;
    private PaneView paneView;

    public VBoxFactory(Box box, PaneView paneView) {
        // Types of visibilities
        VISIBILITY.put("private", "-");
        VISIBILITY.put("public", "+");
        VISIBILITY.put("protected", "#");
        VISIBILITY.put("derived", "/");
        VISIBILITY.put("package", "~");
        this.box = box;
        this.paneView = paneView;

        createClassVBox();

        boxEditor = new BoxEditor(box, classVBox, getTexts());

        //Set style according to explicitly selected values (--> layout-tab)
        BoxModel model = paneView.getModel();
        HashMap<String, String> layout = model.getCustomLayout().get(box.getName());
        if (layout != null) {
            replaceCStyle(classVBox, "-fx-background-color: ", layout.get("backgroundC"));
            replaceCStyle(classVBox, "-fx-border-color: ", layout.get("borderC"));
            for (Text text : texts) {
                text.setFill(CONVERTER.fromString(layout.get("textC")));
                replaceCStyle(text, "-fx-font-family: ", layout.get("fontF"));
            }
        }
        else {
            //Set style according to preferences.
            replaceCStyle(classVBox, "-fx-background-color: ", paneView.getModel().getLayoutPrefs().getPreference(
                    "boxBackgroundColor", "antiquewhite"));
            replaceCStyle(classVBox, "-fx-border-color: ", paneView.getModel().getLayoutPrefs().getPreference(
                    "boxBorderColor", "black"));
            for (Text text : texts) {
                text.setFill(CONVERTER.fromString(paneView.getModel().getLayoutPrefs().getPreference(
                        "boxTextColor", "black")));
                replaceCStyle(text, "-fx-font-family: ", paneView.getModel().getLayoutPrefs().getPreference(
                        "boxTextFontFamily", "Arial"));
            }
        }
    }

    /**
     * Replace the value of a style property.
     * @param node
     * @param property
     * @param newValue
     */
    private void replaceCStyle(Node node, String property, String newValue) {
        node.setStyle(node.getStyle().replaceAll(property + " [^;]*;", "") +
                property + newValue + ";");
    }

    /**
     * Creates the final VBox that will be used to represent a box-element.
     * The VBox consists of 3 components (VBoxes):
     * -Top:       Name
     * -Center:    Attributes
     * -Bottom:    Operations
     */
    private void createClassVBox() {
        // TOP
        VBox top = new VBox();
        Text name = new Text(box.getName());
        texts.add(name);
        name.getStyleClass().add("classname");
        top.getChildren().add(name);
        top.setAlignment(CENTER);
        top.setPadding(new Insets(7, 5, 7, 5));

        // CENTER
        VBox center = new VBox();
        for (Attribute attribute : box.getAttributes()) {
            Text content = new Text(MessageFormat.format(
                    "{0}{1} : {2}",
                    VISIBILITY.get(attribute.getVisibility()), attribute.getName(), attribute.getType()));
            content.setUnderline(attribute.getScope().equals("classifier"));
            center.getChildren().add(content);
            texts.add(content);
        }
        center.setAlignment(CENTER_LEFT);
        center.setPadding(new Insets(7, 5, 7, 5));
        center.getStyleClass().add("classVBoxCenter");

        // BOTTOM
        VBox bottom = new VBox();
        for (Operation operation : box.getOperations()) {
            String opattributes = "";
            StringBuilder sb = new StringBuilder();
            for (Attribute opattribuut : operation.getopAttributes()) {
                sb.append(", " + opattribuut.getName() + " : " + opattribuut.getType());
            }
            if (operation.getopAttributes().size() != 0) {
                opattributes = sb.toString().substring(2);
            }
            Text content = new Text(MessageFormat.format(
                    "{0}{1}({2}) : {3}",
                    VISIBILITY.get(operation.getVisibility()), operation.getName(), opattributes,
                    operation.getType()));
            content.setUnderline(operation.getScope().equals("classifier"));
            bottom.getChildren().add(content);
            texts.add(content);
        }
        bottom.setAlignment(CENTER_LEFT);
        bottom.setPadding(new Insets(7, 5, 7, 5));

        // FINAL VBox
        classVBox = new VBox(top, center, bottom);
        classVBox.getStyleClass().add("classVBox");

        // "Bind" VBox' width to box' width
        classVBox.widthProperty().addListener((v, oldValue, newValue) -> {
            box.setWidth(newValue.doubleValue());
        });

        makeDraggable();
        rightClickOptions();
    }

    /**
     * Enables users to move the VBox by dragging
     */
    private void makeDraggable() {
        classVBox.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                paneView.getScene().setCursor(Cursor.CLOSED_HAND);
            }
        });

        classVBox.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                paneView.getScene().setCursor(Cursor.DEFAULT);
            }
        });

        classVBox.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double deltaX = event.getSceneX() - mouseX;
                double deltaY = event.getSceneY() - mouseY;
                classVBox.relocate(classVBox.getLayoutX() + deltaX, classVBox.getLayoutY() + deltaY);
                box.setRow(box.getRow() + deltaY);
                box.setCol(box.getCol() + deltaX);
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
            }
        });
    }

    /**
     * Open contextmenu (with edit/remove options) on right click
     */
    private void rightClickOptions() {
        MenuItem edit = new MenuItem("Edit");
        MenuItem remove = new MenuItem("Remove");

        ContextMenu options = new ContextMenu(edit, remove);

        classVBox.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.SECONDARY) {
                    t.consume();
                    options.show(classVBox, t.getScreenX(), t.getScreenY());
                }
            }
        });

        remove.setOnAction((event) -> {
            boxEditor.removeBox();
        });

        edit.setOnAction((event) -> {
            boxEditor.editBox();
        });
    }

    public ArrayList<Text> getTexts() {
        return texts;
    }

    public BoxEditor getBoxEditor() {
        return boxEditor;
    }

    public VBox getClassVBox() {
        return classVBox;
    }
}