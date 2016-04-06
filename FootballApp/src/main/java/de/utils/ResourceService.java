package de.utils;

import java.net.URL;

public class ResourceService {
	
	//create an object of SingleObject
	private static ResourceService instance = new ResourceService();

	// predefined fields
	public final URL IMGAGE_ICON_ARROW_DOWN = getRessourceImages("arrow.png");
	public final URL SCRIPT_PYTHON_TEST = getRessourceScripts("test.py");
	public final URL SCRIPT_PYTHON_SOCCERWAY = getRessourceScripts("soccerway.py");
	
	//make the constructor private so that this class cannot be instantiated
	private ResourceService(){}
	
	//Get the only object available
	public static ResourceService getInstance(){
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
		System.out.println(ResourceService.getInstance().getRoot());
		
		System.out.println(ResourceService.getInstance().SCRIPT_PYTHON_SOCCERWAY);
	
		System.out.println(ResourceService.getInstance().getRessourceImages("dortmund.png"));
		
	}
}
