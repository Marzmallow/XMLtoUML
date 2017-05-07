package uml;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import uml.controllers.saveFileReminder.SaveFileReminder;

import java.io.File;
import java.util.List;

/**
 * @author Dries Marzougui
 * Main class.
 * This program makes it possible to visualize XML-files as class diagrams according to
 * the standards of the Unified Modeling Language (UML).
 *
 * The menubar gives the option to open a file and visualize it, close the current diagram and quit the program.
 */

public class Main extends Application {

    private UmlCompanion companion;
    private List<String> argList;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                Main.class.getResource("uml.fxml")
        );
        loader.setController(companion);
        Parent root = loader.load();

        primaryStage.setTitle("XML to UML");
        primaryStage.setScene(new Scene(root));
        primaryStage.getScene().getStylesheets().add(getClass().getResource("uml.css").toExternalForm());
        primaryStage.show();

        // Commandline - Snapshot procedure
        if (argList.size() == 2) {
            companion.setScreenShotFile(new File(argList.get(1)));
            companion.saveAsPng();
            companion.exitProgram();
        }

        // Save file reminder on close event
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                SaveFileReminder sfr = new SaveFileReminder(companion);
                sfr.createDialog();
                event.consume();
            }
        });

    }

    @Override
    public void init() {
        companion = new UmlCompanion();

        // Commandline - Open/Use File Procedure
        argList = getParameters().getRaw();
        if (argList.size() >= 1) {
            companion.setXmlFile(new File(argList.get(0)));
        }
    }

    public static void main(String[] args) {
        if (args.length > 2) {
            throw new IllegalArgumentException("Exceeded maximum number of arguments (2).\nYou gave " + args.length + "!");
        } else {
            launch(args);
        }
    }
}
