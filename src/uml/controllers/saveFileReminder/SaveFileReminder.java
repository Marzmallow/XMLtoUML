package uml.controllers.saveFileReminder;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uml.UmlCompanion;

/**
 * @author Dries Marzougui
 *
 * Reminds the users to save their changes / alerts that unsaved changes get lost.
 */
public class SaveFileReminder {
    private UmlCompanion umlCompanion;
    private SFRCompanion sfrCompanion;

    public SaveFileReminder(UmlCompanion companion) {
        umlCompanion = companion;
    }

    public void createDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("sfr.fxml")
            );
            SFRCompanion companion = new SFRCompanion();
            this.sfrCompanion = companion;
            companion.setUMLCompanion(umlCompanion);
            loader.setController(companion);
            Parent root = loader.load();
            Stage stage = new Stage();

            stage.setResizable(false);
            stage.setTitle("Save file reminder");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
