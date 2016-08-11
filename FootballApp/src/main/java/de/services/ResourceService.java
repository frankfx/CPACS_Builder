package de.services;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

import javax.imageio.ImageIO;

public class ResourceService {

	//create an object of SingleObject
	private static ResourceService instance = new ResourceService();

	// Get current classloader
	private ClassLoader cl;

	// predefined fields
	public final BufferedImage IMAGE_ICON_ARROW_DOWN;
	public final BufferedImage IMAGE_ICON_ADD_GREEN;
	public final BufferedImage IMAGE_ICON_REMOVE_RED;
	public final String SCRIPT_PYTHON_TEST = getResourceScripts("test.py");
	public final String SCRIPT_PYTHON_SOCCERWAY = getResourceScripts("soccerway.py");
	
	// paths
	public static final String DEFAULT_PROPERTIES_FILE = "SW_Team_Observer.properties";
	
	//make the constructor private so that this class cannot be instantiated
	private ResourceService() {
		cl = this.getClass().getClassLoader();
		IMAGE_ICON_ARROW_DOWN = getResourceImages("arrow.png");
		IMAGE_ICON_ADD_GREEN = getResourceImages("add_green.png");
		IMAGE_ICON_REMOVE_RED = getResourceImages("minus_red.png");
	}

	//Get the only object available
	public static ResourceService getInstance() {
		return instance;
	}

	public BufferedImage getResourceImages(String pFilename) {
		try {
			return ImageIO.read(cl.getResourceAsStream("images/" + pFilename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public InputStream getResourceJRMXL(String pPath) {
		return cl.getResourceAsStream("printing/" + pPath);
	}

	public InputStream getResourcePropertyFile(String pPath) {
		return cl.getResourceAsStream(pPath);
	}	
	
	public OutputStream getResourceOutputStreamPropertyFile(String pPath){
		File file;
		try {
			file = new File(cl.getResource(pPath).toURI());
			return new FileOutputStream(file);
		} catch (URISyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
		
	public String getResourceScripts(String pPath) {
		// TODO check if this line is notwendig
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
		System.out.println(ResourceService.getInstance().getResourceImages("dortmund.png"));
	}
}
