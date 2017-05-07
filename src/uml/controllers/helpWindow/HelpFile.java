package uml.controllers.helpWindow;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Dries Marzougui
 *
 * Opens instruction manual.
 */
public class HelpFile {

    public HelpFile() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("helpFile.fxml")
            );
            loader.setController(new HelpFileCompanion());
            Parent root = loader.load();
            Stage stage = new Stage();

            stage.setResizable(false);
            stage.setTitle("Instruction manual");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
