package uml.guifactories.arrowHeads;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import uml.model.xml.subelements.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dries Marzougui
 *
 * Creates the arrowheads for relationtypes 'association' and 'dependency'
 */
public class LineHead extends ArrowHeadFactory {

    public LineHead(Line body, VBox vbox, Relation relation) {
        super(body, vbox, relation);
    }

    public List<Node> createHead() {
        double[][] headcords = getHeadCords();
        List<Node> output = new ArrayList<>();
        output.add(new Line(headcords[0][0], headcords[0][1], headcords[1][0], headcords[1][1]));
        output.add(new Line(headcords[0][0], headcords[0][1], headcords[2][0], headcords[2][1]));
        return output;
    }
}
