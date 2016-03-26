package de.utils;

import java.io.File;
import java.net.URL;

public class RessourceService {
	
	public static URL getRessourceImages(String pFilename){
		return Thread.currentThread().getContextClassLoader().getResource( "images" + File.separator + pFilename);
	}
	
	public static URL IMGAGE_ICON_ARROW_DOWN = getRessourceImages("arrow.png");
	
}
