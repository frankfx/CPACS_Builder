package de.utils;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Utils {
	/**
	 * opens a file chooser
	 */	
	public static File chooseFile(String description, String ... extensions){
		// create JFileChooser-Object and set filter
		JFileChooser chooser = new JFileChooser();
		chooser.setFileFilter(new FileNameExtensionFilter(description, extensions));

		// open the dialog and get result value
        int rueckgabeWert = chooser.showOpenDialog(null);
        
        /* if user pressed "Oeffnen" return the selected file */
        if(rueckgabeWert == JFileChooser.APPROVE_OPTION){
            return chooser.getSelectedFile();
        }
        return null;
	}
	
	/**
	 * ID
	 * @param pID
	 * @return
	 */
	public static String getIDWithoutSuffix(String pID){
		return pID != null ? pID.split("_", 2)[0] : null;
	}
}
