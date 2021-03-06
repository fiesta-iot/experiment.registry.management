//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.10.26 at 03:07:39 �� EEST 
//


package eu.fiestaiot.experiment.erm.jpa.fedspec.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
 *         &lt;element name="discoverable" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element ref="{http://www.fiesta-iot.eu/fedspec}experimentControl"/>
 *         &lt;element ref="{http://www.fiesta-iot.eu/fedspec}experimentOutput"/>
 *         &lt;element ref="{http://www.fiesta-iot.eu/fedspec}queryControl" minOccurs="0"/>
 *         &lt;element name="service" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element ref="{http://www.fiesta-iot.eu/fedspec}rule" minOccurs="0"/>
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
@Table( name = "fismo" ) 
public class FismoJpa {

    protected String description;
    protected Boolean discoverable;
    
    @OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    protected ExperimentControlJpa experimentControl;
    
    @OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    protected ExperimentOutputJpa experimentOutput;
    
    @OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    protected QueryControlJpa queryControl;
    
    protected String service;
    
    @OneToOne(fetch=FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    protected RuleJpa rule;
    
    @Id
    @GeneratedValue(generator="fismoJpaID")
    @GenericGenerator(name="fismoJpaID", strategy = "guid")
    protected String id;
    
    protected String name;
    
    //======================================
    @ManyToOne
    private FemoJpa femo;

    
    public void setFemoJpa(FemoJpa femoJpa) {
        this.femo = femoJpa;
    }
    //======================================
    
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
     * Gets the value of the discoverable property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isDiscoverable() {
        return discoverable;
    }

    /**
     * Sets the value of the discoverable property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDiscoverable(Boolean value) {
        this.discoverable = value;
    }

    /**
     * Gets the value of the experimentControl property.
     * 
     * @return
     *     possible object is
     *     {@link ExperimentControlJpa }
     *     
     */
    public ExperimentControlJpa getExperimentControl() {
        return experimentControl;
    }

    /**
     * Sets the value of the experimentControl property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExperimentControlJpa }
     *     
     */
    public void setExperimentControl(ExperimentControlJpa value) {
        this.experimentControl = value;
    }

    /**
     * Gets the value of the experimentOutput property.
     * 
     * @return
     *     possible object is
     *     {@link ExperimentOutputJpa }
     *     
     */
    public ExperimentOutputJpa getExperimentOutput() {
        return experimentOutput;
    }

    /**
     * Sets the value of the experimentOutput property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExperimentOutputJpa }
     *     
     */
    public void setExperimentOutput(ExperimentOutputJpa value) {
        this.experimentOutput = value;
    }

    /**
     * Gets the value of the queryControl property.
     * 
     * @return
     *     possible object is
     *     {@link QueryControlJpa }
     *     
     */
    public QueryControlJpa getQueryControl() {
        return queryControl;
    }

    /**
     * Sets the value of the queryControl property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryControlJpa }
     *     
     */
    public void setQueryControl(QueryControlJpa value) {
        this.queryControl = value;
    }

    /**
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getService() {
        return service;
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setService(String value) {
        this.service = value;
    }

    /**
     * Gets the value of the rule property.
     * 
     * @return
     *     possible object is
     *     {@link RuleJpa }
     *     
     */
    public RuleJpa getRule() {
        return rule;
    }

    /**
     * Sets the value of the rule property.
     * 
     * @param value
     *     allowed object is
     *     {@link RuleJpa }
     *     
     */
    public void setRule(RuleJpa value) {
        this.rule = value;
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
