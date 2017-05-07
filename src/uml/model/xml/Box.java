package uml.model.xml;


import uml.model.xml.subelements.Attribute;
import uml.model.xml.subelements.Operation;
import uml.model.xml.subelements.Relation;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Dries Marzougui.
 * Represents one Box-element.
 */

@XmlRootElement(name = "box")
public class Box {

    private List<Relation> relation = new ArrayList<>();
    private List<Attribute> attribute = new ArrayList<>();
    private List<Operation> operation = new ArrayList<>();
    private String name;
    private double col;
    private double row;
    private double width;

    @XmlAttribute
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @XmlAttribute(name = "col")
    public double getCol() {
        return col;
    }
    public void setCol(double col) {
        this.col = col;
    }


    @XmlAttribute(name = "row")
    public double getRow() {
        return row;
    }
    public void setRow(double row) {
        this.row = row;
    }


    @XmlAttribute(name = "width")
    public double getWidth() {
        return width;
    }
    public void setWidth(double width) {
        this.width = width;
    }

    @XmlElement(name = "attribute")
    public List<Attribute> getAttributes() {
        return attribute;
    }
    public void setAttributes(List<Attribute> attributes) {
        this.attribute = attributes;
    }

    @XmlElement(name = "relation")
    public List<Relation> getRelations() {
        return relation;
    }
    public void setRelations(List<Relation> relation) {
        this.relation = relation;
    }


    @XmlElement(name = "operation")
    public List<Operation> getOperations() {
        return operation;
    }
    public void setOperations(List<Operation> operation) {
        this.operation = operation;
    }

}
