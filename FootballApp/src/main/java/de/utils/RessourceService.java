package de.utils;

import java.io.File;

public class RessourceService {
	
	public static final String USER_DIR = System.getProperty("user.dir");
	public static final String USER_DIR_SRC =  new StringBuilder(USER_DIR).append(File.separator).append("src").toString();
	public static final String USER_DIR_RESSOURCES = new StringBuilder(USER_DIR_SRC).append(File.separator).append("main")
			.append(File.separator).append("resources").toString();
	public static final String USER_DIR_IMAGES = new StringBuilder(USER_DIR_RESSOURCES).append(File.separator).append("images").toString();

	
	// Concrete image paths
	public static final String IMGAGE_ICON_ARROW_DOWN = new StringBuilder(USER_DIR_IMAGES).append(File.separator).append("arrow.png").toString();
	
	
}
