package uml.controllers.boxEditor;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import uml.controllers.boxEditor.layoutEditor.LayoutTab;
import uml.controllers.boxEditor.propertiesEditor.PropertiesTab;
import uml.model.BoxModel;
import uml.model.xml.Box;

import java.util.ArrayList;


public class BECompanion {
    public TabPane tabPane;
    public TextField nameTextField;
    public VBox attributeVBox;
    public VBox operationVBox;
    public VBox relationVBox;
    public Text errorText;
    public CheckBox checkBox;

    public ComboBox<String> bFontFamily;
    public ComboBox<Color> bBackgroundColor;
    public ComboBox<Color> bBorderColor;
    public ComboBox<Color> bTextColor;

    private BoxModel model;
    private Box box;
    private VBox vbox;
    private ArrayList<Text> texts;
    private LayoutTab layoutTab;
    private PropertiesTab propertiesTab;

    public BECompanion(BoxModel model, Box box, VBox vbox, ArrayList<Text> texts) {
        this.model = model;
        this.box = box;
        this.vbox = vbox;
        this.texts = texts;
    }

    public void initialize() {
        propertiesTab = new PropertiesTab(box, model, this);
        layoutTab = new LayoutTab(model.getPaneView(), this);
    }

    //PROPERTIES TAB
    public void savePropertyChanges() {
        if (propertiesTab.saveChanges()) {
            close();
        }
    }

    /**
     * Save the provided properties and swap to the layout pane.
     */
    public void saveAndSwap() {
        if (propertiesTab.saveChanges()) {
            tabPane.getSelectionModel().select(1);
        }
    }

    /**
     * Used to close the window
     */
    public void close() {
        nameTextField.getScene().getWindow().hide();
    }

    public void newAttribute() {
        propertiesTab.newAttribute();
    }

    public void newOperation() {
        propertiesTab.newOperation();
    }

    public void newRelation() {
        propertiesTab.newRelation();
    }

    //LAYOUT TAB
    public void saveLayoutChanges() {
        layoutTab.saveChanges();
        close();
    }

    public void saveAll() {
        if (propertiesTab.saveChanges()) {
            layoutTab.saveChanges();
            close();
        }
    }

    public BoxModel getModel() {
        return model;
    }

    public Box getBox() {
        return box;
    }

    public VBox getVbox() {
        return vbox;
    }

    public ArrayList<Text> getTexts() {
        return texts;
    }

    public void setVbox(VBox vbox) {
        this.vbox = vbox;
    }
}