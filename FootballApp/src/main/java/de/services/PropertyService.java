package de.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PropertyService{
	
	public static final String PROPERTIES_CHANGED = "properties changed";
	
	/**
	 * loads a properties files and returns the content as a sorted TreeMap
	 * 
	 * @param pFile the properties file
	 * @return a (sorted) TreeMap with all keys and values of the properties file
	 */
	public static Map<Object, Object> getProperties(InputStream pInputStream){
		Properties prop = new Properties();
		try {
			prop.load(pInputStream);
			return new TreeMap<Object, Object>(prop);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}	
	
	/**
	 * writes or updates some data (pair of key and value) to a properties file
	 * 
	 * @param pFile the properties file
	 * @param pData Data to update the given file
	 */
	public static void writeProperties(File pFile, Map<String, String> pData){
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(pFile));
			for (Map.Entry<String, String> entry : pData.entrySet()){
				prop.setProperty(entry.getKey(), entry.getValue());
			}
			
			FileOutputStream fos = new FileOutputStream(pFile);
			prop.store(fos, null);
			fos.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
	}
	
	/**
	 * opens a file chooser
	 */	
	public static File choosePropertiesFile(){
		// JFileChooser-Objekt erstellen
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter("Java Properties", "properties"));

		// Dialog zum Oeffnen von Dateien anzeigen
        int rueckgabeWert = chooser.showOpenDialog(null);
        
        /* Abfrage, ob auf "Oeffnen" geklickt wurde */
        if(rueckgabeWert == JFileChooser.APPROVE_OPTION){
            return chooser.getSelectedFile();
        }
        return null;
	}
	
}
