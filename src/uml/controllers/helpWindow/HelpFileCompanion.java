package uml.controllers.helpWindow;


import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Dries Marzougui
 *
 * Controller/companion for HelpFile.
 */
public class HelpFileCompanion {
    public Text textArea;

    /**
     * Puts text from "helpText.txt" in textArea.
     */
    public void initialize() {
        try {
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("helpText.txt")));
            String line = reader.readLine();
            while (line != null) {
                sb.append(line + "\n");
                line = reader.readLine();
            }
            textArea.setText(sb.toString());
            textArea.getStyleClass().add("textArea");
        } catch (IOException ex) {
            throw new RuntimeException("Couldn't load the help-file", ex);
        }
    }
}
