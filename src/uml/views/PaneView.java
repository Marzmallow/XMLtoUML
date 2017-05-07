package uml.views;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import uml.controllers.WidthLine;
import uml.controllers.boxEditor.BoxEditor;
import uml.guifactories.ArrowFactory;
import uml.guifactories.VBoxFactory;
import uml.model.BoxModel;
import uml.model.xml.Box;
import uml.model.xml.subelements.Relation;

import java.util.HashMap;


/**
 * @author Dries Marzougui
 * The Pane to which we will add all the VBoxes.
 */
public class PaneView extends Pane implements InvalidationListener {

    private BoxModel model;
    private HashMap<Box, VBoxFactory> boxToVBoxFact = new HashMap<>();

    public PaneView() {
        getStyleClass().add("paneView");
    }

    /**
     * Sets the model. Must be called from within the initialize-method of the FXML-files companion.
     */
    public void setModel(BoxModel newModel) {
        if (newModel != model) {
            if (model != null) {
                model.removeListener(this);
            }
            model = newModel;
            if (model != null) {
                model.addListener(this);
            }
        }
    }

    /**
     * Getter is required to use the attribute 'model' inside the uml.fxml.
     *
     * @return BoxModel
     */
    public BoxModel getModel() {
        return model;
    }


    public PaneView(Node... children) {
        super(children);
    }

    /**
     * Visualizes the information that the model currently contains.
     * Adds VBoxes (boxes) to the Pane.
     * Adds Arrows (relations) to the Pane.
     *
     * @param o
     */
    @Override
    public void invalidated(Observable o) {
        getChildren().clear();
        boxToVBoxFact.clear();
        HashMap<String, VBox> nameToVBox = new HashMap<>();
        HashMap<Box, BoxEditor> boxToEditor = new HashMap<>();

        // Convert all boxes to VBoxes and adds them to the Pane.
        for (Box box : model.getListOfBoxes()) {
            Double width = box.getWidth();
            VBoxFactory vboxfactory = new VBoxFactory(box, this);
            vboxfactory.getBoxEditor().setModel(model);
            boxToEditor.put(box, vboxfactory.getBoxEditor());
            VBox vbox = vboxfactory.getClassVBox();

            vbox.setTranslateX(box.getCol());
            vbox.setTranslateY(box.getRow());
            vbox.setPrefWidth(width);
            getChildren().add(vbox);
            boxToVBoxFact.put(box, vboxfactory);
            vbox.toFront();

            layout();

            getChildren().add(new WidthLine(vbox));
            nameToVBox.put(box.getName(), vbox);
        }

        layout();
        applyCss();

        // Converts all relations to arrows and adds them to the Pane.
        for (Box box : model.getListOfBoxes()) {
            for (Relation relation : box.getRelations()) {
                ArrowFactory arrowfactory = new ArrowFactory(boxToVBoxFact.get(box).getClassVBox(),
                        nameToVBox.get(relation.getWith()), relation);
                arrowfactory.setBoxEditor(boxToEditor.get(box));
                Line body = arrowfactory.getBody();
                getChildren().add(body);
                body.toBack();

                getChildren().addAll(arrowfactory.getHead());

                // Recalculate arrowhead when body moves.
                body.boundsInParentProperty().addListener((v, oldValue, newValue) -> {
                    getChildren().removeAll(arrowfactory.getHead());
                    try {
                        arrowfactory.calculateHead();
                        getChildren().addAll(arrowfactory.getHead());
                    } catch (NullPointerException ex) {
                        // do nothing
                    }
                });
            }
        }

        getParent().prefWidth(getBoundsInParent().getWidth());
        getParent().prefHeight(getBoundsInParent().getHeight());

    }

    public HashMap<Box, VBoxFactory> getBoxToVBoxFact() {
        return boxToVBoxFact;
    }
}
