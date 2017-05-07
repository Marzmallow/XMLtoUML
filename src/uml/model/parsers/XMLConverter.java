package uml.model.parsers;

import uml.model.xml.Diagram;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

/**
 * @author Dries Marzougui
 * Converts the current diagram into an XML-file.
 */
public class XMLConverter {
    public void convertToXML(Diagram diagram, File saveTo) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Diagram.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(diagram, saveTo);
        }
        catch (JAXBException ex) {
            throw new RuntimeException("Could not convert to XML", ex);
        }
    }
}
