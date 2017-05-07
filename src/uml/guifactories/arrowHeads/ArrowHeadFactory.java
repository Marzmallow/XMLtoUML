package uml.guifactories.arrowHeads;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import uml.model.xml.subelements.Relation;

import java.util.List;

/**
 * @author Dries Marzougui
 *
 * Superclass for the different kinds of arrowheads.
 */
public abstract class ArrowHeadFactory {
    protected Line body;
    protected VBox endbox;
    protected Relation relation;
    protected static final double ARROWLENGTH = 20.0;
    protected static final double ARROWANGLE = Math.toRadians(30.0);

    public ArrowHeadFactory(Line body, VBox endbox, Relation relation) {
        this.body = body;
        this.endbox = endbox;
        this.relation = relation;
    }

    public abstract List<Node> createHead();

    /**
     * Gets the arrowheads top coordinates.
     * We compute this by checking which side(/edge) of the end VBox the body intersects.
     * Afterwards we can determine the arrowhead apex by calculating the intersection point
     * of the edge and the arrow body.
     *
     * @return double[]
     */
    public double[] getHeadApex() {
        Double[][] hoekpunten = new Double[][]{
                {endbox.getBoundsInParent().getMinX(), endbox.getBoundsInParent().getMinY()},   //LB
                {endbox.getBoundsInParent().getMaxX(), endbox.getBoundsInParent().getMinY()},   //RB
                {endbox.getBoundsInParent().getMaxX(), endbox.getBoundsInParent().getMaxY()},   //RO
                {endbox.getBoundsInParent().getMinX(), endbox.getBoundsInParent().getMaxY()}    //LO
        };
        boolean gevonden = false;

        double[] headApex = null;
        int index = 0;
        while (!gevonden && index < 4) {
            Line rand = new Line();
            rand.setStartX(hoekpunten[index][0]);
            rand.setStartY(hoekpunten[index][1]);
            rand.setEndX(hoekpunten[(index + 1) % 4][0]);
            rand.setEndY(hoekpunten[(index + 1) % 4][1]);
            if (rand.intersects(body.getBoundsInParent())) {
                headApex = getIntersection(rand, body);
                if (pointInShape(headApex, endbox.getBoundsInParent())) {
                    gevonden = true;
                }
            }
            index += 1;
        }
        return headApex;
    }

    /**
     * Check if a point (= double[] which contains the x,y coordinates) are within the bounds of a given shape.
     *
     * @param point
     * @param shape
     * @return boolean
     */
    private boolean pointInShape(double[] point, Bounds shape) {
        return shape.getMinX() <= point[0] && point[0] <= shape.getMaxX()
                && shape.getMinY() <= point[1] && point[1] <= shape.getMaxY();
    }


    /**
     * Calculate the intersection point (= double[] which contains the x,y coordinates) of two lines.
     *
     * @param one
     * @param two
     * @return
     */
    public double[] getIntersection(Line one, Line two) {
        double x1 = one.getStartX();
        double x2 = one.getEndX();
        double x3 = two.getStartX();
        double x4 = two.getEndX();
        double y1 = one.getStartY();
        double y2 = one.getEndY();
        double y3 = two.getStartY();
        double y4 = two.getEndY();
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;
        return new double[]{xi, yi};
    }

    /**
     * Calculate the remaining coordinates to form the arrowhead.
     *
     * @return double[][]
     */
    public double[][] getHeadCords() {
        double[][] headcords = new double[3][2];

        // Add apex
        double[] top = getHeadApex();
        headcords[0][0] = top[0];
        headcords[0][1] = top[1];

        // Calculate and add remaining 2 points
        double dx = body.getStartX() - body.getEndX();
        double dy = body.getStartY() - body.getEndY();
        double angle = Math.atan2(dy, dx);
        headcords[1][0] = Math.cos(angle + ARROWANGLE) * ARROWLENGTH + top[0];
        headcords[1][1] = Math.sin(angle + ARROWANGLE) * ARROWLENGTH + top[1];
        headcords[2][0] = Math.cos(angle - ARROWANGLE) * ARROWLENGTH + top[0];
        headcords[2][1] = Math.sin(angle - ARROWANGLE) * ARROWLENGTH + top[1];

        return headcords;
    }
}
