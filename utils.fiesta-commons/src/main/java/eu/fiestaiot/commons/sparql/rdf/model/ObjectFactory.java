//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.2-147 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.14 at 03:34:41 �� EEST 
//


package eu.fiestaiot.commons.sparql.rdf.model;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the eu.fiestaiot.commons.sparql.rdf.model package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RDF_QNAME = new QName("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "RDF");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: eu.fiestaiot.commons.sparql.rdf.model
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/1999/02/22-rdf-syntax-ns#", name = "RDF")
    public JAXBElement<Object> createRDF(Object value) {
        return new JAXBElement<Object>(_RDF_QNAME, Object.class, null, value);
    }

}
