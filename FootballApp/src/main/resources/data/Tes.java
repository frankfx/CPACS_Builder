package data;

import java.net.URL;

public class Tes {
	
	//create an object of SingleObject
	private static Tes instance = new Tes();

	// predefined fields
	public final URL IMGAGE_ICON_ARROW_DOWN = getRessourceImages("arrow.png");
	public final URL SCRIPT_PYTHON_TEST = getRessourceScripts("test.py");
	public final URL SCRIPT_PYTHON_SOCCERWAY = getRessourceScripts("soccerway.py");
	
	//make the constructor private so that this class cannot be instantiated
	private Tes(){}
	
	//Get the only object available
	public static Tes getInstance(){
		return instance;
	}

	public URL getRoot(){
		return getClass().getResource(".");
	}
	
	public URL getRessourceImages(String pFilename){
		return  getClass().getResource("/images/" + pFilename);
	}

	public URL getRessourceScripts(String pFilename){
		return getClass().getResource( "/scripts/" + pFilename);
	}	

	
	public static void main(String[] args) {
		System.out.println(Tes.getInstance().getRoot());
		
		System.out.println(Tes.getInstance().SCRIPT_PYTHON_SOCCERWAY);
	
		System.out.println(Tes.getInstance().getRessourceImages("dortmund.png"));
		
	}
}
