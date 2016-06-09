
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
 *         &lt;element name="GetLastMatchResult" type="{http://msiggi.de/Sportsdata/Webservices}Matchdata"/>
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
    "getLastMatchResult"
})
@XmlRootElement(name = "GetLastMatchResponse")
public class GetLastMatchResponse {

    @XmlElement(name = "GetLastMatchResult", required = true)
    protected Matchdata getLastMatchResult;

    /**
     * Gets the value of the getLastMatchResult property.
     * 
     * @return
     *     possible object is
     *     {@link Matchdata }
     *     
     */
    public Matchdata getGetLastMatchResult() {
        return getLastMatchResult;
    }

    /**
     * Sets the value of the getLastMatchResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Matchdata }
     *     
     */
    public void setGetLastMatchResult(Matchdata value) {
        this.getLastMatchResult = value;
    }

}
