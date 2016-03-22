package de.business;

import java.util.List;

import de.msiggi.sportsdata.webservices.MatchResult;
import de.msiggi.sportsdata.webservices.Matchdata;
import de.msiggi.sportsdata.webservices.Sport;
import de.msiggi.sportsdata.webservices.Sportsdata;
import de.msiggi.sportsdata.webservices.SportsdataSoap;

public class OpenLigaDBModel {
	private static Sportsdata mSportsdata = new Sportsdata();
	private static SportsdataSoap mSportsdataSoap = mSportsdata.getSportsdataSoap();
	
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

	// 26, "bl1", "2015"
	public static Match [] parseFootballData(int lMatchday, String lLeague, String year){
		Match [] lMatches = new Match [9];
		
		int i = 0;
		for (Matchdata dat : mSportsdataSoap.getMatchdataByGroupLeagueSaison(lMatchday, lLeague, year).getMatchdata()){
			
			List<MatchResult> lMatchResults = dat.getMatchResults().getMatchResult();
			lMatches[i] = new Match(i, dat.getNameTeam1(), dat.getNameTeam2(), lMatchResults.get(1).getPointsTeam1(), lMatchResults.get(1).getPointsTeam2());
			i++;
		}
		return lMatches;
	}
//	for (MatchResult lResult : lMatchResults)
//		System.out.println(lResult.getPointsTeam1());
	
//	List<Goal> goal = dat.getGoals().getGoal();
//	
//	for (Goal g : goal)
//		System.out.println(g.getGoalScoreTeam1() + " - " + g.getGoalScoreTeam2());
	
}
