package uml.model.xml.subelements;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Dries Marzougui
 * Represents one attribute-element.
 * Is superclass for Operation.
 */

@XmlRootElement(name = "attribute")
public class Attribute {

    private String scope;
    private String visibility;
    private String name;
    private String type;

    @XmlAttribute
    public String getScope() {
        return scope;
    }
    public void setScope(String scope) {
        this.scope = scope;
    }

    @XmlAttribute
    public String getVisibility() {
        return visibility;
    }
    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
