
package de.msiggi.sportsdata.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


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
 *         &lt;element name="GetLastChangeDateByLeagueSaisonResult" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "getLastChangeDateByLeagueSaisonResult"
})
@XmlRootElement(name = "GetLastChangeDateByLeagueSaisonResponse")
public class GetLastChangeDateByLeagueSaisonResponse {

    @XmlElement(name = "GetLastChangeDateByLeagueSaisonResult", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar getLastChangeDateByLeagueSaisonResult;

    /**
     * Gets the value of the getLastChangeDateByLeagueSaisonResult property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getGetLastChangeDateByLeagueSaisonResult() {
        return getLastChangeDateByLeagueSaisonResult;
    }

    /**
     * Sets the value of the getLastChangeDateByLeagueSaisonResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setGetLastChangeDateByLeagueSaisonResult(XMLGregorianCalendar value) {
        this.getLastChangeDateByLeagueSaisonResult = value;
    }

}
