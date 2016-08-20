package de.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import de.business.SoccerwayMatchModel;
import de.presentation.popups.PopupFactory;
import de.presentation.popups.PopupType;
import de.types.SoccerwayMatchType;

public class SWJSONParser {

	/**
	 * Handels the team data given by teamID and matchtype
	 * [commands][0]['parameters']['content']
	 */
	public static Iterator<SoccerwayMatchModel> getTeamData(String pTeamID, SoccerwayMatchType pMatchType) {
		List<SoccerwayMatchModel> lResultList = new ArrayList<SoccerwayMatchModel>();

		try {
			// Fetch the interesting part of inputted JSON obj
			String content = new JSONObject(getJSONDataString(pTeamID, pMatchType)).getJSONArray("commands").getJSONObject(0).getJSONObject("parameters").getString("content");

			// Remove uninteresting header and footer data
			content = content.substring(0, content.indexOf("</tbody>"));
			content = content.substring(content.indexOf("<thead") + 7);

			// Split content by <tr> -tags (tr is shorthand for table row)
			Pattern p1 = Pattern.compile("<tr[^<]+?>");
			String[] splitted = p1.split(content);

			// First row is the table header data
			//String header = splitted[0];

			// Rest are the match info 
			String[] data = Arrays.copyOfRange(splitted, 1, splitted.length);

			// Split content by <td> -tags (table columns) and clean other tags
			Pattern p2 = Pattern.compile("<td[^<]+?>");

			for (String row : data) {
				String[] cols = p2.split(row);

				String[] colData = Arrays.copyOfRange(cols, 0, cols.length - 1);

				SoccerwayMatchModel mSoccerwayModel = new SoccerwayMatchModel();

				for (int i = 0; i < colData.length; i++) {
					String val = parseHTMLTableCellContent( colData[i].replace("<[^<]+?>", "").trim() );
					
					switch (i) {
					case 1:
						mSoccerwayModel.setDay(val);
						break;
					case 2:
						mSoccerwayModel.setDate(val);
						break;
					case 3:
						mSoccerwayModel.setCompetition(val);
						break;
					case 4:
						mSoccerwayModel.setTeam1(val);
						break;
					case 5:
						mSoccerwayModel.setResult(val);
						break;
					case 6:
						mSoccerwayModel.setTeam2(val);
						break;
					default:
						break;
					}
				}

				if (colData.length > 0)
					lResultList.add(mSoccerwayModel);
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
			//PopupFactory.getPopup(PopupType.ERROR, "");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return lResultList.iterator();
	}

	/**
	 * Connects to the internet und reads soccerway JSON-String 
	 */
	private static String getJSONDataString(String pTeamID, SoccerwayMatchType pMatchType) throws IOException {

		if (pTeamID != null && pMatchType != null) {
			// Connect to the URL using java's native library
			URL url = new URL("http://nr.soccerway.com/a/block_team_matches?block_id=page_team_1_block_team_matches_5" +
					"&callback_params=%7B%22page%22%3A0%2C%22bookmaker_urls%22%3A%5B%5D%2C%22block_service_id" +
					"%22%3A%22team_matches_block_teammatches%22%2C%22team_id%22%3A" + pTeamID + "%2C%22competition_id" +
					"%22%3A0%2C%22filter%22%3A%22all%22%7D&action=filterMatches&params=%7B%22filter%22%3A%22" + pMatchType + "%22%7D");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// read data
			BufferedReader in = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));

			StringBuilder response = new StringBuilder();
			String inputLine;

			while ((inputLine = in.readLine()) != null)
				response.append(inputLine);

			in.close();

			return response.toString();
		}
		return null;
	}

	/**
	 * private method to parse a html line
	 * 
	 * @param str
	 * @return
	 */
	private static String parseHTMLTableCellContent(String str) {
		if (str == null)
			return null;

		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		char curChar;

		for (int i = 0; i < str.length(); i++) {
			curChar = str.charAt(i);

			switch (curChar) {
			case '<':
				flag = false;
				break;
			case '>':
				flag = true;
				break;
			default:
				if (flag)
					sb.append(curChar);
			}
		}
		return sb.toString();
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
					Iterator<SoccerwayMatchModel> iter = SWJSONParser.getTeamData(value, SoccerwayMatchType.all);
				
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
					Iterator<SoccerwayMatchModel> iter = SWJSONParser.getTeamData(value, SoccerwayMatchType.all);
				
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
	
	
	public static void main(String[] args) {
		Iterator<SoccerwayMatchModel> iter = SWJSONParser.getTeamData("198", SoccerwayMatchType.all);

		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
	}
}
