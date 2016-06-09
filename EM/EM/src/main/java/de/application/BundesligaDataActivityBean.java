package de.application;

import java.util.Iterator;

import de.business.MatchModel;
import de.business.SoccerwayMatchModel;
import de.business.teams.TeamIDEnum;
import de.presentation.MainFrame;
import de.services.LigaWService;
import de.services.SWJSONParser;
import de.types.SoccerwayMatchType;

public class BundesligaDataActivityBean {

	MainFrame mView;
	
	public BundesligaDataActivityBean(MainFrame pView, int pDay, String pLeague, String pYear){
		mView = pView;
		initFixture(pDay, pLeague, pYear);
	}
	
	
	/**
	 * Reads all fixtures from the Webservice and set them to the presentation view
	 * 
	 * @param lMatchday the specific match day.
	 * @param lLeague the football league.
	 * @param lYear the football season.
	 * 
	 * Example: 27, "bl1", "2015" 
	 */
	public void initFixture(int lMatchDay, String lLeague, String lYear) {
		try{
			MatchModel[] lMatches = LigaWService.parseFootballData(lMatchDay, lLeague, lYear);
	
			int i = 0;
			for (MatchModel lMatch : lMatches) {
				if (lMatch != null) {
					mView.initPanelBundesliga(lMatch);
				}
			}
		} catch (ExceptionInInitializerError e){
			System.out.println("connection error");
		}
	}
	
	public void actionTeamDataPythonRequest() {
		String lId = TeamIDEnum.ZWICKAU.getID();
		String lMatchType = "all"; //"home", "away"
		
		Iterator<SoccerwayMatchModel> iter = SWJSONParser.getTeamData(lId, SoccerwayMatchType.getType(lMatchType));

		while (iter.hasNext()) {
			System.out.println(iter.next().toString());
		}
	}	
	
	public void actionRequestCompleteMatchday() {
		int a = 4;
//		int lMatchday = (Integer) mView.getFixturePanel().getComboMatchday().getSelectedItem();
//		String lLeague = mView.getFixturePanel().getComboLeague().getSelectedItem().toString();
//		String lSeason = mView.getFixturePanel().getComboSeason().getSelectedItem().toString();
//
//		initFixture(lMatchday, lLeague, lSeason);
	}	
}
