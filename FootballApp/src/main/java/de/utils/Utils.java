package de.utils;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.business.TipicoModel;
import de.services.Database;
import de.services.SQLService;

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
	
	public static boolean isIDValid(int pID, List<TipicoModel> list){
		for(TipicoModel item : list)
			if(Integer.parseInt(getIDWithoutSuffix(item.getID())) == pID)
				return false;
		return true;
	}	

	public static String generateValidID(List<String> pIDsFromDB) {
		if (pIDsFromDB != null && !pIDsFromDB.isEmpty()) {
			return "" + pIDsFromDB.stream().mapToInt(i -> Integer.parseInt(getIDWithoutSuffix(i))).max().getAsInt() + 1;
		} else {
			return "" + Instant.now().toEpochMilli();
		}
	}
	
	private static String generateUniqueIDWithTimestamp(String pID){
		// Erstellen der ID : Instant timestamp = Instant.now(); (Java 8)
		return pID + "_" + Instant.now().toEpochMilli();
	}	
	
	public static String createInternalID(String pID, Database mDB){
		if (mDB != null && mDB.isConnected()){
			PreparedStatement prepStmt;
			try {
				prepStmt = mDB.getConnection().prepareStatement(SQLService.SQL_CHECK_ID_EXISTS);
				prepStmt.setString(1, pID);
				prepStmt.execute();
				if (mDB.getResultSet().next())
					generateUniqueIDWithTimestamp(pID);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			return generateUniqueIDWithTimestamp(pID);
		}
		return pID;
	}	
}
