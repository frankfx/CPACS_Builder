package de.services;

import de.business.teams.TeamIDEnum;
import de.business.teams.TeamModel;

public class BundesligaService {
	public static TeamModel getTeamModel(TeamIDEnum pID){
		switch (pID) {
		case BVB: return new TeamModel("Borussia Dortmund", "dortmund.png");
		case FCB: return new TeamModel("Bayern Muenchen", "bayern.png");
		case Hertha: return new TeamModel("Hertha BSC", "berlin.gif");
		case BMG: return new TeamModel("Gladbach", "gladbach.gif");
		case S04: return new TeamModel("Schalke 04", "schalke.gif");
		case FSV: return new TeamModel("Mainz", "mainz.gif");
		case LEV: return new TeamModel("Leverkusen", "leverkusen.gif");
		case VFL: return new TeamModel("Wolfsburg", "wolfsburg.gif");		
		case FCK: return new TeamModel("Koeln", "koeln.gif");
		case Ingol: return new TeamModel("Ingolstadt", "ingolstadt.gif");
		case HSV: return new TeamModel("Hamburg", "hamburg.png");
		case FCA: return new TeamModel("Augsburg", "augsburg.gif");
		case D89: return new TeamModel("Darmstadt", "darmstadt.gif");		
		case SVW: return new TeamModel("Bremen", "bremen.png");
		case EINTRACHT: return new TeamModel("Frankfurt", "frankfurt.png");
		case TSG: return new TeamModel("Hoffenheim", "hoffenheim.gif");	
		case EMPTY : return new TeamModel("Empty", null);
		default:
			return new TeamModel("NoDataFound", null);
		}
	}
}
