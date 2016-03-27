package de.utils;

import java.io.File;
import java.net.URL;

public class RessourceService {
	
	public static URL getRessourceImages(String pFilename){
		return Thread.currentThread().getContextClassLoader().getResource( "images" + File.separator + pFilename);
	}

	public static URL getRessourceScripts(String pFilename){
		return Thread.currentThread().getContextClassLoader().getResource( "scripts" + "/" + pFilename);
	}	
	
	public static final URL IMGAGE_ICON_ARROW_DOWN = getRessourceImages("arrow.png");
	public static final URL SCRIPT_PYTHON_TEST = getRessourceScripts("test.py");
	public static final URL SCRIPT_PYTHON_SOCCERWAY = getRessourceScripts("soccerway.py");
}
