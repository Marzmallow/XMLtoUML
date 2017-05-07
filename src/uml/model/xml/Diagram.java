package uml.model.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Dries Marzougui
 * Class that corresponds with the root-element of the xml file.
 */

@XmlRootElement(name = "diagram")
public class Diagram {

    private List<Box> list;

    // Add wrapper
    @XmlElement(name="box")
    public List<Box> getList() { return list; }
    public void setList(List<Box> list) { this.list = list;}

}
