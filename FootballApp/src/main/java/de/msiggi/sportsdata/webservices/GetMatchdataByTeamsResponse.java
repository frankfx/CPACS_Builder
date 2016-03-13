
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
 *         &lt;element name="GetMatchdataByTeamsResult" type="{http://msiggi.de/Sportsdata/Webservices}ArrayOfMatchdata" minOccurs="0"/>
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
    "getMatchdataByTeamsResult"
})
@XmlRootElement(name = "GetMatchdataByTeamsResponse")
public class GetMatchdataByTeamsResponse {

    @XmlElement(name = "GetMatchdataByTeamsResult")
    protected ArrayOfMatchdata getMatchdataByTeamsResult;

    /**
     * Gets the value of the getMatchdataByTeamsResult property.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfMatchdata }
     *     
     */
    public ArrayOfMatchdata getGetMatchdataByTeamsResult() {
        return getMatchdataByTeamsResult;
    }

    /**
     * Sets the value of the getMatchdataByTeamsResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfMatchdata }
     *     
     */
    public void setGetMatchdataByTeamsResult(ArrayOfMatchdata value) {
        this.getMatchdataByTeamsResult = value;
    }

}
