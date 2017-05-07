package uml.controllers.boxEditor.propertiesEditor;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import uml.controllers.boxEditor.BECompanion;
import uml.model.BoxModel;
import uml.model.xml.Box;
import uml.model.xml.subelements.Attribute;
import uml.model.xml.subelements.Operation;
import uml.model.xml.subelements.Relation;

import java.util.ArrayList;

/**
 * @author Dries Marzougui
 *
 * "Companion" for the 'properties'-tab.
 */
public class PropertiesTab {
    private Box box;
    private BoxModel model;
    private BECompanion companion;
    private ComponentCreator componentCreator;

    private ArrayList<HBox> attributeBoxes = new ArrayList<>();
    private ArrayList<VBox> operationBoxes = new ArrayList<>();
    private ArrayList<HBox> relationBoxes = new ArrayList<>();


    /**
     * Visualizes the currently present box' properties (name/attributes/operations/relations)
     * @param box
     * @param model
     * @param companion
     */
    public PropertiesTab(Box box, BoxModel model, BECompanion companion) {
        this.box = box;
        this.model = model;
        this.companion = companion;
        componentCreator = new ComponentCreator(box, model, this);

        //Name
        companion.nameTextField.setText(box.getName());

        //Attributes
        for (Attribute attribute : box.getAttributes()) {
            HBox attributeHBox = componentCreator.createAttributeHBox(attribute);
            attributeHBox.getChildren().add(componentCreator.createRemoveButton(attributeHBox, attributeBoxes, companion.attributeVBox));
            companion.attributeVBox.getChildren().add(attributeHBox);
            attributeBoxes.add(attributeHBox);
        }

        //Operations
        for (Operation operation : box.getOperations()) {
            VBox operVBox = componentCreator.createOperationVBox(operation);
            companion.operationVBox.getChildren().add(operVBox);
            operationBoxes.add(operVBox);
        }

        //Relations
        for (Relation relation : box.getRelations()) {
            HBox relationHBox = componentCreator.createRelationHBox(relation);
            companion.relationVBox.getChildren().add(relationHBox);
            relationBoxes.add(relationHBox);
        }
    }

    /**
     * Creates a new HBox that visualizes the properties of an attribute.
     */
    public void newAttribute() {
        HBox hbox = componentCreator.createAttributeHBox(new Attribute());
        hbox.getChildren().add(componentCreator.createRemoveButton(hbox, attributeBoxes, companion.attributeVBox));
        companion.attributeVBox.getChildren().add(hbox);
        attributeBoxes.add(hbox);
    }

    /**
     * Creates a new VBox that visualizes the properties of an operation.
     */
    public void newOperation() {
        VBox vbox = componentCreator.createOperationVBox(new Operation());
        companion.operationVBox.getChildren().add(vbox);
        operationBoxes.add(vbox);
    }

    /**
     * Creates a new HBox that visualizes the properties of a relation
     */
    public void newRelation() {
        HBox hbox = componentCreator.createRelationHBox(new Relation());
        companion.relationVBox.getChildren().add(hbox);
        relationBoxes.add(hbox);
    }

    /**
     * Saves the currently shown information by changing the box' properties.
     */
    public boolean saveChanges() {
        StringBuilder errorTips = new StringBuilder("Possible mistakes:\n");

        boolean validName = saveName(errorTips);
        boolean validAttributes = saveAttributes(errorTips);
        boolean validOperations = saveOperations(errorTips);
        boolean validRelations = saveRelations(errorTips);

        // Reload and quit editor
        if (validName && validAttributes && validOperations && validRelations) {
            model.fireInvalidationEvent();
            companion.setVbox(model.getPaneView().getBoxToVBoxFact().get(box).getClassVBox());
            return true;
        } else {
            errorTips.append("Please make sure that all empty fields are removed");
            companion.errorText.setText(errorTips.toString());
            return false;
        }
    }

    /**
     * Save name if different && adjust relations with this box as target.
     * @param errorTips
     */
    private boolean saveName(StringBuilder errorTips) {
        boolean validName = !companion.nameTextField.getText().trim().isEmpty();
        if (validName && !box.getName().equals(companion.nameTextField.getText())) {
            int index = 0;
            while (validName && index < model.getListOfBoxes().size()) {
                Box otherBox = model.getListOfBoxes().get(index);
                for (Relation relation : otherBox.getRelations()) {
                    if (relation.getWith().equals(box.getName())) {
                        relation.setWith(companion.nameTextField.getText());
                    }
                }
                if (otherBox != box && otherBox.getName().equals(companion.nameTextField.getText())) {
                    validName = false;
                }
                index++;
            }
        }
        if (validName) {
            box.setName(companion.nameTextField.getText());
        } else {
            companion.nameTextField.getStyleClass().add("ErrorTextField");
            errorTips.append("      - Invalid name");
            errorTips.append(System.lineSeparator());
        }
        return validName;
    }

