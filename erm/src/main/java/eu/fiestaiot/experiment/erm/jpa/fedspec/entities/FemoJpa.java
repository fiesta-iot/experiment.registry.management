//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.10.26 at 03:07:39 �� EEST 
//


package eu.fiestaiot.experiment.erm.jpa.fedspec.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;


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
 *         &lt;element ref="{http://www.fiesta-iot.eu/fedspec}EDM" minOccurs="0"/>
 *         &lt;element ref="{http://www.fiesta-iot.eu/fedspec}domainOfInterest"/>
 *         &lt;element ref="{http://www.fiesta-iot.eu/fedspec}FISMO" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}NCName" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@Entity
@Table( name = "femo" )
public class FemoJpa {

    protected String description;
    
    //// TODO Check the proper definition for BLOB or TEXT for this field (currently with this definition it is set up as "longtext" in MySQL which is fine)
	@Column(length = 4000)
    protected String edm;
    
    @ElementCollection
    protected List<String> domainOfInterest;
    
    @ElementCollection
    @OneToMany(fetch=FetchType.EAGER,  mappedBy="femo", cascade = CascadeType.ALL, orphanRemoval = true)
    protected List<FismoJpa> fismo = new ArrayList<>();
    
    @Id
    @GeneratedValue(generator="femoID")
    @GenericGenerator(name="femoID", strategy = "guid")
    protected String id;
    
    protected String name;

    //======================================
    @ManyToOne
    private FEDSpecJpa fedspec;
    
    
    
    
    
    

    
    public void addFISMO(FismoJpa fismoJpa) {
    	fismo.add(fismoJpa);
    	fismoJpa.setFemoJpa(this);
    }
    
    
    /**
     * @param femoJpa
     */    
    public void removeFismoJpa(FismoJpa fismoJpa) {
    	fismoJpa.setFemoJpa(null);
        this.fismo.remove(fismoJpa);
    }
    
    
    
    
    
    public void setFEDSpecJpa(FEDSpecJpa fedSpecJpa) {
        this.fedspec = fedSpecJpa;
    }
    
    //======================================
    
    
    /**
     * @param femoJpa
     */    
    public void removeDomainOfInterest() {
    	domainOfInterest.clear();
    }
    
    
    
    


    
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
     * Gets the value of the edm property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEDM() {
        return edm;
    }

    /**
     * Sets the value of the edm property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEDM(String value) {
        this.edm = value;
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
     * Gets the value of the fismo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fismo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFISMO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FismoJpa }
     * 
     * 
     */
    public List<FismoJpa> getFISMO() {
        if (fismo == null) {
            fismo = new ArrayList<FismoJpa>();
        }
        return this.fismo;
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
