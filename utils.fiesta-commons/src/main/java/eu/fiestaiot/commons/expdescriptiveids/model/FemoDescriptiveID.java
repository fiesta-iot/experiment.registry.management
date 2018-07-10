//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.09.27 at 08:05:18 �� EEST 
//


package eu.fiestaiot.commons.expdescriptiveids.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.fiesta-iot.eu/fedspec}description" minOccurs="0"/>
 *         &lt;element ref="{http://www.fiesta-iot.eu/fedspec}domainOfInterest" minOccurs="0"/>
 *         &lt;element ref="{urn:fiestaiot:experiment:descriptiveids:xsd:1}FismoDescriptiveID" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "description",
    "domainOfInterest",
    "fismoDescriptiveID"
})
@XmlRootElement(name = "FemoDescriptiveID")
public class FemoDescriptiveID {

    @XmlElement(namespace = "http://www.fiesta-iot.eu/fedspec")
    protected String description;
    @XmlList
    @XmlElement(namespace = "http://www.fiesta-iot.eu/fedspec")
    protected List<String> domainOfInterest;
    @XmlElement(name = "FismoDescriptiveID", required = true)
    protected List<FismoDescriptiveID> fismoDescriptiveID;
    @XmlAttribute(name = "id")
    @XmlSchemaType(name = "anyURI")
    protected String id;
    @XmlAttribute(name = "name")
    protected String name;

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the domainOfInterest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the domainOfInterest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDomainOfInterest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getDomainOfInterest() {
        if (domainOfInterest == null) {
            domainOfInterest = new ArrayList<String>();
        }
        return this.domainOfInterest;
    }

    /**
     * Gets the value of the fismoDescriptiveID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fismoDescriptiveID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFismoDescriptiveID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FismoDescriptiveID }
     * 
     * 
     */
    public List<FismoDescriptiveID> getFismoDescriptiveID() {
        if (fismoDescriptiveID == null) {
            fismoDescriptiveID = new ArrayList<FismoDescriptiveID>();
        }
        return this.fismoDescriptiveID;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

}
