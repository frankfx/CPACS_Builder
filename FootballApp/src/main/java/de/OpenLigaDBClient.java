package de;

import java.net.MalformedURLException;
import java.util.List;

import de.msiggi.sportsdata.webservices.Fussballdaten;
import de.msiggi.sportsdata.webservices.Sport;
import de.msiggi.sportsdata.webservices.Sportsdata;
import de.msiggi.sportsdata.webservices.SportsdataSoap;

public class OpenLigaDBClient {

	Sportsdata sd;
	SportsdataSoap sportsoap;
	
	public OpenLigaDBClient(){
		sd = new Sportsdata();
		sportsoap = sd.getSportsdataSoap();
	}
	
	public void getAvailableSports(){
		System.out.println("*** Test Webservice OpenLigaDB ***");
		System.out.println("");
		System.out.println("Verfügbare Sportarten");		
		
		List<Sport> sportlist = sportsoap.getAvailSports().getSport();

		for (int i = 0; i < sportlist.size(); i++) {
			Sport sport = sportlist.get(i);
			System.out.println("ID: " + sport.getSportsID() + " Sportart: " + sport.getSportsName());
		}	
	}

	public void getFussballData(){
		for (Fussballdaten dat : sportsoap.getFusballdaten(22,"bl1",2015,"Test").getFussballdaten()){
			System.out.println(dat.getTeam1() + " - " + dat.getTeam2() + "  " + dat.getErgebnisTeam1() + ":" + dat.getErgebnisTeam2());
		}
	}
	
	public static void main(String[] args) throws MalformedURLException {
		OpenLigaDBClient client = new OpenLigaDBClient();
		client.getAvailableSports();
		System.out.println();
		client.getFussballData();
	}
		
}

