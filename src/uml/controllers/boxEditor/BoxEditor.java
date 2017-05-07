package uml.controllers.boxEditor;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import uml.model.BoxModel;
import uml.model.xml.Box;
import uml.model.xml.subelements.Relation;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Dries Marzougui
 *
 * Editor used to change the selected box' properties and its layout.
 */
public class BoxEditor  {

    private Box box;
    private BoxModel model;
    private VBox vbox;
    private ArrayList<Text> texts;
    private boolean isCreator;

    public BoxEditor(Box box, VBox vbox, ArrayList<Text> texts) {
        this.box = box;
        this.vbox = vbox;
        this.texts = texts;
        isCreator = false;
    }

    public void setModel(BoxModel model) {
        this.model = model;
    }

    public void removeBox() {
        //Remove the box
        model.getListOfBoxes().remove(box);

        //Remove all references to the removed box
        for (Box box : model.getListOfBoxes()) {
            for (Iterator<Relation> iterator = box.getRelations().iterator(); iterator.hasNext(); ) {
                Relation relation = iterator.next();
                if (relation.getWith().equals(this.box.getName())) {
                    iterator.remove();
                }
            }
        }
        model.fireInvalidationEvent();
    }

    public void removeArrow(Relation relation) {
        box.getRelations().remove(relation);
        model.fireInvalidationEvent();
    }

    /**
     * Uses the editor to edit an existing class.
     */
    public void editBox() {
        createEditor("BoxEditor: " + box.getName());
    }

    /**
     * Uses the editor to create a new class(-> box)
     * @param x
     * @param y
     */
    public void createBox(double x, double y) {
        isCreator = true;
        box.setName("");
        box.setRow(x);
        box.setCol(y);
        model.getListOfBoxes().add(box);
        model.fireInvalidationEvent();
        vbox = model.getPaneView().getBoxToVBoxFact().get(box).getClassVBox();
        createEditor("Creator");
    }

    private void createEditor(String title) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("editor.fxml")
            );
            loader.setController(new BECompanion(model, box, vbox, texts));
            Parent root = loader.load();
            Stage stage = new Stage();

            stage.setResizable(false);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            stage.setOnHiding(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    if (isCreator && box.getName().equals("")) {
                        removeBox();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}