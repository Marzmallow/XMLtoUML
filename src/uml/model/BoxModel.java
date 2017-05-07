package uml.model;

import uml.controllers.boxEditor.layoutEditor.BoxPreferences;
import uml.model.parsers.BoxReader;
import uml.model.xml.Box;
import uml.views.PaneView;

import java.io.File;
import java.util.*;

/**
 * @author Dries Marzougui
 * Model that holds the current list of boxes, preferences and visibility/scope/relation types.
 * Fires invalidation event that visualizes the boxes in the PaneView.
 */

public class BoxModel extends SuperObservable {

    private List<Box> listOfBoxes = new ArrayList<>();
    private BoxPreferences prefs = new BoxPreferences();
    private PaneView paneView;
    private HashMap<String, HashMap<String, String>> customLayout = new HashMap<>();
    private final HashSet<String> VISIBILITY = new HashSet<>(Arrays.asList("public", "private", "protected",
            "derived", "package"));
    private final HashSet<String> SCOPE = new HashSet<>(Arrays.asList("classifier", "instance"));
    private final HashSet<String> RELTYPES = new HashSet<>(Arrays.asList("association", "inheritance",
            "realization", "dependency", "aggregation", "composition"));

    /**
     * Fetches the current list of boxes.
     *
     * @return listOfBoxes
     */
    public List<Box> getListOfBoxes() {
        return listOfBoxes;
    }

    public List<String> getListOfNames() {
        List<String> names = new ArrayList<>();
        for (Box box : listOfBoxes) {
            names.add(box.getName());
        }
        return names;
    }

    /**
     * Retrieves list of boxes from an XML-file using uml.model.parsers.BoxReader's method 'readBoxes'.
     *
     * @param resource
     */
    public void loadListOfBoxes(File resource) {
        setListOfBoxes(new BoxReader().readBoxes(resource));
    }

    /**
     * Sets the given list of boxes provided that it differs from the current one.
     *
     * @param list
     */
    public void setListOfBoxes(List<Box> list) {
        if (list != null) {
            this.listOfBoxes = list;
            fireInvalidationEvent();
        }
    }

    /**
     * Clears the diagram/current list of boxes.
     */
    public void clearPane() {
        listOfBoxes.clear();
        fireInvalidationEvent();
    }

    public BoxPreferences getLayoutPrefs() {
        return prefs;
    }

    public HashMap<String, HashMap<String, String>> getCustomLayout() {
        return customLayout;
    }

    public void setPaneView(PaneView paneView) {
        this.paneView = paneView;
    }

    public PaneView getPaneView() {
        return paneView;
    }

    public HashSet<String> getVISIBILITY() {
        return VISIBILITY;
    }

    public HashSet<String> getSCOPE() {
        return SCOPE;
    }

    public HashSet<String> getRELTYPES() {
        return RELTYPES;
    }
}
