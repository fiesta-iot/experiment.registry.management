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
 *         &lt;element ref="{http://www.fiesta-iot.eu/fedspec}FEMO" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="userID" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@Entity
@Table( name = "fedspec" )
public class FEDSpecJpa {

    @ElementCollection
    @OneToMany(fetch=FetchType.EAGER,  mappedBy="fedspec", cascade = CascadeType.ALL, orphanRemoval = true)  /* mappedBy="fedspec",*/
    protected List<FemoJpa> femo = new ArrayList<>();
    
    @Column(unique=true)
    protected String userID;

    @Id
    @GeneratedValue(generator="fedSpecID")
    @GenericGenerator(name="fedSpecID", strategy = "guid")
    protected String id;
    
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
     * Gets the value of the femo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the femo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFEMO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FemoJpa }
     * 
     * 
     */
    public List<FemoJpa> getFEMO() {
        if (femo == null) {
            femo = new ArrayList<FemoJpa>();
        }
        return this.femo;
    }

    //======================================
    
    public void addFEMO(FemoJpa femoJpa) {
    	femo.add(femoJpa);
    	femoJpa.setFEDSpecJpa(this);
    }
    
    
    /**
     * @param femoJpa
     */    
    public void removeFemoJpa(FemoJpa femoJpa) {
    	femoJpa.setFEDSpecJpa(null);
        this.femo.remove(femoJpa);
    }
    
    //======================================
    
    
    /**
     * Gets the value of the userID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the value of the userID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserID(String value) {
        this.userID = value;
    }

    
    
    
    
    
    


    
}
