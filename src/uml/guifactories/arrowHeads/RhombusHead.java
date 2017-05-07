package uml.guifactories.arrowHeads;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import uml.model.xml.subelements.Relation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dries Marzougui
 *
 * Creates the arrowheads for relationtypes 'aggregation' and 'composition'
 */
public class RhombusHead extends ArrowHeadFactory {

    public RhombusHead(Line body, VBox vbox, Relation relation) {
        super(body, vbox, relation);
    }

    public List<Node> createHead() {
        double[] extrapunt = new double[2];
        double[][] headcords = getHeadCords();
        List<Node> output = new ArrayList<>();

        //Calculate the extra (fourth) cord
        double dx = body.getStartX() - body.getEndX();
        double dy = body.getStartY() - body.getEndY();
        double angle = Math.atan2(dy, dx);
        extrapunt[0] = Math.cos(angle) * (Math.cos(ARROWANGLE) * ARROWLENGTH) * 2 + headcords[0][0];
        extrapunt[1] = Math.sin(angle) * (Math.cos(ARROWANGLE) * ARROWLENGTH) * 2 + headcords[0][1];

        Polygon rhombus = new Polygon(new double[]{
                headcords[0][0], headcords[0][1],
                headcords[1][0], headcords[1][1],
                extrapunt[0], extrapunt[1],
                headcords[2][0], headcords[2][1]
        });

        if (relation.getType().equals("composition")) {
            rhombus.setStyle("-fx-fill: black;");
        }

        rhombus.getStyleClass().add("rhombus");
        output.add(rhombus);

        return output;
    }
}