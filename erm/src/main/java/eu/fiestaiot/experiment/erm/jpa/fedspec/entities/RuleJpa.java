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
 *         &lt;element ref="{http://www.fiesta-iot.eu/fedspec}ruleDefinition"/>
 *         &lt;element ref="{http://www.fiesta-iot.eu/fedspec}domainKnowledge"/>
 *         &lt;element ref="{http://www.fiesta-iot.eu/fedspec}quantityKind"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@Entity
@Table( name = "rule" )
public class RuleJpa {


    protected String ruleDefinition;

    protected String domainKnowledge;

    @ElementCollection
    protected List<String> quantityKind;

    
    protected String name;
    
    
    @Id
    @GeneratedValue(generator="ruleJpaID")
    @GenericGenerator(name="ruleJpaID", strategy = "guid")
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
     * Gets the value of the ruleDefinition property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRuleDefinition() {
        return ruleDefinition;
    }

    /**
     * Sets the value of the ruleDefinition property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRuleDefinition(String value) {
        this.ruleDefinition = value;
    }

    /**
     * Gets the value of the domainKnowledge property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDomainKnowledge() {
        return domainKnowledge;
    }

    /**
     * Sets the value of the domainKnowledge property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDomainKnowledge(String value) {
        this.domainKnowledge = value;
    }

    /**
     * Gets the value of the quantityKind property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the quantityKind property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getQuantityKind().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getQuantityKind() {
        if (quantityKind == null) {
            quantityKind = new ArrayList<String>();
        }
        return this.quantityKind;
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
