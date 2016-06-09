
package de.msiggi.sportsdata.webservices;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="leagueId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="teamId" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    "leagueId",
    "teamId"
})
@XmlRootElement(name = "GetLastMatchByLeagueTeam")
public class GetLastMatchByLeagueTeam {

    protected int leagueId;
    protected int teamId;

    /**
     * Gets the value of the leagueId property.
     * 
     */
    public int getLeagueId() {
        return leagueId;
    }

    /**
     * Sets the value of the leagueId property.
     * 
     */
    public void setLeagueId(int value) {
        this.leagueId = value;
    }

    /**
     * Gets the value of the teamId property.
     * 
     */
    public int getTeamId() {
        return teamId;
    }

    /**
     * Sets the value of the teamId property.
     * 
     */
    public void setTeamId(int value) {
        this.teamId = value;
    }

}
