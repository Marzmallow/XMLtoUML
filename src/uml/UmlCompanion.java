package uml;

import javafx.application.Platform;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import uml.controllers.helpWindow.HelpFile;
import uml.controllers.boxEditor.BoxEditor;
import uml.model.BoxModel;
import uml.model.parsers.XMLConverter;
import uml.model.xml.Box;
import uml.model.xml.Diagram;
import uml.views.PaneView;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * @author Dries Marzougui
 *
 * Companion/controller class.
 *
 * Contains the actions connected with the menuitems.
 * Contains the method for taking a snapshot.
 */

public class UmlCompanion {

    public BoxModel model;
    public PaneView paneView;
    public BorderPane borderPane;
    private File xmlFile;
    private File screenShotFile;
    private double mouseX;
    private double mouseY;

    public void initialize() {
        model = new BoxModel();
        paneView.setModel(model);
        model.setPaneView(paneView);

        rightClickOptions();

        if (xmlFile != null) {
            useFile(xmlFile);
        }
    }

    public void setScreenShotFile(File screenShotFile) {
        this.screenShotFile = screenShotFile;
    }

    public void setXmlFile(File argFile) {
        if (argFile != null) {
            this.xmlFile = argFile;
        }
    }

    public File getXmlFile() {
        return xmlFile;
    }


    /**
     * Contextmenu on right click
     */
    private void rightClickOptions() {
        //ContextMenu on right click
        MenuItem newClass = new MenuItem("New class");
        ContextMenu options = new ContextMenu(newClass);
        paneView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                if (t.getButton() == MouseButton.SECONDARY) {
                    mouseX = t.getX();
                    mouseY = t.getY();
                    options.show(paneView, t.getScreenX(), t.getScreenY());
                }
                else {
                    // Close contextmenu on left click
                    options.hide();
                }
            }
        });

        newClass.setOnAction((event) -> {
            BoxEditor boxEditor = new BoxEditor(new Box(), null, null);
            boxEditor.setModel(model);
            boxEditor.createBox(mouseY, mouseX);
        });
    }

    /**
     * Clears the screen(pane).
     */
    public void closeDiagram() {
        model.clearPane();
    }

    /**
     * Action for menuitem 'File > Open'.
     * Enables the user to choose a file.
     */
    public void openFile() {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            useFile(file);
        }
    }

    /**
     * Action for menuitem 'File > Exit'.
     * Exits the program.
     */
    public void exitProgram() {
        Platform.exit();
    }

    /**
     * Passes the XML-file to the model.
     */
    public void useFile(File file) {
        setXmlFile(file);
        model.loadListOfBoxes(file);
    }

    /**
     * Action for menuitem 'Save as > Screenshot'
     * Saves a screenshot of the diagram.
     */
    public void saveAsScreenshot() {
        FileChooser filechooser = new FileChooser();

        // Set extension filter ~ PNG-files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "PNG files (*.png)", "*.png");
        filechooser.getExtensionFilters().add(extFilter);

        File file = filechooser.showSaveDialog(null);
        if (file != null) {
            setScreenShotFile(file);
            saveAsPng();
        }
    }

    /**
     * Action for menuitem 'Save as > XML-file'
     * Saves the diagram as a XML-file.
     */
    public void saveAsXML() {
        FileChooser filechooser = new FileChooser();

        // Set extension filter ~ PNG-files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        filechooser.getExtensionFilters().add(extFilter);

        File file = filechooser.showSaveDialog(null);
        if (file != null) {
            Diagram diagram = new Diagram();
            diagram.setList(model.getListOfBoxes());
            new XMLConverter().convertToXML(diagram, file);
        }
    }

    /**
     * Snapshot method.
     * Sets borderPanes width and height to fit the full scheme without changing the
     * pop-up windows size.
     */
    public void saveAsPng() {
        borderPane.setMinWidth(borderPane.getBoundsInParent().getWidth() + 50);
        borderPane.setMinHeight(borderPane.getBoundsInLocal().getHeight() + 50);
        borderPane.autosize();
        WritableImage image = new WritableImage((int) borderPane.getBoundsInParent().getWidth(),
                (int) borderPane.getBoundsInLocal().getHeight());
        borderPane.snapshot(new SnapshotParameters(), image);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", screenShotFile);
        } catch (IOException e) {
            throw new RuntimeException("Having trouble taking the snapshot.", e);
        }
    }

    /**
     * Action for menuitem 'Help > Open help'
     * Opens instruction manual.
     */
    public void openHelp() {
        new HelpFile();
    }

    public BoxModel getModel() {
        return model;
    }
}
