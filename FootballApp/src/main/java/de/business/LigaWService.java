package de.business;

import java.util.List;

import de.msiggi.sportsdata.webservices.ArrayOfMatchResult;
import de.msiggi.sportsdata.webservices.MatchResult;
import de.msiggi.sportsdata.webservices.Matchdata;
import de.msiggi.sportsdata.webservices.Sport;
import de.msiggi.sportsdata.webservices.Sportsdata;
import de.msiggi.sportsdata.webservices.SportsdataSoap;

public class LigaWService {
	
	private static Sportsdata mSportsdata = new Sportsdata();
	private static SportsdataSoap mSportsdataSoap = mSportsdata.getSportsdataSoap();
	
	/**
	 * Prints all Available sports
	 */		
	public static void parseAvailableSports(){
		System.out.println("*** Test Webservice OpenLigaDB ***");
		System.out.println("");
		System.out.println("Verfuegbare Sportarten");		
		
		List<Sport> sportlist = mSportsdataSoap.getAvailSports().getSport();

		for (int i = 0; i < sportlist.size(); i++) {
			Sport sport = sportlist.get(i);
			System.out.println("ID: " + sport.getSportsID() + " Sportart: " + sport.getSportsName());
		}	
	}

	/**
	 * Computes football data by given parameters
	 * 
	 * @param lMatchday the specific match day. Example input 26
	 * @param lLeague the football league. Example input "bl1"
	 * @param lYear the football season. Example input "2015"
	 * 
	 * @return Match Array with all data
	 */	
	public static Match [] parseFootballData(int lMatchday, String lLeague, String lYear){
		Match [] lMatches = new Match [9];

		int i = 0;
		for (Matchdata dat : mSportsdataSoap.getMatchdataByGroupLeagueSaison(lMatchday, lLeague, lYear).getMatchdata()){
			if(dat != null){
				lMatches[i] = new Match();
				lMatches[i].setTeam1(dat.getNameTeam1());
				lMatches[i].setTeam2(dat.getNameTeam2());
			
				ArrayOfMatchResult lMatchResults = dat.getMatchResults();
				
				if(lMatchResults != null){
					List<MatchResult> lListMatchResult = lMatchResults.getMatchResult();
					
					if (lListMatchResult != null && lListMatchResult.size() > 0)
						lMatches[i] = new Match(i, dat.getNameTeam1(), dat.getNameTeam2(), lListMatchResult.get(1).getPointsTeam1(), lListMatchResult.get(1).getPointsTeam2());
				}
			}
			i++;
		}
		return lMatches;
	}
}
