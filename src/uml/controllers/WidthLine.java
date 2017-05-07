package uml.controllers;

import javafx.scene.Cursor;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

/**
 * @author Dries Marzougui
 *
 * Transparent line that can only be moved horizontally.
 * Used to change the VBox' (->box') width.
 */
public class WidthLine extends Line {

    private double mouseX;

    public WidthLine(VBox vbox) {
        setStartX(vbox.getBoundsInParent().getMaxX());
        setStartY(vbox.getBoundsInParent().getMinY());
        setEndX(vbox.getBoundsInParent().getMaxX());
        setEndY(vbox.getBoundsInParent().getMaxY());

        // "Bind" line to VBox' right border.
        vbox.boundsInParentProperty().addListener( (v, oldValue, newValue) -> {
            setStartX(newValue.getMaxX());
            setStartY(newValue.getMinY());
            setEndX(newValue.getMaxX());
            setEndY(newValue.getMaxY());
        });

        // Draggable + custom cursor
        setOnMouseEntered(event -> {
            getScene().setCursor(Cursor.H_RESIZE);
        });

        setOnMouseExited(event -> {
            getScene().setCursor(Cursor.DEFAULT);
        });

        setOnMousePressed(event -> {
            mouseX = event.getSceneX() ;
        });

        setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - mouseX ;
            setStartX(getStartX()+deltaX);
            setEndX(getStartX()+deltaX);
            mouseX = event.getSceneX() ;
            // changes the VBox' width
            vbox.setPrefWidth(vbox.getPrefWidth()+deltaX);
        });

        setStrokeWidth(10);
        setStyle("-fx-stroke: transparent");
    }
}
