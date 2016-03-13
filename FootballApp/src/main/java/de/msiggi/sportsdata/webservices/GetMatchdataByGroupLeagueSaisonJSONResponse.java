
package de.msiggi.sportsdata.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element name="GetMatchdataByGroupLeagueSaisonJSONResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "getMatchdataByGroupLeagueSaisonJSONResult"
})
@XmlRootElement(name = "GetMatchdataByGroupLeagueSaisonJSONResponse")
public class GetMatchdataByGroupLeagueSaisonJSONResponse {

    @XmlElement(name = "GetMatchdataByGroupLeagueSaisonJSONResult")
    protected String getMatchdataByGroupLeagueSaisonJSONResult;

    /**
     * Gets the value of the getMatchdataByGroupLeagueSaisonJSONResult property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGetMatchdataByGroupLeagueSaisonJSONResult() {
        return getMatchdataByGroupLeagueSaisonJSONResult;
    }

    /**
     * Sets the value of the getMatchdataByGroupLeagueSaisonJSONResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGetMatchdataByGroupLeagueSaisonJSONResult(String value) {
        this.getMatchdataByGroupLeagueSaisonJSONResult = value;
    }

}
