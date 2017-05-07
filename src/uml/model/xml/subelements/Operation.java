package uml.model.xml.subelements;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Dries Marzougui
 * Represents one operation-element.
 */

@XmlRootElement(name = "operation")
public class Operation extends Attribute {

    private List<Attribute> opAttributes = new ArrayList<>();

    @XmlElement(name = "attribute")
    public List<Attribute> getopAttributes() {
        return opAttributes;
    }
    public void setOpAttributes(List<Attribute> opAttributes) {
        this.opAttributes = opAttributes;
    }
}
