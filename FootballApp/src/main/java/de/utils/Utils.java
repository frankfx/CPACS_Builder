package de.utils;

import java.awt.BorderLayout;
import java.awt.Window;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
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
	
	public static String generateUniqueIDWithTimestamp(String pID){
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
	
	/**
	 * Converts a unix timestamp to LocalDate
	 * 
	 * @param lTimestampStr
	 * @return
	 */
	public static LocalDate getLocalDateByUnixTimestamp(String lTimestampStr) {
		if (lTimestampStr != null && !lTimestampStr.isEmpty()) {
            Timestamp lTimestamp = new Timestamp(Long.parseLong(lTimestampStr)*1000);
         	LocalDate lDate = lTimestamp.toLocalDateTime().toLocalDate();
         	return lDate;
     	}
		return null;
	}	
	
	public static void executeFunctionWithWaitingDialogHint (Window parentComponent, Consumer<List<String>> function, List<String> param) {
		JDialog lLoading = createWaitingDialog(parentComponent);

		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
			@Override
			protected String doInBackground() throws InterruptedException {
				/** Execute some operation */
				function.accept(param);
				return null;
			}

			@Override
			protected void done() {
				lLoading.dispose();
			}
		};

		worker.execute();
		lLoading.setVisible(true);
		try {
			worker.get();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	public static void executeFunctionWithWaitingDialogHint (Window parentComponent, Runnable function) {
		JDialog lLoading = createWaitingDialog(parentComponent);

		SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
			@Override
			protected String doInBackground() throws InterruptedException {
				/** Execute some operation */
				function.run();
				return null;
			}

			@Override
			protected void done() {
				lLoading.dispose();
			}
		};

		worker.execute();
		lLoading.setVisible(true);
		try {
			worker.get();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}	
	
	private static JDialog createWaitingDialog(Window parentComponent) {
		JProgressBar progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);

		JDialog lLoading = new JDialog(parentComponent);
		JPanel p1 = new JPanel(new BorderLayout());
		
		//       p1.add(new JLabel("Please wait..."), BorderLayout.CENTER);
		p1.add(progressBar, BorderLayout.CENTER);
		p1.add(new JLabel("Please wait......."), BorderLayout.PAGE_START);
		
		lLoading.setUndecorated(true);
		lLoading.getContentPane().add(p1);
		lLoading.pack();
		lLoading.setLocationRelativeTo(parentComponent);
		lLoading.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		lLoading.setModal(true);
		return lLoading;
	}
}
