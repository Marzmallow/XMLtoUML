package uml.guifactories.arrowHeads;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import uml.model.xml.subelements.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dries Marzougui
 *
 * Creates the arrowheads for relationtypes 'inheritance' and 'realization'
 */
public class TriangleHead extends ArrowHeadFactory {

    public TriangleHead(Line body, VBox vbox, Relation relation) {
        super(body, vbox, relation);
    }

    public List<Node> createHead() {
        double[][] headcords = getHeadCords();
        List<Node> output = new ArrayList<>();

        Polygon triangle = new Polygon(new double[]{
                headcords[0][0], headcords[0][1],
                headcords[1][0], headcords[1][1],
                headcords[2][0], headcords[2][1],
        });
        triangle.getStyleClass().add("triangle");
        output.add(triangle);
        return output;
    }
}