    private boolean saveAttributes(StringBuilder errorTips) {
        ArrayList<Attribute> attributes = new ArrayList<>();
        boolean validAttributes = true;
        int index = 0;
        while (validAttributes && index < attributeBoxes.size()) {
            Attribute attribute = initAttribute(new Attribute(), attributeBoxes.get(index));
            attributes.add(attribute);
            if (!checkAttribute(attribute)) {
                validAttributes = false;
            } else {
                attribute.setScope(attribute.getScope().toLowerCase());
                attribute.setVisibility(attribute.getVisibility().toLowerCase());
                index++;
            }
        }
        if (validAttributes) {
            box.setAttributes(attributes);
        } else {
            attributeBoxes.get(index).getStyleClass().add("error");
            errorTips.append("      - Invalid attribute (index: " + index + ")");
            errorTips.append(System.lineSeparator());
        }
        return validAttributes;
    }

    private boolean saveOperations(StringBuilder errorTips) {
        ArrayList<Operation> operations = new ArrayList<>();
        boolean validOperations = true;
        int index = 0;
        while (validOperations && index < operationBoxes.size()) {
            VBox vbox = operationBoxes.get(index);
            Operation operation = initAttribute(new Operation(), (HBox) vbox.getChildren().get(0));
            if (!checkAttribute(operation)) {
                validOperations = false;
            } else {
                ArrayList<Attribute> opAttributes = new ArrayList<>();
                int i = 1;
                while (validOperations && i < vbox.getChildren().size() - 1) {
                    HBox hbox = (HBox) vbox.getChildren().get(i);
                    Attribute opAttribute = new Attribute();
                    opAttribute.setName(((TextField) hbox.getChildren().get(1)).getText());
                    opAttribute.setType(((TextField) hbox.getChildren().get(3)).getText());
                    if (!checkOpAttribute(opAttribute)) {
                        validOperations = false;

                    } else {
                        opAttributes.add(opAttribute);
                        i++;
                    }
                }
                operation.setOpAttributes(opAttributes);
                operations.add(operation);
                index++;
            }
        }
        if (validOperations) {
            box.setOperations(operations);
        } else {
            operationBoxes.get(index).getStyleClass().add("error");
            errorTips.append("      - Invalid operation (index: " + index + ")");
            errorTips.append(System.lineSeparator());
        }
        return validOperations;
    }

    public boolean saveRelations(StringBuilder errorTips) {
        ArrayList<Relation> relations = new ArrayList<>();
        boolean validRelations = true;
        int index = 0;
        while (validRelations && index < relationBoxes.size()) {
            HBox hbox = relationBoxes.get(index);
            Relation relation = new Relation();
            relation.setType(((ComboBox<String>) hbox.getChildren().get(0)).getValue());
            relation.setWith(((ComboBox<String>) hbox.getChildren().get(1)).getValue());
            if (relation.getType() == null || relation.getWith() == null) {
                validRelations = false;
            } else {
                relations.add(relation);
                index++;
            }
        }
        if (validRelations) {
            box.setRelations(relations);
        } else {
            relationBoxes.get(index).getStyleClass().add("error");
            errorTips.append("      - Invalid relation (index: " + index + ")\n");
        }
        return validRelations;
    }

    /**
     * Turns a HBox into an attribute/operation
     * @param type
     * @param hbox
     * @param <T>
     * @return
     */
    private <T extends Attribute> T initAttribute(T type, HBox hbox) {
        type.setScope(((ComboBox<String>) hbox.getChildren().get(0)).getValue());
        type.setVisibility(((ComboBox<String>) hbox.getChildren().get(1)).getValue());
        type.setName(((TextField) hbox.getChildren().get(2)).getText());
        type.setType(((TextField) hbox.getChildren().get(3)).getText());
        return type;
    }

    /**
     * Check's if the given attribute is valid.
     * @param attribute
     * @return
     */
    private boolean checkAttribute(Attribute attribute) {
        return attribute.getName() != null && attribute.getType() != null && attribute.getScope() != null
                && attribute.getVisibility() != null
                && !(attribute.getName().trim().isEmpty() || attribute.getScope().trim().isEmpty() ||
                attribute.getVisibility().trim().isEmpty() || attribute.getType().trim().isEmpty());
    }

    /**
     * Checks if the given operation's attribute is valid.
     * @param attribute
     * @return
     */
    private boolean checkOpAttribute(Attribute attribute) {
        return attribute.getName() != null && attribute.getType() != null
                && !(attribute.getName().trim().isEmpty() || attribute.getType().trim().isEmpty());
    }

    /**
     * Action for button > 'Close'
     */
    public void cancelChanges() {
        companion.attributeVBox.getScene().getWindow().hide();
    }

    public ArrayList<VBox> getOperationBoxes() {
        return operationBoxes;
    }

    public ArrayList<HBox> getAttributeBoxes() {
        return attributeBoxes;
    }

    public ArrayList<HBox> getRelationBoxes() {
        return relationBoxes;
    }


    public VBox getOperationVBox() {
        return companion.operationVBox;
    }

    public VBox getRelationVBox() {
        return companion.relationVBox;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }

    public BoxModel getModel() {
        return model;
    }

    public void setModel(BoxModel model) {
        this.model = model;
    }

}
