package de.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class ResourceService {

	//create an object of SingleObject
	private static ResourceService instance = new ResourceService();

	// Get current classloader
	private ClassLoader cl;

	// predefined fields
	public final BufferedImage IMGAGE_ICON_ARROW_DOWN;
	public final String SCRIPT_PYTHON_TEST = getRessourceScripts("test.py");
	public final String SCRIPT_PYTHON_SOCCERWAY = getRessourceScripts("soccerway.py");


	//make the constructor private so that this class cannot be instantiated
	private ResourceService() {
		cl = this.getClass().getClassLoader();
		IMGAGE_ICON_ARROW_DOWN = getRessourceImages("arrow.png");
	}

	//Get the only object available
	public static ResourceService getInstance() {
		return instance;
	}

	public BufferedImage getRessourceImages(String pFilename) {
		try {
			return ImageIO.read(cl.getResourceAsStream("images/" + pFilename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public InputStream getRessourceJRMXL(String pPath) {
		return cl.getResourceAsStream("printing/" + pPath);
	}

	public String getRessourceScripts(String pPath) {
		cl = this.getClass().getClassLoader();
		InputStream a = cl.getResourceAsStream("scripts/" + pPath);

		File tempFile = new File("");

		try {
			tempFile = File.createTempFile("python", ".tmp");
			tempFile.deleteOnExit();

			FileOutputStream out = new FileOutputStream(tempFile);

			//copy stream
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = a.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		return tempFile.getAbsolutePath();
	}

	public static void main(String[] args) {
		System.out.println(ResourceService.getInstance().SCRIPT_PYTHON_SOCCERWAY);
		System.out.println(ResourceService.getInstance().getRessourceImages("dortmund.png"));
	}
}
