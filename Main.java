import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Main {
	
	public static void main(String[] args) throws JSONException {
		Main main = new Main();
		
		String s = "{\"1\":{\"2\":{\"3\":{\"4\":[5,{\"6\":7}]}}}}";
		JSONObject obj = new JSONObject(main.readJSONDataString("142", "all"));
		
		//obj.put("name", "foo");
		//obj.put("num", new Integer(3));
		System.out.println("heir");
		System.out.println(obj);
		System.out.println("hu");
		
		//JSONObject obj1 = main.getJSONObjectByKeyPath(obj, new String[]{"1", "2", "3"});
		
		//JSONArray arr = main.getJSONArrayByKeyPath(obj, new String[]{"1", "2", "3", "4"});
		
		//System.out.println(arr);
	}
	
	
	public String readJSONDataString(String pTeamID, String pMatchType) {
		BufferedReader reader = null;
		URL url = null;
		
		String urlString = "http://nr.soccerway.com/a/block_team_matches?block_id=page_team_1_block_team_matches_5" +
	            "&callback_params=%7B%22page%22%3A0%2C%22bookmaker_urls%22%3A%5B%5D%2C%22block_service_id" +
	            "%22%3A%22team_matches_block_teammatches%22%2C%22team_id%22%3A"+ pTeamID + "%2C%22competition_id" +
	            "%22%3A0%2C%22filter%22%3A%22all%22%7D&action=filterMatches&params=%7B%22" +
	            "filter%22%3A%22" + pMatchType + "%22%7D";	
		
		System.out.println(urlString);
		
	    try{
			url = new URL(urlString);
			
			System.out.println("hiwer");
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        
	        System.out.println("hiwer");
	        
	        while ((read = reader.read(chars)) != -1)
	        	buffer.append(chars, 0, read); 

	        System.out.println("hi");
	        
	        reader.close();
	        
	        return buffer.toString();
	    } catch (IOException e){
	    	e.printStackTrace();
	    }
	        
	    return null;
	}
	
	
	public JSONObject getJSONObjectByKeyPath(JSONObject pObj, String [] keyPath) throws JSONException{
		if (keyPath != null){
			for (int i = 0; i < keyPath.length; i++) {
				if(pObj.has(keyPath[i])){
					pObj = pObj.getJSONObject(keyPath[i]);
				} else{
					return null;
				}
			}
		}
		return pObj;
	}
	
	public JSONArray getJSONArrayByKeyPath(JSONObject pObj, String [] keyPath) throws JSONException{
		if (keyPath != null){
			for (int i = 0; i < keyPath.length - 1; i++) {
				if(pObj.has(keyPath[i])){
					pObj = pObj.getJSONObject(keyPath[i]);
				} else{
					return null;
				}
			}
		}
		
		if (pObj.has(keyPath[keyPath.length-1])){
			return pObj.getJSONArray(keyPath[keyPath.length-1]);
		}
		
		return null;
	}	
}
