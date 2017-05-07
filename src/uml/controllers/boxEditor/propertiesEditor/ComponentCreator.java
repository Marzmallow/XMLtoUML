package uml.controllers.boxEditor.propertiesEditor;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uml.model.BoxModel;
import uml.model.xml.Box;
import uml.model.xml.subelements.Attribute;
import uml.model.xml.subelements.Operation;
import uml.model.xml.subelements.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dries Marzougui
 *
 * Class that creates the components for the propertiestab.
 */
public class ComponentCreator {
    private Box box;
    private BoxModel model;
    private PropertiesTab companion;
    private static final int TEXTFIELDWIDTH = 92;

    public ComponentCreator(Box box, BoxModel model, PropertiesTab companion) {
        this.box = box;
        this.model = model;
        this.companion = companion;

    }

    /**
     * Creates a HBox that represents one attribute in the editor's propertiestab
     * @param attribute
     * @return
     */
    public HBox createAttributeHBox(Attribute attribute) {
        ComboBox<String> scope = new ComboBox<>();
        scope.getSelectionModel().select(attribute.getScope());
        scope.getItems().addAll(model.getSCOPE());

        ComboBox<String> visibility = new ComboBox<>();
        visibility.getSelectionModel().select(attribute.getVisibility());
        visibility.getItems().addAll(model.getVISIBILITY());

        TextField name = new TextField(attribute.getName());
        TextField type = new TextField(attribute.getType());

        HBox hbox = new HBox(scope, visibility, name, type);
        scope.setMinWidth(TEXTFIELDWIDTH);
        visibility.setMinWidth(TEXTFIELDWIDTH);
        name.setMinWidth(TEXTFIELDWIDTH);
        type.setMinWidth(TEXTFIELDWIDTH);
        hbox.getStyleClass().add("componentHBox");
        return hbox;
    }

    /**
     * Creates a VBox that represents one operation in the editor's propertiestab
     * @param operation
     * @return
     */
    public VBox createOperationVBox(Operation operation) {
        HBox opHBox = createAttributeHBox(operation);
        VBox vbox = new VBox(opHBox);
        opHBox.getChildren().add(createRemoveButton(vbox, companion.getOperationBoxes(), companion.getOperationVBox()));

        for (Attribute opAttribute : operation.getopAttributes()) {
            vbox.getChildren().add(createOpABox(opAttribute, vbox));
        }

        Label addAttribute = new Label("add attribute");
        addAttribute.setPadding(new Insets(0,10,0,0));
        addAttribute.setOnMouseClicked((event) -> {
            vbox.getChildren().add(vbox.getChildren().size() - 1, createOpABox(new Attribute(), vbox));
        });
        HBox addAttriBox = new HBox(addAttribute);
        addAttriBox.setAlignment(Pos.CENTER_RIGHT);
        vbox.getChildren().add(addAttriBox);

        return vbox;
    }

    /**
     * Creates a HBox that represents one operation's attribute in the editor's propertiestab
     * @param opAttribute
     * @param vbox
     * @return
     */
    public HBox createOpABox(Attribute opAttribute, VBox vbox) {
        Label nameLabel = new Label("name: ");
        Label typeLabel = new Label("type: ");
        HBox opAbox = new HBox(nameLabel, new TextField(opAttribute.getName()),
                typeLabel, new TextField(opAttribute.getType()));
        opAbox.getChildren().add(createRemoveButton(opAbox, null, vbox));
        opAbox.getStyleClass().add("componentHBox");
        opAbox.setAlignment(Pos.CENTER_RIGHT);
        return opAbox;
    }

    public HBox createRelationHBox(Relation relation) {
        HBox hbox = new HBox();

        ComboBox<String> type = new ComboBox<>();
        type.getSelectionModel().select(relation.getType());
        type.getItems().addAll(model.getRELTYPES());

        ComboBox<String> with = new ComboBox<>();
        with.getSelectionModel().select(relation.getWith());
        List<String> names = model.getListOfNames();
        names.remove(box.getName());
        with.getItems().addAll(names);

        hbox.getChildren().addAll(type, with);
        hbox.getChildren().add(createRemoveButton(hbox, companion.getRelationBoxes(), companion.getRelationVBox()));
        hbox.getStyleClass().add("componentHBox");
        hbox.setAlignment(Pos.CENTER);
        return hbox;
    }

    /**
     * Creates the remove button that's added to every HBox.
     * @param box
     * @param list
     * @param vbox
     * @param <T>
     * @return
     */
    public <T> Button createRemoveButton(T box, ArrayList<T> list, VBox vbox) {
        Button removeButton = new Button();
        removeButton.setOnAction((event) -> {
            vbox.getChildren().remove(box);
            if(list != null) {
                list.remove(box);
            }
        });
        removeButton.getStyleClass().add("removebutton");
        return removeButton;
    }
}
