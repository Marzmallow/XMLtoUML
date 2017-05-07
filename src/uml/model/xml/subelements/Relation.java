package uml.model.xml.subelements;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Dries Marzougui
 * Represents one relation-element.
 */
@XmlRootElement(name = "relation")
public class Relation {

    private String type;
    private String with;

    @XmlAttribute(name = "type")
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    @XmlAttribute(name = "with")
    public String getWith() {
        return with;
    }
    public void setWith(String with) {
        this.with = with;
    }

}
