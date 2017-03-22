package de.services;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.junit.Assert;

import de.business.SoccerwayMatchModel;
import de.presentation.popups.PopupFactory;
import de.presentation.popups.PopupType;
import de.utils.Tupel;
import de.utils.Utils;

public class SWHTMLParser {

	private static String url_fst = "http://www.soccerway.mobi/?sport=soccer&page=team&id=";
	private static String url_snd = "&localization_id=www";
	
	private enum HTMLAttributes {
		SCORING, TIMESTAMP, ROUND, TEAM;
	}
	
	/**
	 * Returns the first team name on the webpage or null
	 * @throws IOException 
	 */
	public static String getTeamNameByID(String pTeamID) {
		String lUrl = url_fst + pTeamID + url_snd;

		try {
			Document doc = Jsoup.connect(lUrl).get();
			Elements links = doc.select("a[href]");
			
			for (Element link : links) {
				String lText = link.text();
				
				if (!lText.contains("More info")) {
					String lLinkString = link.attr("abs:href");
					if (lLinkString.contains("team") ) {
						return lText;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}	
	
	/**
	 * Handels the team data given by teamID 
	 * @throws IOException 
	 */
	public static Iterator<SoccerwayMatchModel> getTeamData(String pTeamID) throws IOException {
		String lUrl = url_fst + pTeamID + url_snd;

		Document doc = Jsoup.connect(lUrl).get();
		Elements links = doc.select("a[href]");
		
		List<SoccerwayMatchModel> lResultList = new ArrayList<SoccerwayMatchModel>();
		
		int idx = 0;
		int size = links.size();
		
		while (idx+3 < size) {	
			// get the competition value (Round)
			Tupel<HTMLAttributes, String> lRound = getHTMLLinkDataTupel(links, idx);
			
			if (lRound != null && lRound.getFirst().equals(HTMLAttributes.ROUND)) {
				
				Element link = links.get(idx);
				Node lNode = link.parent().parent();
				String lTimestampStr = lNode.attr("data-timestamp");
				
				LocalDate lDate = Utils.getLocalDateByUnixTimestamp(lTimestampStr);
				
				Tupel<HTMLAttributes, String> lTeam1 = getHTMLLinkDataTupel(links, idx+1);
				Tupel<HTMLAttributes, String> lScoreOrTime = getHTMLLinkDataTupel(links, idx+2);
				Tupel<HTMLAttributes, String> lTeam2 = getHTMLLinkDataTupel(links, idx+3);
				
				SoccerwayMatchModel mSoccerwayModel = new SoccerwayMatchModel(pTeamID);
				mSoccerwayModel.setDay(lDate.getDayOfWeek().toString());
				mSoccerwayModel.setDate(lDate);
				mSoccerwayModel.setCompetition(lRound.getSecond().toString());
				mSoccerwayModel.setTeam1(lTeam1.getSecond().toString());
				mSoccerwayModel.setResult(lScoreOrTime != null ? lScoreOrTime.getSecond().toString() : "-");
				mSoccerwayModel.setTeam2(lTeam2.getSecond().toString());
				lResultList.add(mSoccerwayModel);
				
				idx += 3;
			} else {
				idx ++;
			}
		}
		return lResultList.iterator();
	}

	/**
	 * private helper to get team, score and time 
	 *  
	 * @param links
	 * @param idx
	 * @return
	 */
	private static Tupel<HTMLAttributes, String> getHTMLLinkDataTupel(Elements links, int idx) {
		
		Tupel<HTMLAttributes, String> lResult = null;
		
		Element link = links.get(idx);
		String lText = link.text();
		
		if (!lText.contains("More info")) {

			String lLinkString = link.attr("abs:href");
		
			if (lLinkString.contains("round")) {
				return new Tupel<HTMLAttributes, String>(HTMLAttributes.ROUND, lText); 
			} else if (lLinkString.contains("team") ) {
				return new Tupel<HTMLAttributes, String>(HTMLAttributes.TEAM, lText);
			} else if (lLinkString.contains("match")) {
				for (Node lNode : link.childNodes()){
					String lClass = lNode.attr("class");
		         	if (lClass.equals("timestamp")) {
			             return new Tupel<HTMLAttributes, String>(HTMLAttributes.TIMESTAMP, lText);
		         	} else if (lClass.equals("scoring")) {
		         		return new Tupel<HTMLAttributes, String>(HTMLAttributes.SCORING, lText);
		         	} 
		         }
			}
		}
		return lResult;
	}

	public static Iterator<SoccerwayMatchModel> getAufgabenBySWObserverPropertyFile(InputStream pPropertyInputStream){
		List<SoccerwayMatchModel> lResultList = new ArrayList<SoccerwayMatchModel>();
		Properties prop = new Properties();

		try {
			prop.load(pPropertyInputStream);
			int lDuration = Integer.parseInt(prop.getProperty("duration"));
			
			LocalDate lToday = LocalDate.now(); 
			LocalDate lFinalDate = lToday.plusDays(lDuration); 
			
			String key , value;
			Enumeration<Object> lAllKeys = prop.keys();
			
			while (lAllKeys.hasMoreElements()) {
				key = lAllKeys.nextElement().toString();
				if(key.startsWith("SW_TEAM_ID_")){
					value = prop.getProperty(key);
					Iterator<SoccerwayMatchModel> iter = getTeamData(value);
				
					SoccerwayMatchModel lCurMatch;
					while (iter.hasNext()){
						lCurMatch = iter.next();
						if (lCurMatch.getDate().isAfter(lFinalDate)){
							// break the iteration to avoid reading all results (after the final date) 
							break;
						} else if (!lCurMatch.getDate().isBefore(lToday)){
							lResultList.add(lCurMatch);
					    }
					}
				}
			}
		} catch (IOException ex) {
			PopupFactory.getPopup(PopupType.ERROR, ex.getMessage());
		}
		
		return lResultList.iterator();
	}	

	public static Iterator<SoccerwayMatchModel> getResultsBySWObserverPropertyFile(InputStream pPropertyInputStream){
		List<SoccerwayMatchModel> lResultList = new ArrayList<SoccerwayMatchModel>();
		Properties prop = new Properties();

		try {
			prop.load(pPropertyInputStream);
			int lDuration = Integer.parseInt(prop.getProperty("duration"));
			
			LocalDate lToday = LocalDate.now(); 
			LocalDate lPastDate = lToday.minusDays(lDuration); 
			
			String key , value;
			Enumeration<Object> lAllKeys = prop.keys();
			
			while (lAllKeys.hasMoreElements()) {
				key = lAllKeys.nextElement().toString();
				if(key.startsWith("SW_TEAM_ID_")){
					value = prop.getProperty(key);
					Iterator<SoccerwayMatchModel> iter = getTeamData(value);
				
					SoccerwayMatchModel lCurMatch;
					while (iter.hasNext()){
						lCurMatch = iter.next();
						if (lCurMatch.getDate().isAfter(lToday)){
							// break the iteration to avoid reading all results (after today)
							break;
						} else if (!lCurMatch.getDate().isAfter(lToday) && !lCurMatch.getDate().isBefore(lPastDate)){
							lResultList.add(lCurMatch);
					    }
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return lResultList.iterator();
	}	

	/**
	 * 
	 * @param pIDList
	 * @param pDuration
	 * @return
	 * @throws IOException
	 */
	public static Iterator<SoccerwayMatchModel> getResultsBySWObserverIDs(List<String> pIDList, int pDuration) {
		List<SoccerwayMatchModel> lResultList = new ArrayList<SoccerwayMatchModel>();
			
		LocalDate lToday = LocalDate.now(); 
		LocalDate lPastDate = lToday.minusDays(pDuration); 

		for (int i = 0; i < pIDList.size(); i++) {
			try {
				Iterator<SoccerwayMatchModel> iter = getTeamData(pIDList.get(i));
				
				SoccerwayMatchModel lCurMatch;
				while (iter.hasNext()){
					lCurMatch = iter.next();
					if (lCurMatch.getDate() == null) {
						
					} else if (lCurMatch.getDate().isAfter(lToday)){
						// break the iteration to avoid reading all results (after today)
						break;
					} else if (!lCurMatch.getDate().isAfter(lToday) && !lCurMatch.getDate().isBefore(lPastDate)){
						lResultList.add(lCurMatch);
				    }
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return lResultList.iterator();
	}	
	
	/**
	 * 
	 * @param pIDList
	 * @param pDuration
	 * @return
	 * @throws IOException
	 */
	public static Iterator<SoccerwayMatchModel> getFixturesBySWObserverIDs(List<String> pIDList, int pDuration) throws IOException{
		List<SoccerwayMatchModel> lResultList = new ArrayList<SoccerwayMatchModel>();
			
		LocalDate lToday = LocalDate.now(); 
		LocalDate lFinalDate = lToday.plusDays(pDuration); 

		for (int i = 0; i < pIDList.size(); i++) {
			Iterator<SoccerwayMatchModel> iter = getTeamData(pIDList.get(i));
			SoccerwayMatchModel lCurMatch;
			while (iter.hasNext()){
				lCurMatch = iter.next();
				if (lCurMatch.getDate().isAfter(lFinalDate)){
					// break the iteration to avoid reading all results (after the final date) 
					break;
				} else if (!lCurMatch.getDate().isBefore(lToday)){
					lResultList.add(lCurMatch);
			    }
			}
		}
		return lResultList.iterator();
	}	
	
	public static void main(String[] args) throws IOException {
		Assert.assertTrue(getTeamNameByID("1001").equals("Hoffenheim"));
		Assert.assertTrue(getTeamNameByID("2370").equals("Zwickau"));
		
		Iterator<?> iter = getTeamData("3434");
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
    }	
}
