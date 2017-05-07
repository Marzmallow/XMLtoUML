package uml.controllers.saveFileReminder;

import javafx.application.Platform;
import javafx.scene.control.ChoiceBox;
import uml.UmlCompanion;

/**
 * @author Dries Marzougui
 *
 * Companion/controller class for SaveFileReminder.
 *
 */
public class SFRCompanion {

    public ChoiceBox<String> saveOptions;
    private UmlCompanion umlCompanion;
    private boolean cancel = false;

    public void initialize() {
    }

    public boolean getCancel() {
        return cancel;
    }

    public void setUMLCompanion(UmlCompanion umlCompanion) {
        this.umlCompanion = umlCompanion;
    }

    /**
     * Action for 'Cancel' button.
     */
    public void noSave() {
        Platform.exit();
    }

    /**
     * Action for 'Save' button.
     * Saves in the selected format (xml/png).
     */
    public void doSave() {
        if (saveOptions.getValue().equals("as XML")) {
            umlCompanion.saveAsXML();
        } else {
            umlCompanion.saveAsScreenshot();
        }
        try {
            umlCompanion.getModel().getLayoutPrefs().getPrefs().flush();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to store preferences: " + ex);
        }
        noSave();
    }

    public void cancelClose() {
        cancel = true;
        saveOptions.getScene().getWindow().hide();
    }
}
