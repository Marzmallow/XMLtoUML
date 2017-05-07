package uml.model.parsers;

import uml.model.xml.Box;
import uml.model.xml.Diagram;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

/**
 * @author Dries Marzougui
 * Reads the XML-file and converts it to a list of boxes.
 */
public class BoxReader {
    public List<Box> readBoxes(File resource) {
        try {
            JAXBContext jc = JAXBContext.newInstance(Diagram.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Diagram dia = (Diagram) unmarshaller.unmarshal(resource);
            return dia.getList();
        } catch (JAXBException ex) {
            throw new RuntimeException("Could not read file: " + resource, ex);
        }
    }
}
