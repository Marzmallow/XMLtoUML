package uml.guifactories;

import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import uml.controllers.boxEditor.BoxEditor;
import uml.guifactories.arrowHeads.ArrowHeadFactory;
import uml.guifactories.arrowHeads.LineHead;
import uml.guifactories.arrowHeads.RhombusHead;
import uml.guifactories.arrowHeads.TriangleHead;
import uml.model.xml.subelements.Relation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Dries Marzougui
 * Visualizes a relationship between two (V)boxes by creating an arrow.
 */

public class ArrowFactory {

    private VBox startbox;
    private VBox endbox;
    private Relation relation;
    private Map<String, ArrowHeadFactory> headMap;
    private Line body;
    private List<Node> head;
    private BoxEditor boxEditor;

    public ArrowFactory(VBox startbox, VBox endbox, Relation relation) {
        this.startbox = startbox;
        this.endbox = endbox;
        this.relation = relation;

        // Create the arrow body
        createArrowBody();


        // Types of relations ~ arrowheads
        headMap = new HashMap<>();
        headMap.put("association", new LineHead(body, endbox, relation));
        headMap.put("inheritance", new TriangleHead(body, endbox, relation));
        headMap.put("realization", new TriangleHead(body, endbox, relation));
        headMap.put("dependency", new LineHead(body, endbox, relation));
        headMap.put("aggregation", new RhombusHead(body, endbox, relation));
        headMap.put("composition", new RhombusHead(body, endbox, relation));

        // Create the arrowhead
        calculateHead();
    }

    public Line getBody() {
        return body;
    }

    public List<Node> getHead() {
        return head;
    }

    public void calculateHead() {
        head = headMap.get(relation.getType()).createHead();
        for (Node node : head) {
            addContextMenu(node);
        }
    }

    /**
     * Creates the body of the arrow.
     * (We add 7 to the heights to account for the padding.
     */
    private void createArrowBody() {
        body = new Line();

        // Start of the body = Center point of the startbox
        Bounds startbounds = startbox.getBoundsInParent();
        body.setStartX(startbounds.getMinX() + (startbounds.getWidth() / 2));
        body.setStartY(startbounds.getMinY() + ((startbounds.getHeight() + 7) / 2));

        // Bind start of body to center of startbox.
        startbox.boundsInParentProperty().addListener((v, oldValue, newValue) -> {
            body.setStartX(newValue.getMinX() + (newValue.getWidth() / 2));
            body.setStartY(newValue.getMinY() + ((newValue.getHeight() + 7) / 2));
        });

        // End of the body = Center point of the endbox
        Bounds endbounds = endbox.getBoundsInParent();
        body.setEndX(endbounds.getMinX() + (endbounds.getWidth() / 2));
        body.setEndY(endbounds.getMinY() + ((endbounds.getHeight() + 7) / 2));

        // Bind end of body to center of the endbox.
        endbox.boundsInParentProperty().addListener((v, oldValue, newValue) -> {
            body.setEndX(newValue.getMinX() + (newValue.getWidth() / 2));
            body.setEndY(newValue.getMinY() + ((newValue.getHeight() + 7) / 2));
        });

        //Check whether it has to be a dotted line.
        if (relation.getType().equals("realization") || relation.getType().equals("dependency")) {
            body.getStrokeDashArray().addAll(15d, 6d);
        }

        addContextMenu(body);
    }

    /**
     * Adds context menu to node and shows it on right click.
     * Gives the option to remove/edit the box.
     * @param node
     */
    private void addContextMenu(Node node) {
        //ContextMenu on right click
        MenuItem edit = new MenuItem("Edit");
        MenuItem remove = new MenuItem("Remove");

        ContextMenu options = new ContextMenu(edit, remove);
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.SECONDARY) {
                    t.consume();
                    options.show(node, t.getScreenX(), t.getScreenY());
                }
            }
        });

        remove.setOnAction((event) -> {
            boxEditor.removeArrow(relation);
        });

        edit.setOnAction((event) -> {
            boxEditor.editBox();
        });
    }

    public void setBoxEditor(BoxEditor boxEditor) {
        this.boxEditor = boxEditor;
    }
}