
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
 *         &lt;element name="GetLastMatchByLeagueTeamResult" type="{http://msiggi.de/Sportsdata/Webservices}Matchdata"/>
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
    "getLastMatchByLeagueTeamResult"
})
@XmlRootElement(name = "GetLastMatchByLeagueTeamResponse")
public class GetLastMatchByLeagueTeamResponse {

    @XmlElement(name = "GetLastMatchByLeagueTeamResult", required = true)
    protected Matchdata getLastMatchByLeagueTeamResult;

    /**
     * Gets the value of the getLastMatchByLeagueTeamResult property.
     * 
     * @return
     *     possible object is
     *     {@link Matchdata }
     *     
     */
    public Matchdata getGetLastMatchByLeagueTeamResult() {
        return getLastMatchByLeagueTeamResult;
    }

    /**
     * Sets the value of the getLastMatchByLeagueTeamResult property.
     * 
     * @param value
     *     allowed object is
     *     {@link Matchdata }
     *     
     */
    public void setGetLastMatchByLeagueTeamResult(Matchdata value) {
        this.getLastMatchByLeagueTeamResult = value;
    }

}
